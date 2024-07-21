import ActionName from "../constants/ActionName"
import LogUtils from "../utils/LogUtils"
export const initialState = {
    open: true,
    popup: null,
    cache: { name: 'test-app' }
}

export default function (state, { type, payload }) {
    let nextState
    switch(type) {
        case ActionName.DO_OPEN:
            nextState = {...state, open: true}
            break
        case ActionName.DO_CLOSE:
            nextState = {...state, open: false}
            break
        case ActionName.DO_SHOW_POPUP:
            nextState = {...state, popup: payload}
            break
        case ActionName.DO_CLOSE_POPUP:
            nextState = {...state, popup: null}
            break
        default:
            return state
    }
    LogUtils.log("state", nextState)
    return nextState
}