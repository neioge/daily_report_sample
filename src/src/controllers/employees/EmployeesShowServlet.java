package src.controllers.employees;

import java.io.IOException;
import java.util.List;

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
import src.models.Relationship;
import src.utils.DBUtil;

@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmployeesShowServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        Boolean checkSameRelationshipFlag = false;
        Relationship r = null;
        try {
                r = em.createNamedQuery("checkRelationship", Relationship.class)
                      .setParameter("following", login_employee)
                      .setParameter("followed", e)
                      .getSingleResult();
        } catch(NoResultException ex) {}
        if(r != null) {
            checkSameRelationshipFlag = true;
        }

        List<Relationship> relationships = em.createNamedQuery("getMyAllRelationships" , Relationship.class)
                .setParameter("employee", login_employee)
                .getResultList();

        List<Like> likes = em.createNamedQuery("getAllLikes", Like.class)
                .setParameter("employee", login_employee)
                .getResultList();

        // 基本的に、このやり方は間違っている。データベースに保存していない、その場しのぎの方法。要修正。
        for (Like like : likes) {
            long liked_count =em.createNamedQuery("getReport'sLikeCount", Long.class)
                    .setParameter("report", like.getReport())
                    .getSingleResult();
            like.getReport().setReport_liked((int)liked_count);
        }

        em.close();

        request.setAttribute("employee", e);
        request.setAttribute("checkSameRelationshipFlag", checkSameRelationshipFlag);
        request.setAttribute("relationships", relationships);
        request.setAttribute("likes", likes);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
        rd.forward(request, response);
    }

}
