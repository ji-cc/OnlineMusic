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

// 添加喜欢的音乐
@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
/*
 function loveInfo(obj) {
            console.log(obj);
            $.ajax({
                url:"/loveMusicServlet",
                type: "post",
                //发送给后端的数据
                data: {"id": obj},
 */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8");
        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);
        User user = (User)req.getSession().getAttribute("user");  // 要获取 user_id
        int user_id = user.getId();
        //先判断当前这首歌是否已经被添加到喜欢的音乐中
        MusicDao musicDao = new MusicDao();
        Map<String,Object> return_map = new HashMap<>();

        if(musicDao.findMusicByMusicId(user_id,musicId)) {
            // 如果为真，说明以前这首歌被添加过为喜欢的音乐,不能被重复添加
            return_map.put("msg",false);
        }else {
            // 此时把这首歌放到喜欢的音乐中
            boolean flg = musicDao.insertLoveMusic(user_id, musicId);
            if(flg) {
                // 如果flg为真，说明添加成功
                return_map.put("msg",true);
            }else {
                return_map.put("msg",false);
            }
        }
        // 如何将 return_map 返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), return_map);
    }
}
