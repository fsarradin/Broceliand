package net.kerflyn.broceliand.util;

import com.google.common.io.Files;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class Templates {

    public static final STGroup TEMPLATE_GROUP = new STGroupDir("", "UTF-8", '$', '$');

    public static ST buildTemplate(String webpage) throws IOException {
        String raw = Files.toString(new File(webpage), UTF_8);
        ST template = new ST(raw, '$', '$');
        return TEMPLATE_GROUP.createStringTemplateInternally(template);
    }

    public static ST createTemplateWithUserAndBasket(Request request, URL groupUrl, UserService userService, BasketService basketService) throws LeaseException {
        User currentUser = Users.getConnectedUser(userService, request);
        List<BasketElement> basketElements = Collections.emptyList();
        long basketCount = 0L;

        if (currentUser != null) {
            basketElements = basketService.findByUser(currentUser);
            basketCount = basketService.countByUser(currentUser);
        }

        STGroupFile group = new STGroupFile(groupUrl, "UTF-8", '$', '$');
        ST template = group.getInstanceOf("page");
        template.addAggr("metadata.{title}", new Object[] { "Title" });
        template.add("user", currentUser);
        template.addAggr("basket.{elements, size}", new Object[]{basketElements, basketCount});
        return template;
    }
}
