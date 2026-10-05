// フォームサブミット
function rentalMemberListFormSubmit(operation) {
    var form = document.createElement("form");
    form.method = "get";
    if (operation == "memberSearch") {
        // 検索ボタンを押下した場合
        form.action = "/TSRENRMT10/member/search";
        // 入力フォームの会員IDを設定する。
        var memberId = document.createElement("input");
        memberId.type = "hidden";
        memberId.name = "memberId";
        memberId.value = document.getElementsByName("memberId").item(0).value;
        form.appendChild(memberId);
        // 入力フォームの名前を設定する。
        var memberName = document.createElement("input");
        memberName.type = "hidden";
        memberName.name = "name";
        memberName.value = document.getElementsByName("name").item(0).value;
        form.appendChild(memberName);
    } else if (operation == "bookSearch") {
        // 資料情報検索ボタンを押下した場合
        form.action = "/TSRENRBT10/init";
        // 選択された会員IDを設定する。
        var memberId = document.createElement("input");
        memberId.type = "hidden";
        memberId.name = "memberId";
        let elements = document.getElementsByName("memberId");
        let len = elements.length;
        for (let i = 0; i < len; i++) {
            if (elements.item(i).checked) {
                memberId.value = elements.item(i).value;
            }
        }
        form.appendChild(memberId);
    }
    document.body.appendChild(form);
    // データを送信する。
    form.submit();
}