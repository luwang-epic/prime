package service;

import dao.UserDao;
import model.User;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class UserService {


    public void isExist(int id){

    }


    public void register(String username, String password, String alais){

        //是否存在


        //不存在插入
        User user = new User(username, password, alais);
        UserDao.insert(user);

    }


    public void updateAlias(int id, String alias){

        UserDao.updateAlias(id, alias);

    }


    public User getUserById(int id){
        User user = UserDao.getUserById(id);
        return user;
    }



}
