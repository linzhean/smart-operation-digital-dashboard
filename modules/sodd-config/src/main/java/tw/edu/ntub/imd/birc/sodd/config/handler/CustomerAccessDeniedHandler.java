package tw.edu.ntub.imd.birc.sodd.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tw.edu.ntub.imd.birc.sodd.config.util.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        ObjectMapper mapper = new ObjectMapper();
        ResponseUtils.response(
                response,
                403,
                false,
                "User - AccessDenied",
                "您並無此操作之權限。",
                request.getRequestURI().endsWith("search") ? mapper.createArrayNode() : mapper.createObjectNode()
        );
    }
}
