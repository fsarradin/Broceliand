/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        put("js", "text/javascript");
        put("jpg", "image/jpg");
        put("png", "image/png");
        put("template", "text/plain");
    }};

    @PathName(".*")
    public void index(Request request, Response response) throws IOException {
        String[] segments = request.getPath().getSegments();

        LOGGER.info("getting resource at \"{}\"", getResourcePath(segments));

        response.setValue("Content-Type", getMimeType(segments[1]));
        Files.copy(new File(getResourcePath(segments)), response.getOutputStream());
    }

    private String getResourcePath(String[] segments) {
        return "public/" + on("/").join(Arrays.copyOfRange(segments, 1, segments.length));
    }

    private String getMimeType(String segment) {
        return CONVERTER.get(segment.toLowerCase());
    }

}
