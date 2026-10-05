package LIP.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ZZ.dao.TBBookLibraryDao;

import LIP.dto.TBLIPFileInDTO;

public class TBLIPFileInService {
	/**
	 * 
	 * @param indto
	 * @return outdto
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public Boolean service(TBLIPFileInDTO indto) throws ClassNotFoundException, SQLException, IOException{
		TBBookLibraryDao mdao = new TBBookLibraryDao();
		
		List<String> isbn_list = new ArrayList<>();
		List<Integer> add_book_cnt = new ArrayList<>();
		
		//Library項目名
		//BOOK_ID
		//ISBN
		String arrival_date = "SYSDATE";
		String disposal_date = null;
		String disposal_note = null;
		int delete_flag = 0;
		int register_id = 9999;
		String register_date = "SYSDATE";
		int update_id = 9999;
		String update_date = "SYSDATE";
		int exclusive_key = 0;
		//　URL取得
		File outputFile;
		// UTF-8設定
		OutputStreamWriter osw;
	
		// ファイル読み取り
		File inputFile = new File(indto.getInputfilename());
		FileReader fr = new FileReader(inputFile);

		if(inputFile.length()==0) {
			System.out.println("MSGLIP001W ファイルの内容が存在しません。確認後再度利用してください。");
			return false;
		} else {
			//　URL取得
			outputFile = new File(indto.getOutputfilename());
			// UTF-8設定
			osw  = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
		}
		
		BufferedReader br = new BufferedReader(fr);
		String lineStr = "";
		
		// LOOP １行単位で繰り返し
		while (lineStr != null) {
			lineStr = br.readLine();
			if (lineStr != null) {
				// 入力レコードの振り分け
				String[] ikkoRecord = lineStr.split(",");
				if(Integer.parseInt(ikkoRecord[1])!=0) {
					isbn_list.add(ikkoRecord[0]);
					add_book_cnt.add(Integer.parseInt(ikkoRecord[1]));	
				}
			}
		}
		
		StringBuffer sb = new StringBuffer();
		
		// Library TABLE 資料件数追加
		if(isbn_list.size()!=0) {
			sb.append("INSERT INTO LIBRARY\r\nSELECT BOOK_ID_SEQ.NEXTVAL, LIB.*\r\nFROM (\r\n");
			// union all SQL処理
			for(int i = 0 ; i < isbn_list.size() ; i++ ) {
				for(int j = 0 ; j < add_book_cnt.get(i) ; j ++) {			
					if( i == isbn_list.size()-1 && j == add_book_cnt.get(i)-1) {
						sb.append("SELECT ");
						sb.append(isbn_list.get(i));
						sb.append(" AS ISBN,");
						sb.append(arrival_date);
						sb.append(" AS ARRIVAL_DATE,");
						sb.append(disposal_date);
						sb.append(" AS DIDSPOSAL_DATE,");
						sb.append(disposal_note);
						sb.append(" AS DISPOSAL_NOTE,");
						sb.append(delete_flag);
						sb.append(" AS DELETE_FLAG,");
						sb.append(register_id);
						sb.append(" AS REGISTER_ID,");
						sb.append(register_date);
						sb.append(" AS REGISTER_DATE,");
						sb.append(update_id);
						sb.append(" AS UPDATE_ID,");
						sb.append(update_date);
						sb.append(" AS UPDATE_DATE,");
						sb.append(exclusive_key);
						sb.append(" AS EXCLUSIVE_KEY FROM DUAL) LIB");			
					} else {
						sb.append("SELECT ");
						sb.append(isbn_list.get(i));
						sb.append(" AS ISBN,");
						sb.append(arrival_date);
						sb.append(" AS ARRIVAL_DATE,");
						sb.append(disposal_date);
						sb.append(" AS DIDSPOSAL_DATE,");
						sb.append(disposal_note);
						sb.append(" AS DISPOSAL_NOTE,");
						sb.append(delete_flag);
						sb.append(" AS DELETE_FLAG,");
						sb.append(register_id);
						sb.append(" AS REGISTER_ID,");
						sb.append(register_date);
						sb.append(" AS REGISTER_DATE,");
						sb.append(update_id);
						sb.append(" AS UPDATE_ID,");
						sb.append(update_date);
						sb.append(" AS UPDATE_DATE,");
						sb.append(exclusive_key);
						sb.append(" AS EXCLUSIVE_KEY FROM DUAL UNION ALL \r\n");		
					}
				}
			}
			
			String sql = sb.toString();
			// 入力情報をDBに登録する。
			mdao.libraryInsert(sql);
			
		} else {
			System.out.println("MSGLIP002W 登録数がすべて0件です。蔵書を再出力してください。");
			return false;
		}
		
		ResultSet record = mdao.selectBookIdList();	
		String resultStr = "BOOK_ID,ISBN,ARRIVAL_DATE\r\n";
		
		// LOOP Library DTO設定
		while(record.next()) {
			int bookId = record.getInt("BOOK_ID");
			String isbn = record.getString("ISBN");
			String arrivalDate = record.getString("ARRIVAL_DATE");
			
			indto.setBookId(bookId);
			indto.setIsbn(isbn);
			indto.setArrivalDate(arrivalDate);
	
			resultStr = resultStr + indto;
		}
		
		// ファイル作成
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(resultStr);
		
		// Writer終了
		bw.flush();
		bw.close();
		
		//　Reader終了
		br.close();
		fr.close();

		return true;
	}
}
