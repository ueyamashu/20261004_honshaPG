package BIE.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import ZZ.dao.TBBookLibraryDao;
import BIE.dto.TBBIEFileOutDTO;

public class TBBIEFileOutService {
	/**
	 * 
	 * @param indto
	 * @return　bookinfo
	 * @throws SQLException 
	 * @throws Exception 
	 * @throws IOException 
	 */
	public TBBIEFileOutDTO service(TBBIEFileOutDTO indto) throws ClassNotFoundException, SQLException, IOException {
		TBBookLibraryDao mdao = new TBBookLibraryDao();
		TBBIEFileOutDTO bookinfo = new TBBIEFileOutDTO();
		
		String resultStr = "";
		Integer count = 1;
		int book_cnt = 0;
		
		// BOOK TABLE 全件取得
		ResultSet record = mdao.selectAllBooks();		
		
		// BOOK TABLE 本情報が0件
		if(!record.isBeforeFirst()) {
			System.out.println("MSGBIE001W 本情報が0件です。本を登録後利用してください。");
			return bookinfo;
		}
		
		// LOOP DTO設定
		while (record.next()) {
			String isbn = record.getString("ISBN");
			String title = record.getString("TITLE");
			String author = record.getString("AUTHOR");
			String publisher = record.getString("PUBLISHER");
			String releaseDate = record.getString("RELEASE_DATE");
			
			// Library 重複件数抽出
			ResultSet RecordCnt = mdao.libraryCount(isbn);

			if (RecordCnt.next()) {
				book_cnt = RecordCnt.getInt("BOOK_CNT");
			}
			
			bookinfo.setIsbn(isbn);
			bookinfo.setTitle(title);
			bookinfo.setAuthor(author);
			bookinfo.setPublisher(publisher);
			bookinfo.setReleaseDate(releaseDate);
			bookinfo.setBook_cnt(book_cnt);

			resultStr = resultStr + count.toString() + "," + bookinfo;
			count++;
		}
		
		//　URL取得
		File file = new File(indto.getFilename());
		// UTF-8設定
		OutputStreamWriter osw  = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		// ファイル作成
		BufferedWriter bw=new BufferedWriter(osw);
		bw.write(resultStr);
		
		bw.flush();
		bw.close();
		
		return bookinfo;
	}
}
