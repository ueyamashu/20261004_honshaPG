package com.springboot.libraryPJ.REN.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.REN.dto.TSRENRBRoutDto;
import com.springboot.libraryPJ.REN.service.TSRENRBR30Service;
import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.dto.TSRENRBTDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料貸出確認
 */
@Controller
@RequestMapping("/TSRENRBR30/")
public class TSRENRBR30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRENRBR30Service tsrenrbr30Service;

	/**
	 * 資料貸出確認画面初期表示
	 *
	 * @param tsrenrbtDto
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("init")
	public String initTSRENRBR30(@ModelAttribute TSRENRBTDto tsrenrbtDto, HttpSession session, BindingResult result,
			Model model) {
		// 入力内容のmodel格納
		model.addAttribute("tsrenrbtDto", tsrenrbtDto);

		// LOG出力
		logger.debug(TSRENRBR30Controller.class.toString());
		logger.debug("rental memberId: " + tsrenrbtDto.getMemberId());
		logger.debug("bookIdList: " + tsrenrbtDto.getBookIdList());
		logger.debug("initTSRENRBR30 tsrenrbtDto: " + tsrenrbtDto);

		try {
			// 初期チェックする。
			initCheck(tsrenrbtDto, result, model);

			// 初期チェックエラー発生した場合貸出資料一覧画面を表示する。
			if (result.hasErrors()) {
				setPaginationModel(tsrenrbtDto, session, model);

				// 貸出資料一覧画面に遷移する
				return "book/rental_list";
			}

			// 会員の名前を取得
			List<MemberEntity> memberList = tsrenrbr30Service.getMemberInfo(tsrenrbtDto.getMemberId());
			// 取得結果がエラーの場合はエラーメッセージを出力する。
			if (memberList.size() == 0) {
				logger.debug("rental impossible");

				// エラーメッセージ設定
				model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
						new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));

				// ページネーションと貸出資料一覧を設定
				setPaginationModel(tsrenrbtDto, session, model);

				// 貸出資料一覧画面に遷移する
				return "book/rental_list";
			}
			// 取得した名前をmodelに設定
			model.addAttribute("userName", memberList.get(0).getName());

			// 貸出情報を取得
			List<TSRENRBRoutDto> rentalList = tsrenrbr30Service.getBookInfo(tsrenrbtDto.getBookIdList());

			// 取得結果がエラーの場合はエラーメッセージを出力する。
			if (rentalList.get(0).getResultCd() == -1) {
				// エラーメッセージ設定
				model.addAttribute("error", rentalList.get(0).getErrmsg());

				// ページネーションと貸出資料一覧を設定
				setPaginationModel(tsrenrbtDto, session, model);

				// 貸出資料一覧画面に遷移する
				return "book/rental_list";
			}

			// サービスからの取得結果をmodelに格納
			model.addAttribute("rentalList", rentalList);

			// 資料貸出確認画面を表示する。
			return "book/rental_confirm";
		} catch (Exception e) {
			logger.debug("資料貸出に失敗しました。");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_BOOK_RENTAL }));
			setPaginationModel(tsrenrbtDto, session, model);
			return "book/rental_list";
		}
	}

	/**
	 * 資料貸出確定
	 *
	 * @param rentalList
	 * @param tsrenrbtDto
	 * @param result
	 * @param session
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("complete")
	public String insertRentalDetermine(@ModelAttribute("rentalList") String rentalList,
			@ModelAttribute("tsrenrbtDto") TSRENRBTDto tsrenrbtDto, BindingResult result, HttpSession session,
			Model model, RedirectAttributes redirectAttributes) {

		// 入力内容のmodel格納
		model.addAttribute("tsrenrbtDto", tsrenrbtDto);
		model.addAttribute("rentalList", rentalList);
		// LOG出力
		logger.debug(TSRENRBR30Controller.class.toString());
		logger.debug("insertRentalDetermine");
		logger.debug("tsrenrbtDto: " + tsrenrbtDto);
		logger.debug("list rentalList : " + rentalList);

		try {
			// サービス呼び出し
			List<Integer> rentalIdList = tsrenrbr30Service.insertRentalInfo(rentalList, tsrenrbtDto.getMemberId(),
					session);
			// 値渡し
			redirectAttributes.addAttribute("rentalIdList", rentalIdList);
			redirectAttributes.addAttribute("memberId", tsrenrbtDto.getMemberId());

			// 貸出資料完了画面へリダイレクト
			return "redirect:/TSRENRBR40/init";
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("資料貸出に失敗しました。");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_BOOK_RENTAL }));
			return "book/rental_confirm";
		}
	}

	/**
	 * 貸出資料一覧画面に戻る
	 *
	 * @return
	 */
	@PostMapping("book/list")
	public String displayRentalBookList() {
		// 資料情報一覧画面へリダイレクト
		return "redirect:/TSRENRBT10/init";
	}

	/**
	 * 貸出資料一覧でチェックした資料IDを確認する。
	 *
	 * @param tsrenrbtDto
	 * @param result
	 * @param model
	 */
	public void initCheck(TSRENRBTDto tsrenrbtDto, BindingResult result, Model model) {
		// チェックした資料IDリストを取得
		List<String> bookIdList = tsrenrbtDto.getBookIdList();

		ArrayList<String> errMsgArr = new ArrayList<String>();

		// 資料IDの存在をチェックする。
		if (bookIdList.isEmpty() || bookIdList.size() == 0) {
			logger.debug("param bookId empty");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM002E,
					new String[] { CommonConstants.STRING_BOOK }));
			ObjectError err = new ObjectError("error", "");
			result.addError(err);
			return;
		}

		// 貸出会員一覧で選択した会員が借りた本の数を取得する
		int rentalBookNumber = tsrenrbr30Service.checkRentalPossibleMember(tsrenrbtDto.getMemberId());

		// 取得結果+貸出資料一覧で選択した本の数が5件を超える場合はエラーメッセージを出力する。
		if (rentalBookNumber + bookIdList.size() > CommonConstants.RANTAL_LIMIT) {
			logger.debug("rental impossible over max number");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGREN001E,
					new String[] { Integer.valueOf(rentalBookNumber).toString() }));
			ObjectError err = new ObjectError("error", "");
			result.addError(err);
			return;
		}

		for (String bookId : bookIdList) {

			// 貸出可能資料なのかをチェックする。
			int chkResult = tsrenrbr30Service.checkRentalPossibleBook(bookId);

			// 資料が貸出中の場合、エラーメッセージを設定する
			if (chkResult == -1) {
				logger.debug("rental impossible bookId: ");
				String errMsg = MessageConstants.getMessage(MessageConstants.MSGREN003E, new String[] { bookId });
				errMsgArr.add(errMsg);
			}
			// 資料が廃棄済みの場合、エラーメッセージを設定する
			else if (chkResult == 0) {
				logger.debug("rental impossible bookId: ");
				String errMsg = MessageConstants.getMessage(MessageConstants.MSGREN004E, new String[] { bookId });
				errMsgArr.add(errMsg);
			}
		}

		// チェック結果にエラーがある場合はエラーメッセージを出力する。
		if (!errMsgArr.isEmpty() && errMsgArr.size() != 0) {
			model.addAttribute("rentalBookIdError", errMsgArr);
			ObjectError err = new ObjectError("rentalBookIdError", "");
			result.addError(err);
		}
	}

	/**
	 * エラーが発生した場合のmodelを設定する
	 *
	 * @param tsrenrbtDto
	 * @param session
	 * @param model
	 */
	public void setPaginationModel(TSRENRBTDto tsrenrbtDto, HttpSession session, Model model) {
		// ページネーションと資料一覧を設定
		List<BookLibraryEntity> bookList = (List<BookLibraryEntity>) session.getAttribute("bookLibraryList");
		String baseUrl = "/TSRENRBT10/init" + "?memberId=" + tsrenrbtDto.getMemberId() + "&";

		List<Object> pageList = new ArrayList<>();
		for (Object item : bookList) {
			pageList.add(item);
		}
		CommonUtil.pageModule(1, 5, model, pageList, "bookLibraryList", baseUrl);
	}
}