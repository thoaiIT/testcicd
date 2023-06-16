package com.madive.bigcommerce.madiveone.admin.biz.cm.web;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.madive.bigcommerce.madiveone.admin.auth.RoleType;
import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.exception.AdminCustomException;
import com.madive.bigcommerce.madiveone.admin.exception.ErrorType;
import com.madive.bigcommerce.madiveone.admin.util.Const;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class PageCont {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ThymeleafProperties thymeleafProperties;

	@GetMapping
	public String main(Authentication authentication) {
		if (!Objects.isNull(authentication)) {
			boolean isTemp = authentication.getAuthorities().stream()
					.anyMatch(authority ->
							RoleType.ROLE_TEMP == RoleType.valueOf(authority.getAuthority()));
			if (isTemp)
				return "redirect:/lo/04";
		}

		return "redirect:/bd/list";
	}

	@GetMapping("lo/01")
	public String auth(
			@SessionAttribute(name = Const.SESSION_USER, required = false) SessionUser sessionUser) {
		if (Objects.isNull(sessionUser))
			return "biz/lo/AD_LO_01";
		return "redirect:/bd/list";
	}

	@GetMapping("{path}/{action}")
	public String page(@PathVariable String path, @PathVariable String action) {
		log.debug("@@ path: {}, action: {}", path, action);

		String view = new StringBuilder()
				.append("biz").append("/")
				.append(path).append("/")
				.append("AD").append("_").append(path.toUpperCase()).append("_").append(action.toUpperCase())
				.toString();

		Resource resource = resourceLoader.getResource(thymeleafProperties.getPrefix() + view + thymeleafProperties.getSuffix());
		if (!resource.exists())
			throw new AdminCustomException(ErrorType.HTTP0404);

		return view;
	}
}