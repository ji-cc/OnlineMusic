package servlet;

import dao.MusicDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 删除选中的音乐
public class DeleteSelMusicServlet extends HttpServlet {
    /*
    url:"/deleteSelMusicServlet",
                        //将id数组，发送给deleteSelectedServlet
                        data:{"id":id},
                        type: "POST",
                        dataType:"json",
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        // values 数组中存放的是需要删除的歌曲id
        String[] values = req.getParameterValues("id[]");
        MusicDao musicDao = new MusicDao();
        for (int i = 0; i < values.length; i++) {
            int id = Integer.parseInt(values[i]);
            
        }

    }
}
