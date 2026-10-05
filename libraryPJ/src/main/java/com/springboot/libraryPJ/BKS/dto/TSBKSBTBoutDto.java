package com.springboot.libraryPJ.BKS.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料一覧画面outDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBTBoutDto {

	private int resultCd;

	private String errmsg;

	private List<IchiranData> bookList = new ArrayList<IchiranData>();

	public void setIchiranData(BookLibraryEntity inputVal) {
    	IchiranData info = new IchiranData();
    	
    	info.setBookId(inputVal.getBookId());
    	info.setIsbn(inputVal.getIsbn());
    	info.setTitle(inputVal.getTitle());
    	info.setCategory(inputVal.getCategory());
    	info.setAuthor(inputVal.getAuthor());
    	info.setPublisher(inputVal.getPublisher());
    	info.setReleaseDate(inputVal.getReleaseDate());
    	info.setArrivalDate(inputVal.getArrivalDate());
    	info.setDisposalDate(inputVal.getDisposalDate());
    	
    	this.bookList.add(info);
    	
    	return;
    }    
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class IchiranData {
	private String isbn;

	private String category;

	private String title;

	private String author;

	private String publisher;

	private Date releaseDate;

	private int bookId;

	private Date arrivalDate;

	private Date disposalDate;
}
