package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 1 (Тип поставщика). Идентификатор/тип
 */
public class SupplierTypeItem {
    private int typeId;
    private String typeName;

    public SupplierTypeItem(int id, String name) {
        typeId = id;
        typeName = new String(name);
    }

    public String getTypeIdStr() {
        return Integer.toString(typeId);
    }

    public String getTypeName() {
        return typeName;
    }
}
