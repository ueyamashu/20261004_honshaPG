package com.springboot.libraryPJ.REN.service;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.REN.dto.TSRENRBRoutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料貸出確認
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSRENRBR30Service {

	@Autowired
	RentalRepository rentalRepository;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	BookLibraryRepository bookLibraryRepository;

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 会員IDから会員情報を取得
	 *
	 * @param memberId
	 * @return
	 */
	public List<MemberEntity> getMemberInfo(String memberId) {

		logger.debug("getMemberInfo");

		// データ取得
		List<MemberEntity> memberList = memberRepository.findByMemberIdAndDeleteFlag(Integer.valueOf(memberId),
				CommonConstants.NOT_DELETE);
		return memberList;
	}

	/**
	 * 資料IDから資料情報を取得
	 *
	 * @param bookIdList
	 * @throws ParseException
	 */
	public List<TSRENRBRoutDto> getBookInfo(List<String> bookIdList) throws ParseException {

		logger.debug("getBookInfo");
		List<BookLibraryEntity> bookList = bookLibraryRepository.findRentalBookListByBookIdList(bookIdList);

		List<TSRENRBRoutDto> outDtoList = new ArrayList<TSRENRBRoutDto>();

		// 取得失敗した場合エラーメッセージを設定する
		if (bookList.size() == 0 || bookList == null) {
			TSRENRBRoutDto outDto = new TSRENRBRoutDto();
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM017E,
					new String[] { CommonConstants.STRING_BOOK_JOHO }));
			outDtoList.add(outDto);
			return outDtoList;
		}

		// 取得結果をDtoに変換する
		outDtoList = bookList.stream().map(o -> new TSRENRBRoutDto(o)).collect(Collectors.toList());

		// 返却期限を設定する
		for (TSRENRBRoutDto outDto : outDtoList) {
			// 出版日判定
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate now = LocalDate.now();
			LocalDate newBook = outDto.getReleaseDate().toLocalDate().plusMonths(3);

			if (CheckerUtil.compareToDate(newBook.format(formatter)) == CommonConstants.CHECK_RESULT_AFTER_DATE) {
				// 出版日から3か月以内は10日まで
				String rentalDueDate = now.plusDays(10).format(formatter);
				outDto.setRentalDueDate(Date.valueOf(rentalDueDate.replace("/", "-")));
			} else {
				// 上記以外はは15日まで
				String rentalDueDate = now.plusDays(15).format(formatter);
				outDto.setRentalDueDate(Date.valueOf(rentalDueDate.replace("/", "-")));
			}
		}
		return outDtoList;
	}

	/**
	 * insertRentalInfo
	 *
	 * @param rentalIdListStr
	 * @param memberId
	 * @param session
	 * @return
	 */
	public List<Integer> insertRentalInfo(String rentalIdListStr, String memberId, HttpSession session) {

		logger.debug("insertRentalInfo");
		List<Integer> rentalIdList = new ArrayList<Integer>();

		// 資料ID取得する
		List<String> bookIdList = new ArrayList<String>();
		for (String token : rentalIdListStr.split(",")) {
			if (token.contains(" bookId=") == true) {
				bookIdList.add(token.substring(" bookId=".length()));
			}
		}
		logger.debug("list bookIdList : " + bookIdList);

		// 取得した資料IDから資料情報を取得する
		List<TSRENRBRoutDto> rentalList = new ArrayList<>();
		try {
			rentalList = getBookInfo(bookIdList);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 資料情報件数分、ループ処理を行い、貸出情報を登録する
		for (TSRENRBRoutDto rentalDto : rentalList) {

			logger.debug("TSRENRBRoutDto bookid: " + rentalDto.getBookId());
			logger.debug("TSRENRBRoutDto memberid: " + memberId);

			RentalEntity rentalEntity = new RentalEntity();

			rentalEntity.setBookId(rentalDto.getBookId());
			rentalEntity.setMemberId(Integer.parseInt(memberId));
			rentalEntity.setRentalDate(Date.valueOf(LocalDate.now()));
			rentalEntity.setRentalDueDate(rentalDto.getRentalDueDate());
			rentalEntity.setRemindFlag(CommonConstants.NOT_REMIND);

			rentalEntity = (RentalEntity) CommonUtil.setWhoInfoInsert(rentalEntity, session);

			rentalRepository.save(rentalEntity);

			rentalIdList.add(rentalEntity.getRentalId());
			logger.debug("rentalEntity rentalId: " + rentalEntity.getRentalId());
		}

		// 貸出IDを返却する
		return rentalIdList;
	}

	/**
	 * 貸出可能資料なのかチェックする。
	 *
	 * @param indto
	 * @return
	 */
	public int checkRentalPossibleMember(String memberId) {
		// 会員IDを条件に貸出情報を取得する。
		List<RentalEntity> rentalList = rentalRepository.findRentalListByMemberId(memberId);
		return rentalList.size();
	}

	/**
	 * 貸出可能資料なのかチェックする。
	 *
	 * @param indto
	 * @return
	 */
	public int checkRentalPossibleBook(String bookId) {
		// 資料IDを条件に貸出情報を取得する。
		List<RentalEntity> rentalList = rentalRepository.findRentalListByBookId(bookId);
		// 取得結果が貸出中の場合はControllerクラスからエラーメッセージを出力する。
		if (rentalList.size() >= 1) {
			return CommonConstants.CHECK_RESULT_RENTAL_BOOK;
		}

		// 資料IDと削除フラグを条件に蔵書情報を取得する。
		List<LibraryEntity> library = libraryRepository.findByBookIdAndDeleteFlag(bookId, CommonConstants.DELETE);
		// 取得結果が廃棄の場合はControllerクラスからエラーメッセージを出力する。
		if (library.size() >= 1) {
			return CommonConstants.CHECK_RESULT_DISPOSAL_BOOK;
		}

		return CommonConstants.CHECK_RESULT_AVAILABLE_RENTAL;
	}
}
