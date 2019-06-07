package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 5 (Самые продаваемые детали). Таблица SPARES + Количество проданных деталей
 */
public class SoldSpareCount extends SpareItem {
    protected int soldCount;

    public SoldSpareCount(int id, String name, String article, int count) {
        super(id, name, article);
        soldCount = count;
    }

    public String getSoldCountStr() {
        return Integer.toString(soldCount);
    }

    public int getSoldCount() {
        return soldCount;
    }
}
