package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Templates;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;

public class SellerController {

    private SellerService sellerService;
    private UserService userService;

    @Inject
    public SellerController(SellerService sellerService, UserService userService) {
        this.sellerService = sellerService;
        this.userService = userService;
    }

    public void render(Request request, Response response) throws IOException, LeaseException {
        User currentUser = Users.getConnectedUser(userService, request);
        boolean isAdmin = Users.isAdmin(currentUser);
        ST template = Templates.buildTemplate("public/create-seller.html");
        template.add("currentUser", currentUser);
        template.add("isAdmin", isAdmin);
        response.getPrintStream().append(template.render());
    }

    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("new".equals(action)) {
            createSeller(request, response);
        } else if ("list".equals(action)) {
            User currentUser = Users.getConnectedUser(userService, request);
            boolean isAdmin = Users.isAdmin(currentUser);
            List<Seller> sellers = sellerService.findAll();
            ST template = Templates.buildTemplate("public/list-sellers.html");
            template.add("sellers", sellers);
            template.add("currentUser", currentUser);
            template.add("isAdmin", isAdmin);
            response.getPrintStream().append(template.render());
        }
    }

    private void createSeller(Request request, Response response) throws IOException {
        Form form = request.getForm();

        Seller seller = new Seller();
        seller.setName(form.get("name"));
        seller.setAddress(form.get("address"));
        seller.setCity(form.get("city"));
        seller.setCountry(form.get("country"));

        sellerService.save(seller);

        redirectTo(response, "/seller/list");
    }

}
