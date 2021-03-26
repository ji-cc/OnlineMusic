package service;

import dao.MusicDao;
import entity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MusicService {

    public  int deleteMusicById(int id) {  //自己写
        // 在这里面可以将其他逻辑嵌套进去，因为这本身就是逻辑
        MusicDao musicDao = new MusicDao();
        if(musicDao.deleteMusicById(id) == 1){
            if(findLoveMusicOnDel(id)) {
                // if语句进来，说明LoveMusic中有这条数据，需要删除
                int ret2 = removeLoveMusicOnDel(id); // 若ret2为1，说明删除成功
                if(ret2 == 1) {
                    return 1; // lovemusic表中的歌单删除成功
                }
            }
            return 1;  // 执行到这一步，表示这首歌没有被添加到lovemusic这张表中，且在music表单中删除成功
        }
        return 0;
    }

    // 根据id在LoveMusic表单中查询
    private boolean findLoveMusicOnDel(int musicId) {  // music 表单中的id，对应于lovemusic表单中的music_id，所以应该上传musicId
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,musicId);
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
    // 根据id删除LoveMusic表单中的歌单
    private int removeLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String sql = "delete from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            int ret = ps.executeUpdate();
            if(ret == 1) {
                return 1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return 0;
    }

    public List<Music> findMusic() {
        MusicDao musicDao = new MusicDao();
        List<Music> musicList = musicDao.findMusic();
        return musicList;
    }
}
