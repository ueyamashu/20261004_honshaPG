package LIP.controller;

import java.io.IOException;
import java.sql.SQLException;

import LIP.dto.TBLIPFileInDTO;
import LIP.service.TBLIPFileInService;

public class TBLIPFileInController {
	public static void main(String[] args) {
		TBLIPFileInDTO indto = new TBLIPFileInDTO();
		TBLIPFileInService service = new TBLIPFileInService();


		// 開始文言を出力する。
		System.out.println("MSGZZ001I 処理を開始します。");
		// InDTO設定
		indto.setInputfilename("C:\\file_libraryBatchPJ\\fileInput\\TBLIP_FI.csv");
		indto.setOutputfilename("C:\\file_libraryBatchPJ\\fileInput\\TBLIP_FO.csv");

		// Service呼び出し
		// ClassNotFoundException ->クラス存在エラー
		// SQLException -> データベースに関連がある追加情報
		// IOException -> ファイルおよびディレクトリ情報エラー
		try {
		  service.service(indto);
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
			System.out.println("MSGLIP001E ファイルが存在しません。確認後再度利用してください。");
			e.printStackTrace();
			return;
		} 

		// 終了文言を出力する。
		System.out.println("MSGZZ002I 処理が正常終了しました。");
	}
}
