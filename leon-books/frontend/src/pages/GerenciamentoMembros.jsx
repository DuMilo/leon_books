import styles from './GerenciamentoMembros.module.css';
import { SearchBox } from '../components/SearchBox';
import { UserPopup } from '../components/UserPopup';

import { useState } from 'react';
import { UserList } from 'phosphor-react';

const membrosMock = [
  { id: 1, nome: 'Milo Moreira', email: 'milomoreira@gmail.com', emprestimos: [
      {
        titulo: 'Machine Learning',
        autor: 'Matt Harrison',
        status: 'em dia',
      },
      {
        titulo: 'React na Prática',
        autor: 'Dan Abramov',
        status: 'atrasado',
      },
    ], },
  { id: 2, nome: 'Sofia Travassos', email: 'sofiatravassos@gmail.com', emprestimos: [
      { 
        titulo: 'Clean Code', 
        autor: 'Robert C. Martin', 
        status: 'em dia' 
      },
    ], },
  { id: 3, nome: 'Marisa Cardoso', email: 'marisacardoso@gmail.com', emprestimos: ''},
  { id: 4, nome: 'Manuela Castro', email: 'manuelacastro@gmail.com', emprestimos: ''},
  { id: 5, nome: 'Murilo Gomes', email: 'murilogomes@gmail.com', emprestimos: ''},
  { id: 6, nome: 'André Melo', email: 'andremelo@gmail.com', emprestimos: ''},
  { id: 7, nome: 'Ana Rocha', email: 'anarocha@gmail.com', emprestimos: ''}
];

export function GerenciamentoMembros() {
    const [busca, setBusca] = useState('');
    const [membroSelecionado, setMembroSelecionado] = useState(null); 
    
    const membrosFiltrados = membrosMock.filter((membro) =>
      membro.email.toLowerCase().includes(busca.toLowerCase()) ||
      membro.nome.toLowerCase().includes(busca.toLowerCase()) ||
      String(membro.id).toLowerCase().includes(busca.toLowerCase())
    );
    
    const membrosExibidos = membrosFiltrados.slice(0, 4);

    const abrirPopup = (membro) => setMembroSelecionado(membro);
    const fecharPopup = () => setMembroSelecionado(null);

    return (
        <div className={styles.membros}>
            <div className={styles.boxes}>
                <div className={styles.box}>
                    <p className={styles.title}>Membros</p>
                    <p className={styles.number}>389</p>
                    <p className={styles.subtitle}>+8 essa semana</p>

                <span className={styles.boxicon}>
                    <UserList size={20}/>
                </span>

                </div>
            </div>

            <div className={styles.membersearch}>
                <SearchBox onSearch={setBusca} />
            </div>

            <div className={styles.searchbox}>
                <div className={styles.searchboxes}>
                    {membrosExibidos.map((membro) => (
                        <div key={membro.id} className={styles.memberbox}>
                            <div className={styles.memberimage}>Membro</div>
                                <div className={styles.memberinfo}>
                                    <p className={styles.membername}>{membro.nome}</p>
                                    <p className={styles.memberemail}>{membro.email}</p>
                                    <button 
                                    className={styles.detailbutton}
                                    onClick={() => abrirPopup(membro)}>
                                        Detalhes do Perfil
                                    </button>
                                </div>
                        </div>
          ))}
                </div>
            </div>

      {membroSelecionado && (
        <UserPopup membro={membroSelecionado} onClose={fecharPopup} />
      )}

        </div>
    )
}