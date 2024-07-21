const doDirtyCheck = (a, b) =>  {
    return Object.keys(b).some(key => a[key] !== b[key])
}

export default {
    doDirtyCheck
}