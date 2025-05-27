import styles from './BookPopup.module.css';
import { X } from 'phosphor-react';
import { useState } from 'react';

export function BookPopup({ livro, onClose, onUpdate, onRemove }) {
  const [modoEdicao, setModoEdicao] = useState({
    titulo: false,
    autor: false,
    isbn: false
  });

  const [dados, setDados] = useState({ ...livro });

  const toggleEdicao = (campo) => {
    setModoEdicao((prev) => ({ ...prev, [campo]: !prev[campo] }));
  };

  const handleChange = (e, campo) => {
    setDados((prev) => ({ ...prev, [campo]: e.target.value }));
  };

  const handleSalvar = () => {
    onUpdate(dados);
    onClose();
  };

  const handleRemover = () => {
    onRemove(dados.isbn);
    onClose(); 
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.popup}>
        <button className={styles.close} onClick={onClose}>
          <X size={16} />
        </button>

        <div className={styles.content}>
          <div className={styles.cover}>Livro</div>

          <div className={styles.info}>
            {['titulo', 'autor', 'isbn'].map((campo) => (
              <div key={campo} className={styles.field}>
                <span className={styles.label}>
                  {campo === 'titulo'
                    ? 'Nome do Livro'
                    : campo.charAt(0).toUpperCase() + campo.slice(1)}
                </span>

                {modoEdicao[campo] ? (
                  <input
                    className={styles.input}
                    value={dados[campo]}
                    onChange={(e) => handleChange(e, campo)}
                    onBlur={() => toggleEdicao(campo)}
                    autoFocus
                  />
                ) : (
                  <button className={styles.editBtn} onClick={() => toggleEdicao(campo)}>
                    Editar
                  </button>
                )}
              </div>
            ))}

            <div className={styles.field}>
              <span className={styles.label}>Status</span>
              <span className={styles.status}>
                {livro.status === 'disponivel' ? 'Disponível' : 'Indisponível'}
              </span>
            </div>
          </div>
        </div>

        <div className={styles.footer}>
          <button className={styles.saveBtn} onClick={handleSalvar}>
            Salvar Alterações
          </button>
          <button className={styles.removeBtn} onClick={handleRemover}>
            Remover Livro
          </button>
        </div>
      </div>
    </div>
  );
}