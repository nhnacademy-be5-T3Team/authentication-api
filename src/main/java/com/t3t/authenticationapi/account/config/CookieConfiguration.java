package com.t3t.authenticationapi.account.config;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CookieConfiguration {

    @Bean
    public CookieSameSiteSupplier cookieSameSiteSupplier(){
        // SameSite 설정을 Strict로 주어서 모든 상황에서 사이트 간 요청을 차단
        // 쿠키가 같은 사이트 요청에만 전송되도록 함
        return CookieSameSiteSupplier.ofStrict();
    }
}
