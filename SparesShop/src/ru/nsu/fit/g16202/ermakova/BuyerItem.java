package ru.nsu.fit.g16202.ermakova;

/*
Запись для работы с покупателями. Таблица BUYERS
 */
public class BuyerItem {
    protected int buyerId;
    protected int linkedBuyerId;
    protected int buyerType;
    protected String phoneNumber;
    protected String emailAddress;
    protected String buyerAddress;
    protected PrivateBuyerItem privateBuyer;
    protected LegalBuyerItem legalBuyer;

    public BuyerItem(int id, int linkedId, int type, String phone, String email, String address) {
        buyerId = id;
        linkedBuyerId = linkedId;
        buyerType = type;
        phoneNumber = phone;
        emailAddress = email;
        buyerAddress = address;
        privateBuyer = null;
        legalBuyer = null;
    }

    public String getIdStr() {
        return Integer.toString(buyerId);
    }

    public Integer getId() {
        return buyerId;
    }

    public String getLinkedIdStr() {
        return Integer.toString(linkedBuyerId);
    }

    public Integer getLinkedId() {
        return linkedBuyerId;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public PrivateBuyerItem getPrivateBuyer() {
        return privateBuyer;
    }

    public LegalBuyerItem getLegalBuyer() {
        return legalBuyer;
    }

    public void setBuyerId(int id) {
        buyerId = id;
    }

    public void setLinkedBuyerId(int id) {
        linkedBuyerId = id;
    }

    public void setBuyerType(int type) {
        buyerType = type;
    }

    public void setPhoneNumber(String phone) {
        phoneNumber = phone;
    }

    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    public void setBuyerAddress(String address) {
        buyerAddress = address;
    }

    public void setPrivateBuyer(PrivateBuyerItem buyer) {
        privateBuyer = new PrivateBuyerItem(buyer.getId(), buyer.getLastName(), buyer.getFirstName(), buyer.getMiddleName(), buyer.getPassportData(), buyer.getRegistrationData());
    }

    public void setLegalBuyer(LegalBuyerItem buyer) {
        legalBuyer = new LegalBuyerItem(buyer.getId(), buyer.getCompanyName(), buyer.getLegalAddress(), buyer.getINN(), buyer.getContactPerson(), buyer.getBankDetails());
    }
}
