package tw.edu.ntub.imd.birc.sodd.aspect;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
public class RequestAspect {
    private final RequestService requestService;

    @Pointcut("within(tw.edu.ntub.imd.birc.sodd.controller..*)")
    public void controllerMethods() {
    }

    @AfterReturning(
            pointcut = "controllerMethods()",
            returning = "returnValue"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object returnValue) {
        if (returnValue instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Object[] args = joinPoint.getArgs();
                HttpServletRequest request = null;
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        request = (HttpServletRequest) arg;
                        break;
                    }
                }
                if (request != null) {
                    JsonObject jsonObject = JsonParser.parseString(
                            (String) Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();
                    requestService.addRequestRecord(jsonObject.get("message").getAsString(), request);
                }
            }
        }
    }

    @AfterThrowing(
            pointcut = "controllerMethods()",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if (request != null) {
            requestService.addRequestRecord(exception.getMessage(), request);
        }
    }
}