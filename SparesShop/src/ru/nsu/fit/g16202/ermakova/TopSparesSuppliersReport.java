package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 5. Самые продаваемые детали и самые дешёвые поставщики
 */
public class TopSparesSuppliersReport extends ReportDialog {
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 5. Самые продаваемые детали и самые дешёвые поставщики.";
    private static final String TOP_SOLD_SPARES_LABEL_TEXT = "Самые продаваемые детали";
    private static final String TOP_LOWCOST_SUPPLIERS_LABEL_TEXT = "Самые дешёвые поставщики";
    private static final String[] COLUMN_NAMES1 = {
            "Деталь",
            "Артикул",
            "Количество"
    };
    private static final String[][] EMPTY_DATA1 = {
            {"", "", ""},
    };
    private static final String[] COLUMN_NAMES2 = {
            "ID",
            "Наименование поставщика",
            "ИНН",
            "Стоимость деталей"
    };
    private static final String[][] EMPTY_DATA2 = {
            {"", "", "", ""},
    };

    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point TOP_SPARES_POSITION = new Point(6, 48);
    private static final Dimension TOP_SPARES_LABEL_SIZE = new Dimension(256, 24);
    private static final Point SCROLL_PANE1_POSITION = new Point(6, 80);
    private static final Dimension SCROLL_PANE1_SIZE = new Dimension(740, 144);
    private static final Point TOP_SUPPLIERS_LABEL_POSITION = new Point(6, 232);
    private static final Dimension TOP_SUPPLIERS_LABEL_SIZE = new Dimension(256, 24);
    private static final Point SCROLL_PANE2_POSITION = new Point(6, 264);
    private static final Dimension SCROLL_PANE2_SIZE = new Dimension(740, 144);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 416);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 494);

    protected JTable reportTable2;
    protected DefaultTableModel tableModel2;
    protected JScrollPane scrollPane2;

    private JLabel sparesLabel;
    private JLabel suppliersLabel;
    private ArrayList<SoldSpareCount> spares;
    private ArrayList<SupplierCost> suppliers;

    public TopSparesSuppliersReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        spares = new ArrayList<>();
        suppliers = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        try {
            loadTopInfo();
        } catch (Exception e) {
            System.err.println("Exception while retrieving report data");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
    }


    @Override
    protected void updateReport() {

    }

    @Override
    protected void initControls() {
        // PARENT CONTROLS BEGIN
        reportNameLabel.setText(REPORT_NAME_LABEL_TEXT);
        reportNameLabel.setSize(REPORT_NAME_LABEL_SIZE);
        reportNameLabel.setLocation(REPORT_NAME_LABEL_POSITION);

        tableModel.setDataVector(EMPTY_DATA1, COLUMN_NAMES1);
        scrollPane.setSize(SCROLL_PANE1_SIZE);
        scrollPane.setLocation(SCROLL_PANE1_POSITION);

        btnClose.setLocation(CLOSE_BUTTON_POSITION);
        // PARENT CONTROLS END

        reportTable2 = new JTable();
        tableModel2 = new DefaultTableModel(TABLE_DATA, TABLE_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells false => table is readonly
            }
        };

        reportTable2.setModel(tableModel2);
        scrollPane2 = new JScrollPane(reportTable2);

        tableModel2.setDataVector(EMPTY_DATA2, COLUMN_NAMES2);
        scrollPane2.setSize(SCROLL_PANE2_SIZE);
        scrollPane2.setLocation(SCROLL_PANE2_POSITION);

        add(scrollPane2);

        sparesLabel = new JLabel(TOP_SOLD_SPARES_LABEL_TEXT);
        sparesLabel.setSize(TOP_SPARES_LABEL_SIZE);
        sparesLabel.setLocation(TOP_SPARES_POSITION);

        suppliersLabel = new JLabel(TOP_LOWCOST_SUPPLIERS_LABEL_TEXT);
        suppliersLabel.setSize(TOP_SUPPLIERS_LABEL_SIZE);
        suppliersLabel.setLocation(TOP_SUPPLIERS_LABEL_POSITION);

        add(sparesLabel);
        add(suppliersLabel);
    }

    private String [][] getSparesArray() {
        String [][] result = new String[spares.size()][3];
        for (int i = 0; i < spares.size(); i++) {
            result[i][0] = spares.get(i).getSpareName();
            result[i][1] = spares.get(i).getSpareArticle();
            result[i][2] = spares.get(i).getSoldCountStr();
        }
        return result;
    }

    private String [][] getVolumeArray() {
        String [][] result = new String[suppliers.size()][4];
        for (int i = 0; i < suppliers.size(); i++) {
            result[i][0] = suppliers.get(i).getSupplierIdStr();
            result[i][1] = suppliers.get(i).getSupplierName();
            result[i][2] = suppliers.get(i).getSupplierINN();
            result[i][3] = suppliers.get(i).getTotalCostStr();
        }
        return result;
    }

    private void loadTopInfo() throws Exception {
        Statement stmt;
        spares = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        String sql = "SELECT SPARES.ID AS ID, SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE, COUNT(SPARES.ID) AS COUNT" +
                " FROM SPARES, WAREHOUSE_SALES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE" +
                " WHERE WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND ORDERS_STRUCTURE.SPARE_ID = SPARES.ID AND WAREHOUSE_STRUCTURE.ID = WAREHOUSE_SALES.WAREHOUSE_ID" +
                " GROUP BY SPARES.ID" +
                " ORDER BY COUNT DESC" +
                " FETCH FIRST 10 ROWS ONLY";
        ResultSet rs = stmt.executeQuery( sql);
        while (rs.next()) {
            spares.add(new SoldSpareCount(rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("ARTICLE"),
                    rs.getInt("COUNT")
            ));
            System.err.println("Record of spare/count retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SPARES/WAREHOUSE_SALES/WAREHOUSE_STRUCTURE/ORDERS_STRUCTURE done successfully");

        suppliers = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        sql = "SELECT SUPPLIERS.ID AS ID, SUPPLIERS.NAME AS NAME, SUPPLIERS.INN AS INN, SUM(SUPPLIER_SPARE.COST) AS COST" +
                " FROM SUPPLIERS, SUPPLIER_SPARE" +
                " WHERE SUPPLIERS.ID = SUPPLIER_SPARE.SUPPLIER_ID" +
                " GROUP BY SUPPLIERS.ID" +
                " ORDER BY COST" +
                " FETCH FIRST 10 ROWS ONLY";
        rs = stmt.executeQuery( sql);
        while (rs.next()) {
            suppliers.add(new SupplierCost(rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("INN"),
                    rs.getString("COST")
            ));
            System.err.println("Record of spare/cost retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SUPPLIERS/SUPPLIER_SPARE done successfully");

        if (spares.size() == 0)
            tableModel.setDataVector(EMPTY_DATA1, COLUMN_NAMES1);
        else
            tableModel.setDataVector(getSparesArray(), COLUMN_NAMES1);
        reportTable.setModel(tableModel);
        if (suppliers.size() == 0)
            tableModel2.setDataVector(EMPTY_DATA2, COLUMN_NAMES2);
        else
            tableModel2.setDataVector(getVolumeArray(), COLUMN_NAMES2);
        reportTable2.setModel(tableModel2);
    }

}
