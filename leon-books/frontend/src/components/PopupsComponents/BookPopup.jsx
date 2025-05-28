import styles from './BookPopup.module.css';
import { X } from 'phosphor-react';
import { useState, useEffect } from 'react';

export function BookPopup({ livro, onClose, onUpdate, onRemove, onAdd }) {
  const modoAdicionar = livro === null;

  const [modoEdicao, setModoEdicao] = useState({
    titulo: modoAdicionar,
    autor: modoAdicionar,
    isbn: modoAdicionar,
  });

  const [dados, setDados] = useState(
    livro ?? {
      titulo: '',
      autor: '',
      isbn: '',
      status: 'disponível',
    }
  );

  useEffect(() => {
    if (livro) {
      setDados(livro);
      setModoEdicao({ titulo: false, autor: false, isbn: false });
    } else {
      setDados({ titulo: '', autor: '', isbn: '', status: 'disponível' });
      setModoEdicao({ titulo: true, autor: true, isbn: true });
    }
  }, [livro]);

  const toggleEdicao = (campo) => {
    setModoEdicao((prev) => ({ ...prev, [campo]: !prev[campo] }));
  };

  const handleChange = (e, campo) => {
    setDados((prev) => ({ ...prev, [campo]: e.target.value }));
  };

  const handleSalvar = () => {
    if (!dados.titulo || !dados.autor || !dados.isbn) {
      alert('Preencha todos os campos!');
      return;
    }

    if (modoAdicionar) {
      onAdd(dados);
    } else {
      onUpdate(dados);
    }

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
          <div className={styles.cover}>
            {modoAdicionar ? 'Novo Livro' : 'Livro'}
          </div>

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
                    onBlur={() => !modoAdicionar && toggleEdicao(campo)}
                    autoFocus={modoAdicionar}
                  />
                ) : (
                  <button
                    className={styles.editBtn}
                    onClick={() => toggleEdicao(campo)}
                  >
                    Editar
                  </button>
                )}
              </div>
            ))}

            {!modoAdicionar && (
              <div className={styles.field}>
                <span className={styles.label}>Status</span>
                <span className={styles.status}>
                  {livro.status === 'disponível' ? 'Disponível' : 'Indisponível'}
                </span>
              </div>
            )}
          </div>
        </div>

        <div className={styles.footer}>
          <button className={styles.saveBtn} onClick={handleSalvar}>
            {modoAdicionar ? 'Adicionar Livro' : 'Salvar Alterações'}
          </button>

          {!modoAdicionar && (
            <button className={styles.removeBtn} onClick={handleRemover}>
              Remover Livro
            </button>
          )}
        </div>
      </div>
    </div>
  );
}