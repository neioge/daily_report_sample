<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>いいね数</th>
                            <td><c:out value="${ likes_count }" /></td>
                        </tr>
                        <tr>
                            <th>承認状態</th>
                            <td>
                                <c:choose>
                                    <c:when test="${ report.approval }">
                                    承認済み
                                    </c:when>
                                    <c:otherwise>
                                    未承認
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>
                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                    <c:choose>
                        <c:when  test="${ checkSameLikeFlag }">
                            <form method="POST" action="/daily_report_sample/likes/destroy">
                                <input type = "hidden" name="_destroyLike" value="${report.id}" />
                                <button class="buttonLiked" type="submit"><span class="nomal">いいね済み</span><span class="hover">いいね取消</span></button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="/daily_report_sample/likes/create">
                                <input type = "hidden" name="_createLike" value="${report.id}" />
                                <button class = "buttonLike" type="submit">いいね</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${sessionScope.login_employee.admin_flag > 0}">
                    <c:if test="${report.approval == false}">
                        <form method="POST" action="${pageContext.request.contextPath}/reports/approve">
                                <input type = "hidden" name="_token" value="${_token }" />
                                <input type = "hidden" name="_approveReport" value="${report.id}" />
                                <button class="buttonApproval" type="submit">承認する</button>
                        </form>
                    </c:if>
                </c:if>

                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                    <c:choose>
                        <c:when  test="${ checkSameRelationshipFlag }">
                            <form method="POST" action="/daily_report_sample/relationships/destroy">
                                <input type = "hidden" name="_destroyRelationship" value="${report.employee.id}" />
                                <button class="buttonFollowed" type="submit"><span class="nomal">作成者をフォロー済み</span><span class="hover">フォローを解除する</span></button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="/daily_report_sample/relationships/create">
                                <input type = "hidden" name="_createRelationship" value="${report.employee.id}" />
                                <button type="submit">作成者をフォロー</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>


            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>