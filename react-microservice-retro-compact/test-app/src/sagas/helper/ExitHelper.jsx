import {
    call,
    all,
    take,
} from 'redux-saga/effects'

import MessageHelper from "./MessageHelper"
import Popup from '../../constants/Popup'
import ActionTaskName from '../../constants/ActionTaskName'
import PopupHelper from './PopupHelper'
import LogUtils from '../../utils/LogUtils'

function* cancel() {
    const listener = {}
    listener.req = call(PopupHelper.showPopup, Popup.CANCEL)
    listener.res = take(ActionTaskName.CLOSE_POPUP_RESPONSE)
    const { res } = yield all(listener)
    return res.payload
}

function* exit(exitCode) {
    LogUtils.log("Exit with code " + exitCode)
    yield call(MessageHelper.send, 'DO_CLOSE')
}

export default {
    exit,
    cancel
}