package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 4 (Деталь в ячейке склада). Идентификатор/деталь/артикул/количество
 */
public class WarehouseSpareVolume extends SpareItem {
    protected int sparesCount;

    public WarehouseSpareVolume(int id, String name, String article, int count) {
        super(id, name, article);
        sparesCount = count;
    }

    public String getSparesCountStr() {
        return Integer.toString(sparesCount);
    }

    public int getSparesCount() {
        return sparesCount;
    }
}
