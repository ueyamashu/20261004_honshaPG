package com.springboot.libraryPJ.ZZ.util;

public class CommonConstants {

	// コード値の定数 ： 会員の会員区分のコード値
	public static final String MEMBER_TABLE_MEMBERSHIP_CODE_VALUE = "[0,1]";

	// コード値の定数 ： 資料情報の分類コードのコード値
	public static final String BOOK_TABLE_CATEGORY_CODE_VALUE = "[0,1,2,3,4,5,6,7,8,9]";

	// メンバテーブルの桁数 ： 会員のパスワードに入力できる最大文字数
	public static final int MEMBER_TABLE_PW_MAX_SIZE = 32;

	// メンバテーブルの桁数 ： 会員のメールに入力できる最大文字数
	public static final int MEMBER_TABLE_EMAIL_MAX_SIZE = 50;

	// メンバテーブルの桁数 ： 会員の会員IDに入力できる最大文字数
	public static final int MEMBER_TABLE_MEMBERID_MAX_SIZE = 4;

	// メンバテーブルの桁数 ： 会員の住所に入力できる最大文字数
	public static final int MEMBER_TABLE_ADDRESS_MAX_SIZE = 100;

	// メンバテーブルの桁数 ： 会員の名前に入力できる最大文字数
	public static final int MEMBER_TABLE_NAME_MAX_SIZE = 50;

	// メンバテーブルの桁数 ： 会員の生年月日に入力できる最大文字数
	public static final int MEMBER_TABLE_BIRTHDAY_MAX_SIZE = 10;

	// メンバテーブルの桁数 ： 会員の郵便番号に入力できる最大文字数
	public static final int MEMBER_TABLE_POSTAL_MAX_SIZE = 8;

	// メンバテーブルの桁数 ： 会員の電話番号に入力できる最大文字数
	public static final int MEMBER_TABLE_TEL_MAX_SIZE = 13;

	// 会員区分 ： 一般
	public static final String MEMBERSHIP_PUBLIC = "0";

	// 会員区分 ： 職員
	public static final String MEMBERSHIP_STAFF = "1";

	// 削除フラグ ： 削除済
	public static final String DELETE = "1";

	// 削除フラグ ： 未削除
	public static final String NOT_DELETE = "0";

	// 文字列 ： パスワード
	public static final String STRING_PASSWORD = "パスワード";

	// 文字列 ： パスワードのフォーマット
	public static final String STRING_PASSWORD_FORMAT = "半角文字";

	// 文字列 ： メールアドレス
	public static final String STRING_EMAIL = "メールアドレス";

	// 文字列 ： メールアドレスのフォーマット
	public static final String STRING_EMAIL_FORMAT = "test@email.co.jp";

	// 文字列 ： 会員
	public static final String STRING_MEMBER = "会員";

	// 文字列 ： 会員ID
	public static final String STRING_MEMBERID = "会員ID";

	// 文字列 ： 会員IDのフォーマット
	public static final String STRING_MEMBER_ID_FORMAT = "半角数字";

	// 文字列 ： 会員のIDに入力できる最大文字数
	public static final String MEMBER_TABLE_MEMBER_ID_MAX_SIZE_STRING = "4";

	// 文字列 ： 会員のパスワードに入力できる最大文字数
	public static final String MEMBER_TABLE_PASSWORD_MAX_SIZE_STRING = "32";

	// 文字列 ： 会員のメールに入力できる最大文字数
	public static final String MEMBER_TABLE_EMAIL_MAX_SIZE_STRING = "50";

	// 文字列 ： 会員の会員IDに入力できる最大文字数
	public static final String MEMBER_TABLE_MEMBERID_MAX_SIZE_STRING = "4";

	// 文字列 ： 会員の名前に入力できる最大文字数
	public static final String MEMBER_TABLE_NAME_MAX_SIZE_STRING = "50";

	// 文字列 ： 会員情報
	public static final String STRING_MEMBER_JOHO = "会員情報";

	// 文字列 ： 会員情報
	public static final String STRING_MEMBER_JOHO_SELECT = "会員情報取得";

	// 文字列 ： 住所
	public static final String STRING_ADDRESS = "住所";

	// 文字列 ： 住所に入力できる最大文字数
	public static final String MEMBER_TABLE_ADDRESS_MAX_SIZE_STRING = "100";

	// 文字列 ： 備考
	public static final String STRING_DISPOSALNOTE = "備考";

	// 文字列 ： 名前
	public static final String STRING_NAME = "名前";

	// 文字列 ： 名前のフォーマット
	public static final String STRING_NAME_FORMAT = "全角漢字、全角カタカナ";

	// 文字列 ： 廃棄年月日
	public static final String STRING_DISPOSALDATE = "廃棄年月日";

	// 文字列 ： 最小桁数
	public static final int MIN_SIZE = 0;

	// 文字列 ： 未来日のエラー
	public static final String STRING_MEMBER_FUTURE = "現在より過去の日付";

	// 文字列 ： 生年月日
	public static final String STRING_BIRTHDAY = "生年月日";

	// 文字列 ： 生年月日に入力できる最大文字数
	public static final String MEMBER_TABLE_BIRTHDAY_MAX_SIZE_STRING = "10";

	// 文字列 ： 生年月日の正規表現
	public static final String STRING_BITHDAY_REGEXP = "yyyy/MM/dd";

	// 文字列 ： 登録者
	public static final int REGISTER_ID = 9999;

	// 文字列 ： 貸出資料
	public static final String STRING_RENTAL_BOOK = "貸出資料";

	// 文字列 ： 資料
	public static final String STRING_BOOK = "資料";

	// 文字列 ： 資料ID
	public static final String STRING_BOOKID = "資料ID";

	// 文字列 ： 資料のIDに入力できる最大文字数
	public static final String RENTAL_TABLE_BOOK_ID_MAX_SIZE_STRING = "6";

	// 文字列 ： 資料のIDに入力できる最大文字数
	public static final String RENTAL_TABLE_TITLE_MAX_SIZE_STRING = "100";

	// 文字列 ： 資料名
	public static final String STRING_TITLE = "資料名";

	// 文字列 ： 資料情報
	public static final String STRING_BOOK_JOHO = "資料情報";

	// 文字列 ： 郵便番号
	public static final String STRING_POSTALCODE = "郵便番号";

	// 文字列 ： 郵便番号に入力できる最大文字数
	public static final String MEMBER_TABLE_POSTALCODE_MAX_SIZE_STRING = "8";

	// 文字列 ： 郵便番号の正規表現
	public static final String STRING_POSTALCODE_REGEXP = "「000-0000」";

	// 文字列 ： 電話番号
	public static final String STRING_TELNUMBAER = "電話番号";

	// 文字列 ： 電話番号に入力できる最大文字数
	public static final String MEMBER_TABLE_TELNUMBER_MAX_SIZE_STRING = "13";

	// 文字列 ： 電話番号の正規表現
	public static final String STRING_TELNUMBER_REGEXP = "「000-0000-0000」";

	// 日付判断の結果 ： 日付チェック処理で日付が現在日の場合返還する値
	public static final int CHECK_RESULT_TODAY_DATE = 0;

	// 日付判断の結果 ： 日付チェック処理で日付が現在日より未来日の場合返還する値
	public static final int CHECK_RESULT_AFTER_DATE = 1;

	// 日付判断の結果 ： 日付チェック処理で日付が現在日より過去日の場合返還する値
	public static final int CHECK_RESULT_BEFORE_DATE = -1;

	// 最大桁数 ： ランダムパスワード最小字数
	public static final int RANDOM_PASSWORD_SIZE = 10;

	// 最大桁数 ： 貸出最大数
	public static final int RANTAL_LIMIT = 5;

	// 本テーブルの桁数 ： 資料のISBNコードに入力できる最大文字数
	public static final int BOOK_TABLE_ISBN_MAX_SIZE = 13;

	// 本テーブルの桁数 ： 資料の著者名に入力できる最大文字数
	public static final int BOOK_TABLE_AUTHOR_MAX_SIZE = 50;

	// 本テーブルの桁数 ： 資料の資料IDに入力できる最大文字数
	public static final int BOOK_TABLE_ID_MAX_SIZE = 6;

	// 本テーブルの桁数 ： 資料の資料名に入力できる最大文字数
	public static final int BOOK_TABLE_PUBLISHER_MAX_SIZE = 100;

	// 本テーブルの桁数 ： 資料の資料名に入力できる最大文字数
	public static final int BOOK_TABLE_TITLE_MAX_SIZE = 100;

	// 正規表現の定数 ： Eメールアドレスのフォーマットを確認する時に使用する正規表現
	public static final String REGEX_EMAILADDRESS_FORMAT = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

	// 正規表現の定数 ： 入力値が全角カタカナ、漢字であるかを確認する時に使用する正規表現
	public static final String REGEX_TEXT_FULL_KATAKANA_KANJI = "^[^\\x01-\\x7E\\uFF61-\\uFF9F]+$";

	// 正規表現の定数 ： 入力値が全角カタカナであるかを確認する時に使用する正規表現
	public static final String REGEX_TEXT_FULL_KATAKANA = "^[\\u30a0-\\u30ff]+$";

	// 正規表現の定数 ： 入力値が半角数値であるかを確認する時に使用する正規表現
	public static final String REGEX_HALF_WIDTH_NUMBER = "^[0-9]*$";

	// 正規表現の定数 ： 入力値が半角英数字であるかを確認する時に使用する正規表現
	public static final String REGEX_HALF_WIDTH_ENG_NUMBER = "^[A-Za-z0-9]+$";

	// 正規表現の定数 ： 入力値が漢字であるかを確認する時に使用する正規表現
	public static final String REGEX_TEXT_KANJI = "^[\u4E00-\u9FFF]+$";

	// 正規表現の定数 ： 日付の形式を確認する時に使用する正規表現
	public static final String REGEX_DATE_FORMAT = "^[0-9]{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$";

	// 正規表現の定数 ： 郵便番号のフォーマットが「000-0000」であるかを確認する時に使用する正規表現
	public static final String REGEX_POSTAL_FORMAT = "^[0-9]{3}-[0-9]{4}$";

	// 正規表現の定数 ： 電話番号のフォーマットが「00-0000-0000」であるかを確認する時に使用する正規表現
	public static final String REGEX_TEL_FORMAT_1 = "^[0-9]{2}-[0-9]{4}-[0-9]{4}$";

	// 正規表現の定数 ： 電話番号のフォーマットが「000-0000-0000」であるかを確認する時に使用する正規表現
	public static final String REGEX_TEL_FORMAT_2 = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";

	// 詳細テーブルの桁数 ： 資料廃棄の備考に入力できる最大文字数
	public static final int DETAIL_TABLE_NOTE_MAX_SIZE = 200;

	// 詳細テーブルの桁数 ： 資料廃棄の備考に入力できる最小文字数
	public static final int DETAIL_TABLE_NOTE_MIN_SIZE = 1;

	// 連絡フラグ ： 未連絡
	public static final String NOT_REMIND = "0";

	// 連絡フラグ ： 連絡済
	public static final String REMIND = "1";

	// 文字列 ： 資料貸出
	public static final String STRING_BOOK_RENTAL = "資料貸出";

	// 文字列 ： 資料貸出情報取得
	public static final String STRING_BOOK_RENTAL_JOHO_SELECT = "資料貸出情報取得";

	// 蔵書テーブルの桁数 ： 会員の生年月日に入力できる最大文字数
	public static final int LIBRARY_TABLE_DISPOSAL_DATE_MAX_SIZE = 10;

	// 文字列 ： 会員区分
	public static final String STRING_MEMBER_KBN = "会員区分";

	// 文字列 ： 貸出情報
	public static final String STRING_RENTAL_JOHO = "貸出情報";

	// 文字列 ： 現在パスワード
	public static final String STRING_NOW_PASSWORD = "現在パスワード";

	// 文字列 ： 新しいパスワード
	public static final String STRING_NEW_PASSWORD1 = "新しいパスワード";

	// 文字列 ： 新しいパスワード（確認）
	public static final String STRING_NEW_PASSWORD2 = " 新しいパスワード（確認）";

	// 文字列 : 会員情報の更新
	public static final String STRING_MEMBER_JOHO_CHANGE = "会員情報の更新";

	// 文字列 : 正しいパスワード
	public static final String RIGHT_PASSWORD = "正しいパスワード";

	// 貸出可能の判断結果：貸出中
	public static final int CHECK_RESULT_RENTAL_BOOK = -1;

	// 貸出可能の判断結果：廃棄済み
	public static final int CHECK_RESULT_DISPOSAL_BOOK = 0;

	// 貸出可能の判断結果：貸出可能
	public static final int CHECK_RESULT_AVAILABLE_RENTAL = 1;

	// メンバテーブルの桁数 ： 会員のパスワードに入力できる最小文字数
	public static final int MEMBER_TABLE_PW_MIN_SIZE = 6;

	// 文字列 ： 会員のパスワードに入力できる最小文字数
	public static final String MEMBER_TABLE_PASSWORD_MIN_SIZE_STRING = "6";

	// 文字列 ： パスワード生成
	public static final String CREATE_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	// 延滞お知らせの表示フラグ：表示しない
	public static final String RENTAL_DUE_DATE_NOT_EXCEED = "0";

	// 延滞お知らせの表示フラグ：表示する
	public static final String RENTAL_DUE_DATE_EXCEED = "1";

	// 文字列 ： 貸出資料
	public static final String STRING_RETURN_BOOK = "返却資料";

}
