import { createSelector } from "reselect"
const selectState = (state) => state
const selectStep = () => createSelector(selectState, (state) => state.step)
const selectStepState = () => createSelector(selectState, (state) => state.stepState[state.step])
const selectFullStepState = () => createSelector(selectState, (state) => state.stepState)
const selectCallback = () => createSelector(selectState, (state) => state.callback)
export default {
    selectStep,
    selectStepState,
    selectFullStepState,
    selectCallback,
}