import ActionresponseName from '../constants/ActionResponseName'

const ActionResponse = {}

for(const x of Object.keys(ActionresponseName)) {
    ActionResponse[x] = (payload) => {
        return {
            type: ActionresponseName[x],
            payload
        }
    }
}

export default ActionResponse