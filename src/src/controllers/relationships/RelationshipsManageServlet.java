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

@WebServlet("/relationships/manage")
public class RelationshipsManageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public RelationshipsManageServlet() {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee following = em.find(Employee.class , Integer.parseInt(request.getParameter("following_id")));
        Employee followed = em.find(Employee.class , Integer.parseInt(request.getParameter("followed_id")));

        Relationship r = em.createNamedQuery("checkRelationship", Relationship.class)
                .setParameter("following", following)
                .setParameter("followed" , followed)
                .getSingleResult();
        r.setDelete_flag(1);
        em.getTransaction().begin();
//      em.remove(r);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", followed.getName() + "を" + following.getName() + "のフォローから解除しました。");
        response.sendRedirect(request.getContextPath() + "/employees/edit?id=" + following.getId());
    }
}
