import ActionTaskName from "../constants/ActionTaskName"

const ActionTask = {}

for(const x of Object.keys(ActionTaskName)) {
    ActionTask[x] = (payload) => {
        return {
            type: ActionTaskName[x],
            payload
        }
    }
}

export default ActionTask