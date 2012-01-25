package net.kerflyn.broceliand.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Connection {

    @Id
    @GeneratedValue
    private Long id;

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime expirationDate;

    @ManyToOne
    private User user;

    private String hostAddress;

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getHostAddress() {
        return hostAddress;
    }
}
