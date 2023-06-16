package com.madive.bigcommerce.madiveone.admin.auth;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.util.Const;

public class AuthUtils {

	public static String getAccessToken() {
		RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		SessionUser sessionUser = (SessionUser) attributes.getAttribute(Const.SESSION_USER, RequestAttributes.SCOPE_SESSION);
		return Objects.isNull(sessionUser) ? StringUtils.EMPTY : sessionUser.getToken();
	}
}