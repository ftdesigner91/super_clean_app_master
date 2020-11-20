package com.gmail.supercleanappmaster.SubmissionsHistory;

public class SubmissionsHistoryModel {

    private String sub_client_date;
    private String sub_client_service_type;
    private String sub_client_bedroom_count;
    private String sub_client_bathroom_count;
    private String sub_client_address;

    private String no_bookings;

    public SubmissionsHistoryModel(){}

    public SubmissionsHistoryModel(String sub_client_date,
                                   String sub_client_service_type,
                                   String sub_client_bedroom_count,
                                   String sub_client_bathroom_count,
                                   String sub_client_address, String no_bookings) {
        this.sub_client_date = sub_client_date;
        this.sub_client_service_type = sub_client_service_type;
        this.sub_client_bedroom_count = sub_client_bedroom_count;
        this.sub_client_bathroom_count = sub_client_bathroom_count;
        this.sub_client_address = sub_client_address;
        this.no_bookings = no_bookings;
    }

    public String getSub_client_date() {
        return sub_client_date;
    }

    public void setSub_client_date(String sub_client_date) {
        this.sub_client_date = sub_client_date;
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

    public String getSub_client_address() {
        return sub_client_address;
    }

    public void setSub_client_address(String sub_client_address) {
        this.sub_client_address = sub_client_address;
    }

    public String getNo_bookings() {
        no_bookings = "All your past bookings will appear here";
        return no_bookings;
    }

    public void setNo_bookings(String no_bookings) {
        this.no_bookings = no_bookings;
    }
}
