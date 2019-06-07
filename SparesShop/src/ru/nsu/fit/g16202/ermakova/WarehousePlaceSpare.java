package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 4 (Деталь в ячейке склада). Идентификатор/деталь/артикул/место
 */
public class WarehousePlaceSpare extends SpareItem {
    protected int warehousePlace;

    public WarehousePlaceSpare(int id, String name, String article, int place) {
        super(id, name, article);
        warehousePlace = place;
    }

    public String getWarehousePlaceStr() {
        return Integer.toString(warehousePlace);
    }

    public int getWarehousePlace() {
        return warehousePlace;
    }
}
