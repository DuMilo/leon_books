import styles from './LoanPopup.module.css';
import { X } from 'phosphor-react';
import { useState, useEffect } from 'react';

export function LoanPopup({ livro, onClose, onUpdate }) {
  const [status, setStatus] = useState(livro.status);
  const [devedor, setDevedor] = useState(livro.devedor || '');
  // New state variables for fine functionality
  const [isOverdueState, setIsOverdueState] = useState(false);
  const [applyFine, setApplyFine] = useState(false);
  const [fineAmount, setFineAmount] = useState('');

  useEffect(() => {
    setStatus(livro.status);
    setDevedor(livro.devedor || '');

    let overdue = false;
    // Check for overdue status - assuming livro.dataDevolucaoPrevista is a string like 'YYYY-MM-DD'
    // and livro.status is the original status of the loan when the popup is opened.
    if (livro.dataDevolucaoPrevista && livro.status === 'indisponível') {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // Normalize today to the start of the day

      const dateParts = livro.dataDevolucaoPrevista.split('-');
      if (dateParts.length === 3) {
        const year = parseInt(dateParts[0], 10);
        const month = parseInt(dateParts[1], 10) - 1; // Month is 0-indexed in JavaScript Date
        const day = parseInt(dateParts[2], 10);
        const dueDate = new Date(year, month, day);
        dueDate.setHours(0, 0, 0, 0); // Normalize due date to the start of the day

        if (dueDate < today) {
          overdue = true;
        }
      }
    }
    setIsOverdueState(overdue);

    // Reset fine states if the loan is not overdue
    if (!overdue) {
      setApplyFine(false);
      setFineAmount('');
    }
    // If it is overdue, the user can decide to apply a fine.
    // We don't automatically set applyFine to true.

  }, [livro]); // Dependency array includes livro

  const handleSalvar = () => {
    if (status === 'indisponível' && !devedor.trim()) {
      alert('Informe o nome do devedor.');
      return;
    }

    // Validate fine amount if a fine is being applied
    if (applyFine && isOverdueState) {
      const numericFineAmount = parseFloat(fineAmount);
      if (isNaN(numericFineAmount) || numericFineAmount <= 0) {
        alert('Informe um valor de multa válido e positivo.');
        return;
      }
    }

    const livroAtualizado = {
      ...livro,
      status,
      devedor: status === 'indisponível' ? devedor : '',
    };

    // Add fine information if the user chose to apply it and the book was overdue
    if (applyFine && isOverdueState) {
      livroAtualizado.acaoMulta = {
        aplicar: true,
        valor: parseFloat(fineAmount),
        // emprestimoId: livro.id // The backend can derive emprestimoId from 'livro.id'
      };
    }

    onUpdate(livroAtualizado);
    onClose();
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.popup}>
        <div className={styles.headerPopup}>
          Status de Empréstimo
          <button className={styles.close} onClick={onClose}>
            <X size={18} weight="bold" />
          </button>
        </div>

        <div className={styles.content}>
          <div className={styles.cover}>Livro</div>
          <div className={styles.info}>
            <div className={styles.field}>
              <span className={styles.label}>Título</span>
              <p className={styles.text}>{livro.titulo}</p>
            </div>

            <div className={styles.field}>
              <span className={styles.label}>Autor</span>
              <p className={styles.text}>{livro.autor}</p>
            </div>

            <div className={styles.field}>
              <span className={styles.label}>ISBN</span>
              <p className={styles.text}>{livro.isbn}</p>
            </div>

            <div className={styles.field}>
              <span className={styles.label}>Status</span>
              <select
                className={styles.input}
                value={status}
                onChange={(e) => {
                  const newStatus = e.target.value;
                  setStatus(newStatus);
                  // If status changes to 'disponível', we might want to reset devedor or fine states,
                  // but for now, we keep fine states until save, as a fine might still be applied upon return.
                  // If the book becomes available, the fine section will hide, but data persists if already checked.
                }}
              >
                <option value="disponível">Disponível</option>
                <option value="indisponível">Indisponível (Emprestado)</option>
              </select>
            </div>

            {status === 'indisponível' && (
              <> {/* Use Fragment to group multiple elements */}
                <div className={styles.field}>
                  <span className={styles.label}>Devedor</span>
                  <input
                    className={styles.input}
                    value={devedor}
                    onChange={(e) => setDevedor(e.target.value)}
                    placeholder="Nome do devedor"
                  />
                </div>

                {/* Fine Section - shows if the loan is overdue AND current status in popup is 'indisponível' */}
                {isOverdueState && (
                  <div className={styles.fineSection}>
                    <p className={styles.overdueMessage}>Atenção: Livro com devolução atrasada!</p>
                    <div className={styles.field}> {/* Re-using .field for consistent layout */}
                      <label className={styles.checkboxLabel}>
                        <input
                          type="checkbox"
                          checked={applyFine}
                          onChange={(e) => setApplyFine(e.target.checked)}
                        />
                        Aplicar Multa por Atraso
                      </label>
                    </div>
                    {applyFine && (
                      <div className={styles.field}> {/* Re-using .field for consistent layout */}
                        <span className={styles.label}>Valor da Multa (R$)</span>
                        <input
                          type="number"
                          className={styles.input}
                          value={fineAmount}
                          onChange={(e) => setFineAmount(e.target.value)}
                          placeholder="Ex: 5.00"
                          min="0.01"
                          step="0.01"
                        />
                      </div>
                    )}
                  </div>
                )}
              </>
            )}
          </div>
        </div>

        <div className={styles.footer}>
          <button className={styles.saveBtn} onClick={handleSalvar}>
            Salvar
          </button>
        </div>
      </div>
    </div>
  );
}