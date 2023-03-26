package mirna.stukk.utils;

import java.lang.annotation.*;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-21 8:08
 **/
@Inherited
@Documented
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitAPI {
    int limit() default 3;
    int second() default 10;
//    默认10秒3次
}
