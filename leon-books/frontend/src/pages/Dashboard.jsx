import styles from './Dashboard.module.css'
import { UserCircle, Book, Handshake, Users, Bell, ArrowsClockwise} from 'phosphor-react'

export function Dashboard() {
    return (
        <div className={styles.dashboard}> 

            <div className={styles.boxes}> 
                <div className={styles.box}>

                    <p className={styles.title}>Total de Livros</p>
                    <p className={styles.number}>1.031</p>
                    <p className={styles.subtitle}>+13 esse mês</p>

                <span className={styles.boxicon}>
                    <Book size={20}/>
                </span>

                </div>

                <div className={styles.box}>

                    <p className={styles.title}>Empréstimos Ativos</p>
                    <p className={styles.number}>183</p>
                    <p className={styles.subtitle}>34 prazos vencem essa semana</p>

                <span className={styles.boxicon}>
                    <Handshake size={20}/>
                </span>

                </div>

                <div className={styles.box}>

                    <p className={styles.title}>Membros</p>
                    <p className={styles.number}>389</p>
                    <p className={styles.subtitle}>+8 essa semana</p>

                <span className={styles.boxicon}>
                    <Users size={20}/>
                </span>

                </div>

            </div>

            <div>
                <div className={styles.bigbox}>

                    <p className={styles.titlebigbox}>Atividades Recentes</p>

                    <div className={styles.members}>
                        <UserCircle size={30}/>
                        <p className={styles.notificationname}>Giullia Lima emprestou 'Mo Dao Zu Shi'</p>
                    </div>

                    <div>
                        <p className={styles.notificationtime}>2 horas atrás</p>
                    </div>

                    <div className={styles.members}>
                        <UserCircle size={30}/>
                        <p className={styles.notificationname}>Luan Barreto emprestou 'Os Miseráveis'</p>
                    </div>

                    <div>
                        <p className={styles.notificationtime}>2 horas atrás</p>
                    </div>

                </div>
            </div>

            <div className={styles.boxes}>

                <div className={styles.twoboxes}>
                    <p className={styles.notification}>Notificações</p>

                    <span className={styles.boxicon}>
                        <Bell size={20}/>
                    </span>

                    <div className={styles.belowboxes}>
                        <p className={styles.boldnotification}>3 livros com prazo vencendo hoje</p>
                        <p className={styles.graynotification}>Checar e registrar lembretes</p>


                    </div>

                    <div className={styles.belowboxes}>
                        <p className={styles.boldnotification}>Novo carregamento de livros chegando</p>
                        <p className={styles.graynotification}>12 novos títulos a serem catalogados</p>
                    </div>
                    
                </div>

                <div className={styles.twoboxes}>

                    <span className={styles.boxicon}>
                        <ArrowsClockwise size={20}/>
                    </span>

                    <p className={styles.notification}>Prazos para Retorno</p>

                    <div className={styles.belowboxes}>
                        <p className={styles.boldnotification}>Mo Dao Zu Shi</p>
                        <p className={styles.graynotification}>Prazo: 31 Out, 2025</p>
                    </div>

                    <div className={styles.belowboxes}>
                        <p className={styles.boldnotification}>Data Science do Zero</p>
                        <p className={styles.graynotification}>Prazo: 04 Abr, 2025</p>
                    </div>

                </div>

            </div>

        </div>
    )
}