package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.UserService;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class UserController extends Controller {

    private UserService userService = new UserService();


    public Result register(){
        ObjectNode resultJson = Json.newObject();
        String username = request().body().asJson().get("username").asText();
        String password = request().body().asJson().get("password").asText();
        String alias = request().body().asJson().get("alias").asText();

        userService.register(username, password, alias);
        resultJson.put("code", "0");
        resultJson.put("message", "register success");

        return ok(resultJson);
    }


    public Result updateAlias(){
        ObjectNode resultJson = Json.newObject();
        int id = request().body().asJson().get("id").asInt();
        String alias = request().body().asJson().get("alias").asText();

        userService.updateAlias(id ,alias);
        resultJson.put("code", "0");
        resultJson.put("id", id);
        resultJson.put("alias", alias);
        resultJson.put("message", "update alias success");

        return ok(resultJson);
    }


    public Result login(){
        ObjectNode resultJson = Json.newObject();
        int id = Integer.parseInt(request().getQueryString("id"));
        User user = userService.getUserById(id);

        resultJson.set("user", Json.toJson(user));

        return ok(resultJson);
    }



}
