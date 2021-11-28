package sonia.webapp.diva.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Configuration
@EnableWebSecurity
public class DiVaSecurityConfig extends WebSecurityConfigurerAdapter
{
  
  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers(
        "/",
        "/public/**",
        "/webjars/**",
        "/api/**" ).permitAll();
  }
}
