package com.madive.bigcommerce.madiveone.admin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.madive.bigcommerce.madiveone.admin.auth.AdminAuthenticationFailureHandler;
import com.madive.bigcommerce.madiveone.admin.auth.AdminAuthenticationFilter;
import com.madive.bigcommerce.madiveone.admin.auth.AdminAuthenticationProvider;
import com.madive.bigcommerce.madiveone.admin.auth.AdminAuthenticationSuccessHandler;
import com.madive.bigcommerce.madiveone.admin.auth.RoleType;
import com.madive.bigcommerce.madiveone.admin.util.Profiles;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AdminAuthenticationProvider authenticationProvider;

	@Autowired
	private AdminAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AdminAuthenticationFailureHandler authenticationFailureHandler;

    @Override
	public void configure(WebSecurity webs) {
    	webs.ignoring()
			.antMatchers("/assets/**", "/js/**", "/favicon.ico")
			;
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	List<String> antMatchers = new ArrayList<>();
    	if (Profiles.isLoc()) {
    		antMatchers.add("/dummy/**");
    		antMatchers.add("/sample/**");  // FIXME ...
    		antMatchers.add("/api/**");  // TODO ... 임시
    	}

    	http.csrf().disable()
    		.authorizeRequests()
    			.antMatchers("/error", "/health").permitAll()
	        	.antMatchers(antMatchers.toArray(new String[0])).permitAll()
	        	.antMatchers("/lo/04/**").hasAuthority(RoleType.ROLE_TEMP.name())
	        	.antMatchers("/lo/02/**").hasAnyAuthority(RoleType.ROLE_TEMP.name(), RoleType.ROLE_USER.name())
	        	.anyRequest().hasAuthority(RoleType.ROLE_USER.name())
	        	.and()
	        .addFilterBefore(new AdminAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
	        .formLogin()
	        	.loginPage("/lo/01").permitAll()
	        	.successHandler(authenticationSuccessHandler)
	        	.failureHandler(authenticationFailureHandler)
	        	.and()
	        .logout()
	        	.logoutUrl("/lo/01/02").permitAll()
	        	.logoutSuccessUrl("/")
	        	.invalidateHttpSession(Boolean.TRUE)
        	;
    }

    @Override
	public void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
    }
}