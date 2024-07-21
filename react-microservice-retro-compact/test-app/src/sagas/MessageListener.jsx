import {
    take,
    put,
    call,
} from 'redux-saga/effects'
import { eventChannel } from 'redux-saga'

function* postMessageReceiver() {
    return eventChannel(emit => {
        window.addEventListener('message', (event) => {
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

export default postMessageListener