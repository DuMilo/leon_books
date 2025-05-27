import styles from './EmprestimosDevolucoes.module.css';
import { SearchBox } from '../components/SearchBox/SearchBox';
import { Handshake } from 'phosphor-react';
import { useState } from 'react';

const livrosMock = [
  { isbn: 1, titulo: 'Dom Casmurro', autor: 'Machado de Assis' },
  { isbn: 2, titulo: 'A Moreninha', autor: 'Joaquim Manuel de Macedo' },
  { isbn: 3, titulo: 'O Cortiço', autor: 'Aluísio Azevedo' },
  { isbn: 4, titulo: 'Capitães da Areia', autor: 'Jorge Amado' },
  { isbn: 5, titulo: 'Memórias Póstumas de Brás Cubas', autor: 'Machado de Assis' },
  { isbn: 6, titulo: 'Mo Dao Zu Shi', autor: 'Mo Xiang Tong Xiu'},
  { isbn: 7, titulo: 'Erha He Ta De Bai Mao Shizun', autor: 'Rou Bao Bu Chi Rou'}
];

export function EmprestimosDevolucoes() {
    const [busca, setBusca] = useState('');
    
    const livrosFiltrados = livrosMock.filter((livro) =>
      livro.titulo.toLowerCase().includes(busca.toLowerCase()) ||
      livro.autor.toLowerCase().includes(busca.toLowerCase()) ||
      String(livro.isbn).toLowerCase().includes(busca.toLowerCase())
    );
    
    const livrosExibidos = livrosFiltrados.slice(0, 4);

  return (
    <div className={styles.emprestimo}>
      <div className={styles.boxes}>
        <div className={styles.box}>
          <p className={styles.title}>Empréstimos Ativos</p>
          <p className={styles.number}>183</p>
          <p className={styles.subtitle}>34 prazos vencem essa semana</p>
          <span className={styles.boxicon}>
            <Handshake size={20} />
          </span>
        </div>
      </div>

      <div className={styles.booksearch}>
        <SearchBox onSearch={setBusca}/>
      </div>

            <div className={styles.searchbox}>
                <div className={styles.searchboxes}>
                    {livrosExibidos.map((livro) => (
                        <div key={livro.id} className={styles.bookbox}>
                            <div className={styles.bookimage}>Livro</div>
                                <div className={styles.bookinfo}>
                                    <p className={styles.booktitle}>{livro.titulo}</p>
                                    <p className={styles.bookauthor}>{livro.autor}</p>
                                    <button 
                                    className={styles.detailbutton}>
                                        Status
                                    </button>
                                </div>
                        </div>
          ))}
                </div>
            </div>

    </div>
  );
}
