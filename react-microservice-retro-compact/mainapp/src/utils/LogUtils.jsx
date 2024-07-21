const LogUtils = {
    log: (...message) => {
        if(__DEBUG__) {
            console.log(...message)
        }
    },
    trace: (...message) => {
        if(__DEBUG__) {
            console.trace(...message)
        }
    },
    warn: (...message) => {
        if(__DEBUG__) console.warn(...message)
    }
}
export default LogUtils