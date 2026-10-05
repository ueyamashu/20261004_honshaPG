package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 資料情報更新画面のService
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIC20Service {
	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BookLibraryRepository bookLibraryRepository;

	/**
	 * @param bookId
	 * @return
	 */
	public TSBKSBIC20outDto searchBookId(String bookId) {

		// log出力
		logger.debug("bookId:" + bookId);

		TSBKSBIC20outDto outDto = new TSBKSBIC20outDto();

		List<BookLibraryEntity> bookList = bookLibraryRepository.findBookListByBookId(Integer.parseInt(bookId));

		if (bookList == null || bookList.size() == 0) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "資料情報" }));
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
			outDto.setIsbnOrigin(bookInfo.getIsbn());
			outDto.setBookExclusiveKey(bookInfo.getBookExclusiveKey());
			outDto.setLibraryExclusiveKey(bookInfo.getLibraryExclusiveKey());
		}

		return outDto;

	}

}
