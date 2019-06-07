package ru.nsu.fit.g16202.ermakova;

import java.text.ParseException;
import java.util.Date;

/*
Хранение записи таблицы MANAGERS
 */
public class Manager {
    private Integer id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String passportData;
    private Date hireDate;
    private Date dismissDate;

    public Manager(Integer id, String lastName, String firstName, String middleName, String passportData, Date hireDate, Date dismissDate) {
        this.id = Integer.valueOf(id);
        this.lastName = new String(lastName);
        this.firstName = new String(firstName);
        if (middleName == null) this.middleName = null;
        else this.middleName = new String(middleName);
        this.passportData = new String(passportData);
        this.hireDate = new Date(hireDate.getTime());
        if (dismissDate == null) this.dismissDate = null;
        else this.dismissDate = new Date(dismissDate.getTime());
    }

    public String getIdStr() {
        if (id == null) return null;
        else return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        if (middleName == null) return null;
        else return middleName;
    }

    public String getPassportData() {
        return passportData;
    }

    public String getHireDateStr() {
        if (hireDate == null) return null;
        else return ShopModel.dateToString(hireDate);
    }

    public String getDismissDateStr() {
        if (dismissDate == null) return null;
        else return ShopModel.dateToString(dismissDate);
    }

    public void setLastName(String str) {
        lastName = new String(str);
    }

    public void setFirstName(String str) {
        firstName = new String(str);
    }

    public void setMiddleName(String str) {
        if (str == null || str.equals("")) middleName = null;
        else middleName = new String(str);;
    }

    public void setPassportData(String str) {
        passportData = new String(str);
    }

    public void setHireDate(String str) {
        try {
            hireDate = ShopModel.stringToDate(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDismissDate(String str) {
        if (str == null || str.equals("")) {
            dismissDate = null;
            return;
        }
        try {
            dismissDate = ShopModel.stringToDate(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
