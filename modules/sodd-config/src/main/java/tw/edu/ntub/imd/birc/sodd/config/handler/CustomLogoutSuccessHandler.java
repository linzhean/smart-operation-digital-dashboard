package tw.edu.ntub.imd.birc.sodd.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.config.util.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseUtils.response(
                response,
                200,
                true,
                "User - Logout",
                "登出成功",
                request.getRequestURI().endsWith("search") ? mapper.createArrayNode() : mapper.createObjectNode()
        );
    }
}
