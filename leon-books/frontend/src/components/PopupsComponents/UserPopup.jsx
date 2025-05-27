import styles from './UserPopup.module.css';
import { X } from 'phosphor-react';

export function UserPopup({ membro, onClose }) {
  return (
    <div className={styles.overlay}>
      <div className={styles.popup}>
        <button className={styles.closeButton} onClick={onClose}>
          <X />
        </button>

        <p className={styles.nome}>{membro.nome}</p>

        <div className={styles.info}>
          <p>E-mail: {membro.email}</p>
          <p>Id: {membro.id}</p>
          <p>Empréstimos anteriores: {membro.emprestimosAnteriores}</p>
          <p>Multas pendentes: {membro.multasPendentes}</p>
        </div>

        <p className={styles.emprestimo}>Livros em Empréstimo</p>
        {membro.emprestimos.length > 0 ? (
          <div className={styles.livrosContainer}>
            {membro.emprestimos.map((livro, index) => (
              <div key={index} className={styles.livroCard}>
                <div className={styles.livroCapa}>Livro</div>
                <div className={styles.livroInfo}>
                  <p>{livro.titulo}</p>
                  <p>{livro.autor}</p>
                  <span className={
                    livro.status === 'em dia'
                      ? styles.statusEmDia
                      : styles.statusAtrasado
                  }>
                    {livro.status === 'em dia' ? 'Em dia' : 'Atrasado'}
                  </span>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className={styles.semLivros}>Nenhum livro em empréstimo.</p>
        )}
      </div>
    </div>
  );
}
