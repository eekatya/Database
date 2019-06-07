package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Отчёт 4. Перечень, объём и номер ячейки деталей на складе.
 */
public class WarehouseInfoReport extends ReportDialog {
    private static final String REPORT_NAME_LABEL_TEXT = "Отчёт 4. Перечень, объём и номер ячейки деталей на складе.";
    private static final String WAREHOUSE_PLACES_LABEL_TEXT = "Занятые ячейки склада";
    private static final String SPARES_COUNT_LABEL_TEXT = "Объём деталей на складе";

    private static final String[] COLUMN_NAMES1 = {
            "Деталь",
            "Артикул",
            "Место"
    };
    private static final String[][] EMPTY_DATA1 = {
            {"", "", ""},
    };
    private static final String[] COLUMN_NAMES2 = {
            "Деталь",
            "Артикул",
            "Количество"
    };
    private static final String[][] EMPTY_DATA2 = {
            {"", "", ""},
    };

    private static final Point REPORT_NAME_LABEL_POSITION = new Point(6, 16);
    private static final Dimension REPORT_NAME_LABEL_SIZE = new Dimension(600, 24);
    private static final Point WAREHOUSE_PLACES_POSITION = new Point(6, 48);
    private static final Dimension WAREHOUSE_PLACES_LABEL_SIZE = new Dimension(256, 24);
    private static final Point SCROLL_PANE1_POSITION = new Point(6, 80);
    private static final Dimension SCROLL_PANE1_SIZE = new Dimension(740, 144);
    private static final Point SPARES_COUNT_LABEL_POSITION = new Point(6, 232);
    private static final Dimension SPARES_COUNT_LABEL_SIZE = new Dimension(256, 24);
    private static final Point SCROLL_PANE2_POSITION = new Point(6, 264);
    private static final Dimension SCROLL_PANE2_SIZE = new Dimension(740, 144);

    private static final Point CLOSE_BUTTON_POSITION = new Point(345, 416);
    private static final Dimension DIALOG_DIMENSION = new Dimension(768, 494);

    protected JTable reportTable2;
    protected DefaultTableModel tableModel2;
    protected JScrollPane scrollPane2;

    private JLabel placesLabel;
    private JLabel volumeLabel;
    private ArrayList<WarehousePlaceSpare> places;
    private ArrayList<WarehouseSpareVolume> volume;

    public WarehouseInfoReport(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        places = new ArrayList<>();
        volume = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        try {
            loadWarehouseInfo();
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

        placesLabel = new JLabel(WAREHOUSE_PLACES_LABEL_TEXT);
        placesLabel.setSize(WAREHOUSE_PLACES_LABEL_SIZE);
        placesLabel.setLocation(WAREHOUSE_PLACES_POSITION);

        volumeLabel = new JLabel(SPARES_COUNT_LABEL_TEXT);
        volumeLabel.setSize(SPARES_COUNT_LABEL_SIZE);
        volumeLabel.setLocation(SPARES_COUNT_LABEL_POSITION);

        add(placesLabel);
        add(volumeLabel);
    }

    private String [][] getPlacesArray() {
        String [][] result = new String[places.size()][3];
        for (int i = 0; i < places.size(); i++) {
            result[i][0] = places.get(i).getSpareName();
            result[i][1] = places.get(i).getSpareArticle();
            result[i][2] = places.get(i).getWarehousePlaceStr();
        }
        return result;
    }

    private String [][] getVolumeArray() {
        String [][] result = new String[volume.size()][3];
        for (int i = 0; i < volume.size(); i++) {
            result[i][0] = volume.get(i).getSpareName();
            result[i][1] = volume.get(i).getSpareArticle();
            result[i][2] = volume.get(i).getSparesCountStr();
        }
        return result;
    }

    private void loadWarehouseInfo() throws Exception {
        Statement stmt;
        places = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        String sql = "SELECT SPARES.ID AS ID, SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE, WAREHOUSE_STRUCTURE.PLACE AS PLACE" +
                " FROM SPARES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE" +
                " WHERE WAREHOUSE_STRUCTURE.ACTIVE = TRUE AND WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND ORDERS_STRUCTURE.SPARE_ID = SPARES.ID" +
                " ORDER BY WAREHOUSE_STRUCTURE.PLACE";
        ResultSet rs = stmt.executeQuery( sql);
        while (rs.next()) {
            places.add(new WarehousePlaceSpare(rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("ARTICLE"),
                    rs.getInt("PLACE")
            ));
            System.err.println("Record of spare/place retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SPARES/WAREHOUSE_STRUCTURE done successfully");

        volume = new ArrayList<>();
        model.setAutoCommit(false);
        stmt = model.createStatement();
        sql = "SELECT SPARES.ID AS ID, SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE, COUNT(SPARES.ID) AS COUNT" +
                " FROM SPARES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE" +
                " WHERE WAREHOUSE_STRUCTURE.ACTIVE = TRUE AND WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND ORDERS_STRUCTURE.SPARE_ID = SPARES.ID" +
                " GROUP BY SPARES.ID" +
                " ORDER BY SPARES.NAME";
        rs = stmt.executeQuery( sql);
        while (rs.next()) {
            volume.add(new WarehouseSpareVolume(rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("ARTICLE"),
                    rs.getInt("COUNT")
            ));
            System.err.println("Record of spare/volume retrieved");
        }
        rs.close();
        stmt.close();
        model.commitConn();
        System.err.println("SELECT from SPARES/WAREHOUSE_STRUCTURE done successfully");

        if (places.size() == 0)
            tableModel.setDataVector(EMPTY_DATA1, COLUMN_NAMES1);
        else
            tableModel.setDataVector(getPlacesArray(), COLUMN_NAMES1);
        reportTable.setModel(tableModel);
        if (volume.size() == 0)
            tableModel2.setDataVector(EMPTY_DATA2, COLUMN_NAMES2);
        else
            tableModel2.setDataVector(getVolumeArray(), COLUMN_NAMES2);
        reportTable2.setModel(tableModel2);
    }
}
