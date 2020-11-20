package com.gmail.supercleanappmaster.Admin;

import android.net.Uri;

public class EmployeesListModel {

    private String emp_id;
    private String emp_name;
    private String emp_email;
    private String emp_image;


    public EmployeesListModel() {}
    public EmployeesListModel(String user_id, String emp_name, String emp_email, String emp_image) {
        this.emp_id = user_id;
        this.emp_name = emp_name;
        this.emp_email = emp_email;
        this.emp_image = emp_image;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String user_id) {
        this.emp_id = user_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_image() {
        return emp_image;
    }

    public void setEmp_image(String emp_image) {
        this.emp_image = emp_image;
    }
}
