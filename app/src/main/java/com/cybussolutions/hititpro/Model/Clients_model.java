package com.cybussolutions.hititpro.Model;

/**
 * Created by Hamza Android on 1/13/2017.
 */
public class Clients_model {

    String client_name;
    String client_id;
    String contact_name,city,fax,email,state,zip;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    String client_adress;
    String client_phone;

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }
    public String getClient_adress() {
        return client_adress;
    }

    public void setClient_adress(String client_adress) {
        this.client_adress = client_adress;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String get_city() {
        return city;
    }

    public void set_city(String city) {
        this.city = city;
    }

    public String get_fax() {
        return fax;
    }

    public void set_fax(String fax) {
        this.fax = fax;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    public String get_state() {
        return state;
    }

    public void set_state(String state) {this.state = state;}

    public String get_zip() {
        return zip;
    }

    public void set_zip(String zip) {
        this.zip = zip;
    }
}
