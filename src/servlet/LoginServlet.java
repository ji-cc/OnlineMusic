package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 加注解 可以让人更快的知道来到哪个路径下
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // login.html:data:{"username":username,"password":password},
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println(username + "" + password);

    }
}
