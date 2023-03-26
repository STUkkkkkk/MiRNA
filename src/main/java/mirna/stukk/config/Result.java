package mirna.stukk.config;


import javax.annotation.Resource;

public class Result<T> {

    private String code;//编码

    private String message;//信息

    private T data;//数据



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T>Result<T> success(){//成功
        Result result = new Result<>();
        result.setCode("0");
        result.setMessage("成功");
        return result;
    }

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode("0");
        result.setMessage("成功");
        result.setData(data);
        return result;
    }


    public static Result error(String code,String message){
        Result result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }




}

