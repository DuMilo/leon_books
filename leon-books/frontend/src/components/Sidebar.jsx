import { ChartLine, UsersThree, Books, ArrowsLeftRight } from 'phosphor-react'

import styles from './Sidebar.module.css';

export function Sidebar() {
    return(
      <aside className={styles.sidebar}>
        <div className={styles.profile}>

          <div>
            <ChartLine size={16} />
            <a href='#'>
              Dashboard
            </a>
          </div>

          <div>
            <Books size={20} />
            <a href='#'>
              Gerenciamento de Livros
            </a>
          </div>

          <div>
            <UsersThree size={20} />
            <a href='#'>
              Gerenciamento de Membros
            </a>
          </div>

          <div>
            <ArrowsLeftRight size={20} />
            <a href='#'>
              Empréstimos e Retornos
            </a>     
          </div>

        </div>
      </aside>
    );
}