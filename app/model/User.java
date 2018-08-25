package model;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class User {

    private int id;
    private String username;
    private String alias;
    private String password;

    public User(){}

    public User(String username, String alias, String password){
        this.username = username;
        this.alias = alias;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User[" + id + ", " + username + ", " + alias + ", " + password + "]";
    }
}
