import { useDispatch } from "react-redux";
import ActionTask from "../actions/ActionTask";

const Bottom = () => {
    const dispatch = useDispatch()
    return (
        <>
            <div className="bottom">
                <div className="button cancel" onClick={() => dispatch(ActionTask.CANCEL())}>CANCEL</div>
                <div className="button next" onClick={() => dispatch(ActionTask.NEXT())}>NEXT</div>
            </div>
        </>
    )
}

export default Bottom