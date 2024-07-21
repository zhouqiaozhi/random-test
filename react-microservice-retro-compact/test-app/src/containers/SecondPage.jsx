import { useState, useMemo } from "react"
import CallbackListener from "../components/CallbackListener"
import { useSelector } from "react-redux"
import Selectors from "../selectors/Selectors"

const SecondPage = () => {
    const state = useSelector(Selectors.selectStepState())
    const [value, setValue] = useState(state.value)
    const payload = useMemo(() => ({value}), [value])
    return (
        <div className="page">
            <CallbackListener payload={payload} />
            <div className="title">SecondPage</div>
            <select value={value} onChange={(e) => {
                setValue(e.target.value)
            }}>
                <option value="SUCCESS">CALL SUCCESS</option>
                <option value="FAILURE">CALL FAILURE</option>
            </select>
        </div>
    )
}

export default SecondPage