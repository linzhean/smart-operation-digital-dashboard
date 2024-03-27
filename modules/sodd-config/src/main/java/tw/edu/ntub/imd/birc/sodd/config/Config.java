package tw.edu.ntub.imd.birc.sodd.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tw.edu.ntub.imd.birc.sodd.config.properties.FileProperties;
import tw.edu.ntub.imd.birc.sodd.config.properties.ImageProperties;

import java.nio.charset.StandardCharsets;

@Configuration
public class Config implements WebMvcConfigurer {
    private final Logger logger = LogManager.getLogger(Config.class);
    private final FileProperties fileProperties;
    private final ImageProperties imageProperties;

    @Autowired
    public Config(
            FileProperties fileProperties,
            ImageProperties imageProperties) {
        this.fileProperties = fileProperties;
        this.imageProperties = imageProperties;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        return converter;
    }

    @Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void customizeConnector(Connector connector) {
                int maxSize = 50000000;
                super.customizeConnector(connector);
                connector.setMaxPostSize(maxSize);
                connector.setMaxSavePostSize(maxSize);
                connector.setParseBodyMethods("GET,POST,DELETE,PUT,PATCH");
                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
                    ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
                    logger.info("Set MaxSwallowSize " + maxSize);
                }
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(String.format("/%s/**", fileProperties.getName()))
                .addResourceLocations(String.format("file:%s", fileProperties.getPath()));
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico");
        registry.addResourceHandler(String.format("/%s/**", imageProperties.getName()))
                .addResourceLocations(String.format("file:%s", imageProperties.getPath()));

        logger.info("增加路徑對應：" + String.format("/%s/**", fileProperties.getName()));
        logger.info("對應到的實體路徑為：" + String.format("file:%s", fileProperties.getPath()));
        logger.info("增加路徑對應：/favicon.ico");
        logger.info("對應到的實體路徑為：classpath:/static/favicon.ico");
        logger.info("增加路徑對應：" + String.format("/%s/**", imageProperties.getName()));
        logger.info("對應到的實體路徑為：" + String.format("file:%s", imageProperties.getPath()));
    }
}
