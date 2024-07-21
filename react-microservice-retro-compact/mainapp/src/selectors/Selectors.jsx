import { createSelector } from "reselect"
const selectState = (state) => state
const selectOpen = () => createSelector(selectState, state => state.open)
const selectPopup = () => createSelector(selectState, state => state.popup)
const selectCache = () => createSelector(selectState, state => state.cache)
export default {
    selectOpen,
    selectPopup,
    selectCache,
}