<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <c:if test="${flush != null}">
                    <div id="flush_success">
                        <c:out value="${flush}"></c:out>
                    </div>
                </c:if>

                <h2>${employee.name} の従業員情報　編集ページ</h2>
                <p>（パスワードは変更する場合のみ入力してください）</p>
                <form method="POST" action="<c:url value='/employees/update' />">
                    <c:import url="_form.jsp" />
                </form>

                <p><a href="#" onclick="confirmDestroy();">この従業員情報を削除する</a></p>
                <form method="POST" action="<c:url value='/employees/destroy' />">
                    <input type="hidden" name="_token" value="${_token}" />
                </form>
                <script>
                    function confirmDestroy() {
                        if(confirm("本当に削除してよろしいですか？")) {
                            document.forms[1].submit();
                        }
                    }
                </script>

                <h3>【${employee.name} がフォローしている従業員　一覧】</h3>
                <table id="employee_list">
                    <tbody>
                        <tr>
                            <th>社員番号</th>
                            <th>氏名</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach var="relationship" items="${activeRelationships}" varStatus="status">
                            <tr class="row${status.count % 2}">
                                <td><c:out value="${relationship.followed.code}" /></td>
                                <td><c:out value="${relationship.followed.name}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${relationship.followed.delete_flag == 1}">
                                            （削除済み）
                                        </c:when>
                                        <c:otherwise>
                                            <form method="POST" action="/daily_report_sample/relationships/manage">
                                            <input type = "hidden" name="following_id" value="${employee.id}" />
                                            <input type = "hidden" name="followed_id" value="${relationship.followed.id}" />
                                            <button class="buttonFollowed" type="submit"><span class="nomal">作成者をフォロー済み</span><span class="hover">フォローを解除する</span></button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
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