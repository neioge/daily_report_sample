<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>フォロー中の従業員　一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                </tr>
                <c:forEach var="relationship" items="${activeRelationships}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${relationship.followed.code}" /></td>
                        <td><c:out value="${relationship.followed.name}" /></td>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${following_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((following_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/following/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/employees/new' />">新規従業員の登録</a></p>
        <p><a href="<c:url value='/followed/index' />">フォロワーを見る</a></p>
    </c:param>
</c:import>