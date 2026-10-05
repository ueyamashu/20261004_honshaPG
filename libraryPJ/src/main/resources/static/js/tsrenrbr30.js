// フォームサブミット
function bookRentalConfirmFormSubmit() {
	// 戻るボタンを押下した場合
	document.confirmForm.action = "/TSRENRBT10/init"; 
	document.confirmForm.method = "get"; 
	// データを送信する。
	document.confirmForm.submit();
}