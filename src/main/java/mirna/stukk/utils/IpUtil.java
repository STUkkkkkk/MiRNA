package mirna.stukk.utils;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-21 8:10
 **/


import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        System.out.println("====ipAddresses:"+ipAddresses);
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            //打印所有头信息
//            String s = headerNames.nextElement();
//            String header = request.getHeader(s);
//            System.out.println(s+"::::"+header);
//        }
//        System.out.println("headerNames:"+ JSON.toJSONString(headerNames));
//        System.out.println("RemoteHost:"+request.getRemoteHost());
//        System.out.println("RemoteAddr:"+request.getRemoteAddr());

        String unknown = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


//    public static String getIpAddr(HttpServletRequest request) {
//        String ipAddress = null;
//        try {
//            ipAddress = request.getHeader("x-forwarded-for");
//            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//                ipAddress = request.getHeader("Proxy-Client-IP");
//            }
//            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//                ipAddress = request.getHeader("WL-Proxy-Client-IP");
//            }
//            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//                ipAddress = request.getRemoteAddr();
//                if (ipAddress.equals("127.0.0.1")) {
//                    // 根据网卡取本机配置的IP
//                    InetAddress inet = null;
//                    try {
//                        inet = InetAddress.getLocalHost();
//                    } catch (UnknownHostException e) {
//                        e.printStackTrace();
//                    }
//                    ipAddress = inet.getHostAddress();
//                }
//            }
//            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
//            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
//                // = 15
//                if (ipAddress.indexOf(",") > 0) {
//                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
//                }
//            }
//        } catch (Exception e) {
//            ipAddress="";
//        }
//        // ipAddress = this.getRequest().getRemoteAddr();
//
//        return ipAddress;
//    }
}


