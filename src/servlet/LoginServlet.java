package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 登录
// 加注解  url:"/loginServlet", 与前端代码相关联
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    /*  login.html
    $("#submit").click(function () {
	var username=$("#user").val();
	var password=$("#password").val();
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.setCharacterEncoding("utf-8");  //   请求体的编码
       resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        // login.html:data:{"username":username,"password":password},
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        UserService userService = new UserService();
        User user = userService.login(loginUser);  // login 的返回值时User
       // 登录成功，需要响应一个true
        // 所有定义一个map表
        Map<String,Object> return_map = new HashMap<>();
       //     msg   true/字符串

        // 如果登录失败，user == null
        if (user != null) {
            System.out.println("登录成功");
            // 登录成功后，记录当前用户的session信息
            req.getSession().setAttribute("user",user);  // 绑定数据
            return_map.put("msg",true);

        } else {
            System.out.println("登录失败");
            return_map.put("msg",false);
        }
        // 如何将 return_map 返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

//        System.out.println(username + "" + password);

    }
}
