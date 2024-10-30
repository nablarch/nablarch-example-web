<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="//fonts.googleapis.com/icon?family=Material+Icons">
        <title>プロジェクト一括登録画面</title>
    </head>
    <body>
        <div class="mainContents">
            <n:include path="/WEB-INF/view/common/menu.jsp" />
            <n:include path="/WEB-INF/view/common/header.jsp" />
            <n:form useToken="true" enctype="multipart/form-data">
                <div class="title-nav">
                    <span>プロジェクト一括登録画面</span>
                    <div class="button-nav">
                        <n:button uri="/action/projectUpload/upload" allowDoubleSubmission="false" cssClass="btn btn-light">登録</n:button>
                    </div>
                </div>
                <div class="message-area margin-top">
                    <c:if test="${not empty uploadProjectSize}">
                        <ul><li class="message-info"><n:message messageId="success.upload.project" option0="${uploadProjectSize}" /></li><ul>
                    </c:if>
                    <n:errors errorCss="message-error"/>
                </div>
                <div class="floatClear"></div>
                <h2 class="font-group mb-3">プロジェクト情報ファイル選択</h2>
                <table class="table">
                    <col width="20%" />
                    <col width="30%" />
                    <col width="50%" />
                    <tbody>
                        <tr>
                            <th class="item-norequired" colspan="2">プロジェクト情報ファイル選択</th>
                        </tr>
                        <tr>
                            <th class="width-250 required">プロジェクト情報ファイル</th>
                            <td >
                                <div class="input-group">
                                    <n:file name="uploadFile" cssClass="form-control form-control-lg" id="uploadFile"/>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="title-nav">
                    <div class="button-nav">
                        <n:button uri="/action/projectUpload/upload" allowDoubleSubmission="false" cssClass="btn btn-light">登録</n:button>
                    </div>
                </div>
            </n:form>
            <n:include path="/WEB-INF/view/common/footer.jsp" />
        </div>
    </body>
</html>