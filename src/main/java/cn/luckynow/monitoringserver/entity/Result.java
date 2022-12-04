package cn.luckynow.monitoringserver.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <E> Result<E> successWithoutData(){
        return new Result<E>(200,null,null);
    }

    public static <E> Result<E> successWithData(E data){
        return new Result<E>(200,null,data);
    }

    public static <E> Result<E> failed(String message){
        return new Result<E>(400,message,null);
    }

}
