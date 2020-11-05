package src.controllers.likes;

import java.io.IOException;

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

@WebServlet("/likes/destroy")
public class LikesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LikesDestroyServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            Like l = em.createNamedQuery("checkLikeEmployeeAndReport", Like.class)
                    .setParameter("employee", (Employee)request.getSession().getAttribute("login_employee"))
                    .setParameter("report" , em.find(Report.class , Integer.parseInt(request.getParameter("_destroyLike"))) )
                    .getSingleResult();

            // index画面でいいね数を表示させるために用意したReportインスタンスのプロパティreport_likedを、１減らした最新の値に更新する。
            /*
            Report r = (Report)request.getSession().getAttribute("report");
            int liked_count = r.getReport_liked();
            liked_count--;
            r.setReport_liked(liked_count);
            */

            /*
            long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                    .setParameter("report", (Report)request.getSession().getAttribute("report"))
                    .getSingleResult();
            Report r = (Report)request.getSession().getAttribute("report");
            r.setReport_liked((int)liked_count);
            */

            em.getTransaction().begin();
            em.remove(l);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "いいねを取り消しました。");

            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}
