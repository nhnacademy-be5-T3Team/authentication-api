package com.t3t.authenticationapi.account.filter;

import com.t3t.authenticationapi.account.exception.JsonFieldNotMatchException;
import com.t3t.authenticationapi.account.exception.TokenAlreadyExistsException;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommonExceptionFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch(Exception e){
            handleException((HttpServletRequest) request, (HttpServletResponse) response, chain, e);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Exception e) throws IOException, ServletException {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        if (e instanceof JsonFieldNotMatchException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new JSONObject().put("errorMessage","Use Proper Field").toString());
        } else if (e instanceof TokenAlreadyExistsException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(new JSONObject().put("errorMessage",e.getMessage()).toString());
        } else if (e instanceof TokenNotExistsException){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(new JSONObject().put("errorMessage",e.getMessage()).toString());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new JSONObject().put("errorMessage",e.getMessage()).toString());
        }
    }

}
