package com.madive.bigcommerce.madiveone.admin.biz.bd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.madive.bigcommerce.madiveone.admin.domain.Board;

@Mapper
public interface BoardMapper {

	List<Board> bd10List(Board search);
	int bd10Count(Board board);
	Board bd10Data(String guid);
	int bd10Update(Board board);
	int bd10Delete(String guid);
	
}