import ActionName from "../constants/ActionName";
import FuncUtils from "../utils/FuncUtils";
import LogUtils from "../utils/LogUtils";

export const initialState = {
    step: 0,
    stepState: {},
    callback: null
}
export const reducer = (state, { type, payload }) => {
    let nextState
    switch(type) {
        case ActionName.DO_NEXT:
            nextState = {...state, step: state.step + 1}
            break
        case ActionName.DO_BACK:
            nextState = {...state, step: state.step - 1}
            break
        case ActionName.DO_JUMP:
            nextState = {...state, step: payload}
            break
        case ActionName.DO_INIT_STEP:
            nextState = {...state, stepState: {...state.stepState, [payload.step]: payload.state }}
            break
        case ActionName.DO_UPDATE_STATE:
            nextState = {...state, stepState: {...state.stepState, [state.step]: payload }}
            break
        case ActionName.DO_SET_CALLBACK:
            nextState = {...state, callback: FuncUtils.createOnceFunction(payload)}
            break
        case ActionName.DO_SET_CACHE:
            const { name } = payload
            nextState = {...state, appName: name}
            break
        default:
            return state
    }
    LogUtils.log("state", nextState)
    return nextState
}