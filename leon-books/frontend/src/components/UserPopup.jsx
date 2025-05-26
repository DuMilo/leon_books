import styles from './UserPopup.module.css';
import { X } from 'phosphor-react';

import { useState } from 'react';
import { SearchBox } from '../components/SearchBox';

export function UserPopup({ membro, onClose }) {
  const [busca, setBusca] = useState('');

  const livrosFiltrados = membro.emprestimos.filter((livro) =>
    livro.titulo.toLowerCase().includes(busca.toLowerCase()) ||
    livro.autor.toLowerCase().includes(busca.toLowerCase()) ||
    livro.status.toLowerCase().includes(busca.toLowerCase())
  );

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
          <p>Empréstimos anteriores: 12</p>
          <p>Multas pendentes: 0</p>
        </div>

        <p className={styles.emprestimo}>Livros em Empréstimo</p>

        <div className={styles.buscaLivros}>
          <SearchBox onSearch={setBusca} />
        </div>

        {livrosFiltrados.length > 0 ? (
          <div className={styles.livrosContainer}>
            {livrosFiltrados.map((livro, index) => (
              <div key={index} className={styles.livroCard}>
                <div className={styles.livroCapa}>Livro</div>
                <div className={styles.livroInfo}>
                  <p>{livro.titulo}</p>
                  <p>{livro.autor}</p>
                  <span
                    className={
                      livro.status === 'em dia'
                        ? styles.statusEmDia
                        : styles.statusAtrasado
                    }
                  >
                    {livro.status === 'em dia' ? 'Em dia' : 'Atrasado'}
                  </span>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className={styles.semLivros}>Nenhum livro encontrado.</p>
        )}
      </div>
    </div>
  );
}