package mirna.stukk.controller;

import io.swagger.annotations.Api;
import mirna.stukk.config.Result;
import mirna.stukk.utils.IpUtil;
import mirna.stukk.utils.LimitAPI;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-21 8:38
 **/
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {

    @GetMapping("/getIp")
    @LimitAPI(limit = 1,second = 10)
    public Result<String> getIp(HttpServletRequest request){
        String ipAddr = IpUtil.getIpAddr(request);
        String requestURI = request.getRequestURI();
        return Result.success(ipAddr + "--->" + requestURI);
    }

}
