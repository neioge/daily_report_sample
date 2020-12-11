package controllers.toppage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Relationship;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public TopPageIndexServlet() {
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
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();
        for (Report report : reports) {
            long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                    .setParameter("report", report)
                    .getSingleResult();
            report.setReport_liked((int)liked_count);
        }
        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();

        List<Report> timeline = new ArrayList<Report>();
        List<Report> allReports = em.createNamedQuery("getAllReports", Report.class)
                                     .getResultList();
        List<Relationship> activeRelationships = em.createNamedQuery("getAllFollowing" , Relationship.class)
                                     .setParameter("following", login_employee)
                                     .getResultList();

        for (Report report: allReports){
            for (Relationship relationship: activeRelationships){
                  if(relationship.getFollowed().getId() == report.getEmployee().getId()){
                      long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                                            .setParameter("report", report)
                                            .getSingleResult();
                      report.setReport_liked((int)liked_count);
                      timeline.add(report);
                  }
            }
        }

        em.close();
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        request.setAttribute("timeline", timeline);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}