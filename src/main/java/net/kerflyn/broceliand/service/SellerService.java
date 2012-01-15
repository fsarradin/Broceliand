package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.Seller;

import java.util.List;
import java.util.SortedMap;

public interface SellerService {

    List<Seller> findAll();

    SortedMap<String,SortedMap<String,Seller>> findAllSorted();

    void save(Seller seller);

    void deleteById(Long sellerId);

    Seller findById(Long sellerId);
}
