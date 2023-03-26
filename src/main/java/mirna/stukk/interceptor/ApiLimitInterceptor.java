package mirna.stukk.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import mirna.stukk.config.Result;
import mirna.stukk.utils.IpUtil;
import mirna.stukk.utils.LimitAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-21 8:33
 **/
@Component
@Log4j2
public class ApiLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        log.info("ip为: "+IpUtil.getIpAddr(request)+"正在访问接口:"+request.getRequestURI());
        try{
            if(handler instanceof HandlerMethod){
                //判断下handle是不是HandlerMethod的实例
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                //获取方法
                Method method = handlerMethod.getMethod();
                //判断方法是不是有我们写的注解
                if(!method.isAnnotationPresent(LimitAPI.class)){
                    return true; //不拦截
                }
                LimitAPI limitAPI = method.getAnnotation(LimitAPI.class);
                if(limitAPI == null){
                    return true;
                }
                int limit = limitAPI.limit(); //最大请求次数
                int second = limitAPI.second(); //请求时间
                synchronized (this){ //加入同步锁
                    String key = IpUtil.getIpAddr(request) +":"+ request.getRequestURI();
                    Object value = redisTemplate.opsForValue().get(key);
                    if(value == null){
                        redisTemplate.opsForValue().set(key,"1");
                        redisTemplate.expire(key,second, TimeUnit.SECONDS); //设置倒计时
                    }
                    else{
                        int currentTimes = Integer.parseInt(value.toString()); //当前次数
                        if(currentTimes < limit){
                            //如果比要求的小
                            redisTemplate.opsForValue().set(key,currentTimes+"",second, TimeUnit.SECONDS);
                        }
                        else{
                            log.info(key+"请求过于频繁");
                            return setResponse(Result.error("5555","操作过于频繁，请稍等..."),response);
                        }
                    }
                }
            }
        }
        catch (Exception e){
            log.error("API限流拦截失败，原因:" + e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public boolean setResponse(Result<String> result , HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = null;
        try{
            response.setHeader("Content-type","application/json; charset=utf-8");
            outputStream = response.getOutputStream();
            outputStream.write(JSON.toJSONString(result).getBytes("utf-8"));
        }
        catch (Exception e){
            log.error("发送请求失败: {}",e);
            return false;
        }
        finally {
            if(outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
        }
        return true;
    }
}
