package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 5 (Самые дешёвые поставщики). Поставщик/наименование поставщика/ИНН/Стоимость деталей
 */
public class SupplierCost extends SupplierItem {
    protected int totalCost;

    public SupplierCost(int id, String name, String INN, int cost) {
        super(id, name, INN);
        totalCost = cost;
    }

    public SupplierCost(int id, String name, String INN, String cost) {
        super(id, name, INN);
        totalCost = ShopModel.parseCost(cost);
    }

    public String getTotalCostStr() {
        return ShopModel.formatCost(totalCost, ",", ShopModel.CURRENCY_TEXT);
    }
}
