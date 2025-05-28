import styles from './EmprestimosDevolucoes.module.css';
import { SearchBox } from '../components/SearchBox/SearchBox';
import { LoanPopup } from '../components/PopupsComponents/LoanPopup';
import { Handshake } from 'phosphor-react';
import { useState } from 'react';

const livrosMockInicial = [
  { isbn: 1, titulo: 'Dom Casmurro', autor: 'Machado de Assis', status: 'disponível', devedor: '' },
  { isbn: 2, titulo: 'A Moreninha', autor: 'Joaquim Manuel de Macedo', status: 'indisponível', devedor: 'Ana Silva' },
  { isbn: 3, titulo: 'O Cortiço', autor: 'Aluísio Azevedo', status: 'disponível', devedor: '' },
  { isbn: 4, titulo: 'Capitães da Areia', autor: 'Jorge Amado', status: 'indisponível', devedor: 'Carlos Lima' },
  { isbn: 5, titulo: 'Memórias Póstumas de Brás Cubas', autor: 'Machado de Assis', status: 'disponível', devedor: '' },
  { isbn: 6, titulo: 'Mo Dao Zu Shi', autor: 'Mo Xiang Tong Xiu', status: 'indisponível', devedor: 'Sofia Travassos' },
  { isbn: 7, titulo: 'Erha He Ta De Bai Mao Shizun', autor: 'Rou Bao Bu Chi Rou', status: 'disponível', devedor: '' }
];

export function EmprestimosDevolucoes() {
  const [busca, setBusca] = useState('');
  const [livros, setLivros] = useState(livrosMockInicial);
  const [livroSelecionado, setLivroSelecionado] = useState(null);

  const livrosFiltrados = livros.filter((livro) =>
    livro.titulo.toLowerCase().includes(busca.toLowerCase()) ||
    livro.autor.toLowerCase().includes(busca.toLowerCase()) ||
    String(livro.isbn).toLowerCase().includes(busca.toLowerCase())
  );

  const livrosExibidos = livrosFiltrados.slice(0, 4);

  function handleAbrirPopupStatus(livro) {
    setLivroSelecionado(livro);
  }

  function handleFecharPopup() {
    setLivroSelecionado(null);
  }

  function handleAtualizarStatusLivro(livroAtualizado) {
    setLivros(prevLivros =>
      prevLivros.map(livro =>
        livro.isbn === livroAtualizado.isbn ? { ...livro, ...livroAtualizado } : livro
      )
    );
    handleFecharPopup();
  }

  return (
    <div className={styles.emprestimo}>
      <div className={styles.boxes}>
        <div className={styles.box}>
          <p className={styles.title}>Empréstimos Ativos</p>
          <p className={styles.number}>{livros.filter(l => l.status === 'indisponível').length}</p>
          <p className={styles.subtitle}>Verifique os prazos!</p>
          <span className={styles.boxicon}>
            <Handshake size={20} />
          </span>
        </div>
      </div>

      <div className={styles.booksearch}>
        <SearchBox onSearch={setBusca} />
      </div>

      <div className={styles.searchbox}>
        <div className={styles.searchboxes}>
          {livrosExibidos.map((livro) => (
            <div key={livro.isbn} className={styles.bookbox}>
              <div className={styles.bookimage}>Livro</div>
              <div className={styles.bookinfo}>
                <p className={styles.booktitle}>{livro.titulo}</p>
                <p className={styles.bookauthor}>{livro.autor}</p>
                <button
                  className={styles.detailbutton}
                  onClick={() => handleAbrirPopupStatus(livro)}
                >
                  Status
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {livroSelecionado && (
        <LoanPopup
          livro={livroSelecionado}
          onClose={handleFecharPopup}
          onUpdate={handleAtualizarStatusLivro}
        />
      )}
    </div>
  );
}
