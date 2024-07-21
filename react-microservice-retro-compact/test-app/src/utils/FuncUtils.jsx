const FuncUtils = {
    createOnceFunction: (func = (obj) => obj) => {
        let called = false;
        return (obj) => {
            if(called) return
            called = true;
            return func(obj)
        };
    }
}

export default FuncUtils