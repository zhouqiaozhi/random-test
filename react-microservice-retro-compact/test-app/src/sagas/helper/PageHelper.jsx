import {
    take,
    race,
} from 'redux-saga/effects'

function* genericPageHandler(next, back, cancel) {
    const listener = {}
    if(next) listener.n = take(next);
    if(back) listener.b = take(back);
    if(cancel) listener.c = take(cancel);
    const { n, b } = yield race(listener)
    if(n) return 0;
    if(b) return 1;
    return 2;
}

export default {
    genericPageHandler
}