package ru.hse.BikeSharing.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http
//                .authorizeRequests()
//                .antMatchers("/api/**").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
////        http.authorizeRequests()
////                //.antMatchers(  "/", "/login", "/css/**","/js/**","/demo/**").permitAll()
////                .antMatchers("/**").hasRole("ADMIN")
////                .anyRequest().authenticated()
////               // .and()
////               // .formLogin()
////                //.loginPage("/login").failureUrl("/login?error=true")
////              //  .defaultSuccessUrl("/admin/index.html")
////               // .usernameParameter("username")
////               // .passwordParameter("password")
////               // .permitAll()
////                .and()
////                .logout()
////                .permitAll();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("{noop}qwerty4321").roles("ADMIN");
//    }
//}

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    @EnableGlobalMethodSecurity(
            securedEnabled = true,
            jsr250Enabled = true,
            prePostEnabled = true
    )
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        CustomUserDetailsService customUserDetailsService;

        @Autowired
        private JwtAuthenticationEntryPoint unauthorizedHandler;

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new NoPasswordEncoder();
        }

        @Bean(BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable();

            // Add our custom JWT security filter
            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
    }


    @Configuration
    @Order(2)
    public static class LoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http
                    .antMatcher("/tokensignin/*")
//                    .antMatcher("/**/*.js")
//                    .antMatcher("/**/*.css")
//                    .antMatcher("/**/*.png")
//                    .antMatcher("/favicon.ico")
                    .authorizeRequests()
                    .anyRequest()
                    .permitAll();

        }
    }

    @Configuration
    @Order(3)
    public static class AdminWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("admin").password("{noop}qwerty4321").roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/");

        }
    }
}