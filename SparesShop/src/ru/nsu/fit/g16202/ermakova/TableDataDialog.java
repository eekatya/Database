package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/*
Базовый класс формы работы с одной таблицей
 */
public abstract class TableDataDialog extends JDialog implements ActionListener {
    private static final String FIRST_RECORD_ICON = "icons/first.png";
    private static final String PREV_RECORD_ICON = "icons/prev.png";
    private static final String NEXT_RECORD_ICON = "icons/next.png";
    private static final String LAST_RECORD_ICON = "icons/last.png";
    private static final String NEW_RECORD_ICON = "icons/new.png";
    private static final String DELETE_RECORD_ICON = "icons/delete.png";

    private static final String FIRST_RECORD_CMD = "Первая запись";
    private static final String PREV_RECORD_CMD = "Предыдущая запись";
    private static final String NEXT_RECORD_CMD = "Следующая запись";
    private static final String LAST_RECORD_CMD = "Последняя запись";
    private static final String NEW_RECORD_CMD = "Новая запись";
    private static final String DELETE_RECORD_CMD = "Удалить запись";
    private static final String CLOSE_BUTTON_CMD = "Close";

    private static final Dimension TOOLBAR_SIZE = new Dimension(200, 24);
    private static final Point TOOLBAR_POSITION = new Point(0, 0);
    private static final Dimension CLOSE_BUTTON_SIZE = new Dimension(90, 32);
    private static final Point CLOSE_BUTTON_FIRST_POSITION = new Point(200, 400);
    private static final String CLOSE_BUTTON_NAME = "Закрыть";

    private JButton btnFirstRecord;
    private JButton btnPrevRecord;
    private JButton btnNextRecord;
    private JButton btnLastRecord;
    private JButton btnNewRecord;
    private JButton btnDeleteRecord;

    private JToolBar toolBar;
    protected JButton btnClose;
    protected ShopModel model;
    protected boolean recordModified;
    protected boolean newRecord;
    protected int recordNum;
    protected int positionedId;

    public TableDataDialog(ShopModel model, Frame owner, String title, Dimension dim) {
        super(owner, title, true);
        this.model = model;
        recordModified = false;
        newRecord = false;
        recordNum = -1;
        positionedId = -1;
        setSize(dim);
        setLocationRelativeTo(getOwner());
        setLayout(null);
        setResizable(false);
        initCloseButton();
        initControls();
        initToolBar();
    }

    public void setPositionedId(int id) {
        positionedId = id;
    }

    private void initCloseButton() {
        btnClose = new JButton(CLOSE_BUTTON_NAME);
        btnClose.setSize(CLOSE_BUTTON_SIZE);
        btnClose.setLocation(CLOSE_BUTTON_FIRST_POSITION);
        btnClose.setActionCommand(CLOSE_BUTTON_CMD);
        btnClose.addActionListener(this);
        add(btnClose);
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
        btnNewRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(NEW_RECORD_ICON)));
        btnNewRecord.setToolTipText(NEW_RECORD_CMD);
        btnNewRecord.setActionCommand(NEW_RECORD_CMD);
        btnDeleteRecord = new JButton(new ImageIcon(TableDataDialog.class.getResource(DELETE_RECORD_ICON)));
        btnDeleteRecord.setToolTipText(DELETE_RECORD_CMD);
        btnDeleteRecord.setActionCommand(DELETE_RECORD_CMD);

        btnFirstRecord.addActionListener(this);
        btnPrevRecord.addActionListener(this);
        btnNextRecord.addActionListener(this);
        btnLastRecord.addActionListener(this);
        btnNewRecord.addActionListener(this);
        btnDeleteRecord.addActionListener(this);

        toolBar.add(btnFirstRecord);
        toolBar.add(btnPrevRecord);
        toolBar.add(btnNextRecord);
        toolBar.add(btnLastRecord);
        toolBar.addSeparator();
        toolBar.add(btnNewRecord);
        toolBar.addSeparator();
        toolBar.add(btnDeleteRecord);

        toolBar.setSize(TOOLBAR_SIZE);
        toolBar.setLocation(TOOLBAR_POSITION);

        add(toolBar);
    }

    public void showDialog() {
        retrieveData();
        moveToID(positionedId);
        setLocationRelativeTo(getOwner());
        setVisible(true);
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
        btnDeleteRecord.setEnabled(false);
    }

    private void setOneRecord() {
        setFirstRecord();
        setLastRecord();
        btnDeleteRecord.setEnabled(true);
    }

    private void setInnerRecord() {
        btnFirstRecord.setEnabled(true);
        btnPrevRecord.setEnabled(true);
        btnLastRecord.setEnabled(true);
        btnNextRecord.setEnabled(true);
        btnDeleteRecord.setEnabled(true);
    }

    protected void addModifyListener(JTextField control) {
        control.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                recordModified = true;
            }
        });
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                recordModified = true;
            }

            public void removeUpdate(DocumentEvent e) {
                recordModified = true;
            }

            public void changedUpdate(DocumentEvent e) {
                recordModified = true;
            }
        };
        control.getDocument().addDocumentListener(dl);
    }

    private void setButtonsState() {
        switch (getDataSize()) {
            case 0:
                setEmptyTable();
                setElementsEditable(false);
                setElementsEmpty();
                break;
            case 1:
                setOneRecord();
                setElementsEditable(true);
                break;
            default:
                setInnerRecord();
                setElementsEditable(true);
                if (recordNum == 0)
                    setFirstRecord();
                else if (recordNum == getDataSize() - 1)
                    setLastRecord();
                break;
        }
    }

    private void firstRecord() {
        if (newRecord) {
            addNewRecord();
        } else {
            if (!updateData()) return;
            moveToFirstRecord();
        }
    }

    private void prevRecord() {
        if (newRecord) {
            addNewRecord();
        } else {
            if (!updateData()) return;
            recordNum--;
            setButtonsState();
            showData();
        }
    }

    private void nextRecord() {
        if (newRecord) {
            addNewRecord();
        } else {
            if (!updateData()) return;
            recordNum++;
            setButtonsState();
            showData();
        }
    }

    private void lastRecord() {
        if (newRecord) {
            addNewRecord();
        } else {
            if (!updateData()) return;
            recordNum = getDataSize() - 1;
            setButtonsState();
            showData();
        }
    }

    private void newRecord() {
        setElementsEmpty();
        setElementsEditable(true);
        setInnerRecord();
        newRecord = true;
    }

    private void moveToID(int id) {
        recordNum = (getDataSize() > 0) ? 0 : -1;
        for (int i = 0; i < getDataSize(); i++)
            if (getDataId(i) == id) {
                recordNum = i;
                break;
            }
        setButtonsState();
        showData();
    }

    private void moveToFirstRecord() {
        recordNum = (getDataSize() > 0) ? 0 : -1;
        setButtonsState();
        showData();
    }

    private void addNewRecord() {
        newRecord = false;
        if (recordModified) {
            recordModified = false;
            Statement stmt;
            String sql;
            int maxID = 0;
            try {
                checkData();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                ResultSet rs = stmt.executeQuery(formatMaxIdSQL());
                while (rs.next()) {
                    maxID = rs.getInt("MAX");
                }
                rs.close();
                stmt.close();
                model.commitConn();
                System.err.println("SELECT from done successfully");

                model.setAutoCommit(false);
                stmt = model.createStatement();
                stmt.executeUpdate(formatInsertSQL(maxID + 1));
                stmt.close();
                model.commitConn();
                System.err.println("New record inserted successfully");
            } catch (DataException e) {
                System.err.println("Incorrect data entered");
                DBMessages.showErrorMessage(this, DBMessages.INPUT_DATA_ERROR_MESSAGE);
                newRecord = true;
                recordModified = true;
                return;
            } catch (ParseException e) {
                System.err.println("Incorrect data entered");
                DBMessages.showErrorMessage(this, DBMessages.INPUT_DATA_ERROR_MESSAGE);
                newRecord = true;
                recordModified = true;
                return;
            } catch (Exception e) {
                System.err.println("Exception in connection to database");
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
                newRecord = true;
                recordModified = true;
                return;
            }
            retrieveData();
            moveToID(maxID + 1);
        } else {
            System.err.println("Record not modified. Cancel adding.");
            retrieveData();
            moveToFirstRecord();
        }
    }

    private void deleteRecord() {
        if (newRecord) {
            setButtonsState();
            showData();
        } else {
            if (hasLinks()) {
                DBMessages.showErrorMessage(this, DBMessages.LINKS_TO_KEY_EXISTS_MSG);
                return;
            }
            Statement stmt;
            try {
                model.setAutoCommit(false);
                stmt = model.createStatement();
                stmt.executeUpdate( formatDeleteSQL());
                model.commitConn();
                stmt.close();
                System.err.println("Record deleted");
                removeCurrentDataRecord();
                if (recordNum >= getDataSize()) recordNum--;
                else moveToID(getCurrentDataId());
                setButtonsState();
                showData();
            } catch (Exception e) {
                System.err.println("Exception in DELETE from Managers ");
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
                return;
            }
        }
    }

    private void retrieveData() {
        clearDataList();
        try {
            model.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Statement stmt = model.createStatement();
             ResultSet rs = stmt.executeQuery(formatSelectSQL())) {
            while (rs.next()) {
                insertDataRecord(rs);
            }
            model.commitConn();
        } catch (Exception e) {
            System.err.println("Exception in SELECT");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
//        Statement stmt;
//        clearDataList();
//        try {
//            model.setAutoCommit(false);
//            stmt = model.createStatement();
//            ResultSet rs = stmt.executeQuery(formatSelectSQL());
//            while (rs.next()) {
//                insertDataRecord(rs);
//                System.err.println("Record from table retrieved");
//            }
//            rs.close();
//            stmt.close();
//            model.commitConn();
//            System.err.println("SELECT done successfully");
//        } catch (Exception e) {
//            System.err.println("Exception in SELECT");
//            e.printStackTrace();
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
//            return;
//        }
    }

    protected boolean updateData() {
        Statement stmt;
        if (recordModified) {
            try {
                checkData();
                model.setAutoCommit(false);
                stmt = model.createStatement();
                stmt.executeUpdate(formatUpdateSQL());
                model.commitConn();
                stmt.close();
            } catch (DataException e) {
                System.err.println("Incorrect data entered");
                DBMessages.showErrorMessage(this, DBMessages.INPUT_DATA_ERROR_MESSAGE);
                return false;
            } catch (ParseException e) {
                System.err.println("Incorrect data entered");
                DBMessages.showErrorMessage(this, DBMessages.INPUT_DATA_ERROR_MESSAGE);
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            updateDataRecord();
        }
        return true;
    }

    private boolean hasLinks() {
        Statement stmt;
        boolean result = false;
        try {
            model.setAutoCommit(false);
            stmt = model.createStatement();
            ResultSet rs = stmt.executeQuery(formatCheckLinksSQL());
            if (rs.next()) {
                int cnt = rs.getInt("COUNT");
                result = (cnt > 0);
            }
            rs.close();
            stmt.close();
            model.commitConn();
            System.err.println("SELECT done successfully");
        } catch (Exception e) {
            System.err.println("Exception in SELECT");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
        }
        return result;
    }

    protected abstract void initControls();

    protected abstract String formatCheckLinksSQL();
    protected abstract String formatInsertSQL(int id) throws ParseException;
    protected abstract String formatDeleteSQL();
    protected abstract String formatSelectSQL();
    protected abstract String formatUpdateSQL() throws ParseException;
    protected abstract String formatMaxIdSQL();

    protected abstract int getDataSize();
    protected abstract void removeCurrentDataRecord();
    protected abstract void clearDataList();
    protected abstract void insertDataRecord(ResultSet rs) throws SQLException;
    protected abstract void updateDataRecord();
    protected abstract int getCurrentDataId();
    protected abstract int getDataId(int pos);

    protected abstract void checkData() throws DataException, ParseException;
    protected abstract void showData();
    protected abstract void setElementsEmpty();
    protected abstract void setElementsEditable(boolean b);

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case FIRST_RECORD_CMD:
                firstRecord();
                break;
            case PREV_RECORD_CMD:
                prevRecord();
                break;
            case NEXT_RECORD_CMD:
                nextRecord();
                break;
            case LAST_RECORD_CMD:
                lastRecord();
                break;
            case NEW_RECORD_CMD:
                newRecord();
                break;
            case DELETE_RECORD_CMD:
                deleteRecord();
                break;
            case CLOSE_BUTTON_CMD:
                closeDialog();
                break;
        }
    }
}
