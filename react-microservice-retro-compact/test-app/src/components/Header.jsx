import { useDispatch } from 'react-redux';
import ActionTask from '../actions/ActionTask';


const Header = () => {
    const dispatch = useDispatch()
    return (
        <>
            <div className="header">
                <div className="back" onClick={() => dispatch(ActionTask.BACK())}>
                    <div className="back_arrow"></div>
                </div>
                <div className="title">test-app</div>
                <div className="header_action"></div>
            </div>
        </>
    )
}

export default Header