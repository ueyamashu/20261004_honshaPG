package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIS20outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

/**
 * 資料入庫画面のService
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIS20Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	BookRepository bookRepository;

	public TSBKSBIS20outDto searchInfo(String isbn) {

		// log出力
		logger.debug("isbn" + isbn);

		TSBKSBIS20outDto outDto = new TSBKSBIS20outDto();

		// book
		BookEntity bookInfo = bookRepository.findByIsbnAndDeleteFlag(isbn, CommonConstants.NOT_DELETE);

		outDto.setIsbn(bookInfo.getIsbn());
		outDto.setTitle(bookInfo.getTitle());
		outDto.setCategory(bookInfo.getCatgory());
		outDto.setAuthor(bookInfo.getAuthor());
		outDto.setPublisher(bookInfo.getPublisher());

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			outDto.setReleaseDate(sdf.format(bookInfo.getReleaseDate()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		// library
		List<LibraryEntity> libraryList = libraryRepository.findByIsbnAndDeleteFlag(isbn, CommonConstants.NOT_DELETE);

		if (libraryList == null || libraryList.size() == 0) {
			outDto.setBookCount(0);
		} else {
			outDto.setBookCount(libraryList.size());
		}
		return outDto;
	}
}
