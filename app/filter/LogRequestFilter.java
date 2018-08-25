package filter;

import akka.stream.Materializer;
import org.slf4j.LoggerFactory;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.Router;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;


/**
 * @author wanglu
 * @date 2018/08/25
 *
 */
public class LogRequestFilter extends Filter {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(LogRequestFilter.class);


    @Inject
    public LogRequestFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {
        long startTime = System.currentTimeMillis();

        return nextFilter.apply(requestHeader).thenApply(result -> {
            try {
                long endTime = System.currentTimeMillis();
                long requestTime = endTime - startTime;

                String method = requestHeader.method();
                String path = requestHeader.path();
                String methodName = requestHeader.tags().get(Router.Tags.ROUTE_CONTROLLER) + "." + requestHeader.tags().get(Router.Tags.ROUTE_ACTION_METHOD);

                //log
                logger.info("invoke metadata method. path: {}, method: {}, method_name: {}, status: {}, token: {} sec", path, method, methodName, result.status(), requestTime / 1.0e3);
            } catch (Exception e){
                logger.info("exception. message: {}", e.getMessage());
            }
            return result;
        });
    }
}
