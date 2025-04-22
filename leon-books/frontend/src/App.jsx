import { Header } from './components/Header';
import { Post } from './Post';
import './styles.css';

export function App() {
  
  return (
    <div>
      <Header />
      <Post 
        author='Milo Moreira' 
        content='Lorem ipsum'
      />  
      <Post 
      author='River Pinotti' 
      content='Você tá endauldi agrummit'
      />  
    </div>
  )
}



