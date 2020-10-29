package src.controllers.reports;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.models.Report;

@WebServlet("/reports/new")
public class ReportsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ReportsNewServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());

        // 日報の日時は、事前に本日の日付を取得して格納しています。今日の日報を新規で登録する際、すでに今日の日付が入力欄に入っていた方が利用者にとって便利だからです。
        Report r = new Report();
        r.setReport_date(new Date(System.currentTimeMillis()));
        request.setAttribute("report", r);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
        rd.forward(request, response);
    }

}
