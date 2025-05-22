import { Header } from './components/Header';
import { Sidebar } from './components/Sidebar';

import { BrowserRouter } from 'react-router-dom';
import { Router } from './Router';

import './global.css';
import styles from './App.module.css';

export function App() {
  
  return (
      <div style={{width: '100%', height: '100vh', overflow: 'auto'}}>
        <Header />

        <div className={styles.wrapper}>
          <Sidebar />

          <BrowserRouter>
            <Router />
          </BrowserRouter>
          
        </div>
      </div>
  )
}