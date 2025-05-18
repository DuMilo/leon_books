import { Routes, Route } from 'react-router-dom'

import { Dashboard } from './pages/Dashboard'
import { GerenciamentoLivros } from './pages/GerenciamentoLivros'
import { GerenciamentoMembros } from './pages/GerenciamentoMembros'
import { EmprestimosDevolucoes } from './pages/EmprestimosDevolucoes';


export function Router() {
    return (
        <Routes>
            <Route path='/' element={<Dashboard />}/>
            <Route path='/livros' element={<GerenciamentoLivros />}/>
            <Route path='/membros' element={<GerenciamentoMembros />}/>
            <Route path='/emprestimos' element={<EmprestimosDevolucoes />}/>
        </Routes>
    );
}