package kr.co.polycube.backendtest.global.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final HttpServletRequest httpServletRequest;

    @Before("execution(* kr.co.polycube.backendtest.domain.user.controller.UserController.saveUser(..)) || " +
            "execution(* kr.co.polycube.backendtest.domain.user.controller.UserController.getUser(..)) || " +
            "execution(* kr.co.polycube.backendtest.domain.user.controller.UserController.updateUser(..))")
    public void logging(JoinPoint joinPoint) {
        String clientAgent = httpServletRequest.getHeader("User-Agent");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info("API Request : {}, Client Agent : {}", method.getName(), clientAgent);
    }
}
