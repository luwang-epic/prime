package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class TestController extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public Result test(){
        ObjectNode resultJson = Json.newObject();

        resultJson.put("test", "prime");

        return ok(resultJson);
    }

}

