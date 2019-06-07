package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 3 (Покупатель юр. лицо). Таблица LEGAL_BUYERS
 */
public class LegalBuyer {
    protected String companyName;
    protected String legalAddress;
    protected String INN;
    protected String contact;
    protected String phone;
    protected String email;
    protected String actualAddress;

    public LegalBuyer(String companyName, String legalAddress, String INN, String contact, String phone, String email, String actualAddress) {
        this.companyName = companyName;
        this.legalAddress = legalAddress;
        this.INN = INN;
        this.contact = contact;
        this.phone = phone;
        this.email = email;
        this.actualAddress = actualAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public String getINN() {
        return INN;
    }

    public String getContact() {
        return contact;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getActualAddress() {
        return actualAddress;
    }
}
