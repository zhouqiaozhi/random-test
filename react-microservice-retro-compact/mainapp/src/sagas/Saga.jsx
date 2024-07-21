import {
    select,
    take,
    put,
    call,
    delay,
    spawn,
    takeLatest
} from 'redux-saga/effects'

import { eventChannel } from 'redux-saga'

import LogUtils from '../utils/LogUtils'
import Selectors from '../selectors/Selectors'
import ActionListenerName from '../constants/ActionListenerName'
import ActionResponseName from '../constants/ActionResponseName'
import Action from '../actions/Action'

function* getCache() {
    const cache = yield select(Selectors.selectCache())
    yield delay(0)
    document.querySelector("#test-app").contentWindow.postMessage({ type: ActionResponseName.GET_CACHE_RESPONSE, payload: cache }, "*")
}

function* closePopupResponse(action) {
    document.querySelector("#test-app").contentWindow.postMessage({ type: ActionResponseName.CLOSE_POPUP_RESPONSE, payload: action.payload }, "*")
    yield put(Action.DO_CLOSE_POPUP())
}

function* postMessageReceiver() {
    return eventChannel(emit => {
        window.addEventListener('message', (event) => {
            LogUtils.log('Receive', event)
            if(typeof event.data === 'string') {
                emit({ type: event.data })
            } else {
                emit(event.data)
            }
        })
        return () => {
            window.removeEventListener('message', emit)
        }
    })
}

function* postMessageListener() {
    const channel = yield call(postMessageReceiver)
    while(true) {
        const message = yield take(channel)
        yield put(message)
    }
}

function* rootSaga() {
    LogUtils.warn("DEBUG ON")
    yield spawn(postMessageListener)
    yield takeLatest(ActionListenerName.GET_CACHE, getCache)
    yield takeLatest(ActionResponseName.CLOSE_POPUP_RESPONSE, closePopupResponse)
}

export default rootSaga