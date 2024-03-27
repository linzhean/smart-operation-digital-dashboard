package tw.edu.ntub.imd.birc.sodd.config.entrypoint;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tw.edu.ntub.imd.birc.sodd.config.util.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        ObjectMapper mapper = new ObjectMapper();
        ResponseUtils.response(
                response,
                401,
                false,
                "User - NotLogin",
                "請重新登入",
                request.getRequestURI().endsWith("search") ? mapper.createArrayNode() : mapper.createObjectNode()
        );
    }
}

