package dao;

import entity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 有关音乐的数据库操作
public class MusicDao {

    // 查询全部歌单
    public static List<Music> findMusic() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List musicList = new ArrayList();
        try {
            String sql = "select * from music";
            connection=DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();  // 执行sql语句，结果集放入rs中
            while(rs.next()) {  // rs.next()不为空，说明查到了结果
                Music music = new Music(); // 创建Music类
                // 循环rs的每条结果，给每个结果设置属性
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                // 把music存到一个列表中
                musicList.add(music);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return musicList;
    }


    // 根据 id  查找音乐
    public static Music findMusicById (int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Music music = null;  // 最后要返回一个 Music 类

        try {
            String sql = "select * from music where id = ?;";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            // sql 语句预编译之后要设置它的值
            ps.setInt(1,id);
            // 设置之后要执行 sql 语句
            rs = ps.executeQuery(); // 结果集放入rs中

            // 根据id查找音乐，结果是一个音乐
            if(rs.next()) {  // 如果 rs.next() 不为空，说明查到了音乐
                // 设置rs结果的属性
                music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));

            }
        }catch (SQLException e) {
            e.printStackTrace();

        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return music;
    }


    // 测试
    public static void main(String[] args) {
        List<Music> musicList = findMusic();
        System.out.println(musicList);
        // [Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}]
        Music music = findMusicById(1);
        System.out.println(music);  // Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}
        
    }
}
