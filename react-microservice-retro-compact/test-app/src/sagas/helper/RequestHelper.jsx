
import {
    take,
    put,
    call,
    all,
} from 'redux-saga/effects'
import Action from '../../actions/Action'
import ActionTaskName from '../../constants/ActionTaskName'
import MessageHelper from './MessageHelper'

function* genericCallbackRequest() {
    const listener = {}
    listener.req = put(Action.DO_SET_CALLBACK())
    listener.res = take(ActionTaskName.SET_CALLBACK_RESULT)
    const { res } = yield all(listener)
    const payload = res.payload
    return payload;
}

function* genericCacheRequest(type, event) {
    const listener = {}
    listener.req = call(MessageHelper.send, type)
    listener.res = take(event)
    const { res } = yield all(listener)
    return res.payload
}

export default {
    genericCallbackRequest,
    genericCacheRequest
}