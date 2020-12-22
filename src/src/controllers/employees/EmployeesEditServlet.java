package src.controllers.employees;

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

@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public EmployeesEditServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        List<Relationship> activeRelationships = em.createNamedQuery("getAllFollowing" , Relationship.class)
                .setParameter("following", e)
                .getResultList();
        em.close();
        request.setAttribute("employee", e);
        request.setAttribute("_token", request.getSession().getId());
        request.getSession().setAttribute("employee_id", e.getId());
        request.setAttribute("activeRelationships", activeRelationships);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        rd.forward(request, response);
    }
}