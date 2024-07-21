import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'

import { Provider } from 'react-redux'

import SagaInit from './sagas/SagaInit.jsx'
import { initialState } from './reducers/reducer.jsx'

const store = SagaInit(initialState)

ReactDOM.createRoot(document.getElementById('root')).render(
  <Provider store={store}>
    <App />
  </Provider>
)
