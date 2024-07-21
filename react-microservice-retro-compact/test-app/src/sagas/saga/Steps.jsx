import {
    put,
    call,
} from 'redux-saga/effects'

import PopupHelper from "../helper/PopupHelper"
import LogUtils from '../../utils/LogUtils'
import Popup from '../../constants/Popup'
import PageHelper from '../helper/PageHelper'
import RequestHelper from '../helper/RequestHelper'
import PayloadHelper from '../../helper/PayloadHelper'

import Action from '../../actions/Action'
import ActionTaskName from '../../constants/ActionTaskName'

const defaultPayload = {
    value: 'SUCCESS'
}

function* step0() {
    LogUtils.log("--------- start step0 ---------")
    // open popup loading
    yield call(PopupHelper.showPopup, Popup.LOADING)
    // download cache
    const cache = yield call(RequestHelper.genericCacheRequest, ActionTaskName.GET_CACHE, ActionTaskName.GET_CACHE_RESPONSE)
    // set cache
    yield put(Action.DO_SET_CACHE(cache))
    // init step 1
    yield put(Action.DO_INIT_STEP({ step: 1,  state: defaultPayload }))
    // go to step 1
    yield put(Action.DO_NEXT())
    // close popup loading
    yield call(PopupHelper.closePopup)
    LogUtils.log("--------- end step0 ---------")
    return 0
}

function* step1() {
    LogUtils.log("--------- start step1 ---------")
    let exitCode = 0
    const op = yield call(PageHelper.genericPageHandler, ActionTaskName.NEXT, ActionTaskName.BACK, ActionTaskName.CANCEL)
    const payload = yield call(RequestHelper.genericCallbackRequest)
    if(op != 0) {
        const isDirty = PayloadHelper.doDirtyCheck(defaultPayload, payload)
        exitCode = isDirty ? -2 : -1
    } else {
        yield put(Action.DO_UPDATE_STATE(payload))
        yield put(Action.DO_INIT_STEP({ step: 2,  state: defaultPayload }))
        yield put(Action.DO_NEXT())
    }
    LogUtils.log("--------- end step1 ---------")
    return exitCode
}

function* step2() {
    LogUtils.log("--------- start step2 ---------")
    let exitCode = 0
    const op = yield call(PageHelper.genericPageHandler, ActionTaskName.NEXT, ActionTaskName.BACK, ActionTaskName.CANCEL)
    if(op === 1) {
        yield put(Action.DO_BACK())
    } else if(op === 2) {
        exitCode = -2;
    } else {
        yield put(Action.DO_INIT_STEP({ step: 3,  state: defaultPayload }))
        yield put(Action.DO_NEXT())
    }
    LogUtils.log("--------- end step2 ---------")
    return exitCode
}

function* step3() {
    LogUtils.log("--------- start step3 ---------")
    let exitCode = 0
    const op = yield call(PageHelper.genericPageHandler, ActionTaskName.NEXT, ActionTaskName.BACK, ActionTaskName.CANCEL)
    if(op === 1) {
        yield put(Action.DO_BACK())
    } else if(op === 2) {
        exitCode = -2;
    } else {
        exitCode = 1;
    }
    LogUtils.log("--------- end step3 ---------")
    return exitCode
}

export default {
    step0,
    step1,
    step2,
    step3,
}