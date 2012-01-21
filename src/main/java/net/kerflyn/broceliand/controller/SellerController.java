package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Templates;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;

public class SellerController {

    private SellerService sellerService;
    private UserService userService;
    private BasketService basketService;

    @Inject
    public SellerController(SellerService sellerService, UserService userService, BasketService basketService) {
        this.sellerService = sellerService;
        this.userService = userService;
        this.basketService = basketService;
    }

    public void render(Request request, Response response) throws IOException, LeaseException {
        final URL groupUrl = new File("template/create-seller.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);
        response.getPrintStream().append(template.render());
    }

    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("new".equals(action)) {
            createSeller(request, response);
        } else if ("list".equals(action)) {
            listSellers(request, response);
        } else if ("delete".equals(action)) {
            Form form = request.getForm();
            Long sellerId = Long.valueOf(form.get("seller-id"));
            sellerService.deleteById(sellerId);
            redirectTo(response, "/seller/list");
        }
    }

    private void listSellers(Request request, Response response) throws LeaseException, IOException {
        final URL groupUrl = new File("template/list-sellers.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<Seller> sellers = sellerService.findAll();

        template.addAggr("data.{sellers}", new Object[] { sellers });
        response.getPrintStream().append(template.render());
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
