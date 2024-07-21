import './App.css'
import Action from './actions/Action';
import ActionResponse from './actions/ActionResponse'
import { useDispatch, useSelector } from 'react-redux';
import Selectors from './selectors/Selectors';

function App() { 
  const dispatch = useDispatch()
  const open = useSelector(Selectors.selectOpen())
  const popup = useSelector(Selectors.selectPopup())

  return (
    <>
      {popup && (
        <div className='popup'>
          <div className='popup_container'>
            <div className='popup_text'>{popup.text}</div>
            {(popup.confirm || popup.cancel) && (
              <div className='button_container'>
                {popup.cancel && <div className="button cancel" onClick={() => {
                  dispatch(ActionResponse.CLOSE_POPUP_RESPONSE(false))
                }}>CANCEL</div>}
                {popup.confirm && <div className="button confirm" onClick={() => {
                  dispatch(ActionResponse.CLOSE_POPUP_RESPONSE(true))
                }}>CONFIRM</div>}
              </div>
            )}
          </div>
        </div>
      )}
      {!open && (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%', width: '100%'}}>
          <button style={{
            width: "100px",
            height: "100px",
            fontSize: "24px"
          }} onClick={() => dispatch(Action.DO_OPEN())}>open</button>
        </div>
      )}
      {open && <iframe id="test-app" src="http://localhost:3001"></iframe>}
    </>
  )
}

export default App
