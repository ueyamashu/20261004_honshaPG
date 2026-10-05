package com.springboot.libraryPJ.BKS.service;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIR30outDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIRinDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.BookRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料情報登録確認画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIR30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BookRepository bookRepository;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	BookLibraryRepository bookLibraryRepository;

	/**
	 * 資料情報重複確認
	 * 
	 * @param dto 資料情報登録情報
	 */
	public TSBKSBIR30outDto checkBookInfo(TSBKSBIRinDto dto) {
		// log出力
		logger.debug("資料情報確認 :" + dto.getIsbn());

		TSBKSBIR30outDto outDto = new TSBKSBIR30outDto();

		List<BookLibraryEntity> bookList = bookLibraryRepository.findBookListByIsbn(dto.getIsbn());

		if (!bookList.isEmpty()) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGBKS003E, new String[] {}));

			return outDto;
		}

		return outDto;
	}

	/**
	 * 資料情報登録処理
	 * 
	 * @param dto 資料情報登録情報
	 */
	public TSBKSBIR30outDto insertBookInfo(TSBKSBIRinDto dto, HttpSession session) {
		// log出力
		logger.debug("資料情報登録 :" + dto.getIsbn());

		TSBKSBIR30outDto outDto = new TSBKSBIR30outDto();

		try {

			this.bookTblInsert(dto, session);
			this.libraryTblInsert(dto, session);

		} catch (Exception e) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM016E, new String[] { "資料情報の登録" }));

			return outDto;
		}

		return outDto;
	}

	/**
	 * 資料テーブルINSERT
	 * 
	 * @param dto 資料情報登録情報
	 */
	private void bookTblInsert(TSBKSBIRinDto dto, HttpSession session) {

		// 更新用データ作成
		BookEntity bookEntity = new BookEntity();
		bookEntity.setReleaseDate(Date.valueOf(dto.getReleaseDate().replace("/", "-")));
		bookEntity.setTitle(dto.getTitle());
		bookEntity.setAuthor(dto.getAuthor());
		bookEntity.setIsbn(dto.getIsbn());
		bookEntity.setPublisher(dto.getPublisher());
		bookEntity.setCatgory(dto.getCategory());

		bookEntity = (BookEntity) CommonUtil.setWhoInfoInsert(bookEntity, session);

		bookRepository.save(bookEntity);
	}

	/**
	 * 蔵書テーブルINSERT
	 * 
	 * @param dto 資料情報登録情報
	 */
	private void libraryTblInsert(TSBKSBIRinDto dto, HttpSession session) {

		// インスタンス生成
		LibraryEntity libraryEntity = new LibraryEntity();
		libraryEntity.setIsbn(dto.getIsbn());
		libraryEntity.setArrivalDate(Date.valueOf(dto.getArrival().replace("/", "-")));

		libraryEntity = (LibraryEntity) CommonUtil.setWhoInfoInsert(libraryEntity, session);

		libraryRepository.save(libraryEntity);
	}
}
