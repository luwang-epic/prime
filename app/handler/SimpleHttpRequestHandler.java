package handler;

import javax.inject.Inject;

import play.http.HandlerForRequest;
import play.mvc.EssentialAction;
import play.mvc.Http;
import play.mvc.Results;
import play.routing.Router;
import play.api.mvc.Handler;
import play.http.HttpRequestHandler;
import play.libs.streams.Accumulator;
import play.core.j.JavaHandler;
import play.core.j.JavaHandlerComponents;

public class SimpleHttpRequestHandler implements HttpRequestHandler {
    private final Router router;
    private final JavaHandlerComponents handlerComponents;

    @Inject
    public SimpleHttpRequestHandler(Router router, JavaHandlerComponents components) {
        this.router = router;
        this.handlerComponents = components;
    }

    @Override
    public HandlerForRequest handlerForRequest(Http.RequestHeader request) {
        Handler handler = router.route(request).orElseGet(() ->
                EssentialAction.of(req -> Accumulator.done(Results.notFound()))
        );
        if (handler instanceof JavaHandler) {
            handler = ((JavaHandler)handler).withComponents(handlerComponents);
        }
        return new HandlerForRequest(request, handler);
    }
}
