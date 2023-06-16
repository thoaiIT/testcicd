package com.madive.bigcommerce.madiveone.admin.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madive.bigcommerce.madiveone.admin.auth.mapper.AuthMapper;
import com.madive.bigcommerce.madiveone.admin.domain.User;

@Service
@Transactional
public class AuthService {

	@Autowired
	private AuthMapper authMapper;

	public User getUser(User user) {
		return authMapper.data(user);
	}

}