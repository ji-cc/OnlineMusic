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
    public List<Music> findMusic() {
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
    public Music findMusicById (int id) {
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
    public List<Music> ifMusic(String str) {
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
    public int insert(String title, String singer, String time, String url,
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

    /**
     * 删除歌曲：
     */
    public int deleteMusicById(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
       // ResultSet rs = null;    // 删除操作，不需要结果集
        try {
            String sql = "delete from music where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            int ret = ps.executeUpdate(); //删除成功，返回值为1
            // 若删除成功，还要判断这首歌是否被添加到lovemusic表单中
            // 若lovemusic表单中有这首歌，就将这首歌从lovemusic中删除
            if(ret == 1) {
//                // 判断中间表是否有这个数据
//                if(findLoveMusicOnDel(id)) {
//                    // if语句进来，说明LoveMusic中有这条数据，需要删除
//                    int ret2 = removeLoveMusicOnDel(id); // 若ret2为1，说明删除成功
//                    if(ret2 == 1) {
//                        return 1; // lovemusic表中的歌单删除成功
//                    }
//                }
                return 1;  // 从music表单中删除成功
            }
        }catch (SQLException e) {
            e.printStackTrace();

        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return 0;
    }




    /**
     * 添加喜欢的音乐的时候，需要先判断该音乐是否存在
     * @param musicID
     * @return
     * */
    public boolean findMusicByMusicId(int user_id,int musicID) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "select * from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            ps.setInt(2,musicID);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return false;
    }


    /**
     * 添加音乐到“喜欢”列表中
     * 用户-》音乐
     * 多对多
     * 需要中间表
     */
    public boolean insertLoveMusic (int userId, int musicId) {
        Connection connection = null;
        PreparedStatement ps = null;
        // 没有用到 ResultSet
        try {
            String sql = "insert into lovemusic(user_id,music_id) value (?,?)";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,musicId);
            int ret= ps.executeUpdate();
            // 判断是否插入成功
            if (ret == 1) {
                // 说明插入成功
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);  // 此时的rs为null
        }
        return false; // 插入失败返回0
    }

    /**
     * @param userId 用户id
     * @param musicId 歌曲id
     * @return 返回受影响的行数
     * 移除当前用户喜欢的这首音乐，因为同一首音乐可能多个用户喜欢
     */
    public int removeLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            String sql = "delete from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,musicId);
            int ret = ps.executeUpdate();
            if(ret == 1) {
                return ret;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return 0;
    }

    /**
     * 查询用户喜欢的全部歌单
     * 返回值：List<Music>
     * @param user_id
     * @return
     */
    public List<Music> findLoveMusic(int user_id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Music> musicList = new ArrayList<>();
        try { // 需要的是根据userId得到music的全部信息
            String sql = "select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
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
        * 根据关键字查询喜欢的歌单
        * @param str
        * @return
        */
    public List<Music> ifMusicLove(String str,int user_id){
        List<Music> musics = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //ps = conn.prepareStatement("select*from music where title like '%"+str+"%'");
            ps = connection.prepareStatement("select m.id as music_id,title,singer,time,url,userid from lovemusic " +
                    "lm,music m where lm.music_id=m.id and user_id=? and  title like '%"+str+"%'");
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while(rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, rs);
        }
        return musics;
    }




    // 测试   // 测试的时候要在方法名前加 static
//    public static void main(String[] args) {
//        // 根据关键字查询用户喜欢的音乐
//        List<Music> musicList = ifMusicLove("铁",1);
//        System.out.println(musicList);

//        // 查询用户喜欢的音乐
//        List<Music> musicList = findLoveMusic(1);
//        System.out.println(musicList);

//        // 查询全部歌单
//        List<Music> musicList = findMusic();
//        System.out.println(musicList);
//        // [Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}]
//
//        // 通过id查询歌单
//        Music music = findMusicById(1);
//        System.out.println(music);  // Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}
//
//        //根据关键字查询歌单
//        List<Music> musicList1 = ifMusic("南方");
//        System.out.println(musicList1);  // [Music{id=1, title='南方姑娘', singer='赵雷', time=2020-07-21, url='music南方姑娘', userid=1}]
//
//        // 上传音乐
//        Insert("铁锤妹妹","c","2021-03-20","music\\铁锤妹妹",1);
//
//        // 添加音乐到“喜欢”列表中
//        System.out.println(insertLoveMusic(1,2));
//    }
}
