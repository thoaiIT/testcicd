package com.madive.bigcommerce.madiveone.admin.interceptor;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madive.bigcommerce.madiveone.admin.domain.Result;
import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.util.Const;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
	@SuppressWarnings("unused")
	private static Logger L = LoggerFactory.getLogger(LoginCheckInterceptor.class);

	public static final String LOGIN_FAILURE = "LOGIN_FAILURE";

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		SessionUser user = (SessionUser) session.getAttribute(Const.SESSION_USER);
		L.debug("## LoginCheckInterceptor == {}", user);
		
		String accept = request.getHeader("Accept");
		if (user == null && accept.indexOf("application/json") > -1) {
			/*
			 * json 일 경우는 메시지 처리 해야함.
			 * return false 인 경우 text/plain 처리 되며 오류 메시지가 화면에 표시되지 않음.
			 */
			// 현재는 모두 json 일 경우만 존재
			response.setContentType("application/json");
			ObjectMapper om = new ObjectMapper();
			OutputStream os = response.getOutputStream();
			os.write(om.writeValueAsBytes(Result.FAILURE(LOGIN_FAILURE)));
			return Boolean.FALSE;
		}

		return super.preHandle(request, response, handler);
	}
}