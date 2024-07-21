import { useState } from "react"
import { useSelector } from "react-redux"
import Selectors from "../selectors/Selectors"

const LastPage = () => {
    const state = useSelector(Selectors.selectStepState())
    const [value, setValue] = useState(state.value)
    return (
        <div className="page">
            <div className="title">LastPage</div>
            <select value={value} onChange={(e) => {
                setValue(e.target.value)
            }}>
                <option value="SUCCESS">CALL SUCCESS</option>
                <option value="FAILURE">CALL FAILURE</option>
            </select>
        </div>
    )
}

export default LastPage