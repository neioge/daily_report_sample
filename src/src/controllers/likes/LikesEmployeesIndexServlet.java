package src.controllers.likes;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Like;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/likes/employees/index")
public class LikesEmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public LikesEmployeesIndexServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        int page = 1;
        try{
        page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }

        List<Like> likes = em.createNamedQuery("getAllLikesAboutTheReport", Like.class)
                .setParameter("report", r)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();
         long likes_count = (long)em.createNamedQuery("getReport'sLikeCount", Long.class)
                 .setParameter("report", r)
                 .getSingleResult();
         em.close();
         request.setAttribute("likes", likes);
         request.setAttribute("likes_count", likes_count);
         request.setAttribute("page", page);
         RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/likes/index.jsp");
         rd.forward(request, response);
    }

}
