package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.repository.SellerRepository;
import net.kerflyn.broceliand.service.SellerService;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.common.collect.Maps.newTreeMap;

public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;

    @Inject
    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @Override
    public SortedMap<String, SortedMap<String, Seller>> findAllSorted() {
        List<Seller> sellers = sellerRepository.findAll();
        SortedMap<String, SortedMap<String, Seller>> sellerMap = newTreeMap();
        for (Seller seller : sellers) {
            SortedMap<String, Seller> sellersByCity = sellerMap.get(seller.getCountry());
            if (sellersByCity == null) {
                sellersByCity = new TreeMap<String, Seller>();
                sellerMap.put(seller.getCountry(), sellersByCity);
            }
            sellersByCity.put(seller.getCity(), seller);
        }
        return sellerMap;
    }

    @Override
    public void save(Seller seller) {
        sellerRepository.save(seller);
    }

    @Override
    public void deleteById(Long sellerId) {
        sellerRepository.deleteById(sellerId);
    }

}
