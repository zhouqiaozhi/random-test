import { useEffect } from 'react'
import './App.css'

import Header from './components/Header'
import Bottom from './components/Bottom'
import MainContent from './components/MainContent'
import EmptyPage from './containers/EmptyPage'
import FirstPage from './containers/FirstPage'
import SecondPage from './containers/SecondPage'
import LastPage from './containers/LastPage'
import { useSelector } from 'react-redux'
import Selectors from './selectors/Selectors'

function App() {
  const step = useSelector(Selectors.selectStep())
  return (
    <div className='main_container'>
      <Header/>
        <MainContent>
          {step === 0 && (<EmptyPage />)}
          {step === 1 && (<FirstPage />)}
          {step === 2 && (<SecondPage />)}
          {step === 3 && (<LastPage />)}
        </MainContent>
      <Bottom/>
    </div>
  )
}

export default App
