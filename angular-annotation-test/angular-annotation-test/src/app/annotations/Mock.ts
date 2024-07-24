export default (service: any) => {
    return (target: object, key:string, descriptor: PropertyDescriptor) => {
        descriptor.value = service.ɵfac()[key]
    }
}