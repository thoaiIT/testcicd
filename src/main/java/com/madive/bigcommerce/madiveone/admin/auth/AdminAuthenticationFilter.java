package com.madive.bigcommerce.madiveone.admin.auth;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.util.Const;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminAuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.trace("## AdminAuthenticationFilter: {}", ((HttpServletRequest) request).getRequestURI());

		SessionUser sessionUser = (SessionUser) ((HttpServletRequest) request).getSession().getAttribute(Const.SESSION_USER);
		if (!Objects.isNull(sessionUser)) {
			// TODO ... check roles
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
				((HttpServletRequest) request).getSession().invalidate();
			}
		}

		chain.doFilter(request, response);
	}
}