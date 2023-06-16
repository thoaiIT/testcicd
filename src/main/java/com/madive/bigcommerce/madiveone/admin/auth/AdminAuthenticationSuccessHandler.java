package com.madive.bigcommerce.madiveone.admin.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.domain.User;
import com.madive.bigcommerce.madiveone.admin.util.Const;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("## AdminAuthenticationSuccessHandler");
		log.debug("## authentication.getPrincipal(): {}", authentication.getPrincipal());
		log.debug("## authentication.getCredentials(): {}", authentication.getCredentials());
		log.debug("## authentication.isAuthenticated(): {}", authentication.isAuthenticated());

		clearAuthenticationAttributes(request);  // TODO ... with SavedRequest ...

		User user = (User) authentication.getPrincipal();

		HttpSession session = request.getSession();
		session.setAttribute(Const.SESSION_USER,
				SessionUser.builder()
    					.guid(user.getAd10Guid())
		    			.id(user.getAd10LoginId())
			    		.name(user.getAd10Name())
					    .build());

		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("message", "");
		response.getOutputStream().write(
				new ObjectMapper().writeValueAsBytes(result));
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}