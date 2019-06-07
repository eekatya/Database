package ru.nsu.fit.g16202.ermakova;

/*
Запись запроса для Отчёта 1, 2, 3 (Деталь). Таблица SPARES
 */
public class SpareItem {
    protected int spareId;
    protected String spareName;
    protected String spareArticle;

    public SpareItem(int id, String name, String article) {
        spareId = id;
        spareName = name;
        spareArticle = article;
    }

    public String getSpareIdStr() {
        return Integer.toString(spareId);
    }

    public Integer getId() {
        return spareId;
    }

    public String getSpareName() {
        return spareName;
    }

    public String getSpareArticle() {
        return spareArticle;
    }

    public void setSpareName(String name) {
        spareName = name;
    }

    public void setSpareArticle(String article) {
        spareArticle = article;
    }
}
