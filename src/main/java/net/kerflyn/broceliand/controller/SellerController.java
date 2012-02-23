package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public void index(Request request, Response response) throws IOException, LeaseException {
        URL groupUrl = new File("template/add-modify-seller.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);
        template.addAggr("data.{action, actionName}", new Object[] { "new", "Create" });
        response.getPrintStream().append(template.render());
    }

    public void details(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();
        Long sellerId = Long.valueOf(form.get("seller-id"));

        Seller seller = sellerService.findById(sellerId);
        
        URL groupUrl = new File("template/add-modify-seller.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);
        template.addAggr("data.{action, actionName, seller}", new Object[] { "modify", "Modify", seller });
        response.getPrintStream().append(template.render());
    }
    
    public void delete(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();
        Long sellerId = Long.valueOf(form.get("seller-id"));
        sellerService.deleteById(sellerId);
        redirectTo(response, "/seller/list");
    }

    public void list(Request request, Response response) throws LeaseException, IOException {
        final URL groupUrl = new File("template/list-sellers.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<Seller> sellers = sellerService.findAll();

        template.addAggr("data.{sellers}", new Object[]{sellers});
        response.getPrintStream().append(template.render());
    }

    @PathName("new")
    public void createSeller(Request request, Response response) throws IOException {
        Form form = request.getForm();

        Seller seller = new Seller();
        seller.setName(form.get("name"));
        seller.setAddress(form.get("address"));
        seller.setCity(form.get("city"));
        seller.setCountry(form.get("country"));

        sellerService.save(seller);

        redirectTo(response, "/seller/list");
    }

    public void modify(Request request, Response response) throws IOException {
        Form form = request.getForm();
        Long sellerId = Long.valueOf(form.get("seller-id"));

        Seller seller = sellerService.findById(sellerId);
        seller.setName(form.get("name"));
        seller.setAddress(form.get("address"));
        seller.setCity(form.get("city"));
        seller.setCountry(form.get("country"));

        redirectTo(response, "/seller/list");
    }

}
