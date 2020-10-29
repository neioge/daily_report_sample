package src.controllers.likes;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Like;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/likes/update")
public class LikesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public LikesUpdateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.CSRF対策
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            // 今回は、レポートのIDをセッションで取得してインスタンス化して、ライクのプロパティに代入したけど、ほかの方法としてReportShowServletの時点で日報ごとセッションに入れておく、という方法もある。そっちのほうがこちらのコードはすっきりする気がする。
            // いや、その方法だと、もしいいねボタンを押さなかった場合にセッションを消す処理がないためあまりよくないと思う。ReportsShowServletからレポートIDを渡すこちらが無難だと思う。
            // いや、それを言ったらIDも同じか。別に入りっぱなしでも、別のレポートを参照すればセッションスコープが更新されるから問題ないか。じゃないや、最後にリムーブするから。ということでなおしてみる。

            // いいねインスタンスを作成する。
            Like like = new Like();

            // いいねインスタンスのプロパティに、4つの情報をセットする。
            like.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            like.setReport((Report)request.getSession().getAttribute("report"));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            like.setCreated_at(currentTime);
            like.setUpdated_at(currentTime);

            // データベースに接続し、上記の変更点をコミットする。
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "いいねしました");

            // indexにリダイレクトする。
            request.getSession().removeAttribute("report");
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}
