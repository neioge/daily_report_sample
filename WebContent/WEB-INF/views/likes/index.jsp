<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>いいねした人　一覧</h2>
        <table id="employee_list">
            <tbody>
               <tr>
                   <th>氏名</th>
                   <th>いいねした日時</th>
               </tr>
               <c:forEach var="like" items="${likes}" varStatus="status">
                   <tr class="row${status.count % 2}">
                       <td><c:out value="${like.employee.name}" /></td>
                       <td><c:out value="${like.created_at}" /></td>
                   </tr>
               </c:forEach>
           </tbody>
        </table>
        <div id="pagination">
            （全 ${likes_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((likes_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a class="btn-push" href="<c:url value='/reports/index' />">一覧へ戻る</a></p>
    </c:param>
</c:import>