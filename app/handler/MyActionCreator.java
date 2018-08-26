package handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.http.ActionCreator;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.Router;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class MyActionCreator implements ActionCreator {
    private final static Logger logger = LoggerFactory.getLogger(MyActionCreator.class);

    @Override
    public Action createAction(Http.Request request, Method actionMethod) {
        try {
            JsonNode jsonNode = request.body().asJson();
            String methodType = request.tags().get(Router.Tags.ROUTE_VERB);
            String path = request.tags().get(Router.Tags.ROUTE_PATTERN);
            String controllerName = request.tags().get(Router.Tags.ROUTE_CONTROLLER);
            String methodName = request.tags().get(Router.Tags.ROUTE_ACTION_METHOD);
            String controllerMethod = controllerName + "." + methodName;
            String comment = request.tags().get(Router.Tags.ROUTE_COMMENTS);
            logger.info("http request. methodType: {}, path: {}, controllerMethod {}, comment: {}, body json: {}",
                    methodType, path, controllerMethod, comment, jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("createAction exception. message: {}", e.getMessage());
        }


        return new Action.Simple() {
            @Override
            public CompletionStage<Result> call(Http.Context ctx) {
                return delegate.call(ctx);
            }
        };
    }
}
