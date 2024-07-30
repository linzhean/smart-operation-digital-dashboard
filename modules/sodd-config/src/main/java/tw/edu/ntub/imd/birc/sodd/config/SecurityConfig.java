package tw.edu.ntub.imd.birc.sodd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tw.edu.ntub.imd.birc.sodd.config.entrypoint.CustomEntryPoint;
import tw.edu.ntub.imd.birc.sodd.config.filter.CustomLoginFilter;
import tw.edu.ntub.imd.birc.sodd.config.filter.JwtAuthenticationFilter;
import tw.edu.ntub.imd.birc.sodd.config.handler.CustomAuthenticationSuccessHandler;
import tw.edu.ntub.imd.birc.sodd.config.handler.CustomLogoutSuccessHandler;
import tw.edu.ntub.imd.birc.sodd.config.handler.CustomerAccessDeniedHandler;
import tw.edu.ntub.imd.birc.sodd.config.properties.FileProperties;
import tw.edu.ntub.imd.birc.sodd.config.properties.ImageProperties;
import tw.edu.ntub.imd.birc.sodd.config.provider.CustomAuthenticationProvider;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String imageUrlName;
    private final String fileUrlName;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(
            FileProperties fileProperties,
            ImageProperties imageProperties,
            UserDetailsService userDetailsService,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.fileUrlName = fileProperties.getName();
        this.imageUrlName = imageProperties.getName();
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 這個表示哪些頁面"不會用到SpringSecurity"，相當於xml中的security="none"
    // 代表在這些連結中會抓不到登入資訊
    // 即SpringContextHolder.getContext() = null
    // 因此這些只能用在靜態資源上
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/doc/**",
                        "/api/**",
                        "/v3/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/csrf",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        "/static/**",
                        "/excel/test",
                        String.format("/%s/**", imageUrlName),
                        String.format("/%s/**", fileUrlName)
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling() // 出錯時的例外處理
                .authenticationEntryPoint(new CustomEntryPoint()) // 未登入處理
                .accessDeniedHandler(new CustomerAccessDeniedHandler()) // 偵測權限不足的處理
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLoginFilter(authenticationManager(), customAuthenticationSuccessHandler), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests() // 設定Requests的權限需求
                // @TODO 不要把這個推上去
                .anyRequest().permitAll()
//                .authenticated()
                .and()
                .formLogin() // 設定Login，如果是用Form表單登入的話
                .loginPage("/login") // 設定Login頁面的URL
                .loginProcessingUrl("/login") // 設定Login動作的URL
                .failureUrl("/login?error") // 設定Login失敗的URL
                .permitAll() // Login不需要權限
                .and()
                .logout() // 設定Logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // 設定Logout URL
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .sessionManagement() // Session管理
                .sessionFixation() // Session固定ID保護
                .migrateSession() // 每次登入，都會產生新的，並將舊的屬性複製，預設值
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
//                "*",
                "https://smart-operation-digital-dashboard.kuohao.wtf",
                "http://140.131.114.166:8080",
                "http://localhost:3000"
                "*"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("X-Auth-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}