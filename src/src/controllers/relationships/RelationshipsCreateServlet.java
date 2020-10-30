package src.controllers.relationships;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Relationship;
import src.utils.DBUtil;

@WebServlet("/relationships/create")
public class RelationshipsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RelationshipsCreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Relationship r = new Relationship();
        r.setFollowing((Employee)request.getSession().getAttribute("login_employee"));
        r.setFollowed((Employee)request.getSession().getAttribute("checking_employee"));
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        r.setCreated_at(currentTime);
        r.setUpdated_at(currentTime);

        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
        em.close();
        Employee e = (Employee)request.getSession().getAttribute("checking_employee");
        request.getSession().setAttribute("flush", e.getName() + "をフォローしました");

        request.getSession().removeAttribute("checking_employee");
        response.sendRedirect(request.getContextPath() + "/employees/index");

    }

}
