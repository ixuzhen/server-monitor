import dayjs from "dayjs";


export function isSuccess(code){
    if(code===200){
        return true;
    }
    return false;
}

export function paringDate(date, template){
     return  dayjs(date).format(template)
}