package com.springboot.libraryPJ.BKS.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.libraryPJ.BKS.dto.TSBKSBOToutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.DelayEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.DelayRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 *
 */
@Service
public class TSBKSBOT10Service {

	@Autowired
	DelayRepository delayRepository;

	@Autowired
	RentalRepository rentalRepository;

	/**
	 * @param remidFlag
	 * @return
	 */
	public TSBKSBOToutDto getDelayBookList(String remidFlag) {
		List<DelayEntity> delayList = delayRepository.findByDelay();

		TSBKSBOToutDto outDto = new TSBKSBOToutDto();
		outDto.setDelayList(delayList);

		// 延滞連絡リセットボタンを非活性にする。
		String delayClearFlg = "0";
		for (DelayEntity entity : delayList) {

			// 未連絡の延滞がある場合
			if (entity.getRemidFlag() == true) {
				// 延滞連絡リセットボタンを活性にする。
				delayClearFlg = "1";
				break;
			}
		}

		outDto.setDelayClearFlg(delayClearFlg);

		return outDto;

	}

	// 連絡済みを表すメソッド
	public void notiFied(long rentalId, HttpSession session) {
		RentalEntity entity = rentalRepository.findByRentalIdAndDeleteFlag((int) rentalId, CommonConstants.NOT_DELETE);

		entity.setRemindFlag(CommonConstants.REMIND);

		long miliseconds = System.currentTimeMillis();
		Date date = new Date(miliseconds);

		entity.setUpdateDate(date);
		entity.setUpdateId(9999);

		entity = (RentalEntity) CommonUtil.setWhoInfoUpdate(entity, session);

		rentalRepository.save(entity);
	}


	public void contactClear(HttpSession session) {
		List<RentalEntity> entityList = rentalRepository.findByRemindFlag(CommonConstants.REMIND);

		for (RentalEntity entity : entityList) {

			entity.setRemindFlag(CommonConstants.NOT_REMIND);

			long miliseconds = System.currentTimeMillis();
			Date date = new Date(miliseconds);

			entity.setUpdateDate(date);
			entity.setUpdateId(9999);

			entity = (RentalEntity) CommonUtil.setWhoInfoUpdate(entity, session);

			rentalRepository.save(entity);
		}

	}
}
