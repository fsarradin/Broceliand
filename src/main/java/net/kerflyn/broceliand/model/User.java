package net.kerflyn.broceliand.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    public static final String ADMIN_LOGIN = "admin";

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Check if the user is an admin
     *
     * @return
     */
    public boolean isAdmin() {
        return ADMIN_LOGIN.equals(login);
    }
}
