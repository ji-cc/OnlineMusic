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

// 删除选中的音乐
@WebServlet("/deleteSelMusicServlet")
public class DeleteSelMusicServlet extends HttpServlet {
    /* list.html
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
        int sum = 0;
        Map<String,Object> return_map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            int id = Integer.parseInt(values[i]); // 转换成整型
            Music music = musicDao.findMusicById(id);  // 先查找这首歌是否存在
            int delete = musicDao.deleteMusicById(id);
            if(delete == 1) {
                File file = new File("F:\\编程\\bite资料\\项目\\OnlineMusic_practice\\web\\music"+music.getUrl()+".mp3");
                if (file.delete()) {
                    sum += delete;
                }else {
                    return_map.put("msg",false);
                    System.out.println("服务器删除失败");
                }
            }else {
                return_map.put("msg",false);
                System.out.println("数据库中数据删除失败");
            }
        }
        if(sum == values.length) { // 说明这个数组里面每一个数据都删完了
            return_map.put("msg",true);
        }else {
            return_map.put("msg",false);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
