package ru.hse.BikeSharing.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
//        http.authorizeRequests()
//                //.antMatchers(  "/", "/login", "/css/**","/js/**","/demo/**").permitAll()
//                .antMatchers("/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//               // .and()
//               // .formLogin()
//                //.loginPage("/login").failureUrl("/login?error=true")
//              //  .defaultSuccessUrl("/admin/index.html")
//               // .usernameParameter("username")
//               // .passwordParameter("password")
//               // .permitAll()
//                .and()
//                .logout()
//                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("{noop}qwerty4321").roles("ADMIN");
    }
}