import { applyMiddleware, legacy_createStore } from 'redux'
import createSagaMiddleware from 'redux-saga'
import rootSaga from './saga/Saga'
import { reducer } from '../reducers/reducer'

const SagaInit = (initialState) => {
    const sagaMiddleware = createSagaMiddleware()
    const store = legacy_createStore(reducer, initialState, applyMiddleware(sagaMiddleware))
    sagaMiddleware.run(rootSaga)
    return store;
}

export default SagaInit