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
        List<Music> musicList = new ArrayList<>();
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
            String sql = "select * from music where id = ?";
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

    //根据关键字查询歌单
    // str 代表歌名
    public static List<Music> ifMusic(String str) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Music> musicList = new ArrayList<>();
        try {
            String sql = "select * from music where title LIKE '%" + str + "%'";   // sql语句：select * from music where title LIKE '%str%';
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musicList.add(music);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return musicList;
    }

    /**
     * 上传音乐
     * 1.可以不给这么多参数
     * 2.其实可以给一个music对象
     *
     * a1:上传文件本身给服务器
     *a2:将音乐信息，插入到数据库当中，此时做的就是这一步
     */
    public static int Insert(String title, String singer, String time, String url,
                      int userid) {
        Connection connection = null;
        PreparedStatement ps = null;
        // 没有用到 ResultSet

        try {
            String sql = "insert into music(title,singer,time,url,userid) value (?,?,?,?,?)";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1,title);
            ps.setString(2,singer);
            ps.setString(3,time);
            ps.setString(4,url);
            ps.setInt(5,userid);
            // inset 属于更新数据库，要用executeUpdate()
            // executeUpdate()的返回值是Int类型,表示受影响的行数
            // 此时插入了1条数据，应该返回1，若返回值为0，说明插入失败
            int ret= ps.executeUpdate();
            // 判断是否插入成功
            if (ret == 1) {
                // 说明插入成功
                return 1;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);  // 此时的rs为null
        }
        return 0; // 插入失败返回0
    }

    // 删除


    // 测试
    public static void main(String[] args) {

        // 查询全部歌单
        List<Music> musicList = findMusic();
        System.out.println(musicList);
        // [Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}]

        // 通过id查询歌单
        Music music = findMusicById(1);
        System.out.println(music);  // Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}

        //根据关键字查询歌单
        List<Music> musicList1 = ifMusic("南方");
        System.out.println(musicList1);  // [Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}]

        // 上传音乐
        Insert("铁锤妹妹","c","2021-03-20","music\\铁锤妹妹",1);
    }
}
