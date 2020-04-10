package io.github;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.io.IOException;
import java.util.Collections;

/**
 * SpringBoot
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
// 开启AspectJ 自动代理模式
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@ServletComponentScan
@SpringBootApplication(scanBasePackages = App.SCAN_BASE_PACKAGES)
public class App extends SpringBootServletInitializer {

    /**
     * 扫描包名
     */
    public static final String SCAN_BASE_PACKAGES = "io.github";

    /**
     * TODO cmd控制台运行乱码待解决
     * mvn clean spring-boot:run -Drun.arguments=--Dfile.encoding=UTF-8
     * mvn clean spring-boot:run -Dspring-boot.run.jvmArguments=--Dfile.encoding=UTF-8
     * mvn clean spring-boot:run -Dspring-boot.run.arguments=--Dfile.encoding=UTF-8
     */
    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(App.class);
        app.setBannerMode(Mode.CONSOLE);
        app.run(args);
    }

    /**
     * 部署Tomcat---mvn clean package(如果使用Tomcat将packaging改为war)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        // This will set to use COOKIE only
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        // This will prevent any JS on the page from accessing the
        // cookie - it will only be used/accessed by the HTTP transport
        // mechanism in use
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        logger.debug(String.format("sessionCookieConfig.isHttpOnly()===>%s", sessionCookieConfig.isHttpOnly()));
        sessionCookieConfig.setHttpOnly(true);
    }

}


