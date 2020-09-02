package pl.gregorymartin.passwordencoder;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder getPasswordEncoder(){

        return new CityPasswordEncoder();
        //return new BCryptPasswordEncoder();
    }

    private UserDetailsService userDetailsService;

    public WebSecurityConfig(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
/*        User userAdmin = new User("Jan",getPasswordEncoder().encode("123"), Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        User userUser = new User("Janek",getPasswordEncoder().encode("123"), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        auth.inMemoryAuthentication().withUser(userAdmin);
        auth.inMemoryAuthentication().withUser(userUser);*/

        auth.userDetailsService(userDetailsService);



    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/all").authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/all")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .rememberMe().tokenValiditySeconds(200000);
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
