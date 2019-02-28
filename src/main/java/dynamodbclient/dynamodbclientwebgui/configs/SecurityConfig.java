package dynamodbclient.dynamodbclientwebgui.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * { @inheritDoc }
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Simple inMemoryAuthentication type.
        auth.inMemoryAuthentication().withUser("admin").password("123").roles("USER").and().withUser("dba")
            .password("dba").roles("dba", "admin");
    }

    /**
     * { @inheritDoc }
     */
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    /*
     * { @inheritDoc }
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS) // permit CORS pre-request options
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();
    }

    /**
     * SpringSecurity WebSecurity springSecurityFilterChain.
     */
    @Override
    public void configure(WebSecurity http) throws Exception {
        // Remove monitoring path from SpringSecurity。
        http.ignoring()
            .antMatchers("/api/**");
    }

    /**
     * { @inheritDoc }
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return new CorsFilter(source);
    }
}
