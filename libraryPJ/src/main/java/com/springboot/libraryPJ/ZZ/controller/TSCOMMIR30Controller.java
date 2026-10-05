package com.springboot.libraryPJ.ZZ.controller;

import java.text.ParseException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
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

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.dto.TSCOMMIRDto;
import com.springboot.libraryPJ.ZZ.service.TSCOMMIR30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 会員登録確認画面
 */
@Controller
@RequestMapping("/TSCOMMIR30/")
public class TSCOMMIR30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSCOMMIR30Service tscommir30Service;

	/**
	 * 初期表示
	 *
	 * @param tscommirDto
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("init")
	public String displayMemberRegisterConfirm(@ModelAttribute TSCOMMIRDto tscommirDto, BindingResult result,
			Model model, HttpSession session) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		String memberId = (String) session.getAttribute("memberId");

		// LOG出力
		logger.debug("memberId: " + memberId);
		tscommirDto.setMemberId(memberId);
		tscommirDto.setPostalCode(tscommirDto.getFullPostalCode());
		tscommirDto.setTelNumber(tscommirDto.getFullTelNumber());

		// 入力チェック実施
		validationCheck(tscommirDto, result, model);

		// バリデーションエラー発生
		if (result.hasErrors()) {
			model.addAttribute("tscommirDto", tscommirDto);
			return "common/member_register_input";
		}

		logger.trace("tscommirDto.getMemberClass : " + tscommirDto.getMemberClass());
		model.addAttribute("tscommirDto", tscommirDto);
		return "common/member_register_confirm";
	}

	/**
	 * 登録ボタン押下
	 *
	 * @param tscommirDto
	 * @param model
	 * @return
	 */
	@PostMapping("register/complete")
	public String memberRegister(RedirectAttributes redirectAttributes, @ModelAttribute TSCOMMIRDto tscommirDto,
			Model model, HttpSession session) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("tscommirDto", tscommirDto);

		// 会員ID取得
		String memberId = (String) session.getAttribute("memberId");
		logger.debug("memberId: " + memberId);

		// ランダムのパスワード生成
		String passwordRandom = createRandomPassword();
		logger.debug("ランダムのパスワード: " + passwordRandom);

		// SHA256対応
		String pwSHA256 = DigestUtils.sha256Hex(passwordRandom);
		logger.debug("暗号化パスワード: " + pwSHA256);

		tscommirDto.setPassword(passwordRandom);
		tscommirDto.setEncryptPassword(pwSHA256);

		try {
			// 会員登録実施
			MemberEntity memberEntity = tscommir30Service.createMember(tscommirDto, memberId, session);

			// 会員登録結果設定
			redirectAttributes.addFlashAttribute("memberId", memberEntity.getMemberId());
			// 値渡し（password）
			redirectAttributes.addFlashAttribute("password", tscommirDto.getPassword());
			// 値渡し
			redirectAttributes.addFlashAttribute("tscommirDto", tscommirDto);
			return "redirect:/TSCOMMIR40/init";

		} catch (Exception e) {
			// LOG出力
			logger.debug("会員登録エラー");
			model.addAttribute("error", MessageConstants.MSGZZ002E);
			return "common/member_register_confirm";
		}
	}

	/**
	 * 入力パラメータの単項目チェック
	 *
	 * @param tscommirDto
	 * @param result
	 * @param model
	 */
	public void validationCheck(@ModelAttribute TSCOMMIRDto tscommirDto, BindingResult result, Model model) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// チェック用変数初期化
		String nameErrorMessage = null;
		String birthdayErrorMessage = null;
		String postalCodeErrorMessage = null;
		String addressErrorMessage = null;
		String telNumberErrorMessage = null;
		String emailErrorMessage = null;

		// 入力内容に誤りがある場合
		// 名前
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getName())) {
			nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_NAME });
		} else if (!CheckerUtil.dataTypeCheck(tscommirDto.getName(), CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
			nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
					new String[] { CommonConstants.STRING_NAME });
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getName(), 0,
				CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE)) {
			nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { CommonConstants.STRING_NAME, CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE_STRING });
		}

		// 未来日入力不可追加
		// 生年月日
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getBirthday())) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_BIRTHDAY });
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getBirthday(), 0,
				CommonConstants.MEMBER_TABLE_BIRTHDAY_MAX_SIZE)) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_BIRTHDAY, CommonConstants.MEMBER_TABLE_BIRTHDAY_MAX_SIZE_STRING });
		} else if (!CheckerUtil.dateFormatCheck(tscommirDto.getBirthday())) {
			birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM008E,
					new String[] { CommonConstants.STRING_BIRTHDAY });
		} else
			try {
				if (CheckerUtil.compareToDate(tscommirDto.getBirthday()) == CommonConstants.CHECK_RESULT_AFTER_DATE) {
					birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM007E,
							new String[] { CommonConstants.STRING_BIRTHDAY });
				}
			} catch (ParseException e) {
				birthdayErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM007E,
						new String[] { CommonConstants.STRING_BIRTHDAY });

				// LOG出力
				logger.debug("birthday error");
				model.addAttribute("validationBirthdayError", birthdayErrorMessage);
				result.addError(new ObjectError("error", null));
			}

		// 郵便番号
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getPostalCode1())
				|| !CheckerUtil.dataRequiredCheck(tscommirDto.getPostalCode2())) {
			postalCodeErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_POSTALCODE });
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getPostalCode(),
				CommonConstants.MEMBER_TABLE_POSTAL_MAX_SIZE, CommonConstants.MEMBER_TABLE_POSTAL_MAX_SIZE)) {
			postalCodeErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM009E,
					new String[] { CommonConstants.MEMBER_TABLE_POSTALCODE_MAX_SIZE_STRING });
		} else if (!CheckerUtil.postalValueCheck(tscommirDto.getPostalCode())) {
			postalCodeErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_POSTALCODE, CommonConstants.STRING_POSTALCODE_REGEXP });
		}

		// 住所
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getAddress())) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_ADDRESS });
		} else if (!CheckerUtil.dataTypeCheck(tscommirDto.getAddress(),
				CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
					new String[] { CommonConstants.STRING_ADDRESS });
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getAddress(), 0,
				CommonConstants.MEMBER_TABLE_ADDRESS_MAX_SIZE)) {
			addressErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_ADDRESS, CommonConstants.MEMBER_TABLE_ADDRESS_MAX_SIZE_STRING });
		}

		// 電話番号
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getTelNumber1())
				|| !CheckerUtil.dataRequiredCheck(tscommirDto.getTelNumber2())
				|| !CheckerUtil.dataRequiredCheck(tscommirDto.getTelNumber3())) {
			telNumberErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_TELNUMBAER });
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getTelNumber(), 0,
				CommonConstants.MEMBER_TABLE_TEL_MAX_SIZE)) {
			telNumberErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_TELNUMBAER, CommonConstants.MEMBER_TABLE_TELNUMBER_MAX_SIZE_STRING });
		} else if (!CheckerUtil.telValueCheck(tscommirDto.getTelNumber())) {
			telNumberErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM014E,
					new String[] { CommonConstants.STRING_TELNUMBAER });
		}

		// メールアドレス
		if (!CheckerUtil.dataRequiredCheck(tscommirDto.getEmail())) {
			emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_EMAIL });
		} else if (!CheckerUtil.emailAddressFormatCheck(tscommirDto.getEmail())) {
			emailErrorMessage = MessageConstants.MSGCOM010E;
		} else if (!CheckerUtil.stringLengthCheck(tscommirDto.getEmail(), 0,
				CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE)) {
			emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { CommonConstants.STRING_EMAIL, CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE_STRING });
		}

		// メールアドレス存在チェック
		else if (tscommir30Service.selectEmail(tscommirDto.getEmail()) != 0) {
			emailErrorMessage = MessageConstants.MSGZZ003E;
		}

		// 名前
		if (!StringUtils.isEmpty(nameErrorMessage)) {
			// LOG出力
			logger.debug("name error");
			model.addAttribute("validationNameError", nameErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// 生年月日
		if (!StringUtils.isEmpty(birthdayErrorMessage)) {
			// LOG出力
			logger.debug("birthday error");
			model.addAttribute("validationBirthdayError", birthdayErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// 郵便番号
		if (!StringUtils.isEmpty(postalCodeErrorMessage)) {
			// LOG出力
			logger.debug("postalCode error");
			model.addAttribute("validationPostalCodeError", postalCodeErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// 住所
		if (!StringUtils.isEmpty(addressErrorMessage)) {
			// LOG出力
			logger.debug("address error");
			model.addAttribute("validationAddressError", addressErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// 電話番号
		if (!StringUtils.isEmpty(telNumberErrorMessage)) {
			// LOG出力
			logger.debug("telNumber error");
			model.addAttribute("validationTelNumberError", telNumberErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// メールアドレス
		if (!StringUtils.isEmpty(emailErrorMessage)) {
			// LOG出力
			logger.debug("email error");
			model.addAttribute("validationEmailError", emailErrorMessage);
			result.addError(new ObjectError("error", null));
		}
	}

	/**
	 * ランダムのパスワード生成
	 *
	 * @return
	 */
	public String createRandomPassword() {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// パスワード生成
		String passwordRandom = null;
		String alphabet = CommonConstants.CREATE_PASSWORD;
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CommonConstants.RANDOM_PASSWORD_SIZE; i++) {
			int index = random.nextInt(alphabet.length());
			char randomChar = alphabet.charAt(index);
			sb.append(randomChar);
		}
		passwordRandom = sb.toString();
		return passwordRandom;
	}

}
