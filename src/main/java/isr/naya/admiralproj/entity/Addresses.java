package isr.naya.admiralproj.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
public class Addresses {
    private int id;
    private String area;
    private String city;
    private String street;
    private Short houseNumber;
    private int clientId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "area")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "house_number")
    public Short getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Short houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Basic
    @Column(name = "client_id")
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Addresses addresses = (Addresses) o;

        if (id != addresses.id) return false;
        if (clientId != addresses.clientId) return false;
        if (area != null ? !area.equals(addresses.area) : addresses.area != null) return false;
        if (city != null ? !city.equals(addresses.city) : addresses.city != null) return false;
        if (street != null ? !street.equals(addresses.street) : addresses.street != null) return false;
        if (houseNumber != null ? !houseNumber.equals(addresses.houseNumber) : addresses.houseNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0);
        result = 31 * result + clientId;
        return result;
    }
}
