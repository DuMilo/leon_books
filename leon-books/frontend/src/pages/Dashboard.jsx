import styles from './Dashboard.module.css'

export function Dashboard() {
    return (
        <div className={styles.dashboard}> 

            <div className={styles.boxes}> 
                <div className={styles.box}>a</div>
                <div className={styles.box}>a</div>
                <div className={styles.box}>a</div>
            </div>

            <div>
                <div className={styles.bigbox}>a</div>
            </div>

            <div className={styles.boxes}>
                <div className={styles.twoboxes}>a</div>
                <div className={styles.twoboxes}>a</div>
            </div>

        </div>
    )
}