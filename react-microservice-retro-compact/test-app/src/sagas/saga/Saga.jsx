import {
    select,
    call,
    spawn,
} from 'redux-saga/effects'

import LogUtils from '../../utils/LogUtils'
import Selectors from '../../selectors/Selectors'
import postMessageListener from '../MessageListener'
import Steps from './Steps'
import ExitHelper from '../helper/ExitHelper'

function* mainFlow() {
    LogUtils.log("START TEST-APP MAINFLOW")
    let exitCode = 0
    while(true) {
        if(exitCode !== 0) {
            if(exitCode === -2) {
                const cancel = yield call(ExitHelper.cancel)
                if(!cancel) {
                    exitCode = 0;
                    continue;
                }
            }
            break
        }
        const step = yield select(Selectors.selectStep())
        switch(step) {
            case 0:
                exitCode = yield call(Steps.step0)
                break;
            case 1:
                exitCode = yield call(Steps.step1)
                break
            case 2:
                exitCode = yield call(Steps.step2)
                break
            case 3:
                exitCode = yield call(Steps.step3)
                break
            default:
                LogUtils.log("impossible")
                break
        }
    }
    if(__DEBUG__) {
        // get stepStates
        const stepStates = yield select(Selectors.selectFullStepState())
        // print for mock
        LogUtils.log(JSON.stringify(stepStates))
    }
    yield call(ExitHelper.exit, exitCode)
}

function* rootSaga() {
    LogUtils.warn("DEBUG ON")
    yield spawn(postMessageListener)
    yield spawn(mainFlow)
}

export default rootSaga