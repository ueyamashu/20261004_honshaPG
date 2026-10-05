// フォームサブミット
function rentalBokkLibraryListFormSubmit(operation) {
    var form = document.createElement("form");
    form.method = "get";
    if (operation == "bookSearch") {
        // 検索ボタンを押下した場合
        form.action = "/TSRENRBT10/search"; 
        // 入力フォームの資料IDを設定する。
        var bookId = document.createElement("input");
        bookId.type = "hidden";
        bookId.name = "bookId";
        bookId.value = document.getElementsByName("bookId").item(0).value;
        form.appendChild(bookId);
        // 入力フォームの資料名を設定する。
        var bookTitle = document.createElement("input");
        bookTitle.type = "hidden";
        bookTitle.name = "title";
        bookTitle.value = document.getElementsByName("title").item(0).value;
        form.appendChild(bookTitle);
        // 入力フォームの資料名を設定する。
        var memberId = document.createElement("input");
        memberId.type = "hidden";
        memberId.name = "memberId";
        memberId.value = document.getElementsByName("memberId").item(0).value;
        form.appendChild(memberId);
    } else if (operation == "bookRental") {
        // 貸出処理ボタンを押下した場合
        form.action = "/TSRENRBR30/init"; 
        // 選択された資料IDを設定する。
        var bookId = document.createElement("input");
        bookId.type = "hidden";
        bookId.name = "bookId";
        let elements = document.getElementsByName("bookId");
        let len = elements.length;
        for (let i = 0; i < len; i++){
            if (elements.item(i).checked){
                bookId.value = elements.item(i).value;
            }
        }
        form.appendChild(bookId);
    }
    document.body.appendChild(form);
    // データを送信する。
    form.submit();
}