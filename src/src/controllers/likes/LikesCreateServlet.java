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

@WebServlet("/likes/create")
public class LikesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public LikesCreateServlet() {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("_createLike")));
            Like l = new Like();

            l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            l.setReport(r);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            l.setCreated_at(currentTime);
            l.setUpdated_at(currentTime);

            // index画面でいいね数を表示させるために用意したReportインスタンスのプロパティreport_likedを、１増やした最新の値に更新する。
            /* 案1
            Report r = (Report)request.getSession().getAttribute("report");
            int liked_count = r.getReport_liked();
            liked_count++;
            r.setReport_liked(liked_count);
             */

            /* 案2
             long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                    .setParameter("report", (Report)request.getSession().getAttribute("report"))
                    .getSingleResult();
            r.setReport_liked((int)liked_count);
            */

            em.getTransaction().begin();
            em.persist(l);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "いいねしました");
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}
