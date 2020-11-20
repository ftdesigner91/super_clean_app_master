package com.gmail.supercleanappmaster.employees;

public class EmpViewAssignmentsModel {

    private String sub_date_and_client_id;
    private String client_name;
    private String client_phone;
    private String client_address;
    private String date;
    private String service_type;
    private String bedrooms;
    private String bathrooms;

    public EmpViewAssignmentsModel() {}
    public EmpViewAssignmentsModel(String sub_date_and_client_id, String client_name, String client_phone, String client_address, String date, String service_type, String bedrooms, String bathrooms) {
        this.sub_date_and_client_id = sub_date_and_client_id;
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.client_address = client_address;
        this.date = date;
        this.service_type = service_type;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
    }

    public String getSub_date_and_client_id() {
        return sub_date_and_client_id;
    }

    public void setSub_date_and_client_id(String sub_date_and_client_id) {
        this.sub_date_and_client_id = sub_date_and_client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}