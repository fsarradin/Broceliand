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

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.ShippingChargeStrategyService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.SessionManager;
import net.kerflyn.broceliand.util.ShippingCharges;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;

public class SellerController {

    private SellerService sellerService;
    private UserService userService;
    private BasketService basketService;
    private ShippingChargeStrategyService shippingChargeStrategyService;
    private SessionManager sessionManager;

    @Inject
    public SellerController(SellerService sellerService,
                            UserService userService,
                            BasketService basketService,
                            ShippingChargeStrategyService shippingChargeStrategyService,
                            SessionManager sessionManager) {
        this.sellerService = sellerService;
        this.userService = userService;
        this.basketService = basketService;
        this.shippingChargeStrategyService = shippingChargeStrategyService;
        this.sessionManager = sessionManager;
    }

    public void index(Request request, Response response) throws IOException, LeaseException {
        URL groupUrl = new File("template/add-modify-seller.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        template.addAggr("data.{action, actionName}", new Object[] { "new", "Create" });

        response.getPrintStream().append(template.render());
    }

    public void details(Request request, Response response) throws IOException, LeaseException {
        Seller seller = sellerService.findById(Long.valueOf(request.getPart("seller-id").getContent()));
        
        URL groupUrl = new File("template/add-modify-seller.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        template.addAggr("data.{action, actionName, seller, shippingChargeStrategies}",
                new Object[] { "modify", "Modify", seller, ShippingCharges.getShippingChargeStrategyFrom(seller)});

        response.getPrintStream().append(template.render());
    }

    public void delete(Request request, Response response) throws IOException, LeaseException {
        sellerService.deleteById(Long.valueOf(request.getPart("seller-id").getContent()));

        redirectTo(response, "/seller/list");
    }

    public void list(Request request, Response response) throws LeaseException, IOException {
        URL groupUrl = new File("template/list-sellers.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        template.addAggr("data.{sellers}", new Object[]{sellerService.findAll()});

        response.getPrintStream().append(template.render());
    }

    @PathName("new")
    public void createSeller(Request request, Response response) throws IOException {
        Seller seller = new Seller();
        seller.setName(request.getPart("name").getContent());
        seller.setAddress(request.getPart("address").getContent());
        seller.setCity(request.getPart("city").getContent());
        seller.setCountry(request.getPart("country").getContent());

        Set<ShippingChargeStrategy> strategies = ShippingCharges.instantiateFrom(request);

        shippingChargeStrategyService.saveAll(strategies);

        sellerService.setShippingChargeStrategy(seller, strategies);
        sellerService.save(seller);

        redirectTo(response, "/seller/list");
    }

    public void modify(Request request, Response response) throws IOException {
        Seller seller = sellerService.findById(Long.valueOf(request.getPart("seller-id").getContent()));
        seller.setName(request.getPart("name").getContent());
        seller.setAddress(request.getPart("address").getContent());
        seller.setCity(request.getPart("city").getContent());
        seller.setCountry(request.getPart("country").getContent());

        Set<ShippingChargeStrategy> strategies = ShippingCharges.instantiateFrom(request);

        shippingChargeStrategyService.saveAll(strategies);
        sellerService.setShippingChargeStrategy(seller, strategies);

        redirectTo(response, "/seller/list");
    }

}
