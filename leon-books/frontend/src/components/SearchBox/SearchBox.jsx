import { MagnifyingGlass } from 'phosphor-react';
import { useState } from 'react';
import styles from './SearchBox.module.css';

export function SearchBox({ onSearch }) {
  const [input, setInput] = useState('');

  const handleChange = (e) => {
    setInput(e.target.value);
    onSearch(e.target.value);
  };

  return (
    <div className={styles.searchContainer}>
      <input
        type="text"
        placeholder="Procurar..."
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
