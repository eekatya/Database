package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 3. Перечень покупателей, купивших указанный вид товара за период.
 */
public class BuyersInfoReport extends ReportDialog {
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 3. Перечень покупателей, купивших указанный вид товара за период.";
    private static final String SPARE_LABEL_TEXT = "Деталь";
    private static final String DATE_FROM_LABEL_TEXT = "Покупки с";
    private static final String DATE_TO_LABEL_TEXT = "по";
    private static final String BUYERS_COUNT_LABEL_TEXT = "Количество покупателей";
    private static final String PRIVATE_BUYERS_LIST_LABEL_TEXT = "Перечень покупателей - физ. лиц";
    private static final String LEGAL_BUYERS_LIST_LABEL_TEXT = "Перечень покупателей - юр. лиц";
    private static final String[] COLUMN_NAMES1 = {
            "ФИО",
            "Паспорт",
            "Регистрация",
            "Телефон",
            "Email",
            "Адрес"
    };
    private static final String[][] EMPTY_DATA1 = {
            {"", "", "", "", "", ""},
    };
    private static final String[] COLUMN_NAMES2 = {
            "Наименование",
            "Юр. адрес",
            "ИНН",
            "Контакт",
            "Телефон",
            "Email",
            "Адрес"
    };
    private static final String[][] EMPTY_DATA2 = {
            {"", "", "", "", "", "", ""},
    };
    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point SPARE_LABEL_POSITION = new Point(6, 48);
    private static final Dimension SPARE_LABEL_SIZE = new Dimension(160, 24);
    private static final Point SPARE_COMBO_POSITION = new Point(172, 48);
    private static final Dimension SPARE_COMBO_SIZE = new Dimension(384, 24);
    private static final Point DATE_FROM_LABEL_POSITION = new Point(6, 80);
    private static final Dimension DATE_FROM_LABEL_SIZE = new Dimension(96, 24);
    private static final Point DATE_FROM_FIELD_POSITION = new Point(172, 80);
    private static final Dimension DATE_FROM_FIELD_SIZE = new Dimension(96, 24);
    private static final Point DATE_TO_LABEL_POSITION = new Point(274, 80);
    private static final Dimension DATE_TO_LABEL_SIZE = new Dimension(96, 24);
    private static final Point DATE_TO_FIELD_POSITION = new Point(344, 80);
    private static final Dimension DATE_TO_FIELD_SIZE = new Dimension(96, 24);
    private static final Point BUYERS_COUNT_LABEL_POSITION = new Point(6, 112);
    private static final Dimension BUYERS_COUNT_LABEL_SIZE = new Dimension(160, 24);
    private static final Point BUYERS_COUNT_FIELD_POSITION = new Point(172, 112);
    private static final Dimension BUYERS_COUNT_FIELD_SIZE = new Dimension(96, 24);

    private static final Point PRIVATE_BUYERS_LIST_LABEL_POSITION = new Point(6, 144);
    private static final Dimension PRIVATE_BUYERS_LIST_LABEL_SIZE = new Dimension(256, 24);
    private static final Point LEGAL_BUYERS_LIST_LABEL_POSITION = new Point(6, 328);
    private static final Dimension LEGAL_BUYERS_LIST_LABEL_SIZE = new Dimension(256, 24);
    private static final Point SCROLL_PANE1_POSITION = new Point(6, 176);
    private static final Dimension SCROLL_PANE1_SIZE = new Dimension(740, 144);
    private static final Point SCROLL_PANE2_POSITION = new Point(6, 360);
    private static final Dimension SCROLL_PANE2_SIZE = new Dimension(740, 144);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 512);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 590);

    protected JTable reportTable2;
    protected DefaultTableModel tableModel2;
    protected JScrollPane scrollPane2;
    private int spareSelectedItem;
    private int buyersCount;
    private JLabel spareLabel;
    private JLabel dateFromLabel;
    private JLabel dateToLabel;
    private JLabel buyersCountLabel;
    private JLabel privateBuyersListLabel;
    private JLabel legalBuyersListLabel;
    private JComboBox spareCombo;
    private ReportDateTextField dateFromField;
    private ReportDateTextField dateToField;
    private JTextField buyersCountField;
    private ArrayList<SpareItem> spares;
    private ArrayList<PrivateBuyer> privateBuyers;
    private ArrayList<LegalBuyer> legalBuyers;

    public BuyersInfoReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        privateBuyers = new ArrayList<>();
        legalBuyers = new ArrayList<>();
        spares = null;
        spareSelectedItem = 0;
        buyersCount = 0;
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
                tableModel.setDataVector(EMPTY_DATA1, COLUMN_NAMES1);
                reportTable.setModel(tableModel);
                tableModel2.setDataVector(EMPTY_DATA2, COLUMN_NAMES2);
                reportTable2.setModel(tableModel2);
            } else {
                privateBuyers = new ArrayList<>();
                legalBuyers = new ArrayList<>();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                StringBuilder sql = new StringBuilder("");
                sql.append("SELECT BUYERS.ID AS ID, PRIVATE_BUYERS.ID AS BUYER_ID, PRIVATE_BUYERS.LNAME AS LNAME, PRIVATE_BUYERS.FNAME AS FNAME, PRIVATE_BUYERS.MNAME AS MNAME,");
                sql.append(" PRIVATE_BUYERS.PASSPORT AS PASSPORT, PRIVATE_BUYERS.REGISTRATION AS REGISTRATION, BUYERS.PHONE AS PHONE, BUYERS.EMAIL AS EMAIL, BUYERS.ADDRESS AS ADDRESS");
                sql.append(" FROM PRIVATE_BUYERS, BUYERS");
                sql.append(" WHERE PRIVATE_BUYERS.ID IN (");
                sql.append(" SELECT BUYERS.BUYER_ID AS BUYER_ID");
                sql.append(" FROM BUYERS, SALES, WAREHOUSE_SALES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE, SPARES");
                sql.append(" WHERE SALES.BUYER_ID = BUYERS.ID AND");
                sql.append(" BUYERS.TYPE = 1 AND"); // Физ. лица!
                if (dateFromField.getDateValue() != null) {
                    sql.append(" SALES.SALE_DATE >= '");
                    sql.append(ShopModel.dateToString(dateFromField.getDateValue()));
                    sql.append("' AND");
                }
                if (dateToField.getDateValue() != null) {
                    sql.append(" SALES.SALE_DATE <= '");
                    sql.append(ShopModel.dateToString(dateToField.getDateValue()));
                    sql.append("' AND");
                }
                sql.append(" SALES.ID = WAREHOUSE_SALES.SALE_ID AND");
                sql.append(" WAREHOUSE_SALES.WAREHOUSE_ID = WAREHOUSE_STRUCTURE.ID AND");
                sql.append(" WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND");
                sql.append(" ORDERS_STRUCTURE.SPARE_ID = SPARES.ID AND");
                sql.append(" SPARES.ID = ");
                sql.append(spares.get(spareSelectedItem - 1).getSpareIdStr());
                sql.append(" GROUP BY BUYERS.ID");
                sql.append(" ORDER BY BUYERS.ID) AND");
                sql.append(" PRIVATE_BUYERS.ID = BUYERS.BUYER_ID  AND");
                sql.append(" BUYERS.TYPE = 1");
                sql.append(" GROUP BY PRIVATE_BUYERS.ID, BUYERS.ID");
                sql.append(" ORDER BY LNAME, FNAME, MNAME, PRIVATE_BUYERS.ID");

                ResultSet rs = stmt.executeQuery(sql.toString());
                while (rs.next()) {
                    privateBuyers.add(new PrivateBuyer(rs.getInt("BUYER_ID"),
                            rs.getString("LNAME"),
                            rs.getString("FNAME"),
                            rs.getString("MNAME"),
                            rs.getString("PASSPORT"),
                            rs.getString("REGISTRATION"),
                            rs.getString("PHONE"),
                            rs.getString("EMAIL"),
                            rs.getString("ADDRESS")
                    ));
                }
                rs.close();
                stmt.close();
                model.commitConn();
                System.err.println("Private Buyers selected successfully");
                tableModel.setDataVector(getPrivateBuyersArray(), COLUMN_NAMES1);
                reportTable.setModel(tableModel);

                stmt = model.createStatement();
                sql = new StringBuilder("");
                sql.append("SELECT BUYERS.ID AS ID, LEGAL_BUYERS.ID AS BUYER_ID, LEGAL_BUYERS.NAME AS NAME, LEGAL_BUYERS.ADDRESS AS LEGAL_ADDRESS, LEGAL_BUYERS.INN AS INN,");
                sql.append(" LEGAL_BUYERS.CONTACT AS CONTACT, BUYERS.PHONE AS PHONE, BUYERS.EMAIL AS EMAIL, BUYERS.ADDRESS AS ADDRESS");
                sql.append(" FROM LEGAL_BUYERS, BUYERS");
                sql.append(" WHERE LEGAL_BUYERS.ID IN (");
                sql.append(" SELECT BUYERS.BUYER_ID AS BUYER_ID");
                sql.append(" FROM BUYERS, SALES, WAREHOUSE_SALES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE, SPARES");
                sql.append(" WHERE SALES.BUYER_ID = BUYERS.ID AND");
                sql.append(" BUYERS.TYPE = 2 AND"); // Юр. лица!
                if (dateFromField.getDateValue() != null) {
                    sql.append(" SALES.SALE_DATE >= '");
                    sql.append(ShopModel.dateToString(dateFromField.getDateValue()));
                    sql.append("' AND");
                }
                if (dateToField.getDateValue() != null) {
                    sql.append(" SALES.SALE_DATE <= '");
                    sql.append(ShopModel.dateToString(dateToField.getDateValue()));
                    sql.append("' AND");
                }
                sql.append(" SALES.ID = WAREHOUSE_SALES.SALE_ID AND");
                sql.append(" WAREHOUSE_SALES.WAREHOUSE_ID = WAREHOUSE_STRUCTURE.ID AND");
                sql.append(" WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND");
                sql.append(" ORDERS_STRUCTURE.SPARE_ID = SPARES.ID AND");
                sql.append(" SPARES.ID = ");
                sql.append(spares.get(spareSelectedItem - 1).getSpareIdStr());
                sql.append(" GROUP BY BUYERS.ID");
                sql.append(" ORDER BY BUYERS.ID) AND");
                sql.append(" LEGAL_BUYERS.ID = BUYERS.BUYER_ID AND");
                sql.append(" BUYERS.TYPE = 2");
                sql.append(" GROUP BY LEGAL_BUYERS.ID, BUYERS.ID");
                sql.append(" ORDER BY NAME, INN, LEGAL_BUYERS.ID");

                rs = stmt.executeQuery(sql.toString());
                while (rs.next()) {
                    legalBuyers.add(new LegalBuyer(rs.getString("NAME"),
                            rs.getString("LEGAL_ADDRESS"),
                            rs.getString("INN"),
                            rs.getString("CONTACT"),
                            rs.getString("PHONE"),
                            rs.getString("EMAIL"),
                            rs.getString("ADDRESS")
                    ));
                }
                rs.close();
                stmt.close();
                model.commitConn();
                System.err.println("Legal Buyers selected successfully");
                tableModel2.setDataVector(getLegalBuyersArray(), COLUMN_NAMES2);
                reportTable2.setModel(tableModel2);
            }
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
        buyersCountField.setText(Integer.toString(privateBuyers.size() + legalBuyers.size()));
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

        dateFromLabel = new JLabel(DATE_FROM_LABEL_TEXT);
        dateFromLabel.setSize(DATE_FROM_LABEL_SIZE);
        dateFromLabel.setLocation(DATE_FROM_LABEL_POSITION);
        dateFromField = new ReportDateTextField("", this);
        dateFromField.setSize(DATE_FROM_FIELD_SIZE);
        dateFromField.setLocation(DATE_FROM_FIELD_POSITION);

        dateToLabel = new JLabel(DATE_TO_LABEL_TEXT);
        dateToLabel.setSize(DATE_TO_LABEL_SIZE);
        dateToLabel.setLocation(DATE_TO_LABEL_POSITION);
        dateToField = new ReportDateTextField("", this);
        dateToField.setSize(DATE_TO_FIELD_SIZE);
        dateToField.setLocation(DATE_TO_FIELD_POSITION);

        buyersCountLabel = new JLabel(BUYERS_COUNT_LABEL_TEXT);
        buyersCountLabel.setSize(BUYERS_COUNT_LABEL_SIZE);
        buyersCountLabel.setLocation(BUYERS_COUNT_LABEL_POSITION);
        buyersCountField = new JTextField(Integer.toString(buyersCount));
        buyersCountField.setSize(BUYERS_COUNT_FIELD_SIZE);
        buyersCountField.setLocation(BUYERS_COUNT_FIELD_POSITION);
        buyersCountField.setEditable(false);

        privateBuyersListLabel = new JLabel(PRIVATE_BUYERS_LIST_LABEL_TEXT);
        privateBuyersListLabel.setSize(PRIVATE_BUYERS_LIST_LABEL_SIZE);
        privateBuyersListLabel.setLocation(PRIVATE_BUYERS_LIST_LABEL_POSITION);

        legalBuyersListLabel = new JLabel(LEGAL_BUYERS_LIST_LABEL_TEXT);
        legalBuyersListLabel.setSize(LEGAL_BUYERS_LIST_LABEL_SIZE);
        legalBuyersListLabel.setLocation(LEGAL_BUYERS_LIST_LABEL_POSITION);

        add(spareLabel);
        add(spareCombo);
        add(dateFromLabel);
        add(dateFromField);
        add(dateToLabel);
        add(dateToField);
        add(buyersCountLabel);
        add(buyersCountField);
        add(privateBuyersListLabel);
        add(legalBuyersListLabel);
    }

    private String [][] getPrivateBuyersArray() {
        String [][] result = new String[privateBuyers.size()][6];
        for (int i = 0; i < privateBuyers.size(); i++) {
            result[i][0] = privateBuyers.get(i).getFullName();
            result[i][1] = privateBuyers.get(i).getPassportData();
            result[i][2] = privateBuyers.get(i).getRegistrationData();
            result[i][3] = privateBuyers.get(i).getPhone();
            result[i][4] = privateBuyers.get(i).getEmail();
            result[i][5] = privateBuyers.get(i).getActualAddress();
        }
        return result;
    }

    private String [][] getLegalBuyersArray() {
        String [][] result = new String[legalBuyers.size()][7];
        for (int i = 0; i < legalBuyers.size(); i++) {
            result[i][0] = legalBuyers.get(i).getCompanyName();
            result[i][1] = legalBuyers.get(i).getLegalAddress();
            result[i][2] = legalBuyers.get(i).getINN();
            result[i][3] = legalBuyers.get(i).getContact();
            result[i][4] = legalBuyers.get(i).getPhone();
            result[i][5] = legalBuyers.get(i).getEmail();
            result[i][6] = legalBuyers.get(i).getActualAddress();
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
