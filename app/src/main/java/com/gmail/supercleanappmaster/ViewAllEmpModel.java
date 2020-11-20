package com.gmail.supercleanappmaster;

public class ViewAllEmpModel {

    private String emp_name;
    private String emp_email;

    public ViewAllEmpModel() {}
    public ViewAllEmpModel(String emp_name, String emp_email) {
        this.emp_name = emp_name;
        this.emp_email = emp_email;
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
}
