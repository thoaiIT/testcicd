package com.madive.bigcommerce.madiveone.admin.biz.bd.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.madive.bigcommerce.madiveone.admin.biz.bd.mapper.BoardMapper;
import com.madive.bigcommerce.madiveone.admin.biz.cm.mapper.AttachFileMapper;
import com.madive.bigcommerce.madiveone.admin.domain.AttachFile;
import com.madive.bigcommerce.madiveone.admin.domain.Board;
import com.madive.bigcommerce.madiveone.admin.domain.Result;
import com.madive.bigcommerce.madiveone.admin.exception.AdminCustomException;
import com.madive.bigcommerce.madiveone.admin.exception.ErrorType;
import com.madive.bigcommerce.madiveone.admin.util.IDGenerator;
import com.madive.bigcommerce.madiveone.admin.util.StringUtils;

@Service
@Transactional
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private AttachFileMapper attachFileMapper;

	public Board getData(String guid) {
		Board board = boardMapper.bd10Data(guid);
		if(board != null) {
			AttachFile af = AttachFile.builder()
					.af10OwnerTable("bd10")
					.af10OwnerGuid(guid)
					.build();
			List<AttachFile> afList = attachFileMapper.af10List(af);
			if(afList.size() > 0) {
				List<Map<String, String>> attachFiles = Lists.newArrayList();
				for(AttachFile item : afList) {
					Map<String, String> data = Maps.newHashMap();
					data.put("fileId", item.getAf10Guid());
					data.put("fileName", item.getAf10OrgName());
					
					attachFiles.add(data);
				}
				board.setAttachFiles(attachFiles);
			}
		}
		return board;
	}

	public Result getList(Board board) {
		return Result.LIST(boardMapper.bd10Count(board), boardMapper.bd10List(board));
	}
	
	public int update(Board board) {
		return boardMapper.bd10Update(board);
	}
	
	public boolean isUpdate(Board board) {
		if(this.update(board) > 0) {
			if(board.getAttachFiles() != null) {
				for(Map<String, String> file : board.getAttachFiles()) {
					if(!StringUtils.isNull(file.get("isNew"))) {
						AttachFile af = AttachFile.builder()
								.af10Guid(IDGenerator.generate(32))
								.af10OwnerTable("bd10")
								.af10OwnerGuid(board.getBd10Guid())
								.af10OrgName(file.get("fileName"))
								.af10SavePath(board.getPath()+file.get("fileId"))
								.af10RegGuid(board.getBd10RegGuid())
								.build();
						
						if(attachFileMapper.af10Insert(af) == 0) {
							throw new AdminCustomException(ErrorType.DATA0006);
						};
					}
				}
			}
			
			if(board.getDeleteFiles() != null) {
				for(String guid : board.getDeleteFiles()) {
					attachFileMapper.af10Delete(guid);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public int delete(String guid) {
		return boardMapper.bd10Delete(guid);
	}
	
	public boolean isDelete(String guid) {
		if(this.delete(guid) > 0) {
			return true;
		}
		
		return false;
	}
}