package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//根据关键字查询歌单

// 加注解
@WebServlet("/findMusic")
public class FindMusicServlet extends HttpServlet {
/* *  list.html
*  url: "/findMusic",
*  type:"get",
* data:{musicName:musicName},
* dataType: "json",
*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        String musicName = req.getParameter("musicName");
        MusicDao musicDao = new MusicDao();
        List<Music> musicList = new ArrayList<>();
        if(musicName != null) {
            musicList = musicDao.ifMusic(musicName);  //根据关键字查询歌单
        }else {
            // 如果 musicName == null ,则查询所有
            musicList = musicDao.findMusic();
        }
        // musicList 中的内容返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musicList);
    }
}
