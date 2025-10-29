package zw.co.firstmutual.userservice.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zw.co.hcpwebcommons.handler.ExceptionHandlerFilter;
import zw.co.hcpwebcommons.security.AuthServiceFeignClient;
import zw.co.hcpwebcommons.security.JWTAuthenticationFilter;
import zw.co.hcpwebcommons.security.JwtAuthenticationEntryPoint;
import zw.co.hcpwebcommons.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenProvider tokenProvider;
    private final AuthServiceFeignClient authServiceFeignClient;
//    private static  Environment env;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/**", "/api/v1/users/signUp/**", "/api/v1/auth/login").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/users/reset-password").permitAll()
                .antMatchers("/", "/eureka/**").permitAll()
                .antMatchers("/api/v1/admin/users/").access("hasRole('ADMIN')")
                .antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable()
                // Filter to handle authentication exceptions and non-controller exceptions
                .addFilterBefore(exceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new JWTAuthenticationFilter(tokenProvider, authServiceFeignClient), UsernamePasswordAuthenticationFilter.class);

    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
//
//    @Bean
//    public ActiveDirectoryLdapAuthenticationProvider adProvider() {
//        return new ActiveDirectoryLdapAuthenticationProvider("firstmutual.co.zw", "ldaps://172.16.4.24:636/dc=firstmutual,dc=co,dc=zw");
//    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
}