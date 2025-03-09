package kr.co.polycube.backendtest.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class UrlValidationFilter implements Filter {

    private static final String REGEX = "^[a-zA-Z0-9?&=:/]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        if (queryString != null) {
            url += ("?" + httpRequest.getQueryString());
        }

        if (!PATTERN.matcher(url).matches()) {
            sendJsonErrorResponse(httpResponse);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendJsonErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = Map.of(
                "reason", "허용되지 않은 특수문자가 포함되어 있습니다."
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
