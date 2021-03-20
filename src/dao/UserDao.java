package dao;

import entity.User;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 有关用户的数据库操作
public class UserDao {
    // 登录
    public static User login(User loginUser){
        User user = null;
        Connection connection = null;   // 获取连接
        PreparedStatement ps = null;     // 预编译sql语句
        ResultSet rs = null;      // 从数据库拿到的结果集的集合


        try {
            String sql = "select * from user where  username =? and password=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);  // 对sql语句的预编译，有这个语句的话，填写sql语句时会有提示
            ps.setString(1,loginUser.getUsername());  // 给sql语句中的第一个参数传参
            ps.setString(2,loginUser.getPassword());  // 给sql语句中的第二个参数传参

            // 执行sql语句
            rs = ps.executeQuery();
            if(rs.next()) { // 用户存在，就会返回用户的id  name  password
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs); //创建的连接要关闭
        }
        return user;  // 若找不到user,就进不去if语句，返回的是null。
    }

    // 测试
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("bit");
        user.setPassword("123");
        User loginUser = login(user);
        System.out.println(loginUser);
        // User{id=1, username='bit', password='123', gender='男', age=10, email='1262913815@qq.com'}
    }

}

