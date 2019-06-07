package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 6. Среднее число продаж на месяц.
 */
public class AverageMonthSellsReport extends ReportDialog{
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 6. Среднее число продаж на месяц по всем деталям.";
    private static final String YEAR_LABEL_TEXT = "Год";
    private static final String SPARES_SELLS_LIST_LABEL_TEXT = "Среднее число продаж";
    private static final String[] COLUMN_NAMES = {
            "ID",
            "Наименование",
            "Артикул",
            "Число продаж"
    };
    private static final String[][] EMPTY_DATA = {
            {"", "", "", ""},
    };
    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point YEAR_LABEL_POSITION = new Point(6, 48);
    private static final Dimension YEAR_LABEL_SIZE = new Dimension(160, 24);
    private static final Point YEAR_TEXT_POSITION = new Point(172, 48);
    private static final Dimension YEAR_TEXT_SIZE = new Dimension(96, 24);
    private static final Point SPARES_SELLS_LABEL_POSITION = new Point(6, 80);
    private static final Dimension SPARES_SELLS_LIST_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SCROLL_PANE_POSITION = new Point(6, 112);
    private static final Dimension SCROLL_PANE_SIZE = new Dimension(740, 196);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 316);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 396);

    private int year;
    private JLabel yearLabel;
    private JLabel sparesListLabel;
    private ReportYearTextField yearField;
    private ArrayList<SoldSpareCount> records;

    public AverageMonthSellsReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        year = -1;
        records = null;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void updateReport() {
        Statement stmt;
        try {
            year = (yearField.getYearValue() == null) ? -1 : yearField.getYearValue();
            if (year == -1) {
                tableModel.setDataVector(EMPTY_DATA, COLUMN_NAMES);
                reportTable.setModel(tableModel);
            } else {
                records = new ArrayList<>();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                String dateFrom = model.firstDayOfYear(year);
                String dateTo = model.lastDayOfYear(year);
                String sql = "SELECT ORDERS_STRUCTURE.SPARE_ID AS SPARE_ID, COUNT(ORDERS_STRUCTURE.SPARE_ID) AS SELLS_COUNT" +
                        " FROM ORDERS_STRUCTURE, WAREHOUSE_STRUCTURE, WAREHOUSE_SALES, SALES, SPARES" +
                        " WHERE WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND" +
                        " WAREHOUSE_SALES.WAREHOUSE_ID = WAREHOUSE_STRUCTURE.ID AND" +
                        " SALES.ID = WAREHOUSE_SALES.SALE_ID AND" +
                        " SPARES.ID = ORDERS_STRUCTURE.SPARE_ID AND" +
                        " SALES.SALE_DATE >= '" + dateFrom + "' AND" +
                        " SALES.SALE_DATE <= '" + dateTo + "'" +
                        " GROUP BY ORDERS_STRUCTURE.SPARE_ID";
                ResultSet rs = stmt.executeQuery(sql);
                ArrayList<Point> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new Point(rs.getInt("SPARE_ID"),
                            rs.getInt("SELLS_COUNT")
                    ));
                }
                rs.close();
                stmt.close();
                model.commitConn();
                System.err.println("SELECT done successfully");
                for (int i = 0; i < result.size(); i++) {
                    stmt = model.createStatement();
                    sql = "SELECT SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE" +
                            " FROM SPARES" +
                            " WHERE SPARES.ID = " + result.get(i).x;
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        records.add(new SoldSpareCount(result.get(i).x,
                                rs.getString("NAME"),
                                rs.getString("ARTICLE"),
                                result.get(i).x));
                    }
                }
                tableModel.setDataVector(getRecordsArray(), COLUMN_NAMES);
                reportTable.setModel(tableModel);
            }
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
    }

    @Override
    protected void initControls() {
        // PARENT CONTROLS BEGIN
        reportNameLabel.setText(REPORT_NAME_LABEL_TEXT);
        reportNameLabel.setSize(REPORT_NAME_LABEL_SIZE);
        reportNameLabel.setLocation(REPORT_NAME_LABEL_POSITION);

        tableModel.setDataVector(EMPTY_DATA, COLUMN_NAMES);
        scrollPane.setSize(SCROLL_PANE_SIZE);
        scrollPane.setLocation(SCROLL_PANE_POSITION);

        btnClose.setLocation(CLOSE_BUTTON_POSITION);
        // PARENT CONTROLS END

        yearLabel = new JLabel(YEAR_LABEL_TEXT);
        yearLabel.setSize(YEAR_LABEL_SIZE);
        yearLabel.setLocation(YEAR_LABEL_POSITION);
        yearField = new ReportYearTextField("", this, 1985, 2020);
        yearField.setSize(YEAR_TEXT_SIZE);
        yearField.setLocation(YEAR_TEXT_POSITION);
        sparesListLabel = new JLabel(SPARES_SELLS_LIST_LABEL_TEXT);
        sparesListLabel.setSize(SPARES_SELLS_LIST_LABEL_SIZE);
        sparesListLabel.setLocation(SPARES_SELLS_LABEL_POSITION);

        add(yearLabel);
        add(yearField);
        add(sparesListLabel);
    }

    private String [][] getRecordsArray() {
        String [][] result = new String[records.size()][4];
        for (int i = 0; i < records.size(); i++) {
            result[i][0] = records.get(i).getSpareIdStr();
            result[i][1] = records.get(i).getSpareName();
            result[i][2] = records.get(i).getSpareArticle();
            float cnt = (float) (records.get(i).getSoldCount()) / 12.0f;
            result[i][3] = String.valueOf(cnt);
        }
        return result;
    }
}
