import { Books } from 'phosphor-react';

import { useState } from 'react';

import { SearchBox } from '../components/SearchBox';
import styles from './GerenciamentoLivros.module.css';

const livrosMock = [
  { isbn: 1, titulo: 'Dom Casmurro', autor: 'Machado de Assis' },
  { isbn: 2, titulo: 'A Moreninha', autor: 'Joaquim Manuel de Macedo' },
  { isbn: 3, titulo: 'O Cortiço', autor: 'Aluísio Azevedo' },
  { isbn: 4, titulo: 'Capitães da Areia', autor: 'Jorge Amado' },
  { isbn: 5, titulo: 'Memórias Póstumas de Brás Cubas', autor: 'Machado de Assis' },
  { isbn: 6, titulo: 'Mo Dao Zu Shi', autor: 'Mo Xiang Tong Xiu'},
  { isbn: 7, titulo: 'Erha He Ta De Bai Mao Shizun', autor: 'Rou Bao Bu Chi Rou'}
];

export function GerenciamentoLivros() {
    const [busca, setBusca] = useState('');

    const livrosFiltrados = livrosMock.filter((livro) =>
  livro.titulo.toLowerCase().includes(busca.toLowerCase()) ||
  livro.autor.toLowerCase().includes(busca.toLowerCase()) ||
  String(livro.isbn).toLowerCase().includes(busca.toLowerCase())
);

    const livrosExibidos = livrosFiltrados.slice(0, 4);

    return (
    <div className={styles.livros}> 
    
        <div className={styles.boxes}>

            <div className={styles.box}>
                <p className={styles.boxtitle}>Total de Livros</p>
                <div className={styles.boxcontent}>
                    <p className={styles.boxnumber}>1.031</p>
                    <p className={styles.boxsub}>+13 esse mês</p>
                </div>
                <span className={styles.boxicon}>
                    <Books size={20}/>
                </span>
            </div>
    
            <div className={styles.box}>
                <p className={styles.boxtitle}>Ações Rápidas</p>
                    <div className={styles.actions}>
                        <button className={styles.actionbtn}>Adicionar Livro</button>
                        <button className={styles.actionbtn}>Remover Livro</button>
                    </div>
            </div>

        </div>

      <div className={styles.booksearch}>
        <SearchBox onSearch={setBusca} />
      </div>

      <div className={styles.searchbox}>
        <div className={styles.searchboxes}>
          {livrosExibidos.map((livro) => (
            <div key={livro.id} className={styles.bookbox}>
              <div className={styles.bookimage}>Livro</div>
                <div className={styles.bookinfo}>
                    <p className={styles.booktitle}>{livro.titulo}</p>
                    <p className={styles.bookauthor}>{livro.autor}</p>
                    <button className={styles.editbutton}>Editar</button>
              </div>
            </div>
          ))}
        </div>
      </div>

    </div>
    )
}