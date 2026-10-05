package com.springboot.libraryPJ.RTN.service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTB30outDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTBinDto;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.DelayRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalMemberRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TSRTNRTB30Service {

	@Autowired
	RentalMemberRepository rmRep;
	@Autowired
	RentalRepository rRep;
	@Autowired
	DelayRepository dRep;

	/**
	 * 資料返却確認情報を取得する
	 *
	 * @param inDto
	 * @return
	 */
	public List<TSRTNRTB30outDto> getRtnBook(TSRTNRTBinDto inDto) {

		// outDtoから貸出IDリストを取得する
		List<String> ids = inDto.getRentalIdList(); // getterで取得

		List<RentalMemberEntity> returnBook = rmRep.getReturnBook(ids);

		// 取得した情報をDTOに格納する
		List<TSRTNRTB30outDto> outDto = returnBook.stream().map(o -> new TSRTNRTB30outDto(o))
				.collect(Collectors.toList());
		outDto.sort(Comparator.comparing(TSRTNRTB30outDto::getRentalDueDate).reversed());

		return outDto;
	}

	/**
	 * 貸出テーブルの削除フラグを削除済みに更新する
	 *
	 * @param id
	 * @param session
	 */
	public void updateDeleteFlagByRentalId(String id, HttpSession session) {
		RentalEntity entity = rRep.findByRentalIdAndDeleteFlag(Integer.parseInt(id), CommonConstants.NOT_DELETE);

		// 返却日に現在日付を設定する
		try {
			long millis = System.currentTimeMillis();
			java.sql.Date sqlDate = new java.sql.Date(millis);
			entity.setReturnDate(sqlDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setDeleteFlag(CommonConstants.DELETE);

		entity = (RentalEntity) CommonUtil.setWhoInfoUpdate(entity, session);

		// 貸出テーブルを更新する
		rRep.save(entity);
	}

	/**
	 * 延滞確認用メソッド追加、処理後延滞中の資料情報を返却する
	 *
	 * @param outDto
	 * @return
	 */
	public String overDueCheck(List<TSRTNRTB30outDto> outDto) {
		// デフォルトで "0" を設定する
		String overDueFlag = CommonConstants.RENTAL_DUE_DATE_NOT_EXCEED;

		// 今日の日付取得
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.MILLISECOND);
		Date today = cal.getTime();

		// 今日と貸出期限を比較する処理
		for (int i = 0; i < outDto.size(); i++) {
			Date rentalDueDate = outDto.get(i).getRentalDueDate();
			// 超過していた場合は "1" を設定する
			if (today.compareTo(rentalDueDate) > 0) {
				overDueFlag = CommonConstants.RENTAL_DUE_DATE_EXCEED;
			}
		}
		return overDueFlag;
	}

}
