<%@page import="com.nablarch.example.app.web.common.code.ProjectClass"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page session="false" %>
<n:script type="text/javascript" src="/javascripts/sideMenu.js"></n:script>
<nav class="col-md-2 menu">
    <div class="card">
        <div class="card-body">
            <ul>
                <li>
                    <div class="sideMenu">
                        <h4 class="text-muted fs-4 mb-3"><strong>プロジェクトをさがす</strong></h4>
                        <n:form method="GET" action="list">
                            <!-- 入力フォームで送信しない条件を保持するため、hiddenパラメータとして持つ -->
                            <n:plainHidden name="searchForm.projectStartDateBeginStr"/>
                            <n:plainHidden name="searchForm.projectStartDateEndStr"/>
                            <n:plainHidden name="searchForm.projectEndDateBeginStr"/>
                            <n:plainHidden name="searchForm.projectEndDateEndStr"/>
                            <n:plainHidden name="searchForm.sortKey"/>
                            <n:plainHidden name="searchForm.sortDir"/>

                            <!-- 検索結果の表示ページ番号を指定する -->
                            <input id="firstPageNumber" type="hidden" name="searchForm.pageNumber" value="1" />
                            <ul>
                                <li>
                                    <span class="text-primary">ランクからさがす</span>
                                    <ul>
                                        <c:forEach var="projectClass" items="<%= ProjectClass.values() %>">
                                            <li>
                                                <div class="checkbox form-check">
                                                    <label class="form-check-label form-control-lg">
                                                        <n:checkbox name="searchForm.projectClass" cssClass="form-check-input" value="${projectClass.value}"/>
                                                        <n:write name="projectClass.label"/>
                                                    </label>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </li>
                                <li class="mb-3">
                                    <span class="text-primary">期間からさがす</span>
                                    <c:url value="list" var="uri">
                                        <c:param name="searchForm.clientId" value="${searchForm.clientId}"/>
                                        <c:param name="searchForm.clientName" value="${searchForm.clientName}"/>
                                        <c:param name="searchForm.projectName" value="${searchForm.projectName}"/>
                                        <c:param name="searchForm.projectType" value="${searchForm.projectType}"/>
                                        <c:forEach items="${searchForm.projectClass}" var="projectClass">
                                            <c:param name="searchForm.projectClass" value="${projectClass}" />
                                        </c:forEach>
                                        <c:param name="searchForm.sortKey" value="${searchForm.sortKey}"/>
                                        <c:param name="searchForm.sortDir" value="${searchForm.sortDir}"/>
                                        <c:param name="searchForm.pageNumber" value="1"/>
                                    </c:url>
                                    <ul>
                                        <li class="py-2">
                                            <n:a id="startThisYear" href="${uri}">
                                                <span class="text-muted fs-5 ps-3">今年開始</span>
                                            </n:a>
                                        </li>
                                        <li class="py-2">
                                            <n:a id="endThisYear" href="${uri}">
                                                <span class="text-muted fs-5 ps-3">今年終了</span>
                                            </n:a>
                                        </li>
                                        <li class="py-2">
                                            <n:a id="endLastYear" href="${uri}">
                                                <span class="text-muted fs-5 ps-3">昨年までに終了</span>
                                            </n:a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                            <fieldset>
                                <div>
                                    <div class="form-group label-static mb-3">
                                        <div class="mb-2">
                                            <label for="client-name" class="control-label mb-3">顧客名</label>
                                            <n:text id="client-id" name="searchForm.clientId" readonly="true" cssClass="form-control form-control-lg mb-2" placeholder="顧客ID"/>
                                            <n:text id="client-name" name="searchForm.clientName" readonly="true" cssClass="form-control form-control-lg" placeholder="顧客名"/>
                                        </div>
                                        <div class="text-end">
                                            <a href="#" data-bs-toggle="modal" data-bs-target="#client-search-dialog" class="badge rounded-pill text-dark bg-body-secondary"><i class="material-icons">search</i></a>
                                            <a href="#" class="badge rounded-pill text-dark bg-body-secondary" id="client-remove"><i class="material-icons">remove</i></a>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="form-group mb-3">
                                        <label for="projectName" class="control-label mb-3">プロジェクト名</label>
                                        <div>
                                            <n:text id="projectName" name="searchForm.projectName" size="25" maxlength="64" cssClass="form-control form-control-lg" errorCss="input-error form-control form-control-lg" placeholder="プロジェクト名"/>
                                            <n:error errorCss="message-error" name="searchForm.projectName" />
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center">
                                    <input type="submit" id="search" class="btn btn-primary" value="検索" />
                                </div>
                            </fieldset>
                        </n:form>
                    </div>
                </li>
            </ul>
        </div>
    </div>
  <%-- 顧客検索 --%>
  <n:include path="/WEB-INF/view/client/index.jsp" />
</nav>