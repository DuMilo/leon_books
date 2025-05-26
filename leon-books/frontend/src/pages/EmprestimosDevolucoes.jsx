import styles from './EmprestimosDevolucoes.module.css';
import { SearchBox } from '../components/SearchBox';
import { Handshake } from 'phosphor-react';
import { useState } from 'react';

const emprestimosMock = [
  { id: 1, titulo: 'O Pequeno Príncipe', autor: 'Antoine de Saint-Exupéry', status: 'em dia' },
  { id: 2, titulo: 'Dom Casmurro', autor: 'Machado de Assis', status: 'atrasado' },
  { id: 3, titulo: '1984', autor: 'George Orwell', status: 'em dia' },
  { id: 4, titulo: 'Capitães da Areia', autor: 'Jorge Amado', status: 'atrasado' },
];

export function EmprestimosDevolucoes() {
  const [busca, setBusca] = useState('');

  const emprestimosFiltrados = emprestimosMock.filter((item) =>
    item.titulo.toLowerCase().includes(busca.toLowerCase()) ||
    item.autor.toLowerCase().includes(busca.toLowerCase()) ||
    item.status.toLowerCase().includes(busca.toLowerCase())
  );

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

      <div className={styles.membersearch}>
        <SearchBox onSearch={setBusca} />
      </div>

      <div className={styles.listaEmprestimos}>
        {emprestimosFiltrados.length > 0 ? (
          emprestimosFiltrados.map((item) => (
            <div key={item.id} className={styles.emprestimoCard}>
              <p className={styles.titulo}><strong>{item.titulo}</strong></p>
              <p className={styles.autor}>{item.autor}</p>
              <span
                className={
                  item.status === 'em dia'
                    ? styles.statusEmDia
                    : styles.statusAtrasado
                }
              >
                {item.status === 'em dia' ? 'Em dia' : 'Atrasado'}
              </span>
            </div>
          ))
        ) : (
          <p className={styles.semResultados}>Nenhum empréstimo encontrado.</p>
        )}
      </div>
    </div>
  );
}
