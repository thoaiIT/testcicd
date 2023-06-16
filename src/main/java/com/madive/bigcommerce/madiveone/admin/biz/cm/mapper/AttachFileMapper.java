package com.madive.bigcommerce.madiveone.admin.biz.cm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.madive.bigcommerce.madiveone.admin.domain.AttachFile;

@Mapper
public interface AttachFileMapper {

	List<AttachFile> af10List(AttachFile attachFile);
	AttachFile af10Data(String guid);
	int af10Insert(AttachFile attachFile);
	int af10Delete(String af10Guid);
	
}