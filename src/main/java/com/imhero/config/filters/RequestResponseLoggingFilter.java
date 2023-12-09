package com.imhero.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestBody = getBody(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        String responseBody = getBody(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());

        log.info("request {} {} {} \n response {} {}"
                , requestWrapper.getMethod(), requestWrapper.getRequestURI(), requestBody, response.getStatus(), responseBody);

        responseWrapper.copyBodyToResponse();
    }

    private String getBody(byte[] contentAsByteArray, String characterEncoding) {
        String body = "";
        try {
            body = new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException();
        }
        return body;
    }
}