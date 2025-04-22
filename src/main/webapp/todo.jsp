<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8">
    <title>TODO List Servlet</title>
</head>
<body>
<h1>TODO List</h1>

<form action="${pageContext.request.contextPath}/" method="POST">
    <label>
        <span>Введите заметку:</span>
        <input type="text" name="text">
    </label>

    <button type="submit" name="action" value="create">Создать</button>
    <c:if test="${not empty createError}">
        <div style="color: red;">${createError}</div>
    </c:if>
</form>

<ul>
    <c:forEach var="todoItem" items="${todoItems}">
        <li style="margin-bottom: 1em;">
            <form action="${pageContext.request.contextPath}/" method="POST">
                <input type="text" name="text" value="${todoItem.text}">
                <button type="submit" name="action" value="save">Сохранить</button>
                <button type="submit" name="action" value="delete">Удалить</button>
                <c:if test="${not empty updateError and not empty updateErrorItemId and updateErrorItemId == todoItem.id}">
                    <div style="color: red;">${updateError}</div>
                </c:if>
                <input type="hidden" name="id" value="${todoItem.id}">
            </form>
        </li>
    </c:forEach>
</ul>

<c:if test="${not empty findError}">
    <div style="color: red;">${findError}</div>
</c:if>
</body>
</html>
