package servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// 删除歌曲
// list.html
@WebServlet("/deleteServlet")
public class DeleteServlet extends HttpServlet {
    /*
    function deleteInfo(obj) {
            console.log(obj);
            $.ajax({
                url:"/deleteServlet",
                type: "post",
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        String idStr = req.getParameter("id");
        int id = Integer.parseInt("idStr");  // 转换成整型
        MusicDao musicDao = new MusicDao();
        // 删除之前先找一下这首歌是否存在
        Music music = musicDao.findMusicById(id);
        if (music == null) return;
        int delete = musicDao.deleteMusicById(id);
        Map<String,Object> return_map = new HashMap<>();
        if (delete == 1) {
            // 仅仅代表数据库删除，但是服务器上的音乐是否存在
            File file = new File("F:\\编程\\bite资料\\项目\\OnlineMusic_practice\\web\\music"+music.getUrl()+".mp3");
            if (file.delete()) {
                return_map.put("msg",true);
                System.out.println("服务器删除成功");
            }else {
                return_map.put("msg",false);
                System.out.println("服务器删除失败");
            }
        }else {
            return_map.put("msg",false);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

    }
}
