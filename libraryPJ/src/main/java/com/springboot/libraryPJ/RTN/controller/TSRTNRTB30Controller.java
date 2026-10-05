package com.springboot.libraryPJ.RTN.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTB30outDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTBinDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTBoutDto;
import com.springboot.libraryPJ.RTN.service.TSRTNRTB10Service;
import com.springboot.libraryPJ.RTN.service.TSRTNRTB30Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/*
 * 資料返却確認
 */
@Controller
@RequestMapping("/TSRTNRTB30/")
public class TSRTNRTB30Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRTNRTB30Service tsrtnrtb30Service;

	@Autowired
	private TSRTNRTB10Service tsrtnrtb10Service;

	/**
	 * 資料返却確認画面の初期表示
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param inDto
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRTNRTB30(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRTNRTBinDto inDto, HttpSession session, Model model) {
		model.addAttribute("inDto", inDto);
		// ログ出力
		logger.debug("TSRTNRTB30 init rentalId:" + inDto);

		// 資料IDの存在をチェックする。
		if (inDto.getRentalIdList() == null || inDto.getRentalIdList().isEmpty() || inDto.getRentalIdList().size() == 0) {
			logger.debug("param rentalIds empty");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM002E,
					new String[] { CommonConstants.STRING_RETURN_BOOK }));

			// サービス呼び出し
			List<TSRTNRTBoutDto> outDto = tsrtnrtb10Service.getReturnBookList(CommonConstants.NOT_DELETE);

			// サービスからの取得結果をmodelに格納
			List<Object> pageList = new ArrayList<>();
			for (Object item : outDto) {
				pageList.add(item);
			}
			CommonUtil.pageModule(page, size, model, pageList, "outDto", "/TSRTNRTB10/init?");

			// 資料返却一覧に戻る
			return "book/return_list";
		}

		// サービス呼び出し
		List<TSRTNRTB30outDto> outDto = tsrtnrtb30Service.getRtnBook(inDto);

		// 貸出期限を超過しているか確認
		String overDueDayFlg = tsrtnrtb30Service.overDueCheck(outDto);

		// 上記の結果をmodelに設定する
		// 結果により'1'又は、'0'を設定する
		model.addAttribute("overDueDayFlg", overDueDayFlg);

		// 返却資料情報をmodelに格納
		model.addAttribute("outDto", outDto);

		// 画面遷移
		return "book/return_confirm";
	}

	/**
	 * 資料返却処理を行う
	 *
	 * @param redirectAttributes
	 * @param inDto
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("complete")
	public String complete(RedirectAttributes redirectAttributes, @ModelAttribute TSRTNRTBinDto inDto,
			HttpSession session, Model model) {
		model.addAttribute("inDto", inDto);

		// ログ出力
		logger.debug("TSRTNRTB30 complete rentalId:" + inDto);

		// outDtoから貸出IDリストを取得する
		List<String> ids = inDto.getRentalIdList();
		for (int i = 0; i < ids.size(); i++)
			// サービス呼び出し
			tsrtnrtb30Service.updateDeleteFlagByRentalId(ids.get(i), session);

		// 値渡し
		redirectAttributes.addAttribute("rentalIdList", ids);

		// 画面遷移
		return "redirect:/TSRTNRTB40/init";
	}

}
