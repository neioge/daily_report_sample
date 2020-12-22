package src.controllers.relationships;

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
import src.models.Relationship;
import src.utils.DBUtil;

@WebServlet("/following/index")
public class FollowingIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public FollowingIndexServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }

        List<Relationship> activeRelationships = em.createNamedQuery("getAllFollowing" , Relationship.class)
                        .setParameter("following", login_employee)
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();
        long following_count = (long)em.createNamedQuery("getFollowingCount", Long.class)
                                       .setParameter("following", login_employee)
                                       .getSingleResult();
        em.close();

        request.setAttribute("activeRelationships", activeRelationships);
        request.setAttribute("following_count", following_count);
        request.setAttribute("page", page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/relationships/following.jsp");
        rd.forward(request, response);
    }

}
