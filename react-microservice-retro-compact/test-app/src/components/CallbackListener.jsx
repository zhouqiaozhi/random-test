import { useEffect } from "react"
import { useDispatch, useSelector } from "react-redux"
import Selectors from "../selectors/Selectors"
import ActionTask from "../actions/ActionTask"

const CallbackListener = ({payload}) => {
    const dispatch = useDispatch()
    const callback = useSelector(Selectors.selectCallback())
    useEffect(() => {
        if(callback) {
            const result = callback(payload)
            if(result) dispatch(ActionTask.SET_CALLBACK_RESULT(result))
        }
    }, [callback])
    return (<></>)
}

export default CallbackListener