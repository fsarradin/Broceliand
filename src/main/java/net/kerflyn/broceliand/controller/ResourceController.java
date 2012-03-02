package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import net.kerflyn.broceliand.route.PathName;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Joiner.on;

public class ResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    private static final Map<String, String> CONVERTER = new HashMap<String, String>() {{
        put("css", "text/css");
        put("js", "text/script");
        put("jpg", "image/jpg");
        put("png", "image/png");
    }};

    @PathName(".*")
    public void index(Request request, Response response) throws IOException {
        String[] segments = request.getPath().getSegments();

        String mimeType = getMimeType(segments[1]);

        String resourcePath = getResourcePath(segments);
        LOGGER.info("getting resource at \"{}\"", resourcePath);

        response.set("Content-Type", mimeType);
        Files.copy(new File(resourcePath), response.getOutputStream());
    }

    private String getResourcePath(String[] segments) {
        return "public/" + on("/").join(Arrays.copyOfRange(segments, 1, segments.length));
    }

    private String getMimeType(String segment) {
        String type = segment.toLowerCase();
        return CONVERTER.get(type);
    }

}
