package src.controllers.employees;

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

        Boolean checkSameRelationshipFlag = false;
        Relationship r = null;
        try {
                r = em.createNamedQuery("checkRelationship", Relationship.class)
                      .setParameter("following", (Employee)request.getSession().getAttribute("login_employee"))
                      .setParameter("followed", e)
                      .getSingleResult();
        } catch(NoResultException ex) {}
        if(r != null) {
            checkSameRelationshipFlag = true;
        }

        em.close();

        request.setAttribute("employee", e);

        // relationshipCreateとrelationshipDestroyで参照中の従業員を扱うために、セッションにセットする。
        request.getSession().setAttribute("checking_employee", e);

        // フラグをビューへ渡す。
        request.setAttribute("checkSameRelationshipFlag", checkSameRelationshipFlag);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
        rd.forward(request, response);
    }

}
