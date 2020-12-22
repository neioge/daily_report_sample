package src.controller.timeline;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/timeline")
public class TimelineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public TimelineServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        List<Report> timeline = em.createNamedQuery("getMyTimeline", Report.class)
                                        .setParameter("employee", login_employee)
                                        .setFirstResult(15 * (page - 1))
                                        .setMaxResults(15)
                                        .getResultList();
        // 基本的にこのやり方は間違っている。要修正。
        for (Report report : timeline) {
            long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                    .setParameter("report", report)
                    .getSingleResult();
            report.setReport_liked((int)liked_count);
        }
        long timeline_count = (long)em.createNamedQuery("getMyTimelineCount", Long.class)
                                        .setParameter("employee", login_employee)
                                        .getSingleResult();

        em.close();
        request.setAttribute("page", page);
        request.setAttribute("timeline_count", timeline_count);
        request.setAttribute("timeline", timeline);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/timeline/timeline.jsp");
        rd.forward(request, response);
    }

}
