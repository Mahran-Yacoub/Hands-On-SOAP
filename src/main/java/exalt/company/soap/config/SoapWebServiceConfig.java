package exalt.company.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableWs
@Configuration
public class SoapWebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        // server:port/customers/*
        return new ServletRegistrationBean(servlet, "/customers/*");
    }


    @Bean
    public XsdSchema userSchema() {

        return new SimpleXsdSchema(new ClassPathResource("customers.xsd"));
    }

    // WSDL Document will be in server:port/customers/soap.wsdl
    @Bean(name = "soap")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userSchema) {

        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setSchema(userSchema);
        definition.setLocationUri("/customers");
        definition.setPortTypeName("UserServicePort");
        definition.setTargetNamespace("http://soap.example.com/spring-boot-soap");
        return definition;
    }


    @Bean
    public Docket swaggerDocumentation() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    /**
     * This method is used to set metadata about project when we use swagger ui
     * to document it
     *
     * @return ApiInfo that has metaData about the app
     *
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Hands on SOAP")
                .description("This is a SpringBoot Application such that it represents backend " +
                        "side with some of APIs to Create , delete , update , read resources " +
                        "(CRUD) using http protocol and localhost as server and 8080 as port " +
                        "\"http://localhost:8080/\" " +
                        "and endpoint part like http://localhost:8080/customer/all.")

                .version("1.1").build();
    }

}
