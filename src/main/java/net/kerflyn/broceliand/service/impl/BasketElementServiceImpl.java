package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BasketElementService;

import java.util.List;

public class BasketElementServiceImpl implements BasketElementService {

    private BasketElementRepository basketElementRepository;

    @Inject
    public BasketElementServiceImpl(BasketElementRepository basketElementRepository) {
        this.basketElementRepository = basketElementRepository;
    }

    @Override
    public List<BasketElement> findByUser(User user) {
        return basketElementRepository.findByUser(user);
    }

    @Override
    public int countByUser(User user) {
        return basketElementRepository.countByUser(user);
    }
}
