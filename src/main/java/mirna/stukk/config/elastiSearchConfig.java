package mirna.stukk.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Configuration
public class elastiSearchConfig {

    @Value("${stukk.elasticsearch.host}")
    private String host;

    @Value("${stukk.elasticsearch.username}")
    private String userName;

    @Value("${stukk.elasticsearch.password}")
    private String password;


    /**
     * 连接超时时间
     */
    @Value("${stukk.elasticsearch.connectTimeout}")
    private int connectTimeout;

    /**
     * Socket 连接超时时间
     */
    @Value("${stukk.elasticsearch.socketTimeout}")
    private int socketTimeout;

    /**
     * 获取连接的超时时间
     */
    @Value("${stukk.elasticsearch.connectionRequestTimeout}")
    private int connectionRequestTimeout;

    /**
     * 最大连接数
     */
    @Value("${stukk.elasticsearch.maxConnectNum}")
    private int maxConnectNum;

    /**
     * 最大路由连接数
     */
    @Value("${stukk.elasticsearch.maxConnectPerRoute}")
    private int maxConnectPerRoute;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient(){
        String[] split = host.split(",");
        HttpHost[]httpHosts = new HttpHost[split.length];
        for(int i = 0;i< split.length;i++){
            httpHosts[i] = new HttpHost(split[i].split(":")[0],Integer.parseInt(split[i].split(":")[1]),"http");
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY , new UsernamePasswordCredentials(userName, password));
        RestClientBuilder builder = RestClient.builder(httpHosts);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);

            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
           httpAsyncClientBuilder.disableAuthCaching(); //禁用身份验证
            httpAsyncClientBuilder.setKeepAliveStrategy((httpResponse,httpContext) -> TimeUnit.MINUTES.toMillis(3));
            //显式开启tcp keepalive
            httpAsyncClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build());
            return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        return new RestHighLevelClient(builder);
    }




}
