package com.madive.bigcommerce.madiveone.admin.biz.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madive.bigcommerce.madiveone.admin.biz.cm.mapper.AttachFileMapper;
import com.madive.bigcommerce.madiveone.admin.domain.AttachFile;

@Service
@Transactional
public class AttachFileService {

	@Autowired
	private AttachFileMapper attachFileMapper;

	public AttachFile getData(String guid) {
		return attachFileMapper.af10Data(guid);
	}
}