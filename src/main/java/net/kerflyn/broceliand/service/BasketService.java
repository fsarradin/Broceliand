package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface BasketService {

    List<BasketElement> findByUser(User user);

    long countByUser(User user);

    void addBookById(User user, Long bookId);

    Invoice getCurrentInvoiceFor(User user);
}
