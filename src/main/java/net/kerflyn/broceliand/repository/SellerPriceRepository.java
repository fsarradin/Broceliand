package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.SellerPrice;

import java.util.List;

public interface SellerPriceRepository {

    List<SellerPrice> findAllByBook(Book book);

}
