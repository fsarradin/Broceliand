package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import net.kerflyn.broceliand.route.PathName;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @PathName(".*")
    public void index(Request request, Response response) throws IOException {
        String[] segments = request.getPath().getSegments();

        String resourceType = segments[segments.length - 2];
        String resourceName = segments[segments.length - 1];

        String resourcePath = "public/" + resourceType + "/" + resourceName;
        LOGGER.info("getting resource at \"{}\"", resourcePath);

        Files.copy(new File(resourcePath), response.getOutputStream());
    }

}
