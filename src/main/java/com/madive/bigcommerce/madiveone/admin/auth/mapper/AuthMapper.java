package com.madive.bigcommerce.madiveone.admin.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.madive.bigcommerce.madiveone.admin.domain.User;

@Mapper
public interface AuthMapper {

	User data(User user);
}