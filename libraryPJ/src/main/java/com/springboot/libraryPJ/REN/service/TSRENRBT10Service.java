package com.springboot.libraryPJ.REN.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.REN.dto.TSRENRBTinDto;
import com.springboot.libraryPJ.REN.dto.TSRENRBToutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 貸出資料一覧
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSRENRBT10Service {

	@Autowired
	BookLibraryRepository booklibraryRepository;
	@Autowired
	RentalRepository rentalRepository;

	/**
	 * 会員が貸出できる資料情報リストを取得する。
	 *
	 * @param indto
	 * @return
	 */
	public TSRENRBToutDto findBookLibraryList() {
		// 出力DTOを設定する。
		TSRENRBToutDto outDto = new TSRENRBToutDto();

		// 会員IDを条件に貸出可能な資料情報を取得する。
		List<BookLibraryEntity> bookLibraryList = booklibraryRepository.findRentalBookList();

		// 取得結果が0件の場合はControllerクラスからエラーメッセージを出力する。
		if (bookLibraryList.size() == 0 || bookLibraryList == null) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM017E,
					new String[] { CommonConstants.STRING_RENTAL_BOOK }));
		}

		// 取得した貸出可能な資料情報をセットする。
		outDto.setBookLibraryList(bookLibraryList);

		// 出力DTOを返す。
		return outDto;
	}

	/**
	 * 貸出可能な資料を検索する。
	 *
	 * @param indto
	 * @return
	 */
	public TSRENRBToutDto searchBookList(TSRENRBTinDto indto) {
		// 出力DTOを設定する。
		TSRENRBToutDto outDto = new TSRENRBToutDto();

		// 貸出資料リストを定義する。
		List<BookLibraryEntity> bookLibraryList = new ArrayList<BookLibraryEntity>();

		// 資料IDのみ存在する場合は会員IDと資料IDを条件に資料情報を取得する。
		if (!StringUtils.isEmpty(indto.getBookId()) && StringUtils.isEmpty(indto.getTitle())) {
			bookLibraryList = booklibraryRepository.findRentalBookListByBookId(Integer.parseInt(indto.getBookId()));
		}
		// 資料名のみ存在する場合は会員IDと資料名を条件に資料情報を取得する。
		else if (StringUtils.isEmpty(indto.getBookId()) && !StringUtils.isEmpty(indto.getTitle())) {
			bookLibraryList = booklibraryRepository.findRentalBookListByTitle(indto.getTitle());
		}
		// 資料IDと資料名が存在する場合は会員IDと資料ID、資料名を条件に資料情報を取得する。
		else if (!StringUtils.isEmpty(indto.getBookId()) && !StringUtils.isEmpty(indto.getTitle())) {
			bookLibraryList = booklibraryRepository
					.findRentalBookListByBookIdAndTitle(Integer.parseInt(indto.getBookId()), indto.getTitle());
		}
		// その他の場合は会員IDを条件に資料情報を取得する。
		else {
			bookLibraryList = booklibraryRepository.findRentalBookList();
		}

		// 取得結果が0件の場合はエラーメッセージを出力する。
		if (bookLibraryList.size() == 0 || bookLibraryList == null) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM013E,
					new String[] { CommonConstants.STRING_RENTAL_BOOK }));
		}

		// 取得した貸出可能な資料情報をセットする。
		outDto.setBookLibraryList(bookLibraryList);

		// 出力DTOを返す。
		return outDto;
	}

	/**
	 * 貸出可能会員なのかチェックする。
	 *
	 * @param indto
	 * @return
	 */
	public TSRENRBToutDto checkRentalPossibleMember(TSRENRBTinDto indto) {
		// 出力DTOを設定する。
		TSRENRBToutDto outDto = new TSRENRBToutDto();

		// 会員IDの延滞情報を取得する。
		List<RentalEntity> arrearsList = rentalRepository.findArrearsListByMemberId(indto.getMemberId());

		// 取得結果が延滞中の場合はControllerクラスからエラーメッセージを出力する。
		if (arrearsList.size() >= 1) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.MSGREN002E);
		}

		// 会員IDを条件に貸出情報を取得する。
		List<RentalEntity> rentalList = rentalRepository.findRentalListByMemberId(indto.getMemberId());

		// 取得結果が5件以上の場合はControllerクラスからエラーメッセージを出力する。
		if (rentalList.size() >= CommonConstants.RANTAL_LIMIT) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGREN001E,
					new String[] { Integer.valueOf(CommonConstants.RANTAL_LIMIT).toString() }));
		}

		// 出力DTOを返す。
		return outDto;
	}

}
