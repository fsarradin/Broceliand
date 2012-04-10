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

package net.kerflyn.broceliand.util;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class Templates {

    public static ST createTemplateWithUserAndBasket(Request request, URL groupUrl, UserService userService, BasketService basketService) throws LeaseException {
        User currentUser = Users.getConnectedUser(userService, request);
        List<BasketElement> basketElements = Collections.emptyList();
        long basketCount = 0L;

        if (currentUser != null) {
            basketElements = basketService.findByUser(currentUser);
            basketCount = basketService.countByUser(currentUser);
        }

        ST template = buildTemplate(groupUrl);
        template.addAggr("metadata.{title}", new Object[] { "Title" });
        template.add("user", currentUser);
        template.addAggr("basket.{elements, size}", new Object[]{basketElements, basketCount});

        return template;
    }

    public static ST buildTemplate(URL groupUrl) {
        STGroupFile group = new STGroupFile(groupUrl, "UTF-8", '$', '$');

        return group.getInstanceOf("page");
    }
}
