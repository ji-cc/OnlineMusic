package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 移除喜欢的音乐
//url:"/removeLoveServlet",
//                type: "post",
//                //发送给后端的数据
//                data: {"id": obj},
@WebServlet("/removeLoveServlet")
public class RemoveLoveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);
        User user = (User)req.getSession().getAttribute("user");   //req.getSession().getAttribute("user")得到的是Object类型。强制类型转换为User
        int user_id = user.getId();
        MusicDao musicDao = new MusicDao();
        int ret = musicDao.removeLoveMusic(user_id,musicId);
        Map<String,Object> return_map = new HashMap<>();
        if(ret == 1) { // 删除成功
            return_map.put("msg",true);
        }else { // 删除失败
            return_map.put("msg",false);
        }
        // 如何将 return_map 返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
