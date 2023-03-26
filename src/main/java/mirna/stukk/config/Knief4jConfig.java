package mirna.stukk.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Knief4jConfig {

    /*
     * 创建连接的包信息
     * @param: 方法名称随便起，虽然它的名字会被作为Bean对象的名字注入spring容器
     * @date: 2020年08月11日 0011 9:51
     * @param:
     * @return: springfox.documentation.spring.web.plugins.Docket 返回创建状况
     */
    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2) // 选择swagger2版本
                .useDefaultResponseMessages(false)
                // 接口文档的基本信息
                .apiInfo(apiInfo())
                .select()
                // 这里指定Controller扫描包路径(项目路径也行)
                // 方式一：配置扫描：所有想要在swagger界面统一管理的接口，都必须在此包下
                // .apis(RequestHandlerSelectors.basePackage("com.company.project.web.controller.entrance"))
                // 方式二：只有当方法上有@ApiOperation注解时，才能生成对应的接口文档
                .apis(RequestHandlerSelectors.basePackage("mirna.stukk.controller"))
                // 路径使用any风格（指定所有路径）
                .paths(PathSelectors.any())
                .build();

    }

    /*
     * 设置文档信息主页的内容说明
     * @date: 2020年08月11日 0011 9:52
     * @param:
     * @return: springfox.documentation.service.ApiInfo 文档信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("基于机器学习的mirna-疾病预测算法和平台搭建 后台服务API接口文档")
                .description("基于机器学习的mirna-疾病预测算法和平台搭建的服务相关接口(knife4j)")
                // 服务Url（网站地址）
                .contact(new Contact("stukk",null,"stukk123@163.com"))
                .version("1.0")
                .build();
    }
}
