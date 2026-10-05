package BIE.controller;

import java.io.IOException;
import java.sql.SQLException;

import BIE.dto.TBBIEFileOutDTO;
import BIE.service.TBBIEFileOutService;

public class TBBIEFileOutController {
	public static void main(String[] args) {
		TBBIEFileOutDTO outdto = new TBBIEFileOutDTO();
		TBBIEFileOutService service = new TBBIEFileOutService();


		// 開始文言を出力する。
		System.out.println("MSGZZ001I 処理を開始します。");
		// OutDTO設定
		outdto.setFilename("C:\\file_libraryBatchPJ\\fileoutput\\TBBIE_FO.csv");
	

		// Service呼び出し
		// ClassNotFoundException ->クラス存在エラー
		// SQLException -> データベースに関連がある追加情報
		// IOException -> ファイルおよびディレクトリ情報エラー
		try {
		  service.service(outdto);
		} catch (ClassNotFoundException e) {
			// 例外処理
			System.out.println("MSGZZ005E クラスが存在しません。確認後再度利用してください。");
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// 例外処理
			System.out.println("MSGZZ004E DBアクセスに失敗しました。システム管理者に問い合わせください。");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// 例外処理
			System.out.println("MSGBIE001E 格納されたディレクトリが存在しません。");
			e.printStackTrace();
			return;
		} 
		
		// 終了文言を出力する。
		System.out.println("MSGZZ002I 処理が正常終了しました。");
	}
}
