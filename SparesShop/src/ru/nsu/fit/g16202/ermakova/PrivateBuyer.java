package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 3 (Покупатель физ. лицо). Таблица PRIVATE_BUYERS
 */
public class PrivateBuyer extends PrivateBuyerItem {
    protected String phone;
    protected String email;
    protected String actualAddress;

    public PrivateBuyer(int id, String lastName, String firstName, String middleName, String passport, String registrationAddress, String phone, String email, String actualAddress) {
        super(id, lastName, firstName, middleName, passport, registrationAddress);
        this.phone = phone;
        this.email = email;
        this.actualAddress = actualAddress;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder("");
        sb.append(lastName);
        sb.append(" ");
        sb.append(firstName);
        if (middleName != null) {
            sb.append(" ");
            sb.append(middleName);
        }
        return sb.toString();
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
