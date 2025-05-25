import { useState } from 'react';
import styles from './BookSearch.module.css';
import { MagnifyingGlass } from 'phosphor-react';

export function BookSearch({ onSearch }) {
  const [input, setInput] = useState('');

  const handleChange = (e) => {
    setInput(e.target.value);
    onSearch(e.target.value);
  };

  return (
    <div className={styles.searchContainer}>
      <input
        type="text"
        placeholder="Procurar livros..."
        value={input}
        onChange={handleChange}
        className={styles.searchInput}
      />
      <button className={styles.searchButton}>
        <MagnifyingGlass size={16} />
      </button>
    </div>
  );
}
