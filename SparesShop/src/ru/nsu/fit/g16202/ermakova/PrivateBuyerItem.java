package ru.nsu.fit.g16202.ermakova;

/*
Запись для работы с покупателями физ. лицами. Таблица PRIVATE_BUYERS
 */
public class PrivateBuyerItem {
    protected int buyerId;
    protected String lastName;
    protected String firstName;
    protected String middleName;
    protected String passportData;
    protected String registrationData;

    public PrivateBuyerItem(int id, String lname, String fname, String mname, String passport, String registration) {
        buyerId = id;
        lastName = lname;
        firstName = fname;
        middleName = mname;
        passportData = passport;
        registrationData = registration;
    }

    public String getIdStr() {
        return Integer.toString(buyerId);
    }

    public Integer getId() {
        return buyerId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPassportData() {
        return passportData;
    }

    public String getRegistrationData() {
        return registrationData;
    }

    public void setLastName(String lname) {
        lastName = lname;
    }

    public void setFirstName(String fname) {
        firstName = fname;
    }

    public void setMiddleName(String mname) {
        middleName = mname;
    }

    public void setPassportData(String passport) {
        passportData = passport;
    }

    public void setRegistrationData(String registration) {
        registrationData = registration;
    }
}
