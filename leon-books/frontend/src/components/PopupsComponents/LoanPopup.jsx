import styles from './LoanPopup.module.css'
import { X } from 'phosphor-react'
import { useState, useEffect } from 'react'

export function LoanPopup({ livro, onClose, onUpdate }) {
  const [status, setStatus] = useState(livro.status)
  const [devedor, setDevedor] = useState(livro.devedor || '')

  useEffect(() => {
    setStatus(livro.status)
    setDevedor(livro.devedor || '')
  }, [livro])

  const handleSalvar = () => {
    if (status === 'indisponível' && !devedor.trim()) {
      alert('Informe o nome do devedor.')
      return
    }
    const livroAtualizado = {
      ...livro,
      status,
      devedor: status === 'indisponível' ? devedor : ''
    }
    onUpdate(livroAtualizado)
    onClose()
  }

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
                onChange={(e) => setStatus(e.target.value)}
              >
                <option value="disponível">Disponível</option>
                <option value="indisponível">Indisponível (Emprestado)</option>
              </select>
            </div>

            {status === 'indisponível' && (
              <div className={styles.field}>
                <span className={styles.label}>Devedor</span>
                <input
                  className={styles.input}
                  value={devedor}
                  onChange={(e) => setDevedor(e.target.value)}
                  placeholder="Nome do devedor"
                />
              </div>
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
  )
}
