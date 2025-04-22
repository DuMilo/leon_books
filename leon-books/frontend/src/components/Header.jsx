import styles from './Header.module.css'
import LBLogo from '../assets/leon-books-symbol.svg'

export function Header() {
    return (
        <div className={styles.div}>
            <header className={styles.header}>
            <img src={LBLogo} alt='Logotipo do Leon Books' />
            <p>
                <span>Leon Books</span>
            </p>
            </header>
        </div>
    )
}