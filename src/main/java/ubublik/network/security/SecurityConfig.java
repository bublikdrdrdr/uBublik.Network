package ubublik.network.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import ubublik.network.security.jwt.CustomTokenFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;//TokenUserServiceImpl


    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we use jwt so that we can disable csrf protection
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/*").permitAll();
        http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl()
        ;
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationManager;
    }

    //commented cause of custom authentication manager implementation
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }*/

    /*@Override
    protected UserDetailsService userDetailsService() {
        return this.userDetailsService;
    }*/

    @Bean
    public CustomTokenFilter getFilter(){
        return new CustomTokenFilter();
    }

    //TEMP
    private AuthenticationEntryPoint getUnauthorizedEntryPoint(){
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }


/*


    public SecurityConfig() {
        super(true);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(getUnauthorizedEntryPoint()).and()//TODO: try without this

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers().cacheControl();
    }





    */
}
