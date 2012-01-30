package net.kerflyn.broceliand.util;

import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

public class HttpUtils {

    public static void redirectTo(Response response, String url) {
        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", url);
    }

}
