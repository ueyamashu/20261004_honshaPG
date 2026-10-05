package com.springboot.libraryPJ.MEM.controller;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
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

import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20DTOForm;
import com.springboot.libraryPJ.MEM.service.TSMEMMIC30Service;
import com.springboot.libraryPJ.ZZ.service.TSCOMMIR30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 会員情報更新確認
 */
@Controller
@RequestMapping("/TSMEMMIC30/")
public class TSMEMMIC30Controller {

	// ログ処理
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMIC30Service tsmemmic30Service;

	@Autowired
	private TSCOMMIR30Service tscommir30Service;

	/*
	 * TODO radioボタンがエラーメッセージが表示され画面更新された際に外れてしまう不具合の修正を行う
	 */
	/**
	 * 初期処理
	 *
	 * @param tsmemmic20Dto
	 * @param result
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("init")
	public String initTSMEMMIC30(@ModelAttribute TSMEMMIC20DTOForm tsmemmic20Dto, BindingResult result, Model model)
			throws ParseException {

		// log出力
		logger.debug("initTSMEMMIC30 complete MemberId:" + tsmemmic20Dto.getMemberId());
		logger.debug("initTSMEMMIC30 complete MemberKbn:" + tsmemmic20Dto.getMemberKbn());

		// 郵便番号（結合）設定
		tsmemmic20Dto.setPostNo(CommonUtil.makePostNo(tsmemmic20Dto.getPostNo1(), tsmemmic20Dto.getPostNo2()));
		// 電話番号（結合）設定
		tsmemmic20Dto.setPhoneNo(CommonUtil.makePhoneNo(tsmemmic20Dto.getPhoneNo1(), tsmemmic20Dto.getPhoneNo2(),
				tsmemmic20Dto.getPhoneNo3()));

		// バリデーションチェック
		validationCheck(tsmemmic20Dto, result, model);

		// バリデーションエラー発生
		if (result.hasErrors()) {
			logger.debug("PostNo:" + tsmemmic20Dto.getPostNo());
			model.addAttribute("tsmemmic20dto", tsmemmic20Dto);
			return "member/member_edit_input";
		}

		// 返却値設定
		model.addAttribute("tsmemmic20dto", tsmemmic20Dto);

		return "member/member_edit_confirm";
	}

	/**
	 * @param redirectAttributes
	 * @param tsmemmic20dto
	 * @param result
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("complete")
	public String complete(RedirectAttributes redirectAttributes, @ModelAttribute TSMEMMIC20DTOForm tsmemmic20dto,
			BindingResult result, HttpSession session, Model model) {

		model.addAttribute("tsmemmic20dto", tsmemmic20dto);

		// log出力
		logger.debug("complete complete MemberId:" + tsmemmic20dto.getMemberId());
		logger.debug("complete complete MemberKbn:" + tsmemmic20dto.getMemberKbn());

		try {
			// DB登録
			tsmemmic30Service.updateMember(tsmemmic20dto, session);

			// 値渡し（memberIdのみ）
			redirectAttributes.addFlashAttribute("memberId", tsmemmic20dto.getMemberId());

			// 画面遷移
			return "redirect:/TSMEMMIC40/init";
		} catch (Exception e) {
			logger.debug("会員情報更新失敗");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_CHANGE }));
			return "member/member_edit_confirm";
		}
	}

	/**
	 * バリデーションチェック
	 *
	 * @param tsmemmic20Dto
	 * @param result
	 * @param model
	 * @throws ParseException
	 */
	public void validationCheck(@ModelAttribute TSMEMMIC20DTOForm tsmemmic20Dto, BindingResult result, Model model)
			throws ParseException {

		// 変数初期化
		String memberClassErrorMessage = null;
		String memberNameErrorMessage = null;
		String birthdayErrorMessage = null;
		String postErrorMessage = null;
		String addressErrorMessage = null;
		String tellErrorMessage = null;
		String emailErrorMessage = null;

		// 会員区分：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getMemberKbn())) {
			memberClassErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_MEMBER_KBN });
		}

		// 名前：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getMemberName())) {
			memberNameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_NAME });
			// 名前：桁数（0～50文字）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getMemberName(), 0,
				CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE)) {
			memberNameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { CommonConstants.STRING_NAME, "50" });
			// 名前：全角
		} else if (!CheckerUtil.dataTypeCheck(tsmemmic20Dto.getMemberName(),
				CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
			memberNameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
					new String[] { CommonConstants.STRING_NAME });
		}

		// 生年月日：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getBirthDate())) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_BIRTHDAY });

			// 生年月日：桁数（10～10桁）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getBirthDate(),
				CommonConstants.MEMBER_TABLE_BIRTHDAY_MAX_SIZE, CommonConstants.MEMBER_TABLE_BIRTHDAY_MAX_SIZE)) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_BIRTHDAY, CommonConstants.MEMBER_TABLE_BIRTHDAY_MAX_SIZE_STRING });
			// 生年月日：yyyy/mm/dd
		} else if (!CheckerUtil.dateFormatCheck(tsmemmic20Dto.getBirthDate())) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_BIRTHDAY, CommonConstants.STRING_BITHDAY_REGEXP });
			// 生年月日：未来日の制限
		} else if (!(CheckerUtil.compareToDate(tsmemmic20Dto.getBirthDate()) == -1)) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM007E,
					new String[] { CommonConstants.STRING_BIRTHDAY });
		}

		// 郵便番号：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getPostNo1() + tsmemmic20Dto.getPostNo2())) {
			postErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_POSTALCODE });

			// 郵便番号：桁数（8～8桁）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getPostNo(),
				CommonConstants.MEMBER_TABLE_POSTAL_MAX_SIZE, CommonConstants.MEMBER_TABLE_POSTAL_MAX_SIZE)) {
			postErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM009E,
					new String[] { CommonConstants.MEMBER_TABLE_POSTALCODE_MAX_SIZE_STRING });

			// 郵便番号：半角数字
		} else if (!CheckerUtil.dataTypeCheck(tsmemmic20Dto.getPostNo1(), CommonConstants.REGEX_HALF_WIDTH_NUMBER)) {
			postErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_POSTALCODE, CommonConstants.STRING_MEMBER_ID_FORMAT });
		} else if (!CheckerUtil.dataTypeCheck(tsmemmic20Dto.getPostNo2(), CommonConstants.REGEX_HALF_WIDTH_NUMBER)) {
			postErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_POSTALCODE, CommonConstants.STRING_MEMBER_ID_FORMAT });
			// 郵便番号：000-0000
		} else if (!CheckerUtil.postalValueCheck(tsmemmic20Dto.getPostNo())) {
			postErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_POSTALCODE, CommonConstants.STRING_POSTALCODE_REGEXP });
		}

		// 住所：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getAddress())) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_ADDRESS });

			// 住所：桁数（0～100文字）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getAddress(), 0,
				CommonConstants.MEMBER_TABLE_ADDRESS_MAX_SIZE)) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_ADDRESS, CommonConstants.MEMBER_TABLE_ADDRESS_MAX_SIZE_STRING });
			// 住所：全角
		} else if (!CheckerUtil.dataTypeCheck(tsmemmic20Dto.getAddress(),
				CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
					new String[] { CommonConstants.STRING_ADDRESS });
		}

		// 電話番号：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getPhoneNo1())
				|| !CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getPhoneNo2())
				|| !CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getPhoneNo3())) {
			tellErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_TELNUMBAER });
			// 電話番号：桁数（13～13桁）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getPhoneNo(), CommonConstants.MEMBER_TABLE_TEL_MAX_SIZE,
				CommonConstants.MEMBER_TABLE_TEL_MAX_SIZE)) {
			tellErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_TELNUMBAER, CommonConstants.MEMBER_TABLE_TELNUMBER_MAX_SIZE_STRING });
			// 電話番号：000-0000-0000
		} else if (!CheckerUtil.telValueCheck(tsmemmic20Dto.getPhoneNo())) {
			tellErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E, // MSGCOM005E
																						// {1}は{2}形式で入力してください。
					new String[] { CommonConstants.STRING_TELNUMBAER, CommonConstants.STRING_TELNUMBER_REGEXP });
		}

		// メールアドレス：必須
		if (!CheckerUtil.dataRequiredCheck(tsmemmic20Dto.getMailAddress())) {
			emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_EMAIL });

			// メールアドレス：桁数（0～50文字）
		} else if (!CheckerUtil.stringLengthCheck(tsmemmic20Dto.getMailAddress(), 0,
				CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE)) {
			emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { CommonConstants.STRING_EMAIL, CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE_STRING });

			// メールアドレス：test@aaaa.bbb
		} else if (!CheckerUtil.emailAddressFormatCheck(tsmemmic20Dto.getMailAddress())) {
			emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_EMAIL, CommonConstants.STRING_EMAIL_FORMAT });

			// メールアドレス存在チェック
		} else if ((tsmemmic20Dto.getMailAddress().equals(tsmemmic20Dto.getMailAddressMoto()) == false)
				&& tscommir30Service.selectEmail(tsmemmic20Dto.getMailAddress()) != 0) {
			emailErrorMessage = MessageConstants.MSGZZ003E;
		}

		// 会員区分
		if (!StringUtils.isEmpty(memberClassErrorMessage)) {
			// LOG出力
			logger.debug("name error");
			model.addAttribute("validationMemberClassError", memberClassErrorMessage);
			ObjectError validationError = new ObjectError("validationClassError", memberClassErrorMessage);
			result.addError(validationError);
		}

		// 名前
		if (!StringUtils.isEmpty(memberNameErrorMessage)) {
			// LOG出力
			logger.debug("class error");
			model.addAttribute("validationMemberNameError", memberNameErrorMessage);
			ObjectError validationError = new ObjectError("validationNameError", memberNameErrorMessage);
			result.addError(validationError);
		}

		// 生年月日
		if (!StringUtils.isEmpty(birthdayErrorMessage)) {
			// LOG出力
			logger.debug("birthday error");
			model.addAttribute("validationBirthdayError", birthdayErrorMessage);
			ObjectError validationError = new ObjectError("validationBirthdayError", birthdayErrorMessage);
			result.addError(validationError);
		}

		// 郵便番号
		if (!StringUtils.isEmpty(postErrorMessage)) {

			// LOG出力
			logger.debug("postalCode error");
			model.addAttribute("validationPostError", postErrorMessage);
			ObjectError validationError = new ObjectError("validationPostalCodeError", postErrorMessage);
			result.addError(validationError);
		}

		// 住所
		if (!StringUtils.isEmpty(addressErrorMessage)) {
			// LOG出力
			logger.debug("address error");
			model.addAttribute("validationAddressError", addressErrorMessage);
			ObjectError validationError = new ObjectError("validationAddressError", addressErrorMessage);
			result.addError(validationError);
		}

		// 電話番号
		if (!StringUtils.isEmpty(tellErrorMessage)) {
			// LOG出力
			logger.debug("telNumber error" + tellErrorMessage);
			model.addAttribute("validationTellNumberError", tellErrorMessage);
			ObjectError validationError = new ObjectError("validationTelNumberError", tellErrorMessage);
			result.addError(validationError);
		}

		// メールアドレス
		if (!StringUtils.isEmpty(emailErrorMessage)) {
			// LOG出力
			logger.debug("email error");
			model.addAttribute("validationEmailError", emailErrorMessage);
			ObjectError validationError = new ObjectError("validationEmailError", emailErrorMessage);
			result.addError(validationError);
		}

	}
}