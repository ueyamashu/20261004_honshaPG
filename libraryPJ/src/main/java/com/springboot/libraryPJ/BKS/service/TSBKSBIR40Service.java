package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIR40outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;

/**
 * 資料情報登録確認画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIR40Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BookLibraryRepository bookLibraryRepository;

	/**
	 * @param isbn
	 * @return
	 */
	public TSBKSBIR40outDto findBookInfo(String isbn) {
		// log出力
		logger.debug("isbn:" + isbn);

		TSBKSBIR40outDto outDto = new TSBKSBIR40outDto();

		List<BookLibraryEntity> bookList = bookLibraryRepository.findBookListByIsbn(isbn);

		if (bookList == null || bookList.size() == 0) {
			outDto.setResultCd(-1);
		} else {
			BookLibraryEntity bookInfo = bookList.get(0);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			outDto.setBookId(bookInfo.getBookId());
			outDto.setTitle(bookInfo.getTitle());
			outDto.setCategory(bookInfo.getCategory());
			outDto.setAuthor(bookInfo.getAuthor());
			outDto.setPublisher(bookInfo.getPublisher());
			outDto.setReleaseDate(sdf.format(bookInfo.getReleaseDate()));
			outDto.setIsbn(bookInfo.getIsbn());
			outDto.setArrival(sdf.format(bookInfo.getArrivalDate()));
		}

		return outDto;
	}
}
