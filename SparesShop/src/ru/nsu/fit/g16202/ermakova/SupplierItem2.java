package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 2. Поставщик/наименование поставщика/ИНН + срок поставки/стоимость детали
 */
public class SupplierItem2 extends SupplierItem {
    protected int delivertTime;
    protected int spareCost;

    public SupplierItem2(int id, String name, String INN, int time, int cost) {
        super(id, name, INN);
        delivertTime = time;
        spareCost = cost;
    }

    public SupplierItem2(int id, String name, String INN, int time, String cost) {
        super(id, name, INN);
        delivertTime = time;
        spareCost = ShopModel.parseCost(cost);
    }

    public String getDelivertTimeStr() {
        return Integer.toString(delivertTime);
    }

    public String getSpareCostStr() {
        return ShopModel.formatCost(spareCost, ",", ShopModel.CURRENCY_TEXT);
    }
}
