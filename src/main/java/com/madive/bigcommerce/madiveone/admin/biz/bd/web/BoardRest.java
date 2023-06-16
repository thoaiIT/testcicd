package com.madive.bigcommerce.madiveone.admin.biz.bd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.madive.bigcommerce.madiveone.admin.biz.bd.service.BoardService;
import com.madive.bigcommerce.madiveone.admin.domain.Board;
import com.madive.bigcommerce.madiveone.admin.domain.Result;
import com.madive.bigcommerce.madiveone.admin.domain.SessionUser;
import com.madive.bigcommerce.madiveone.admin.util.Const;
import com.madive.bigcommerce.madiveone.admin.util.IDGenerator;

@RestController
@RequestMapping("bd")
public class BoardRest {
	
	@Autowired
	private BoardService boardService;

	@GetMapping("nt/01")
	public Result list(Board board) {
		return boardService.getList(board);
	}

	@GetMapping("nt/02/{id}")
	public Result data(@PathVariable("id") String guid) {
		return Result.DATA(boardService.getData(guid));
	}

	@PostMapping("nt/03")
	public Result regist(@RequestBody Board board, 
			@SessionAttribute(name = Const.SESSION_USER, required = false) SessionUser sessionUser) {
		
		if(board.getBd10Guid() == null) {
			board.setBd10Guid(IDGenerator.generate(32));
		}
		board.setBd10RegGuid(sessionUser.getGuid());
		
		if(!boardService.isUpdate(board)) {
			return Result.FAILURE("Registration failed");
		}
		
		return Result.SUCCESS;
	}

	@DeleteMapping("nt/04/{id}")
	public Result delete(@PathVariable("id") String guid) {
		if(boardService.isDelete(guid)) {
			return Result.SUCCESS;
		}
		
		return Result.FAILURE("Delete failed.");
	}
}