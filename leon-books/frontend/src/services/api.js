import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// operações com Livros
export function fetchBooks() {
  return api.get('/livros');
}

export function createBook(bookData) {
  return api.post('/livros', bookData);
}

export function updateBook(id, bookData) {
  return api.put(`/livros/${id}`, bookData);
}

export function deleteBook(id) {
  return api.delete(`/livros/${id}`);
}

// operações com Membros
export function fetchMembers() {
  return api.get('/membros');
}

export function createMember(memberData) {
  return api.post('/membros', memberData);
}

// operações com Empréstimos
export function fetchLoans() {
  return api.get('/emprestimos');
}

export function createLoan(loanData) {
  return api.post('/emprestimos', loanData);
}

export function returnLoan(id) {
  return api.put(`/emprestimos/${id}/devolucao`);
}

export default api;