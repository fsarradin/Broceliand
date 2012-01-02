package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface BasketElementService {

    List<BasketElement> findByUser(User user);

    int countByUser(User user);
}
