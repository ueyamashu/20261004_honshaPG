package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIS30inDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIS30outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 * 資料入庫確認画面のService
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIS30Service {
	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LibraryRepository libraryRepository;
	
	public TSBKSBIS30outDto insertLibraryInfo(TSBKSBIS30inDto inDto, HttpSession session) {
		// log出力
		logger.debug("isbn: " + inDto.getIsbn());
		
		TSBKSBIS30outDto outDto = new TSBKSBIS30outDto();
		
		java.sql.Date arrivalDate = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			java.util.Date utilDate = sdf.parse(inDto.getArrivalDate());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			arrivalDate = sqlDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Integer> bookIdList = new ArrayList<Integer>();
		
		for(int i=0; i < inDto.getAddBookCount(); i++) {
			LibraryEntity entity = new LibraryEntity();
			
			entity.setIsbn(inDto.getIsbn());
			entity.setArrivalDate(arrivalDate);
			
			entity = (LibraryEntity) CommonUtil.setWhoInfoInsert(entity, session);

			libraryRepository.save(entity);
			
			logger.debug("insert No." + (i+1) + " input bookId: " + entity.getBookId());
			
			bookIdList.add(entity.getBookId());
		}
		
		outDto.setIsbn(inDto.getIsbn());
		outDto.setBookIdList(bookIdList);
		
		return outDto;
	}
}
