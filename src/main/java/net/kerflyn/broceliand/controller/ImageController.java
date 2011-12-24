package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.File;
import java.io.IOException;

public class ImageController {

    public void render(Request request, Response response, String imageName) throws IOException {
        Files.copy(new File("public/img/" + imageName), response.getOutputStream());
    }

}
