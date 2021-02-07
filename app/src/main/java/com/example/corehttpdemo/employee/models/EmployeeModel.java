package com.example.corehttpdemo.employee.models;

import java.io.Serializable;

public class EmployeeModel implements Serializable {
    private String eNo;
    private String eName;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String geteDob() {
        return eDob;
    }

    public void seteDob(String eDob) {
        this.eDob = eDob;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteNo() {
        return eNo;
    }

    public void seteNo(String eNo) {
        this.eNo = eNo;
    }

    private String eDob;
}
