package ru.nsu.fit.g16202.ermakova;

public class SpareCostsItem extends SpareItem {
    int minCost;
    int maxCost;

    public SpareCostsItem(int id, String name, String article, int min, int max) {
        super(id, name, article);
        minCost = min;
        maxCost = max;
    }

    public int getMinCost() {
        return minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public String getMinCostStr() {
        return ShopModel.formatCost(minCost, ",", ShopModel.CURRENCY_TEXT);
    }

    public String getMaxCostStr() {
        return ShopModel.formatCost(maxCost, ",", ShopModel.CURRENCY_TEXT);
    }
}
