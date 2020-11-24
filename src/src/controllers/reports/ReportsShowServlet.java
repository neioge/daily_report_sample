package src.controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Like;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ReportsShowServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        long likes_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                             .setParameter("report", r)
                             .getSingleResult();
        Boolean checkSameLikeFlag = false;
        Like l = null;
        try {
                l = em.createNamedQuery("checkLikeEmployeeAndReport", Like.class)
                      .setParameter("employee", (Employee)request.getSession().getAttribute("login_employee"))
                      .setParameter("report", r)
                      .getSingleResult();
        } catch(NoResultException ex) {}
        if(l != null) {
            checkSameLikeFlag = true;
        }
        em.close();
        request.setAttribute("report", r);
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("likes_count", likes_count);
        request.setAttribute("checkSameLikeFlag", checkSameLikeFlag);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }
}
