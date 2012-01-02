package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface BasketElementRepository {

    List<BasketElement> findByUser(User user);

    int countByUser(User user);
}
