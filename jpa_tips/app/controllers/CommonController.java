package controllers;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Finally;

public class CommonController extends Controller {

    static final ThreadLocal<Long> beginTimeMillis = new ThreadLocal<>();

    @Before()
    static void loggerRequestBefore() {
        beginTimeMillis.set(System.currentTimeMillis());
    }

    @Finally(unless = "noAction")
    static void loggerRequestFinally() {
        Long term = System.currentTimeMillis() - beginTimeMillis.get();
        Logger.info("request. [%s](%s) %s %s, response time: %dms", request.method, request.remoteAddress, request.path, request.querystring, term);
    }

}
