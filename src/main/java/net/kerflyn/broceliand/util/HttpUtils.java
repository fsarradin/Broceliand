package net.kerflyn.broceliand.util;

import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

/**
 * Created by IntelliJ IDEA.
 * User: fsarradin
 * Date: 10/01/12
 * Time: 01:10
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtils {
    public static void redirectTo(Response response, String url) {
        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", url);
    }
}
