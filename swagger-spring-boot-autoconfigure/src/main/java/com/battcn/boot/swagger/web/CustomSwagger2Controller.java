package com.battcn.boot.swagger.web;

import com.battcn.boot.swagger.properties.SwaggerSecurityProperties;
import com.battcn.boot.swagger.utils.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Levin
 * @since 2018/7/6 0006
 */
@RestController
@ApiIgnore
public class CustomSwagger2Controller {

    private static final String SWAGGER_SECURITY_URL = "/v2/swagger-security";
    private static final String SWAGGER_SECURITY_LOGIN_URL = "/v2/swagger-login";
    private final SwaggerSecurityProperties swaggerSecurityProperties;

    public CustomSwagger2Controller(SwaggerSecurityProperties swaggerSecurityProperties) {
        this.swaggerSecurityProperties = swaggerSecurityProperties;
    }

    @GetMapping(value = SWAGGER_SECURITY_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Map<String, Boolean>> getCustomDocumentation() {
        Map<String, Boolean> meteData = new HashMap<>(2);
        meteData.put("security", swaggerSecurityProperties.isFilterPlugin());
        return new ResponseEntity<>(meteData, HttpStatus.OK);
    }

    @PostMapping(value = SWAGGER_SECURITY_LOGIN_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Void> loginSwagger(HttpServletResponse response, String username, String password) throws IOException {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            RequestUtils.writeForbidden(response);
        }
        if (!(username.equals(swaggerSecurityProperties.getUsername()) && password.equals(swaggerSecurityProperties.getPassword()))) {
            RequestUtils.writeForbidden(response);
        }
        RequestUtils.SWAGGER_IS_LOGIN = true;
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
