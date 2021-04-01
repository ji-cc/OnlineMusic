package servlet;

import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

// 将音乐写入数据库中
@WebServlet("/uploadsucess") // <form method="POST" action="/uploadsucess">
public class UploadInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");  //   请求体的编码
        resp.setContentType("application/json;charset=utf-8"); // 响应体的编码
        String singer = req.getParameter("singer");  // uploadsucess.html 中上传了singer
        // 在 UploadMusicServlet 中已经将fileName写入到session中
        // 此处将fileName从session中拿出来
        String fileName = (String)req.getSession().getAttribute("fileName");  //得到的是Object类型，所有需要类型转换
        // 这里得到的字符串是  title.mp3   但是插入数据库中只需要title
        // 以.号分割，只取前面部分   分割完成后是一个数组
        String[] strings = fileName.split("\\.");  // 点号在正则表达式中有特殊含义，应加转义字符\\再分割
        String title = strings[0]; //只取title部分
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //2020-03-31
        String time = sdf.format(new Date());  // 把当前日期格式化，通过字符串来接收
        User user = (User)req.getSession().getAttribute("user");  // 要获取 user_id
        int user_id = user.getId();
        // 要获取url,可以通过字符串来拼接
        String url = "music/"+title;
        MusicDao musicDao = new MusicDao();
        int ret = musicDao.insert(title, singer, time, url, user_id);
        if (ret == 1) {
            resp.sendRedirect("list.html");  //插入成功，返回歌单页面
        }

    }
}
