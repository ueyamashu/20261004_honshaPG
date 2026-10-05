package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20DTO;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料情報更新確認画面のService
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIC30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	BookRepository bookRepository;

	public TSBKSBIC20outDto updateBookInfo(TSBKSBIC20DTO tsbksbic20dto, HttpSession session) {

		logger.debug("update target bookId:" + tsbksbic20dto.getBookId());
		logger.debug("update target ibn:" + tsbksbic20dto.getIsbnOrigin());

		TSBKSBIC20outDto outDto = new TSBKSBIC20outDto();

		List<BookEntity> bookList = bookRepository.findByBookIdAndIsbnAndExclusiveKeyAndDeleteFlag(
				tsbksbic20dto.getIsbnOrigin(), tsbksbic20dto.getBookExclusiveKey(), CommonConstants.NOT_DELETE);

		if (bookList == null || bookList.size() == 0) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM016E, new String[] { "資料情報更新" }));

			return outDto;
		}

		BookEntity bookEntity = bookList.get(0);

		bookEntity.setCatgory(tsbksbic20dto.getCategory());
		bookEntity.setTitle(tsbksbic20dto.getTitle());
		bookEntity.setAuthor(tsbksbic20dto.getAuthor());
		bookEntity.setPublisher(tsbksbic20dto.getPublisher());

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			java.util.Date utilDate = sdf.parse(tsbksbic20dto.getReleaseDate());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			bookEntity.setReleaseDate(sqlDate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		bookEntity = (BookEntity) CommonUtil.setWhoInfoUpdate(bookEntity, session);

		bookRepository.save(bookEntity);

		List<LibraryEntity> libraryList = libraryRepository.findByBookIdAndIsbnAndExclusiveKeyAndDeleteFlag(
				tsbksbic20dto.getBookId(), tsbksbic20dto.getIsbnOrigin(), tsbksbic20dto.getLibraryExclusiveKey(),
				CommonConstants.NOT_DELETE);
		if (libraryList == null || libraryList.size() == 0) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM016E, new String[] { "資料情報更新" }));

			return outDto;
		}

		LibraryEntity libraryEntity = libraryList.get(0);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			java.util.Date utilDate = sdf.parse(tsbksbic20dto.getArrival());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			libraryEntity.setArrivalDate(sqlDate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		libraryEntity = (LibraryEntity) CommonUtil.setWhoInfoUpdate(libraryEntity, session);

		libraryRepository.save(libraryEntity);

		return outDto;

	}
}
