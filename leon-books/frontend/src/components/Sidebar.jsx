import styles from './Sidebar.module.css';
import DBIcon from '../assets/dashboard.png';
import LIcon from '../assets/livros.png';
import MIcon from '../assets/membros.png';
import RIcon from '../assets/retornos.png';

export function Sidebar() {
    return(
        <aside className={styles.sidebar}>
        <div>
          <img src={DBIcon} alt='Dashboard'/>
          <p>Dashboard</p>
        </div>
        <div>
          <img src={LIcon} alt='Livros'/>
          <p>Gerenciamento de Livros</p>
        </div>
        <div>
          <img src={MIcon} alt='Membros'/>
          <p>Gerenciamento de Membros</p>
        </div>
        <div>
          <img src={RIcon} alt='Retornos'/>
          <p>Empréstimos e Retornos</p>     
        </div>
      </aside>
    );
}