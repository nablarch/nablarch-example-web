<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page session="false" %>

<div>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-md p-3" data-bs-theme="dark">
        <div class="container-fluid">
            <a class="navbar-brand px-2 fs-4">Project</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item px-2">
                        <n:a href="/action/project/index" cssClass="nav-link active">プロジェクト検索</n:a>
                    </li>
                    <li class="nav-item px-2">
                        <n:a href="/action/projectBulk/index" cssClass="nav-link active">プロジェクト一括更新</n:a>
                    </li>
                    <c:if test="${userContext.admin}">
                        <li class="nav-item px-2">
                            <n:a href="/action/projectUpload" cssClass="nav-link active">プロジェクト一括登録</n:a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
</div>
