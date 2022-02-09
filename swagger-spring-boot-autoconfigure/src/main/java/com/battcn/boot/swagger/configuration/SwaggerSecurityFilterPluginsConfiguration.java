package com.battcn.boot.swagger.configuration;

import com.battcn.boot.swagger.properties.SwaggerSecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import sun.dc.pr.PRError;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:1837307557@qq.com">Levin</a>
 * @since 2.0.2
 */
@EnableConfigurationProperties(SwaggerSecurityProperties.class)
public class SwaggerSecurityFilterPluginsConfiguration implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SwaggerSecurityFilterPluginsConfiguration.class);

    private SwaggerSecurityProperties swaggerSecurityProperties;

    public SwaggerSecurityFilterPluginsConfiguration(SwaggerSecurityProperties swaggerSecurityProperties){
        this.swaggerSecurityProperties = swaggerSecurityProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("==================== init swagger security filter plugin ====================");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String username = request.getHeader("swagger-username");
        final String password = request.getHeader("swagger-password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            serviceUnavailable((HttpServletResponse) servletResponse);
            return;
        }
        if (!(username.equals(swaggerSecurityProperties.getUsername()) && password.equals(swaggerSecurityProperties.getPassword()))) {
            serviceUnavailable((HttpServletResponse) servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("==================== destroy swagger security filter plugin ====================");
    }

    /**
     * 回写服务不可用状态
     *
     * @param response 写出流
     * @throws IOException IO异常
     */
    private void serviceUnavailable(HttpServletResponse response) throws IOException {
        final HttpStatus status = HttpStatus.FORBIDDEN;
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        String error = "{\"code\":%d,\"message\":\"%s\"}";
        final String format = String.format(error, status.value(), status.getReasonPhrase());
        logger.error(format);
        outputStream.write(format.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
