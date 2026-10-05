package com.springboot.libraryPJ.BKS.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBTBinDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBTBoutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import io.micrometer.common.util.StringUtils;

/**
 * 資料一覧画面Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBTB10Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BookLibraryRepository bookLibraryRepository;

	/**
	 * 資料一覧画面検索
	 * 
	 * @param inDto
	 * @return
	 */
	public TSBKSBTBoutDto searchBookInfo(TSBKSBTBinDto inDto) {
		TSBKSBTBoutDto outDto = new TSBKSBTBoutDto();

		List<BookLibraryEntity> bookList = new ArrayList<BookLibraryEntity>();

		// ２次開発
		if (!StringUtils.isEmpty(inDto.getIsbn())) {
			logger.debug("case : isbn");
			bookList = bookLibraryRepository.findBookListByIsbn(inDto.getIsbn());
		} else if (!StringUtils.isEmpty(inDto.getBookTitle()) && !StringUtils.isEmpty(inDto.getAuthorName())
				&& !StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : All");
			bookList = bookLibraryRepository.findBookListByTitleAndAuthorAndCategory(inDto.getBookTitle(),
					inDto.getAuthorName(), inDto.getCategoryCode());
		} else if (!StringUtils.isEmpty(inDto.getBookTitle()) && !StringUtils.isEmpty(inDto.getAuthorName())
				&& StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : title, author");
			bookList = bookLibraryRepository.findBookListByTitleAndAuthor(inDto.getBookTitle(), inDto.getAuthorName());
		} else if (!StringUtils.isEmpty(inDto.getBookTitle()) && StringUtils.isEmpty(inDto.getAuthorName())
				&& !StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : title, category");
			bookList = bookLibraryRepository.findBookListByTitleAndCategory(inDto.getBookTitle(),
					inDto.getCategoryCode());
		} else if (StringUtils.isEmpty(inDto.getBookTitle()) && !StringUtils.isEmpty(inDto.getAuthorName())
				&& !StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : author, category");
			bookList = bookLibraryRepository.findBookListByAuthorAndCategory(inDto.getAuthorName(),
					inDto.getCategoryCode());
		} else if (!StringUtils.isEmpty(inDto.getBookTitle()) && StringUtils.isEmpty(inDto.getAuthorName())
				&& StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : title");
			bookList = bookLibraryRepository.findBookListByTitle(inDto.getBookTitle());
		} else if (StringUtils.isEmpty(inDto.getBookTitle()) && !StringUtils.isEmpty(inDto.getAuthorName())
				&& StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : author");
			bookList = bookLibraryRepository.findBookListByAuthor(inDto.getAuthorName());
		} else if (StringUtils.isEmpty(inDto.getBookTitle()) && StringUtils.isEmpty(inDto.getAuthorName())
				&& !StringUtils.isEmpty(inDto.getCategoryCode())) {
			logger.debug("case : category");
			bookList = bookLibraryRepository.findBookListByCategory(inDto.getCategoryCode());
		} else {
			logger.debug("case : -");
			bookList = bookLibraryRepository.findBookList();
		}

		if (bookList == null || bookList.size() == 0) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "資料情報" }));
		}
		
		for(BookLibraryEntity entity : bookList) {
			outDto.setIchiranData(entity);			
		}

		return outDto;
	}
}
