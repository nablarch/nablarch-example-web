<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <title>プロジェクト一覧更新完了画面</title>
    </head>
    <body>
        <n:include path="/WEB-INF/view/common/noscript.jsp" />
        <div class="mainContents">
            <n:include path="/WEB-INF/view/common/menu.jsp" />
            <n:include path="/WEB-INF/view/common/header.jsp" />
            <section>
                <n:form>
                    <div class="title-nav">
                        <span class="page-title">プロジェクト一覧更新完了画面</span>
                        <div class="button-nav">
                            <div class="button-block link-button-block">
                                <n:a id="topReturnList" href="#" cssClass="btn btn-lg btn-success">次へ</n:a>
                            </div>
                        </div>
                    </div>
                    <div class="message-area">
                        プロジェクトの更新が完了しました。
                    </div>
                    <div class="title-nav">
                        <div class="button-nav">
                            <div class="button-block link-button-block">
                                <n:a id="bottomReturnList" href="#" cssClass="btn btn-lg btn-success">次へ</n:a>
                            </div>
                        </div>
                    </div>
                </n:form>
            </section>
            <n:include path="/WEB-INF/view/common/footer.jsp" />
        </div>
        <n:script type="text/javascript">
            $(function(){
                setListUrlTo("topReturnList");
                setListUrlTo("bottomReturnList");
            });
        </n:script>
    </body>
</html>