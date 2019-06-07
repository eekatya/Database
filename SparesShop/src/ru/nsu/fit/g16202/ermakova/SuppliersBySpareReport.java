package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 1. Перечень поставщиков определённой категории, поставляющих указанный вид товара.
 */
public class SuppliersBySpareReport extends ReportDialog {
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 1. Перечень поставщиков определённой категории, поставляющих указанный вид товара.";
    private static final String SUPPLIER_TYPE_LABEL_TEXT = "Тип поставщика";
    private static final String SPARE_LABEL_TEXT = "Деталь";
    private static final String SUPPLIERS_COUNT_LABEL_TEXT = "Количество поставщиков";
    private static final String SUPPLIERS_LIST_LABEL_TEXT = "Перечень поставщиков";
    private static final String[] COLUMN_NAMES = {
            "ID",
            "Наименование поставщика",
            "ИНН"
    };
    private static final String[][] EMPTY_DATA = {
            {"", "", ""},
    };
    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point SUPPLIER_TYPE_LABEL_POSITION = new Point(6, 48);
    private static final Dimension SUPPLIER_TYPE_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SUPPLIER_TYPE_COMBO_POSITION = new Point(172, 48);
    private static final Dimension SUPPLIER_TYPE_COMBO_SIZE = new Dimension(384, 24);
    private static final Point SPARE_LABEL_POSITION = new Point(6, 80);
    private static final Dimension SPARE_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SPARE_COMBO_POSITION = new Point(172, 80);
    private static final Dimension SPARE_COMBO_SIZE = new Dimension(384, 24);
    private static final Point SUPPLIERS_COUNT_LABEL_POSITION = new Point(6, 112);
    private static final Dimension SUPPLIERS_COUNT_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SUPPLIERS_COUNT_FIELD_POSITION = new Point(172, 112);
    private static final Dimension SUPPLIERS_COUNT_FIELD_SIZE = new Dimension(96, 24);
    private static final Point SUPPLIERS_LIST_LABEL_POSITION = new Point(6, 144);
    private static final Dimension SUPPLIERS_LIST_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SCROLL_PANE_POSITION = new Point(6, 176);
    private static final Dimension SCROLL_PANE_SIZE = new Dimension(740, 196);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 380);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 460);

    private int supplierSelectedItem;
    private int spareSelectedItem;
    private JLabel supplierTypeLabel;
    private JLabel spareLabel;
    private JLabel suppliersCountLabel;
    private JLabel suppliersListLabel;
    private JComboBox supplierTypeCombo;
    private JComboBox spareCombo;
    private JTextField suppliersCountField;
    private int suppliersCount;
    private ArrayList<SupplierItem> records;
    private ArrayList<SupplierTypeItem> supplierTypes;
    private ArrayList<SpareItem> spares;

    public SuppliersBySpareReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        records = null;
        suppliersCount = 0;
        supplierSelectedItem = 0;
        spareSelectedItem = 0;
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

        supplierTypeLabel = new JLabel(SUPPLIER_TYPE_LABEL_TEXT);
        supplierTypeLabel.setSize(SUPPLIER_TYPE_LABEL_SIZE);
        supplierTypeLabel.setLocation(SUPPLIER_TYPE_LABEL_POSITION);
        supplierTypeCombo = new JComboBox();
        supplierTypeCombo.setSize(SUPPLIER_TYPE_COMBO_SIZE);
        supplierTypeCombo.setLocation(SUPPLIER_TYPE_COMBO_POSITION);
        supplierTypeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.err.println("supplierTypeCombo: " + supplierTypeCombo.getSelectedIndex());
                supplierSelectedItem = supplierTypeCombo.getSelectedIndex();
                if (supplierSelectedItem == -1) supplierSelectedItem = 0;
                updateReport();
            }
        });

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

        suppliersCountLabel = new JLabel(SUPPLIERS_COUNT_LABEL_TEXT);
        suppliersCountLabel.setSize(SUPPLIERS_COUNT_LABEL_SIZE);
        suppliersCountLabel.setLocation(SUPPLIERS_COUNT_LABEL_POSITION);
        suppliersCountField = new JTextField(Integer.toString(suppliersCount));
        suppliersCountField.setSize(SUPPLIERS_COUNT_FIELD_SIZE);
        suppliersCountField.setLocation(SUPPLIERS_COUNT_FIELD_POSITION);
        suppliersCountField.setEditable(false);

        suppliersListLabel = new JLabel(SUPPLIERS_LIST_LABEL_TEXT);
        suppliersListLabel.setSize(SUPPLIERS_LIST_LABEL_SIZE);
        suppliersListLabel.setLocation(SUPPLIERS_LIST_LABEL_POSITION);

        add(supplierTypeLabel);
        add(supplierTypeCombo);
        add(spareLabel);
        add(spareCombo);
        add(suppliersCountLabel);
        add(suppliersCountField);
        add(suppliersListLabel);
    }

    @Override
    protected void onStart() {
        try {
            loadSupplierTypes();
            loadSpares();
        } catch (Exception e) {
            System.err.println("Exception while retrieving report data");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
    }

    private String [][] getRecordsArray() {
        String [][] result = new String[records.size()][3];
        for (int i = 0; i < records.size(); i++) {
            result[i][0] = records.get(i).getSupplierIdStr();
            result[i][1] = records.get(i).getSupplierName();
            result[i][2] = records.get(i).getSupplierINN();
        }
        return result;
    }

    @Override
    protected void updateReport() {
        Statement stmt;
        try {
            if (spareSelectedItem == 0 || supplierSelectedItem == 0) {
                tableModel.setDataVector(EMPTY_DATA, COLUMN_NAMES);
                reportTable.setModel(tableModel);
                suppliersCount = 0;
            } else {
                records = new ArrayList<>();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                String sql = "SELECT SUPPLIERS.ID AS ID, SUPPLIERS.NAME AS NAME, SUPPLIERS.INN AS INN FROM SUPPLIERS, SPARES, SUPPLIER_SPARE" +
                        " WHERE SUPPLIERS.ID = SUPPLIER_SPARE.SUPPLIER_ID" +
                        " AND SUPPLIER_SPARE.SPARE_ID = SPARES.ID" +
                        " AND SPARES.ID = " + spares.get(spareSelectedItem - 1).getSpareIdStr() +
                        " AND SUPPLIERS.TYPE = " + supplierTypes.get(supplierSelectedItem - 1).getTypeIdStr() +
                        " ORDER BY SUPPLIERS.NAME";
                ResultSet rs = stmt.executeQuery( sql);
                while (rs.next()) {
                    records.add(new SupplierItem(rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("INN")
                    ));
                }
                suppliersCount = records.size();
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
        suppliersCountField.setText(Integer.toString(suppliersCount));
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
        spareCombo.addItem(new String(""));
        for (int i = 0; i < spares.size(); i++)
            spareCombo.addItem(new String(spares.get(i).getSpareName() + " (" +
                    spares.get(i).getSpareArticle()) + ")");
        spareSelectedItem = 0;
        spareCombo.setSelectedIndex(0);
    }

    private void loadSupplierTypes() throws Exception {
        Statement stmt;
        supplierTypes = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM SUPPLIER_TYPES ORDER BY ID");
        while (rs.next()) {
            supplierTypes.add(new SupplierTypeItem(rs.getInt("ID"),
                    rs.getString("NAME")
            ));
            System.err.println("Record from SUPPLIER_TYPES table retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SUPPLIER_TYPES done successfully");
        supplierTypeCombo.removeAllItems();
        supplierTypeCombo.addItem("");
        for (int i = 0; i < supplierTypes.size(); i++)
            supplierTypeCombo.addItem(supplierTypes.get(i).getTypeName());
        supplierSelectedItem = 0;
        supplierTypeCombo.setSelectedIndex(0);
    }
}
