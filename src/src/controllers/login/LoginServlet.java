package src.controllers.login;

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
import src.utils.DBUtil;
import src.utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());
        // 一発目のログインを想定している。エラーはないので、ビューにエラーリストを表示しないようにするため偽をビューに渡す
        request.setAttribute("hasError", false);
        // ログアウトコントローラからリダイレクトされた場合、「ログアウトしました」というメッセージがフラッシュに入っている
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        //　ログイン画面へフォワード
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 認証結果を格納する変数
        Boolean check_result = false;

        String code = request.getParameter("code");
        String plain_pass = request.getParameter("password");

        Employee e = null;

        //　社員番号とパスワードが空白ではない
        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            EntityManager em = DBUtil.createEntityManager();

            //　データベースに保存されているパスワードはハッシュ化されているので、比較するためにプレインパスもハッシュ化
            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String)this.getServletContext().getAttribute("pepper")
                    );

            // 社員番号とパスワードが正しいかチェックする　DTOのNamedQueryを使う
            try {
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                      .setParameter("code", code)
                      .setParameter("pass", password)
                      .getSingleResult();
            } catch(NoResultException ex) {}

            em.close();

            //　従業員インスタンスがちゃんとある、つまりデータベースに一致するものがあった場合、「認証結果を格納する変数」を真にする
            if(e != null) {
                check_result = true;
            }
        }

        if(!check_result) {
            // 認証できなかったらログイン画面に戻る
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("code", code);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);
        } else {
            // 認証できたらログイン状態にしてトップページへリダイレクト

            //　ログイン機能において一番重要な行。このセッションスコープにインスタンスが入っている情報をもってログイン状態とする
            request.getSession().setAttribute("login_employee", e);

            request.getSession().setAttribute("flush", "ログインしました。");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

}