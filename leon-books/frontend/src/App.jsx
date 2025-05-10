import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import { Header } from './components/Header';
import { Sidebar } from './components/Sidebar';
import { Dashboard } from './pages/Dashboard';
import { GerenciamentoLivros } from './pages/GerenciamentoLivros';
import { GerenciamentoMembros } from './pages/GerenciamentoMembros';
import { EmprestimosDevolucoes } from './pages/EmprestimosDevolucoes';
import './global.css';
import styles from './App.module.css';

export function App() {
  
  return (
    <div>
      <Header />

        <div className={styles.wrapper}>
          <Sidebar />

          <main>
            
          </main>
          
        </div>
    </div>
  )
}