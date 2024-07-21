import {
    call,
} from 'redux-saga/effects'

import MessageHelper from "./MessageHelper"

function* showPopup(body) {
    yield call(MessageHelper.send, 'DO_SHOW_POPUP', body)
}
function* closePopup() {
    yield call(MessageHelper.send, 'DO_CLOSE_POPUP')
}

export default {
    showPopup,
    closePopup
}