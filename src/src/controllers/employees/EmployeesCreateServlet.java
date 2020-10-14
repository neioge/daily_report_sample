package src.controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Employee;
import src.models.validators.EmployeeValidator;
import src.utils.DBUtil;
import src.utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // CSRF対策として、トークンを宣言・スコープから取得
        String _token = (String)request.getParameter("_token");

        //　CSRF対策
        if(_token != null && _token.equals(request.getSession().getId())) {

            //　データベース接続の用意
            EntityManager em = DBUtil.createEntityManager();

            //　従業員インスタンスを宣言　ニューコントローラで一度インスタンスを作成しているが、このコントローラで受け取るようとして用意する宣言する必要がある
            Employee e = new Employee();

            //　用意した従業員インスタンスに、ニュービュー（フォームビュー）から送信されたパラメータを格納
            e.setCode(request.getParameter("code"));
            e.setName(request.getParameter("name"));
            //　生のパスワードでデータベースに入れられないので、ハッシュ化してから格納
            e.setPassword(
                EncryptUtil.getPasswordEncrypt(
                    request.getParameter("password"),
                        (String)this.getServletContext().getAttribute("pepper")
                    )
                );
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            //　日付情報を格納する
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            e.setDelete_flag(0);

            //　バリデーションを行い、戻り値をエラーリストに代入
            List<String> errors = EmployeeValidator.validate(e, true, true);

            //　エラーリストに要素があれば、データベースに保存はせず、トークン・従業員インスタンス・エラーリストをニュービューへ送る
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
                rd.forward(request, response);

            //　エラーがなければ、従業員インスタンスをデータベースへ保存する
            } else {
                em.getTransaction().begin();
                em.persist(e);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "登録が完了しました。");
                em.close();

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
