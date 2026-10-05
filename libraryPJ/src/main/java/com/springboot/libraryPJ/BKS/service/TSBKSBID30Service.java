package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIDinDto;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 * 資料情報廃棄確認
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBID30Service {

	@Autowired
	LibraryRepository libraryRepository;

	/**
	 * 資料情報を廃棄する
	 *
	 * @param inDto
	 */
	public void disposalLibraryRecord(TSBKSBIDinDto inDto, HttpSession session) {
		// キー情報で、ENTITYファイルを抽出する。
		LibraryEntity entity = libraryRepository.findByBookIdAndDeleteFlag(Integer.parseInt(inDto.getBookId()),
				CommonConstants.NOT_DELETE);

		// 更新するデータをENTITYに設定する
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = sdf.parse(inDto.getDisposalDate());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			entity.setDisposalDate(sqlDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setDisposalNote(inDto.getDisposalNote());
		entity.setDeleteFlag(CommonConstants.DELETE);

		entity = (LibraryEntity) CommonUtil.setWhoInfoUpdate(entity, session);

		// 更新を実施する
		libraryRepository.save(entity);
	}
}
