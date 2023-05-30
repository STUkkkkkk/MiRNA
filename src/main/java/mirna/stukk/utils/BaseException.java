package mirna.stukk.utils;

import lombok.Data;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-28 12:43
 **/
@Data
public class BaseException extends RuntimeException {
    //    错误码
    private String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }
}
