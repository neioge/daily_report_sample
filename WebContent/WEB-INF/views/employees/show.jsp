<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <h2>id : ${employee.id} の従業員情報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>社員番号</th>
                            <td><c:out value="${employee.code}" /></td>
                        </tr>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>権限</th>
                            <td>
                                <c:choose>
                                    <c:when test="${employee.admin_flag == 3}">部長</c:when>
                                    <c:when test="${employee.admin_flag == 2}">課長</c:when>
                                    <c:when test="${employee.admin_flag == 1}">係長</c:when>
                                    <c:otherwise>一般</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${employee.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${employee.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id != employee.id}">
                    <c:choose>
                        <c:when  test="${ checkSameRelationshipFlag }">
                            <form method="POST" action="/daily_report_sample/relationships/destroy">
                                <input type = "hidden" name="_token" value="${_token }" />
                                <input type = "hidden" name="_destroyRelationship" value="${employee.id}" />
                                <button type="submit">フォロー済み</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="/daily_report_sample/relationships/create">
                                <input type = "hidden" name="_token" value="${_token }" />
                                <input type = "hidden" name="_createRelationship" value="${employee.id}" />
                                <button type="submit">フォローする</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <p><a href="<c:url value='/employees/edit?id=${employee.id}' />">この従業員情報を編集する</a></p>

                <h2>フォローしている従業員　一覧</h2>
                <table id="employee_list">
                    <tbody>
                        <tr>
                            <th>社員番号</th>
                            <th>氏名</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach var="relationship" items="${relationships}" varStatus="status">
                            <tr class="row${status.count % 2}">
                                <td><c:out value="${relationship.followed.code}" /></td>
                                <td><c:out value="${relationship.followed.name}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${relationship.followed.delete_flag == 1}">
                                            （削除済み）
                                        </c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='/employees/show?id=${relationship.followed.id}' />">詳細を表示</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <h2>いいねした日報　一覧</h2>
                <table id="report_list">
                    <tbody>
                        <tr>
                            <th class="report_name">氏名</th>
                            <th class="report_date">日付</th>
                            <th class="report_title">タイトル</th>
                            <th class="report_likes">いいね数</th>
                            <th class="report_action">操作</th>
                        </tr>
                        <c:forEach var="like" items="${likes}" varStatus="status">
                            <tr class="row${status.count % 2}">
                                <td class="report_name"><c:out value="${like.report.employee.name}" /></td>
                                <td class="report_date"><fmt:formatDate value='${like.report.report_date}' pattern='yyyy-MM-dd' /></td>
                                <td class="report_title"><c:out value="${like.report.title}" /></td>
                                <td class="report_likes"><c:out value="${like.report.report_liked}" /></td>
                                <td class="report_action"><a href="<c:url value='/reports/show?id=${like.report.id}' />">詳細を見る</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/employees/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>