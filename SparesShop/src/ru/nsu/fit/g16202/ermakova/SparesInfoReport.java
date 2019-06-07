package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 2. Сведения по деталям (поставщик, цена, время поставки).
 */
public class SparesInfoReport extends ReportDialog {
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 2. Сведения по деталям (поставщик, цена, время поставки).";
    private static final String SPARE_LABEL_TEXT = "Деталь";
    private static final String SUPPLIERS_LIST_LABEL_TEXT = "Перечень поставщиков";
    private static final String[] COLUMN_NAMES = {
            "ID",
            "Наименование поставщика",
            "ИНН",
            "Время поставки",
            "Стоимость"
    };
    private static final String[][] EMPTY_DATA = {
            {"", "", "", "", ""},
    };
    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point SPARE_LABEL_POSITION = new Point(6, 48);
    private static final Dimension SPARE_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SPARE_COMBO_POSITION = new Point(172, 48);
    private static final Dimension SPARE_COMBO_SIZE = new Dimension(384, 24);
    private static final Point SUPPLIERS_LIST_LABEL_POSITION = new Point(6, 80);
    private static final Dimension SUPPLIERS_LIST_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SCROLL_PANE_POSITION = new Point(6, 112);
    private static final Dimension SCROLL_PANE_SIZE = new Dimension(740, 196);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 316);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 396);

    private int spareSelectedItem;
    private JLabel spareLabel;
    private JLabel suppliersListLabel;
    private JComboBox spareCombo;
    private ArrayList<SupplierItem2> records;
    private ArrayList<SpareItem> spares;

    public SparesInfoReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        records = null;
        spareSelectedItem = 0;
    }

    @Override
    protected void onStart() {
        try {
            loadSpares();
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
        Statement stmt;
        try {
            if (spareSelectedItem == 0) {
                tableModel.setDataVector(EMPTY_DATA, COLUMN_NAMES);
                reportTable.setModel(tableModel);
            } else {
                records = new ArrayList<>();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                String sql = "SELECT SUPPLIERS.ID, SUPPLIERS.NAME, SUPPLIERS.INN, SUPPLIER_SPARE.TIME, SUPPLIER_SPARE.COST FROM SUPPLIERS, SPARES, SUPPLIER_SPARE" +
                        " WHERE SUPPLIERS.ID = SUPPLIER_SPARE.SUPPLIER_ID" +
                        " AND SUPPLIER_SPARE.SPARE_ID = SPARES.ID" +
                        " AND SPARES.ID = " + spares.get(spareSelectedItem - 1).getSpareIdStr() +
                        " ORDER BY SUPPLIER_SPARE.COST";
                ResultSet rs = stmt.executeQuery( sql);
                while (rs.next()) {
                    records.add(new SupplierItem2(rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("INN"),
                            rs.getInt("TIME"),
                            rs.getString("COST")
                    ));
                }
                rs.close();
                stmt.close();
                model.commitConn();
                System.err.println("SELECT SUPPLIERS-SPARES done successfully");
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

        spareLabel = new JLabel(SPARE_LABEL_TEXT);
        spareLabel.setSize(SPARE_LABEL_SIZE);
        spareLabel.setLocation(SPARE_LABEL_POSITION);
        spareCombo = new JComboBox();
        spareCombo.setSize(SPARE_COMBO_SIZE);
        spareCombo.setLocation(SPARE_COMBO_POSITION);
        spareCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.err.println("spareCombo: " + spareCombo.getSelectedIndex());
                spareSelectedItem = spareCombo.getSelectedIndex();
                if (spareSelectedItem == -1) spareSelectedItem = 0;
                updateReport();
            }
        });

        suppliersListLabel = new JLabel(SUPPLIERS_LIST_LABEL_TEXT);
        suppliersListLabel.setSize(SUPPLIERS_LIST_LABEL_SIZE);
        suppliersListLabel.setLocation(SUPPLIERS_LIST_LABEL_POSITION);

        add(spareLabel);
        add(spareCombo);
        add(suppliersListLabel);
    }

    private String [][] getRecordsArray() {
        String [][] result = new String[records.size()][5];
        for (int i = 0; i < records.size(); i++) {
            result[i][0] = records.get(i).getSupplierIdStr();
            result[i][1] = records.get(i).getSupplierName();
            result[i][2] = records.get(i).getSupplierINN();
            result[i][3] = records.get(i).getDelivertTimeStr();
            result[i][4] = records.get(i).getSpareCostStr();
        }
        return result;
    }

    private void loadSpares() throws Exception {
        Statement stmt;
        spares = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM SPARES ORDER BY NAME");
        while (rs.next()) {
            spares.add(new SpareItem(rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("ARTICLE")
            ));
            System.err.println("Record from SPARES table retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SPARES done successfully");
        spareCombo.removeAllItems();
        spareCombo.addItem("");
        for (int i = 0; i < spares.size(); i++)
            spareCombo.addItem((spares.get(i).getSpareName() + " (" +
                    spares.get(i).getSpareArticle() + ")"));
        spareSelectedItem = 0;
        spareCombo.setSelectedIndex(0);
    }
}
