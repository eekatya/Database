package ru.nsu.fit.g16202.ermakova;

/*
Запись для работы с покупателями юр. лицами. Таблица LEGAL_BUYERS
 */
public class LegalBuyerItem {
    protected int buyerId;
    protected String companyName;
    protected String legalAddress;
    protected String INN;
    protected String contactPerson;
    protected String bankDetails;

    public LegalBuyerItem(int id, String name, String address, String inn, String contact, String details) {
        buyerId = id;
        companyName = name;
        legalAddress = address;
        INN = inn;
        contactPerson = contact;
        bankDetails = details;
    }

    public String getIdStr() {
        return Integer.toString(buyerId);
    }

    public Integer getId() {
        return buyerId;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setCompanyName(String name) {
        companyName = name;
    }

    public void setLegalAddress(String address) {
        legalAddress = address;
    }

    public void setINN(String inn) {
        INN = inn;
    }

    public void setContactPerson(String contact) {
        contactPerson = contact;
    }

    public void setBankDetails(String details) {
        bankDetails = details;
    }
}
