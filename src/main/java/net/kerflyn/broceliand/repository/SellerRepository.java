package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.Seller;

import java.util.List;

public interface SellerRepository {

    List<Seller> findAll();

    Seller findById(Long sellerId);

    void save(Seller seller);

    void deleteById(Long sellerId);
}
