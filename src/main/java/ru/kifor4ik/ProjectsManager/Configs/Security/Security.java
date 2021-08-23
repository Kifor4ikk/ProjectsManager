package ru.kifor4ik.ProjectsManager.Configs.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()

                .antMatchers(HttpMethod.GET,"/profile/all").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/project/all").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/profile").anonymous()
                .antMatchers(HttpMethod.GET, "/profile/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/profile").authenticated()
                .antMatchers(HttpMethod.DELETE, "/profile").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/project").authenticated()
                .antMatchers(HttpMethod.GET, "/project/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/project").authenticated()
                .antMatchers(HttpMethod.DELETE, "/project").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/project/task").authenticated()
                .antMatchers(HttpMethod.GET, "/project/task/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/project/task").authenticated()
                .antMatchers(HttpMethod.DELETE, "/project/task").hasAuthority("ADMIN")
                .and()
                //@TODO ???
                .csrf().disable()
                .formLogin();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
