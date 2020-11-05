package src.controllers.relationships;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Relationship;
import src.utils.DBUtil;

@WebServlet("/relationships/destroy")
public class RelationshipsDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RelationshipsDestroyServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee e = em.find(Employee.class , Integer.parseInt(request.getParameter("_destroyRelationship")));

        Relationship r = em.createNamedQuery("checkRelationship", Relationship.class)
                .setParameter("following", (Employee)request.getSession().getAttribute("login_employee"))
                .setParameter( "followed" , e )
                .getSingleResult();

        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", e.getName() + "さんのフォローを解除しました。");

        response.sendRedirect(request.getContextPath() + "/employees/index");
    }

}
