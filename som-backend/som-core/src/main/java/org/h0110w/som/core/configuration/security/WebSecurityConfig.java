package org.h0110w.som.core.configuration.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.h0110w.som.core.controller.UserAccountsController;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Collection;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .antMatchers("/public/message").permitAll()
                                .antMatchers("/auth/token").permitAll()
                                .antMatchers("/user/message").hasRole(UserAccountType.REGULAR.name())
                                .antMatchers(UserAccountsController.URL+"/*").hasRole(UserAccountType.ADMIN.name())
                                .anyRequest().authenticated())
                .addFilterBefore(new AccessDeniedExceptionFilter(), FilterSecurityInterceptor.class)
                .oauth2ResourceServer(resourceServerConfigurer ->
                        resourceServerConfigurer
                                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        return new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwtSource) {
                Collection<GrantedAuthority> authorities = converter.convert(jwtSource);
                if (jwtSource.getClaim("realm_access") == null) {
                    return authorities;
                }
                JSONObject realmAccess = jwtSource.getClaim("realm_access");
                if (realmAccess.get("roles") == null) {
                    return authorities;
                }
                JSONArray roles = (JSONArray) realmAccess.get("roles");

                if (roles.contains(UserAccountType.ADMIN.toString())) {
                    authorities.addAll(UserAccountType.ADMIN.getGrantedAuthorities());
                } else if (roles.contains(UserAccountType.REGULAR)) {
                    authorities.addAll(UserAccountType.REGULAR.getGrantedAuthorities());
                }

                return authorities;
            }
        };
    }


}
