package com.gmail.supercleanappmaster.Admin;

public class SubmissionsListModel {

    private String sub_client_id;
    private String sub_client_name;
    private String sub_client_phone;
    private String sub_client_address;
    private String sub_client_service_type;
    private String sub_client_bedroom_count;
    private String sub_client_bathroom_count;
    private String sub_client_date;
    private String sub_client_price;

    public SubmissionsListModel() {}
    public SubmissionsListModel(String sub_client_id, String sub_client_name,
                                String sub_client_phone,
                                String sub_client_address, String sub_client_service_type,
                                String sub_client_bedroom_count, String sub_client_bathroom_count,
                                String sub_client_date, String sub_client_price) {

        this.sub_client_id = sub_client_id;
        this.sub_client_name = sub_client_name;
        this.sub_client_phone = sub_client_phone;
        this.sub_client_address = sub_client_address;

        this.sub_client_service_type = sub_client_service_type;
        this.sub_client_bedroom_count = sub_client_bedroom_count;
        this.sub_client_bathroom_count = sub_client_bathroom_count;
        this.sub_client_date = sub_client_date;
        this.sub_client_price = sub_client_price;
    }


    public String getSub_client_id() {
        return sub_client_id;
    }

    public void setSub_client_id(String sub_client_id) {
        this.sub_client_id = sub_client_id;
    }

    public String getSub_client_name() {
        return sub_client_name;
    }

    public void setSub_client_name(String sub_client_name) {
        this.sub_client_name = sub_client_name;
    }

    public String getSub_client_price() {
        return sub_client_price;
    }

    public void setSub_client_price(String sub_client_price) {
        this.sub_client_price = sub_client_price;
    }

    public String getSub_client_phone() {
        return sub_client_phone;
    }

    public void setSub_client_phone(String sub_client_phone) {
        this.sub_client_phone = sub_client_phone;
    }

    public String getSub_client_service_type() {
        return sub_client_service_type;
    }

    public void setSub_client_service_type(String sub_client_service_type) {
        this.sub_client_service_type = sub_client_service_type;
    }

    public String getSub_client_bedroom_count() {
        return sub_client_bedroom_count;
    }

    public void setSub_client_bedroom_count(String sub_client_bedroom_count) {
        this.sub_client_bedroom_count = sub_client_bedroom_count;
    }

    public String getSub_client_bathroom_count() {
        return sub_client_bathroom_count;
    }

    public void setSub_client_bathroom_count(String sub_client_bathroom_count) {
        this.sub_client_bathroom_count = sub_client_bathroom_count;
    }

    public String getSub_client_date() {
        return sub_client_date;
    }

    public void setSub_client_date(String sub_client_date) {
        this.sub_client_date = sub_client_date;
    }

    public String getSub_client_address() {
        return sub_client_address;
    }

    public void setSub_client_address(String sub_client_address) {
        this.sub_client_address = sub_client_address;
    }
}
