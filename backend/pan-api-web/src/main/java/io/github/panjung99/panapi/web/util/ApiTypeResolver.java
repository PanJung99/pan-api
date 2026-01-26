package io.github.panjung99.panapi.web.util;


import io.github.panjung99.panapi.web.annotation.OpenAiApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

public class ApiTypeResolver {

    public static boolean isOpenAiApi(HttpServletRequest request) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> controllerClass = handlerMethod.getBeanType();

        return controllerClass.isAnnotationPresent(OpenAiApi.class);
    }
}
