import ActionName from "../constants/ActionName"

const Action = {}

for(const x of Object.keys(ActionName)) {
    Action[x] = (payload) => {
        return {
            type: ActionName[x],
            payload
        }
    }
}

export default Action