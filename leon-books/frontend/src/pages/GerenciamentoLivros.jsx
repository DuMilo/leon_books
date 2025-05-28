import { Books } from 'phosphor-react';
import { useState } from 'react';

import { SearchBox } from '../components/SearchBox/SearchBox';
import { BookPopup } from '../components/PopupsComponents/BookPopup';

import styles from './GerenciamentoLivros.module.css';

const livrosMock = [
  { isbn: 1, titulo: 'Dom Casmurro', autor: 'Machado de Assis', status: 'disponível' },
  { isbn: 2, titulo: 'A Moreninha', autor: 'Joaquim Manuel de Macedo', status: 'disponível' },
  { isbn: 3, titulo: 'O Cortiço', autor: 'Aluísio Azevedo', status: 'indisponível' },
  { isbn: 4, titulo: 'Capitães da Areia', autor: 'Jorge Amado', status: 'disponível' },
  { isbn: 5, titulo: 'Memórias Póstumas de Brás Cubas', autor: 'Machado de Assis', status: 'disponível' },
  { isbn: 6, titulo: 'Mo Dao Zu Shi', autor: 'Mo Xiang Tong Xiu', status: 'indisponível' },
  { isbn: 7, titulo: 'Erha He Ta De Bai Mao Shizun', autor: 'Rou Bao Bu Chi Rou', status: 'disponível' }
];

export function GerenciamentoLivros() {
  const [busca, setBusca] = useState('');
  const [livros, setLivros] = useState(livrosMock);
  const [livroSelecionado, setLivroSelecionado] = useState(null);
  const [modoAdicionar, setModoAdicionar] = useState(false);

  const livrosFiltrados = livros.filter((livro) =>
    livro.titulo.toLowerCase().includes(busca.toLowerCase()) ||
    livro.autor.toLowerCase().includes(busca.toLowerCase()) ||
    String(livro.isbn).includes(busca)
  );

  const livrosExibidos = livrosFiltrados.slice(0, 4);

  function handleEditarLivro(livro) {
    setLivroSelecionado(livro);
  }

  function handleFecharPopup() {
    setLivroSelecionado(null);
    setModoAdicionar(false);
  }

  function handleAtualizarLivro(livroAtualizado) {
    const atualizados = livros.map((l) =>
      l.isbn === livroAtualizado.isbn ? livroAtualizado : l
    );
    setLivros(atualizados);
    handleFecharPopup();
  }

  function handleRemoverLivro(isbn) {
    const filtrados = livros.filter((l) => l.isbn !== isbn);
    setLivros(filtrados);
    handleFecharPopup();
  }

  function handleAdicionarLivro(novoLivro) {
    const isbnExistente = livros.some((l) => String(l.isbn) === String(novoLivro.isbn));
    if (isbnExistente) {
      alert('Já existe um livro com esse ISBN.');
      return;
    }

    setLivros([...livros, novoLivro]);
    handleFecharPopup();
  }

  function handleAbrirAdicionar() {
    setModoAdicionar(true);
  }

  return (
    <div className={styles.livros}>
      <div className={styles.boxes}>
        <div className={styles.box}>
          <p className={styles.boxtitle}>Total de Livros</p>
          <div className={styles.boxcontent}>
            <p className={styles.boxnumber}>{livros.length}</p>
            <p className={styles.boxsub}>+13 esse mês</p>
          </div>
          <span className={styles.boxicon}>
            <Books size={20} />
          </span>
        </div>

        <div className={styles.box}>
          <p className={styles.boxtitle}>Ações Rápidas</p>
          <div className={styles.actions}>
            <button className={styles.actionbtn} onClick={handleAbrirAdicionar}>
              Adicionar Livro
            </button>
          </div>
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
                  className={styles.editbutton}
                  onClick={() => handleEditarLivro(livro)}
                >
                  Editar
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {(livroSelecionado || modoAdicionar) && (
        <BookPopup
          livro={livroSelecionado}
          onClose={handleFecharPopup}
          onUpdate={handleAtualizarLivro}
          onRemove={handleRemoverLivro}
          onAdd={handleAdicionarLivro}
        />
      )}
    </div>
  );
}