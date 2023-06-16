package com.madive.bigcommerce.madiveone.admin.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = {"menus"})
@Builder
public class SessionUser implements Serializable {

	private static final long serialVersionUID = -1792337907328266912L;

	private final String guid;
	private final String token;
	private final String id;
	private final String name;
	private final String dept;

	private final List<Menu> menus;

	@Getter
	@RequiredArgsConstructor
	public static class Menu implements Serializable {

		private static final long serialVersionUID = -8048039147257467487L;

		private final int seq;
		private final String name;
		private final String uri;

		private List<Menu> menus = new ArrayList<>();

		public boolean isCurr(String uri) {
			for (Menu menu : menus)
				if (uri.startsWith(menu.uri))
					return true;
			return false;
		}
	}
}