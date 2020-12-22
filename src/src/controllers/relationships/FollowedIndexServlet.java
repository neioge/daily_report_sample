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

@WebServlet("/followed/index")
public class FollowedIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public FollowedIndexServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }

        List<Relationship> passiveRelationships = em.createNamedQuery("getAllFollowed" , Relationship.class)
                        .setParameter("followed", login_employee)
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();
        List<Relationship> activeRelationships = em.createNamedQuery("getAllFollowing" , Relationship.class)
                        .setParameter("following", login_employee)
                        .getResultList();
        long followed_count = (long)em.createNamedQuery("getFollowedCount", Long.class)
                                       .setParameter("followed", login_employee)
                                       .getSingleResult();
        em.close();

        request.setAttribute("passiveRelationships", passiveRelationships);
        request.setAttribute("activeRelationships", activeRelationships);
        request.setAttribute("followed_count", followed_count);
        request.setAttribute("page", page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/relationships/followed.jsp");
        rd.forward(request, response);
    }

}
