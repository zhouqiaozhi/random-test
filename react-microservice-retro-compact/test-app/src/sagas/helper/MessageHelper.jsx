function* send(type, payload) {
    window.parent.postMessage(payload ? { type, payload } : type, '*')
}

export default {
    send
}