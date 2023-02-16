package com.nablarch.example.app.web.filter;

import nablarch.core.repository.SystemRepository;
import nablarch.fw.web.servlet.WebFrontController;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * RESTfulアプリケーション用のリクエストコントローラ。
 * <p/>
 * {@link SystemRepository}から取得したRESTfulアプリケーション用の{@link WebFrontController}に
 * リクエスト処理を委譲するリクエストコントローラ。
 *
 * @author Nabu Rakutaro
 */
public class RestfulWebFrontController implements Filter {

    /** {@link WebFrontController} */
    private WebFrontController controller;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        controller = SystemRepository.get("jaxrsController");
        controller.setServletFilterConfig(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        controller.doFilter(servletRequest, servletResponse, filterChain);
    }

    @Override
    public void destroy() {
        controller.destroy();
    }
}
