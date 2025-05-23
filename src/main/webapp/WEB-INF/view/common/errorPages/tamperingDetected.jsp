<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>エラー画面</title>
    </head>

    <body>
        <n:include path="/WEB-INF/view/common/noscript.jsp" />

        <div class="mainContents">
            <n:include path="/WEB-INF/view/common/menu.jsp" />
            <n:include path="/WEB-INF/view/common/header.jsp" />

            <n:form>
                <div class="title-nav">
                    <span class="page-title">エラー画面</span>
                </div>
                <div class="message-area">
                    <p>
                         既にログアウトされています。ログイン後に処理を行ってください。
                    </p>
                </div>

                <div class="title-nav">
                </div>
            </n:form>

            <n:include path="/WEB-INF/view/common/footer.jsp" />
        </div>
    </body>
</html>