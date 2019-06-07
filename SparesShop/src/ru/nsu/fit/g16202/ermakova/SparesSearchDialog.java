package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
Класс формы для поиска деталей по наименованию и стоимости
 */
public class SparesSearchDialog extends JDialog implements ActionListener {
    private static final String FIRST_RECORD_ICON = "icons/first.png";
    private static final String PREV_RECORD_ICON = "icons/prev.png";
    private static final String NEXT_RECORD_ICON = "icons/next.png";
    private static final String LAST_RECORD_ICON = "icons/last.png";
    private static final String FIRST_RECORD_CMD = "Первая страница";
    private static final String PREV_RECORD_CMD = "Предыдущая страница";
    private static final String NEXT_RECORD_CMD = "Следующая страница";
    private static final String LAST_RECORD_CMD = "Последняя страница";

    private static final String SPARE_LABEL_NAME = "Деталь";
    private static final String COST_FROM_LABEL_NAME = "Стоимость от";
    private static final String COST_TO_LABEL_NAME = "до";
    private static final String LIST_LABEL_NAME = "Список деталей";
    private static final String PAGE_LABEL_NAME = "Страница";
    private static final String PAGE_FROM_LABEL_NAME = "из";

    private static final String CLOSE_BUTTON_NAME = "Закрыть";
    private static final String CLOSE_BUTTON_CMD = "Close";

    private static final String[] COLUMN_NAMES = {
            "ID",
            "Наименование детали",
            "Артикул",
            "Мин. стоимость",
            "Макс. стоимость"
    };
    private static final String[][] EMPTY_DATA = {
            {"", "", "", "", ""},
    };

    private static final Dimension DIALOG_DIMENSION = new Dimension(640, 300);
    private static final Point TOOLBAR_POSITION = new Point(0, 0);
    private static final Dimension TOOLBAR_SIZE = new Dimension(300, 24);
    private static final Point CLOSE_BUTTON_POSITION = new Point(300, 220);
    private static final Dimension CLOSE_BUTTON_SIZE = new Dimension(90, 32);
    private static final Point SPARE_LABEL_POSITION = new Point(6, 32);
    private static final Dimension SPARE_LABEL_SIZE = new Dimension(64, 24);
    private static final Point COST_FROM_LABEL_POSITION = new Point(278, 32);
    private static final Dimension COST_FROM_LABEL_SIZE = new Dimension(96, 24);
    private static final Point COST_TO_LABEL_POSITION = new Point(482, 32);
    private static final Dimension COST_TO_LABEL_SIZE = new Dimension(32, 24);
    private static final Point LIST_LABEL_POSITION = new Point(6, 64);
    private static final Dimension LIST_LABEL_SIZE = new Dimension(96, 24);
    private static final Point PAGE_LABEL_POSITION = new Point(6, 204);
    private static final Dimension PAGE_LABEL_SIZE = new Dimension(64, 24);
    private static final Point CURRENT_PAGE_LABEL_POSITION = new Point(76, 204);
    private static final Dimension CURRENT_PAGE_LABEL_SIZE = new Dimension(16, 24);
    private static final Point PAGE_FROM_LABEL_POSITION = new Point(98, 204);
    private static final Dimension PAGE_FROM_LABEL_SIZE = new Dimension(16, 24);
    private static final Point TOTAL_PAGES_LABEL_POSITION = new Point(120, 204);
    private static final Dimension TOTAL_PAGES_LABEL_SIZE = new Dimension(16, 24);
    private static final Point SPARE_FIELD_POSITION = new Point(76, 32);
    private static final Dimension SPARE_FIELD_SIZE = new Dimension(196, 24);
    private static final Point COST_FROM_FIELD_POSITION = new Point(380, 32);
    private static final Dimension COST_FROM_FIELD_SIZE = new Dimension(96, 24);
    private static final Point COST_TO_FIELD_POSITION = new Point(520, 32);
    private static final Dimension COST_TO_FIELD_SIZE = new Dimension(96, 24);
    private static final Point REPORT_TABLE_POSITION = new Point(6, 96);
    private static final Dimension REPORT_TABLE_SIZE = new Dimension(610, 100);

    private static final int RECORDS_PER_PAGE = 3;

    private JButton btnFirstRecord;
    private JButton btnPrevRecord;
    private JButton btnNextRecord;
    private JButton btnLastRecord;

    private JLabel spareLabel;
    private JLabel costFromLabel;
    private JLabel costToLabel;
    private JLabel listLabel;
    private JLabel pageLabel;
    private JLabel currentPageLabel;
    private JLabel pageFromLabel;
    private JLabel totalPagesLabel;

    private JTextField spareField;
    private JTextField costFromField;
    private JTextField costToField;

    private JTable reportTable;
    private DefaultTableModel tableModel;
    protected JScrollPane scrollPane;
    private JToolBar toolBar;
    private JButton btnClose;
    private ShopModel model;
    private int pagesCount;
    private int currentPage;

    private ArrayList<SpareCostsItem> records;

    public SparesSearchDialog(ShopModel model, Frame owner, String title) {
        super(owner, title, true);
        this.model = model;
        pagesCount = 0;
        currentPage = 0;
        records = null;
        setSize(DIALOG_DIMENSION);
        setLocationRelativeTo(getOwner());
        setLayout(null);
        setResizable(false);
        initControls();
        initToolBar();
    }

    private void initToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnFirstRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(FIRST_RECORD_ICON)));
        btnFirstRecord.setToolTipText(FIRST_RECORD_CMD);
        btnFirstRecord.setActionCommand(FIRST_RECORD_CMD);
        btnPrevRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(PREV_RECORD_ICON)));
        btnPrevRecord.setToolTipText(PREV_RECORD_CMD);
        btnPrevRecord.setActionCommand(PREV_RECORD_CMD);
        btnNextRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(NEXT_RECORD_ICON)));
        btnNextRecord.setToolTipText(NEXT_RECORD_CMD);
        btnNextRecord.setActionCommand(NEXT_RECORD_CMD);
        btnLastRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(LAST_RECORD_ICON)));
        btnLastRecord.setToolTipText(LAST_RECORD_CMD);
        btnLastRecord.setActionCommand(LAST_RECORD_CMD);

        btnFirstRecord.addActionListener(this);
        btnPrevRecord.addActionListener(this);
        btnNextRecord.addActionListener(this);
        btnLastRecord.addActionListener(this);

        toolBar.add(btnFirstRecord);
        toolBar.add(btnPrevRecord);
        toolBar.add(btnNextRecord);
        toolBar.add(btnLastRecord);

        toolBar.setSize(TOOLBAR_SIZE);
        toolBar.setLocation(TOOLBAR_POSITION);

        add(toolBar);
    }

    public void showDialog() {
        clearFields();
        updateData();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    public class FocusListenerUpdater implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            updateData();
        }
    }

    private void initControls() {
        spareLabel = new JLabel(SPARE_LABEL_NAME);
        spareLabel.setSize(SPARE_LABEL_SIZE);
        spareLabel.setLocation(SPARE_LABEL_POSITION);
        costFromLabel = new JLabel(COST_FROM_LABEL_NAME);
        costFromLabel.setSize(COST_FROM_LABEL_SIZE);
        costFromLabel.setLocation(COST_FROM_LABEL_POSITION);
        costToLabel = new JLabel(COST_TO_LABEL_NAME);
        costToLabel.setSize(COST_TO_LABEL_SIZE);
        costToLabel.setLocation(COST_TO_LABEL_POSITION);
        listLabel = new JLabel(LIST_LABEL_NAME);
        listLabel.setSize(LIST_LABEL_SIZE);
        listLabel.setLocation(LIST_LABEL_POSITION);
        pageLabel = new JLabel(PAGE_LABEL_NAME);
        pageLabel.setSize(PAGE_LABEL_SIZE);
        pageLabel.setLocation(PAGE_LABEL_POSITION);
        currentPageLabel = new JLabel("1");
        currentPageLabel.setSize(CURRENT_PAGE_LABEL_SIZE);
        currentPageLabel.setLocation(CURRENT_PAGE_LABEL_POSITION);
        pageFromLabel = new JLabel(PAGE_FROM_LABEL_NAME);
        pageFromLabel.setSize(PAGE_FROM_LABEL_SIZE);
        pageFromLabel.setLocation(PAGE_FROM_LABEL_POSITION);
        totalPagesLabel = new JLabel("1");
        totalPagesLabel.setSize(TOTAL_PAGES_LABEL_SIZE);
        totalPagesLabel.setLocation(TOTAL_PAGES_LABEL_POSITION);

        spareField = new JTextField();
        spareField.setSize(SPARE_FIELD_SIZE);
        spareField.setLocation(SPARE_FIELD_POSITION);
        costFromField = new JTextField();
        costFromField.setSize(COST_FROM_FIELD_SIZE);
        costFromField.setLocation(COST_FROM_FIELD_POSITION);
        costToField = new JTextField();
        costToField.setSize(COST_TO_FIELD_SIZE);
        costToField.setLocation(COST_TO_FIELD_POSITION);

        spareField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateData();
            }
        });
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateData();
            }

            public void removeUpdate(DocumentEvent e) {
                updateData();
            }

            public void changedUpdate(DocumentEvent e) {
                updateData();
            }
        };
        spareField.getDocument().addDocumentListener(dl);

        costFromField.addFocusListener(new FocusListenerUpdater());
        costToField.addFocusListener(new FocusListenerUpdater());

        reportTable = new JTable();
        tableModel = new DefaultTableModel(EMPTY_DATA, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells false => table is readonly
            }
        };
        reportTable.setModel(tableModel);
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setSize(REPORT_TABLE_SIZE);
        scrollPane.setLocation(REPORT_TABLE_POSITION);

        btnClose = new JButton(CLOSE_BUTTON_NAME);
        btnClose.setSize(CLOSE_BUTTON_SIZE);
        btnClose.setLocation(CLOSE_BUTTON_POSITION);
        btnClose.setActionCommand(CLOSE_BUTTON_CMD);
        btnClose.addActionListener(this);

        add(spareLabel);
        add(costFromLabel);
        add(costToLabel);
        add(listLabel);
        add(pageLabel);
        add(currentPageLabel);
        add(pageFromLabel);
        add(totalPagesLabel);

        add(spareField);
        add(costFromField);
        add(costToField);

        add(scrollPane);
        add(btnClose);
    }

    private void clearFields() {
        spareField.setText(null);
        costFromField.setText(null);
        costToField.setText(null);
    }

    public void closeDialog() {
        setVisible(false);
    }

    private void setFirstRecord() {
        btnFirstRecord.setEnabled(false);
        btnPrevRecord.setEnabled(false);
    }

    private void setLastRecord() {
        btnLastRecord.setEnabled(false);
        btnNextRecord.setEnabled(false);
    }

    private void setEmptyTable() {
        setFirstRecord();
        setLastRecord();
    }

    private void setOneRecord() {
        setFirstRecord();
        setLastRecord();
    }

    private void setInnerRecord() {
        btnFirstRecord.setEnabled(true);
        btnPrevRecord.setEnabled(true);
        btnLastRecord.setEnabled(true);
        btnNextRecord.setEnabled(true);
    }

    private void setButtonsState() {
        pagesCount = (records.size() == 0) ? 0 : (records.size() - 1) / RECORDS_PER_PAGE + 1;
        switch (pagesCount) {
            case 0:
                setEmptyTable();
//                setElementsEditable(false);
//                setElementsEmpty();
                break;
            case 1:
                setOneRecord();
//                setElementsEditable(true);
                break;
            default:
                setInnerRecord();
//                setElementsEditable(true);
                if (currentPage == 0)
                    setFirstRecord();
                else if (currentPage == pagesCount - 1)
                    setLastRecord();
                break;
        }
    }

    private void firstPage() {
        currentPage = 0;
        showData();
    }

    private void prevPage() {
        currentPage--;
        showData();
    }

    private void nextPage() {
        currentPage++;
        showData();
    }

    private void lastPage() {
        currentPage = pagesCount - 1;
        showData();
    }

    private String [][] getRecordsArray() {
        String [][] result = new String[RECORDS_PER_PAGE][5];
        int start = currentPage * RECORDS_PER_PAGE;
        for (int i = 0; i < RECORDS_PER_PAGE; i++) {
            if (i + start < records.size()) {
                result[i][0] = records.get(i + start).getSpareIdStr();
                result[i][1] = records.get(i + start).getSpareName();
                result[i][2] = records.get(i + start).getSpareArticle();
                result[i][3] = records.get(i + start).getMinCostStr();
                result[i][4] = records.get(i + start).getMaxCostStr();
            } else {
                result[i][0] = "";
                result[i][1] = "";
                result[i][2] = "";
                result[i][3] = "";
                result[i][4] = "";
            }
        }
        return result;
    }

    protected void updateData() {
        Statement stmt;
        try {
            records = new ArrayList<>();
            model.setAutoCommit(false);
            stmt = model.createStatement();
            String sql = "SELECT SPARES.ID AS ID, SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE" +
                    " FROM SPARES";
            if (spareField.getText() != null && !(spareField.getText().equals("")))
                sql += " WHERE SPARES.NAME LIKE '%" + spareField.getText() + "%'";
            sql += " ORDER BY SPARES.ID";
            ResultSet rs = stmt.executeQuery( sql);
            ArrayList<SpareItem> spares = new ArrayList<>();
            while (rs.next()) {
                spares.add(new SpareItem(rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("ARTICLE")
                ));
            }
            rs.close();
            stmt.close();
            model.commitConn();
            boolean minCostSet = false;
            boolean maxCostSet = false;
            int minCost = 0;
            int maxCost = 0;
            minCostSet = (costFromField.getText() != null && !(costFromField.getText().equals("")));
            if (minCostSet) {
                minCost = model.parseTextCost(costFromField.getText());
                if (minCost == -1) {
                    minCost = 0;
                    costFromField.setText(null);
                    minCostSet = false;
                }
            }
            maxCostSet = (costToField.getText() != null && !(costToField.getText().equals("")));
            if (maxCostSet) {
                maxCost = model.parseTextCost(costToField.getText());
                if (maxCost == -1) {
                    maxCost = 0;
                    costToField.setText(null);
                    maxCostSet = false;
                }
            }
            int min, max;
            String strResult;
            for (int i = 0; i < spares.size(); i++) {
                min = max = 0;
                stmt = model.createStatement();
                sql = "SELECT MIN(SUPPLIER_SPARE.COST)" +
                        " FROM SUPPLIER_SPARE" +
                        " WHERE SUPPLIER_SPARE.SPARE_ID = " + spares.get(i).getId();
                rs = stmt.executeQuery( sql);
                if (rs.next()) {
                    strResult = rs.getString("MIN");
                    min = (strResult == null) ? 0 : model.parseCost(strResult);
                }
                rs.close();
                stmt.close();
                model.commitConn();

                stmt = model.createStatement();
                sql = "SELECT MAX(SUPPLIER_SPARE.COST)" +
                        " FROM SUPPLIER_SPARE" +
                        " WHERE SUPPLIER_SPARE.SPARE_ID = " + spares.get(i).getId();
                rs = stmt.executeQuery( sql);
                if (rs.next()) {
                    strResult = rs.getString("MAX");
                    max = (strResult == null) ? 0 : model.parseCost(strResult);
                }
                rs.close();
                stmt.close();
                model.commitConn();

                boolean filterSpare = minCostSet && (max < minCost);
                if (!filterSpare)
                    filterSpare = maxCostSet && (min > maxCost);
                if (!filterSpare) {
                    records.add(new SpareCostsItem(spares.get(i).getId(),
                            spares.get(i).getSpareName(),
                            spares.get(i).getSpareArticle(),
                            min,
                            max));
                }
            }
            showData();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
    }

    private void showData() {
        pagesCount = (records.size() == 0) ? 0 : (records.size() - 1) / RECORDS_PER_PAGE + 1;
        if (currentPage >= pagesCount) currentPage = pagesCount - 1;
        if (currentPage < 0) currentPage = 0;
        currentPageLabel.setText(String.valueOf(currentPage + 1));
        totalPagesLabel.setText(String.valueOf(pagesCount));
        if (records.size() > 0) {
            tableModel.setDataVector(getRecordsArray(), COLUMN_NAMES);
            reportTable.setModel(tableModel);
        } else {
            tableModel.setDataVector(EMPTY_DATA, COLUMN_NAMES);
            reportTable.setModel(tableModel);
        }
        setButtonsState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case FIRST_RECORD_CMD:
                firstPage();
                break;
            case PREV_RECORD_CMD:
                prevPage();
                break;
            case NEXT_RECORD_CMD:
                nextPage();
                break;
            case LAST_RECORD_CMD:
                lastPage();
                break;
            case CLOSE_BUTTON_CMD:
                closeDialog();
                break;
        }
    }
}
