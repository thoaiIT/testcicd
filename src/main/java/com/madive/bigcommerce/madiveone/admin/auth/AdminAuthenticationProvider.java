package com.madive.bigcommerce.madiveone.admin.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.madive.bigcommerce.madiveone.admin.auth.service.AuthService;
import com.madive.bigcommerce.madiveone.admin.domain.User;
import com.madive.bigcommerce.madiveone.admin.exception.AdminAuthenticationException;
import com.madive.bigcommerce.madiveone.admin.exception.ErrorType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AuthService authService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("## AdminAuthenticationProvider");
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		log.debug("## username: {}", username);
		log.trace("## password: {}", password);

		User user = User.builder()
				.ad10LoginId(username)
				.ad10Pwd(password)
				.build();

		user = authService.getUser(user);
		log.debug("## user: {}", user);
		
		if(user == null || !user.getAd10Pwd().equals(user.getAd10PwdIn())) {
			throw new AdminAuthenticationException(ErrorType.INVALID_USERNAME_PASSWORD);
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()));

		return new UsernamePasswordAuthenticationToken(user, user.getAd10Guid(), authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}