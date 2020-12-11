<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <h2>${employee.name} の従業員情報　詳細ページ</h2>

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
                                <button class="buttonFollowed" type="submit"><span class="nomal">フォロー済み</span><span class="hover">フォロー解除</span></button>
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

                <ul class="tab-menu">
                    <li><a href="#" class="active" data-id="reportsIndex">作成した日報</a></li>
                    <li><a href="#" data-id="followingIndex">フォロー</a></li>
                    <li><a href="#" data-id="followedIndex">フォロワー</a></li>
                    <li><a href="#" data-id="likesIndex" >いいねした日報</a></li>
                </ul>

                <section class="content active" id="reportsIndex">
                    <table id="report_list">
                        <tbody>
                            <tr>
                                <th class="report_name">氏名</th>
                                <th class="report_date">日付</th>
                                <th class="report_title">タイトル</th>
                                <th class="report_likes">いいね数</th>
                                <th class="report_action">操作</th>
                            </tr>
                            <c:forEach var="report" items="${reports}" varStatus="status">
                                <tr class="row${status.count % 2}">
                                    <td class="report_name"><c:out value="${report.employee.name}" /></td>
                                    <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                                    <td class="report_title"><c:out value="${report.title}" /></td>
                                    <td class="report_likes"><c:out value="${report.report_liked}" /></td>
                                    <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </section>

                <section class="content" id="followingIndex">
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
                                               <a href="<c:url value='/employees/show?id=${relationship.followed.id}' />">詳細を表示</a>
                                           </c:otherwise>
                                       </c:choose>
                                   </td>
                               </tr>
                           </c:forEach>
                      </tbody>
                    </table>
                </section>

                <section class="content" id="followedIndex">
                    <table id="employee_list">
                        <tbody>
                            <tr>
                                <th>社員番号</th>
                                <th>氏名</th>
                                <th>操作</th>
                            </tr>
                            <c:forEach var="relationship" items="${passiveRelationships}" varStatus="status">
                                <tr class="row${status.count % 2}">
                                    <td><c:out value="${relationship.following.code}" /></td>
                                    <td><c:out value="${relationship.following.name}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${relationship.followed.delete_flag == 1}">
                                        （削除済み）
                                        </c:when>
                                        <c:otherwise>
                                           <a href="<c:url value='/employees/show?id=${relationship.following.id}' />">詳細を表示</a>
                                        </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </section>

                <section class="content" id="likesIndex">
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
                </section>

            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <script type="text/javascript" charset="UTF-8">
        'use strict';
        {
            const menuItems = document.querySelectorAll('.tab-menu li a');
            const contents = document.querySelectorAll('.content');

            menuItems.forEach(clickedItem => {
              clickedItem.addEventListener('click', e => {
                e.preventDefault();

                menuItems.forEach(item => {
                    item.classList.remove('active');
                })
                clickedItem.classList.add('active');

                contents.forEach(content => {
                    content.classList.remove('active');
                });
                document.getElementById(clickedItem.dataset.id).classList.add('active');
              });
            });
        }
        </script>

        <p><a href="<c:url value='/employees/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>