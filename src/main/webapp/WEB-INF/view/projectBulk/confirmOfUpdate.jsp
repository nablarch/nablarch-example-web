<%@page import="com.nablarch.example.app.web.common.code.ProjectType"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags/listSearchResult" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <%-- javascript --%>
        <n:script type="text/javascript" src="/javascripts/lib/jquery-3.7.1.min.js"></n:script>
        <n:script type="text/javascript" src="/javascripts/projectList.js"></n:script>
        <title>プロジェクト検索一覧更新画面</title>
    </head>
    <body>
        <n:include path="/WEB-INF/view/common/noscript.jsp" />
        <div class="mainContents">
            <n:include path="/WEB-INF/view/common/menu.jsp" />
            <n:include path="/WEB-INF/view/common/header.jsp" />
            <section>
                <div class="title-nav">
                    <span>プロジェクト検索一覧更新画面</span>
                    <div class="button-nav">
                        <n:form useToken="true">
                            <n:submit type="button" uri="backToList" cssClass="btn btn-lg btn-light" value="入力へ戻る"></n:submit>
                            <n:submit type="button" uri="update" allowDoubleSubmission="false" cssClass="btn btn-lg btn-success" value="確定"></n:submit>
                        </n:form>
                    </div>
                </div>
                <h2 class="font-group my-3">プロジェクト変更一覧</h2>
                <div>
                    <table class="table table-striped table-hover">
                        <tr>
                            <th>プロジェクトID</th>
                            <th>プロジェクト名</th>
                            <th>プロジェクト種別</th>
                            <th>開始日</th>
                            <th>終了日</th>
                        </tr>
                        <c:forEach var="row" items="${projectListDto.projectList}">
                            <tr class="<n:write name='oddEvenCss' />">
                                <td>
                                    <n:write name="row.projectId" />
                                </td>
                                <td>
                                    <n:write name="row.projectName" />
                                </td>
                                <td>
                                    <c:forEach var="projectType" items="<%= ProjectType.values() %>">
                                        <c:if test="${projectType.value == row.projectType}">
                                            <n:write name="projectType.label" />
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <n:write value="${n:formatByDefault('dateTime', row.projectStartDate)}" />
                                </td>
                                <td>
                                    <n:write value="${n:formatByDefault('dateTime', row.projectEndDate)}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </section>
            <div class="title-nav page-footer">
                <div class="button-nav">
                    <n:form useToken="true">
                        <n:submit type="button" uri="backToList" cssClass="btn btn-lg btn-light" value="入力へ戻る"></n:submit>
                        <n:submit type="button" uri="update" allowDoubleSubmission="false" cssClass="btn btn-lg btn-success" value="確定"></n:submit>
                    </n:form>
                </div>
            </div>
            <n:include path="/WEB-INF/view/common/footer.jsp" />
        </div>
    </body>
</html>