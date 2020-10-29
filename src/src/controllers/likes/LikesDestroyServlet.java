package src.controllers.likes;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.Like;
import src.models.Report;
import src.utils.DBUtil;

@WebServlet("/likes/destroy")
public class LikesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LikesDestroyServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            Employee e = (Employee)request.getSession().getAttribute("login_employee");
            Report r = (Report)request.getSession().getAttribute("report");

            Like l = em.createNamedQuery("checkLikeEmployeeAndReport", Like.class)
                    .setParameter("employee", e)
                    .setParameter("report", r)
                    .getSingleResult();

            em.getTransaction().begin();
            em.remove(l);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "いいねを取り消しました。");

            request.getSession().removeAttribute("report");
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}
