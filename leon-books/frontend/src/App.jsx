import { Header } from './components/Header';
import { Sidebar } from './components/Sidebar';

import { BrowserRouter } from 'react-router-dom';
import { Router } from './Router';

import './global.css';
import styles from './App.module.css';

export function App() {
  
  return (
    <div>
      <Header />

        <div className={styles.wrapper}>
          <Sidebar />
          <BrowserRouter>
            <Router />
          </BrowserRouter>
          <main>
            
          </main>
          
        </div>
    </div>
  )
}