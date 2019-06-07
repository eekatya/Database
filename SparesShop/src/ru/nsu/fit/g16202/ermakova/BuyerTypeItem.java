package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для формы покупателей (Тип покупателя). Идентификатор/тип
 */
public class BuyerTypeItem {
    private int typeId;
    private String typeName;

    public BuyerTypeItem(int id, String name) {
        typeId = id;
        typeName = name;
    }

    public String getTypeIdStr() {
        return Integer.toString(typeId);
    }

    public String getTypeName() {
        return typeName;
    }
}
