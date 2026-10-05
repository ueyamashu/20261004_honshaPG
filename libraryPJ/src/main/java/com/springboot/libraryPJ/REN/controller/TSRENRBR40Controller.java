package com.springboot.libraryPJ.REN.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.REN.dto.TSRENRBRoutDto;
import com.springboot.libraryPJ.REN.service.TSRENRBR40Service;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.dto.TSRENRBTDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料貸出完了
 */
@Controller
@RequestMapping("/TSRENRBR40/")
public class TSRENRBR40Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRENRBR40Service tsrenrbr40Service;

	/**
	 * 資料貸出完了表示
	 *
	 * @param rentalIdList
	 * @param memberId
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRENRBR40(@ModelAttribute("rentalIdList") List<Integer> rentalIdList, String memberId,
			HttpSession session, BindingResult result, Model model) {

		// 入力内容のmodel格納
		TSRENRBTDto tsrenrbtDto = new TSRENRBTDto();
		tsrenrbtDto.setMemberId(memberId);
		model.addAttribute("tsrenrbtDto", tsrenrbtDto);

		// ログ出力
		logger.debug("initTSRENRBR40 rentalIdList: " + rentalIdList);
		logger.debug("initTSRENRBR40 memberId: " + memberId);

		try {
			// 会員の名前を取得
			List<MemberEntity> memberList = tsrenrbr40Service.getMemberInfo(memberId);

			// 取得結果がエラーの場合はエラーメッセージを出力する。
			if (memberList.size() == 0) {

				logger.debug("rental info get failed");
				// エラーメッセージ設定
				model.addAttribute("paramError", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
						new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
				// 貸出資料一覧画面に遷移する
				return "book/rental_complete";
			}
			// 取得した名前をmodelに設定
			model.addAttribute("userName", memberList.get(0).getName());

			// 貸出ID件数分、ループ処理を行い、貸出情報を取得する
			List<TSRENRBRoutDto> outDtoList = tsrenrbr40Service.getReturnBook(rentalIdList);

			// サービスからの取得結果をmodelに格納
			model.addAttribute("rentalList", outDtoList);

			// 資料貸出完了画面を表示する
			return "book/rental_complete";
		} catch (Exception e) {
			logger.debug("資料貸出情報取得に失敗しました。");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_BOOK_RENTAL_JOHO_SELECT }));
			return "book/rental_complete";
		}
	}
}
