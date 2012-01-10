package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.Seller;

import java.util.List;

public interface SellerRepository {

    List<Seller> findAll();

    void save(Seller seller);

}
