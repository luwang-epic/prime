package controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import play.Application;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import utils.JdbcUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class UserControllerTest {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private Application app = null;


    @Before
    public void init(){
        //Set up connection to test database, different from main database. Config better should be used instead of hard-coding.
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("data.source.name", "test");
        app = Helpers.fakeApplication(settings);
        Helpers.start(app);

        logger.info("datasource name is ：" + ((HikariDataSource)JdbcUtil.dataSource).getJdbcUrl());
    }


    @After
    public void stopApp() throws Exception {
        if (app != null) {
            Helpers.stop(app);
        }
    }



    @Test
    public void register(){
        ObjectNode json = Json.newObject();
        json.put("username", "zhangsan");
        json.put("password", "123456");
        json.put("alias", "张三");

        Http.RequestBuilder request = Helpers.fakeRequest().method(Helpers.POST)
                .uri("/prime/user/register").bodyJson(json);

        Result result = Helpers.route(app, request);

        logger.info("[TEST] register user. result: {}", Helpers.contentAsString(result));

        Assert.assertEquals(Helpers.OK, result.status());
    }


    @Test
    public void updateAlias(){
        ObjectNode json = Json.newObject();
        json.put("id", 1);
        json.put("alias", "张三222");

        Http.RequestBuilder request = Helpers.fakeRequest().method(Helpers.POST)
                .uri("/prime/user/alias").bodyJson(json);

        Result result = Helpers.route(app, request);

        logger.info("[TEST] update user alias. result: {}", Helpers.contentAsString(result));

        Assert.assertEquals(Helpers.OK, result.status());
    }


    @Test
    public void login(){
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(Helpers.GET)
                .uri("/prime/user/login?username=zhangsan&password=123456");

        Result result = Helpers.route(app, request);

        logger.info("[TEST] user login. result: {}", Helpers.contentAsString(result));
        Assert.assertEquals(Helpers.OK, result.status());
    }

}
