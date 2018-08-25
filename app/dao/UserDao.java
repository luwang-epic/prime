package dao;

import model.User;
import org.springframework.dao.DataAccessException;
import utils.JdbcUtil;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static final String INSERT_UESR = "INSERT ignore into users(username, password, alias) VALUES(:username, :password, :alias)";

    private static final String UPDATE_USER_ALIAS = "UPDATE users set alias = :alias WHERE id = :id";

    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=:id";



    public static void insert(User user){
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("alias", user.getAlias());

        JdbcUtil.mdmpNamedJdbcTemplate.update(INSERT_UESR, params);
    }

    public static void updateAlias(int id, String alias){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("alias", alias);

        JdbcUtil.mdmpNamedJdbcTemplate.update(UPDATE_USER_ALIAS, params);
    }


    public static User getUserById(int id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        Map<String, Object> result = JdbcUtil.mdmpNamedJdbcTemplate.queryForMap(SELECT_USER_BY_ID, params);

        User user = new User();
        user.setId(id);
        user.setUsername(result.get("username").toString());
        user.setPassword(result.get("password").toString());
        user.setAlias(result.get("alias").toString());

        return user;
    }



}
