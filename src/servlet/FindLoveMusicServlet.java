package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 查询所有喜欢的音乐
// list.html  <a class="btn btn-primary" href="/loveMusic.html">喜欢列表</a>
// url: "/findLoveMusic",
//   type:"get",
//   data:{loveMusicName:loveMusicName},
@WebServlet("/findLoveMusic")
public class FindLoveMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8");
        String loveMusicName = req.getParameter("loveMusicName");
        MusicDao musicDao = new MusicDao();
        User user = (User)req.getSession().getAttribute("user");   //req.getSession().getAttribute("user")得到的是Object类型。强制类型转换为User
        int user_id = user.getId();
        List<Music> musicList = new ArrayList<>();
        if(loveMusicName != null) {
            musicList = musicDao.ifMusicLove(loveMusicName,user_id); // 根据关键字查询喜欢的歌单
        }else {  // loveMusicName==Null ,默认查找全部音乐
            musicList = musicDao.findLoveMusic(user_id);
        }
        // musicList 中的内容返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musicList);
    }
}
