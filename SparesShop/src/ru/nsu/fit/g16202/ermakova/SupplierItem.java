package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 1. Поставщик/наименование поставщика/ИНН
 */
public class SupplierItem {
    protected int supplierId;
    protected String supplierName;
    protected String supplierINN;

    public SupplierItem(int id, String name, String INN) {
        supplierId = id;
        supplierName = new String(name);
        supplierINN = new String(INN);
    }

    public String getSupplierIdStr() {
        return Integer.toString(supplierId);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierINN() {
        return supplierINN;
    }
}
