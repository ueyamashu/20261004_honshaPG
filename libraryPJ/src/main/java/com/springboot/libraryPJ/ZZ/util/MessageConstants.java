package com.springboot.libraryPJ.ZZ.util;

public class MessageConstants {
	// {1}を入力してください。
	public static final String MSGCOM001E = "MSGCOM001E　{1}を入力してください。";

	// {1}を選択してください。
	public static final String MSGCOM002E = "MSGCOM002E　{1}を選択してください。";

	// {1}指定に不正な値があります。
	public static final String MSGCOM003E = "MSGCOM003E　{1}指定に不正な値があります。";

	// {1}は{2}で入力してください。
	public static final String MSGCOM004E = "MSGCOM004E　{1}は{2}で入力してください。";

	// {1}は{2}形式で入力してください。
	public static final String MSGCOM005E = "MSGCOM005E　{1}は{2}形式で入力してください。";

	// {1}は{2}文字以内で入力してください。
	public static final String MSGCOM006E = "MSGCOM006E　{1}は{2}文字以内で入力してください。";

	// {1}は過去日を入力してください。
	public static final String MSGCOM007E = "MSGCOM007E　{1}は過去日を入力してください。";

	// {1}指定に不正な値があります。半角数字で正しい日付を入力してください。
	public static final String MSGCOM008E = "MSGCOM008E　{1}指定に不正な値があります。半角数字で正しい日付を入力してください。";

	// 郵便番号指定に不正な値があります。{1}桁の半角数字で入力してください。
	public static final String MSGCOM009E = "MSGCOM009E　郵便番号指定に不正な値があります。{1}桁の半角数字で入力してください。";

	// メールアドレスを正しく入力してください
	public static final String MSGCOM010E = "MSGCOM010E　メールアドレスを正しく入力してください";

	// 廃棄年月日は過去日を指定できません。
	public static final String MSGCOM011E = "MSGCOM011E　廃棄年月日は過去日を指定できません。";

	// レコードが存在しない。
	public static final String MSGCOM012E = "MSGCOM012E　{1}が存在しません。";

	// {1}が存在しません。
	public static final String MSGCOM017E = "MSGCOM017E　{1}が存在しません。";

	// 検索条件に一致する{1}が存在しません。
	public static final String MSGCOM013E = "MSGCOM013E　検索条件に一致する{1}が存在しません。";

	// {1}は半角数字で入力してください。
	public static final String MSGCOM014E = "MSGCOM014E　{1}は半角数字で入力してください。";

	// {1}は全角文字で入力してください。
	public static final String MSGCOM015E = "MSGCOM015E　{1}は全角文字で入力してください。";

	// ログインIDとパスワードに該当する会員情報が存在しません
	public static final String MSGCOM999E = "MSGCOM999E　ログインIDとパスワードに該当する会員情報が存在しません";

	// 職員ユーザーのみでログインしてください。
	public static final String MSGZZ001E = "MSGZZ001E　職員ユーザーのみでログインしてください。";

	// 職員ユーザーのみでログインしてください。
	public static final String MSGZZ002E = "MSGZZ002E　会員情報登録失敗しました。";

	// 同じメールアドレスが登録できません
	public static final String MSGZZ003E = "MSGZZ003E　同じメールアドレスが登録できません";

	// {1}に失敗しました。
	public static final String MSGCOM016E = "MSGCOM016E　{1}に失敗しました。";

	// {1}冊貸出しているため、貸出できません。
	public static final String MSGREN001E = "MSGREN001E　貸出中の資料が{1}冊なので貸出できません。";

	// 延滞中なので貸出できません。
	public static final String MSGREN002E = "MSGREN002E　延滞中なので貸出できません。";

	// 貸出中なので貸出できません。
	public static final String MSGREN003E = "MSGREN003E　資料ID：{1}は貸出中なので貸出できません。";

	// 廃棄されたため貸出できません。
	public static final String MSGREN004E = "MSGREN004E　資料ID：{1}は廃棄されたため貸出できません。";

	// {1}は未来日を指定できません。
	public static final String MSGBKS001E = "MSGBKS001E　{1}は未来日を指定できません。";

	// 入荷年月日は出版日より過去日を指定できません。
	public static final String MSGBKS002E = "MSGBKS002E　入荷年月日は出版日より過去日を指定できません。";

	// 入荷年月日は出版日より過去日を指定できません。
	public static final String MSGBKS003E = "MSGBKS003E 既に登録済みのISBN番号です。";

	// 入荷年月日は出版日より過去日を指定できません。
	public static final String MSGBKS004E = "MSGBKS004E　貸出中なので廃棄できません。";

	// {1}と{2}が一致しません。
	public static final String MSGCOM018E = "MSGCOM018E　{1}と{2}が一致しません。";

	// 新しいパスワードは現在のパスワードと異なる文字列で作成してください。
	public static final String MSGMEM001E = "MSGMEM001E　新しいパスワードは現在のパスワードと異なる文字列で作成してください。";
	
	// {1}は{2}文字以上{3}文字以下で入力してください。
	public static final String MSGCOM019E = "MSGCOM019E　{1}は{2}文字以上{3}文字以下で入力してください。";

	/**
	 * メッセージの取得<br>
	 * 利用例：<br>
	 * String result = MessageConstants.getMessage(MessageConstants.MSGCOM004E, new
	 * String[]{"試験１","試験２"});
	 *
	 * @param messageCode
	 * @param umekomimoji
	 * @return
	 */
	public static String getMessage(String messageCode, String[] umekomimoji) {

		String value = messageCode;
		int i = 1;
		for (String ume : umekomimoji) {
			value = value.replace("{" + (i++) + "}", ume);
		}

		return value;
	}
}
