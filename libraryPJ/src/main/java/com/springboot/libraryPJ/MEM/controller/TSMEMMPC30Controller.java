package com.springboot.libraryPJ.MEM.controller;

import java.util.List;

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

import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20Dto;
import com.springboot.libraryPJ.MEM.service.TSMEMMPC30Service;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/*
 * 会員パスワード更新確認
 */

@Controller
@RequestMapping("/TSMEMMPC30/")
public class TSMEMMPC30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMPC30Service tsmemmpc30Service;

	/**
	 * 確認ボタン押下時
	 * 
	 * @param tsmemmpc20Dto
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("init")
	public String initTSMEMMPC30(@ModelAttribute TSMEMMPC20Dto tsmemmpc20Dto, BindingResult result, Model model) {

		model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);

		// log出力
		logger.debug("MemberId:" + tsmemmpc20Dto.getMemberId());

		// バリデーションチェック
		try {
			validationCheck(tsmemmpc20Dto, result, model);

		} catch (Exception e) {
			model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);
			return "member/member_password_input";
		}

		// バリデーションエラー発生
		if (result.hasErrors()) {
			model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);
			return "member/member_password_input";
		}

		// 返却値設定
		model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);
		return "member/member_password_confirm";
	}

	/**
	 * 変更ボタン押下時
	 * 
	 * @param redirectAttributes
	 * @param tsmemmpc20Dto
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("confirm")
	public String confirmTSMEMMPC30(RedirectAttributes redirectAttributes, @ModelAttribute TSMEMMPC20Dto tsmemmpc20Dto,
			BindingResult result, HttpSession session, Model model) {

		model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);

		try {
			// log出力
			logger.debug("MemberId:" + tsmemmpc20Dto.getMemberId());
			logger.debug("change password:" + tsmemmpc20Dto.getMemberPasswordNew());

			// DB登録
			logger.debug("DB更新");
			tsmemmpc30Service.updatePassword(tsmemmpc20Dto, session);

			// 値渡し（memberId、新しいpassword）
			logger.debug("値渡し（memberId、New password）");
			redirectAttributes.addFlashAttribute("memberId", tsmemmpc20Dto.getMemberId());
			redirectAttributes.addFlashAttribute("password", tsmemmpc20Dto.getMemberPasswordNew());
			redirectAttributes.addFlashAttribute("tsmemmpc20Dto", tsmemmpc20Dto);

		} catch (Exception e) {
			logger.debug("会員情報更新失敗");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_CHANGE }));
			return "member/member_password_confirm";
		}

		// 画面遷移
		logger.debug("TSMEMMPC40開始");
		return "redirect:/TSMEMMPC40/init";
	}

	public void validationCheck(@ModelAttribute TSMEMMPC20Dto tsmemmpc20Dto, BindingResult result, Model model) {

		// 変数初期化
		String oldPasswordError = null;
		String newPassword1Error = null;
		String newPassword2Error = null;

		// 現在パスワード：必須、半角文字、32文字以内
		if (!CheckerUtil.dataRequiredCheck(tsmemmpc20Dto.getMemberPassword())) {
			oldPasswordError = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_NOW_PASSWORD });
		} else if (!CheckerUtil.dataTypeCheck(tsmemmpc20Dto.getMemberPassword(),
				CommonConstants.REGEX_HALF_WIDTH_ENG_NUMBER)) {
			oldPasswordError = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
					new String[] { CommonConstants.STRING_PASSWORD, CommonConstants.STRING_PASSWORD_FORMAT });
		} else if (!CheckerUtil.stringLengthCheck(tsmemmpc20Dto.getMemberPassword(),
				CommonConstants.MEMBER_TABLE_PW_MIN_SIZE, CommonConstants.MEMBER_TABLE_PW_MAX_SIZE)) {
			oldPasswordError = MessageConstants.getMessage(MessageConstants.MSGCOM019E,
					new String[] { CommonConstants.STRING_PASSWORD,
							CommonConstants.MEMBER_TABLE_PASSWORD_MIN_SIZE_STRING,
							CommonConstants.MEMBER_TABLE_PASSWORD_MAX_SIZE_STRING });
		}
		// 現在のパスワードの一致
		else if (!passwordCheck(tsmemmpc20Dto)) {
			oldPasswordError = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.RIGHT_PASSWORD });
		}

		// 新しいパスワード：必須、半角文字、32文字以内、
		if (!CheckerUtil.dataRequiredCheck(tsmemmpc20Dto.getMemberPasswordNew())) {
			newPassword1Error = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_NEW_PASSWORD1 });
		} else if (!CheckerUtil.dataTypeCheck(tsmemmpc20Dto.getMemberPasswordNew(),
				CommonConstants.REGEX_HALF_WIDTH_ENG_NUMBER)) {
			newPassword1Error = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
					new String[] { CommonConstants.STRING_PASSWORD, CommonConstants.STRING_PASSWORD_FORMAT });
		} else if (!CheckerUtil.stringLengthCheck(tsmemmpc20Dto.getMemberPasswordNew(),
				CommonConstants.MEMBER_TABLE_PW_MIN_SIZE, CommonConstants.MEMBER_TABLE_PW_MAX_SIZE)) {
			newPassword1Error = MessageConstants.getMessage(MessageConstants.MSGCOM019E,
					new String[] { CommonConstants.STRING_PASSWORD,
							CommonConstants.MEMBER_TABLE_PASSWORD_MIN_SIZE_STRING,
							CommonConstants.MEMBER_TABLE_PASSWORD_MAX_SIZE_STRING });
		}
		// 現在のパスワードと新しいパスワードが同じではない事
		else if (tsmemmpc20Dto.getMemberPasswordNew().equals(tsmemmpc20Dto.getMemberPassword())) {
			newPassword1Error = MessageConstants.getMessage(MessageConstants.MSGMEM001E, new String[] {});
		}
		// 新しいパスワードと確認用パスワードの一致
		else if (CheckerUtil.dataRequiredCheck(tsmemmpc20Dto.getMemberPasswordNew2())
				&& !(tsmemmpc20Dto.getMemberPasswordNew().equals(tsmemmpc20Dto.getMemberPasswordNew2()))) {
			newPassword1Error = MessageConstants.getMessage(MessageConstants.MSGCOM018E,
					new String[] { CommonConstants.STRING_NEW_PASSWORD1, CommonConstants.STRING_NEW_PASSWORD2 });
		}

		// 新しいパスワード（確認）必須、半角文字、32文字以内
		if (!CheckerUtil.dataRequiredCheck(tsmemmpc20Dto.getMemberPasswordNew2())) {
			newPassword2Error = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_NEW_PASSWORD2 });
		} else if (!CheckerUtil.dataTypeCheck(tsmemmpc20Dto.getMemberPasswordNew2(),
				CommonConstants.REGEX_HALF_WIDTH_ENG_NUMBER)) {
			newPassword2Error = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
					new String[] { CommonConstants.STRING_PASSWORD, CommonConstants.STRING_PASSWORD_FORMAT });
		} else if (!CheckerUtil.stringLengthCheck(tsmemmpc20Dto.getMemberPasswordNew2(),
				CommonConstants.MEMBER_TABLE_PW_MIN_SIZE, CommonConstants.MEMBER_TABLE_PW_MAX_SIZE)) {
			newPassword2Error = MessageConstants.getMessage(MessageConstants.MSGCOM019E,
					new String[] { CommonConstants.STRING_PASSWORD,
							CommonConstants.MEMBER_TABLE_PASSWORD_MIN_SIZE_STRING,
							CommonConstants.MEMBER_TABLE_PASSWORD_MAX_SIZE_STRING });
		}
		// 現在のパスワードと新しいパスワード(確認用)が同じではない事
		else if (tsmemmpc20Dto.getMemberPasswordNew2().equals(tsmemmpc20Dto.getMemberPassword())) {
			newPassword2Error = MessageConstants.getMessage(MessageConstants.MSGMEM001E, new String[] {});
		}
		// 新しいパスワードと新しいパスワード(確認用)の一致
		else if (CheckerUtil.dataRequiredCheck(tsmemmpc20Dto.getMemberPasswordNew())
				&& !(tsmemmpc20Dto.getMemberPasswordNew2().equals(tsmemmpc20Dto.getMemberPasswordNew()))) {
			newPassword2Error = MessageConstants.getMessage(MessageConstants.MSGCOM018E,
					new String[] { CommonConstants.STRING_NEW_PASSWORD1, CommonConstants.STRING_NEW_PASSWORD2 });
		}

		// 現在パスワード
		if (!StringUtils.isEmpty(oldPasswordError)) {
			// LOG出力
			logger.debug("member password error");
			model.addAttribute("validationPasswordError", oldPasswordError);
			ObjectError validationError = new ObjectError("validationPasswordError", oldPasswordError);
			result.addError(validationError);
		}

		// 新しいパスワード
		if (!StringUtils.isEmpty(newPassword1Error)) {
			// LOG出力
			logger.debug("member password new error");
			model.addAttribute("validationNewPasswordError", newPassword1Error);
			ObjectError validationError = new ObjectError("validationNewPasswordError", newPassword1Error);
			result.addError(validationError);
		}

		// 新しいパスワード（確認）
		if (!StringUtils.isEmpty(newPassword2Error)) {
			// LOG出力
			logger.debug("member password new2 error");
			model.addAttribute("validationNewPassword2Error", newPassword2Error);
			ObjectError validationError = new ObjectError("validationNewPassword2Error", newPassword2Error);
			result.addError(validationError);
		}

	}

	// 現在のパスワードの一致
	public boolean passwordCheck(TSMEMMPC20Dto tsmemmpc20Dto) {
		// 会員情報確認
		List<MemberEntity> member = tsmemmpc30Service.searchLoginMember(tsmemmpc20Dto);

		// ログインユーザーチェック
		if (member.size() == 0) {
			return false;
		}
		return true;
	}
}
