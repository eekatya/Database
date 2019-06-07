package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/*
Форма для работы с таблицей менеджеров
 */
public class ManagersReference extends TableDataDialog {
    private static final String ID_LABEL_TEXT = "Идентификатор";
    private static final String LNAME_LABEL_TEXT = "Фамилия";
    private static final String FNAME_LABEL_TEXT = "Имя";
    private static final String MNAME_LABEL_TEXT = "Отчество";
    private static final String PASSPORT_LABEL_TEXT = "Паспортные данные";
    private static final String HIRE_LABEL_TEXT = "Дата приёма на работу";
    private static final String DISMISS_LABEL_TEXT = "Дата увольнения";

    private static final Point ID_LABEL_POSITION = new Point(6, 32);
    private static final Dimension ID_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LNAME_LABEL_POSITION = new Point(6, 64);
    private static final Dimension LNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point FNAME_LABEL_POSITION = new Point(6, 96);
    private static final Dimension FNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point MNAME_LABEL_POSITION = new Point(6, 128);
    private static final Dimension MNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PASSPORT_LABEL_POSITION = new Point(6, 160);
    private static final Dimension PASSPORT_LABEL_SIZE = new Dimension(128, 24);
    private static final Point HIRE_LABEL_POSITION = new Point(6, 192);
    private static final Dimension HIRE_LABEL_SIZE = new Dimension(128, 24);
    private static final Point DISMISS_LABEL_POSITION = new Point(6, 224);
    private static final Dimension DISMISS_LABEL_SIZE = new Dimension(128, 24);

    private static final Point ID_TEXT_POSITION = new Point(140, 32);
    private static final Dimension ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point LNAME_TEXT_POSITION = new Point(140, 64);
    private static final Dimension LNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point FNAME_TEXT_POSITION = new Point(140, 96);
    private static final Dimension FNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point MNAME_TEXT_POSITION = new Point(140, 128);
    private static final Dimension MNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point PASSPORT_TEXT_POSITION = new Point(140, 160);
    private static final Dimension PASSPORT_TEXT_SIZE = new Dimension(288, 24);
    private static final Point HIRE_TEXT_POSITION = new Point(140, 192);
    private static final Dimension HIRE_TEXT_SIZE = new Dimension(96, 24);
    private static final Point DISMISS_TEXT_POSITION = new Point(140, 224);
    private static final Dimension DISMISS_TEXT_SIZE = new Dimension(96, 24);

    private static final Point CLOSE_BUTTON_POSITION = new Point(177, 256);
    private static final Dimension DIALOG_DIMENSION = new Dimension(450, 336);

    private JLabel idLabel;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel passportLabel;
    private JLabel hireLabel;
    private JLabel dismissLabel;
    private JTextField idField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField passportField;
    private JTextField hireField;
    private JTextField dismissField;

    private ArrayList<Manager> records;

    public ManagersReference(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        records = null;
    }

    @Override
    protected int getDataSize() {
        return records.size();
    }

    @Override
    protected void initControls() {
        idLabel = new JLabel(ID_LABEL_TEXT);
        idLabel.setSize(ID_LABEL_SIZE);
        idLabel.setLocation(ID_LABEL_POSITION);
        lastNameLabel = new JLabel(LNAME_LABEL_TEXT);
        lastNameLabel.setSize(LNAME_LABEL_SIZE);
        lastNameLabel.setLocation(LNAME_LABEL_POSITION);
        firstNameLabel = new JLabel(FNAME_LABEL_TEXT);
        firstNameLabel.setSize(FNAME_LABEL_SIZE);
        firstNameLabel.setLocation(FNAME_LABEL_POSITION);
        middleNameLabel = new JLabel(MNAME_LABEL_TEXT);
        middleNameLabel.setSize(MNAME_LABEL_SIZE);
        middleNameLabel.setLocation(MNAME_LABEL_POSITION);
        passportLabel = new JLabel(PASSPORT_LABEL_TEXT);
        passportLabel.setSize(PASSPORT_LABEL_SIZE);
        passportLabel.setLocation(PASSPORT_LABEL_POSITION);
        hireLabel = new JLabel(HIRE_LABEL_TEXT);
        hireLabel.setSize(HIRE_LABEL_SIZE);
        hireLabel.setLocation(HIRE_LABEL_POSITION);
        dismissLabel = new JLabel(DISMISS_LABEL_TEXT);
        dismissLabel.setSize(DISMISS_LABEL_SIZE);
        dismissLabel.setLocation(DISMISS_LABEL_POSITION);
        idField = new JTextField();
        idField.setSize(ID_TEXT_SIZE);
        idField.setLocation(ID_TEXT_POSITION);
        lastNameField = new JTextField();
        lastNameField.setSize(LNAME_TEXT_SIZE);
        lastNameField.setLocation(LNAME_TEXT_POSITION);
        firstNameField = new JTextField();
        firstNameField.setSize(FNAME_TEXT_SIZE);
        firstNameField.setLocation(FNAME_TEXT_POSITION);
        middleNameField = new JTextField();
        middleNameField.setSize(MNAME_TEXT_SIZE);
        middleNameField.setLocation(MNAME_TEXT_POSITION);
        passportField = new JTextField();
        passportField.setSize(PASSPORT_TEXT_SIZE);
        passportField.setLocation(PASSPORT_TEXT_POSITION);
        hireField = new JTextField();
        hireField.setSize(HIRE_TEXT_SIZE);
        hireField.setLocation(HIRE_TEXT_POSITION);
        dismissField = new JTextField();
        dismissField.setSize(DISMISS_TEXT_SIZE);
        dismissField.setLocation(DISMISS_TEXT_POSITION);

        idField.setEditable(false);
        add(idLabel);
        add(lastNameLabel);
        add(firstNameLabel);
        add(middleNameLabel);
        add(passportLabel);
        add(hireLabel);
        add(dismissLabel);
        add(idField);
        add(lastNameField);
        add(firstNameField);
        add(middleNameField);
        add(passportField);
        add(hireField);
        add(dismissField);

        addModifyListener(lastNameField);
        addModifyListener(firstNameField);
        addModifyListener(middleNameField);
        addModifyListener(passportField);
        addModifyListener(hireField);
        addModifyListener(dismissField);

        btnClose.setLocation(CLOSE_BUTTON_POSITION);
    }

    @Override
    protected String formatCheckLinksSQL() {
        return "SELECT COUNT(*) AS COUNT FROM ORDERS WHERE ORDERS.MANAGER = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatInsertSQL(int id) throws ParseException {
        Date hireDate = model.stringToDate(hireField.getText());
        Date dismissDate = null;
        if (dismissField.getText() != null && !(dismissField.getText().equals("")))
            dismissDate = model.stringToDate(dismissField.getText());
        return "INSERT INTO MANAGERS (ID, LNAME, FNAME, MNAME, PASSPORT, HIRE, DISMISS) VALUES (" +
                id +
                ", '" + lastNameField.getText() + "'" +
                ", '" + firstNameField.getText() + "'" +
                ((middleNameField.getText() == null || middleNameField.getText().equals("")) ? ", NULL" : (", '" + middleNameField.getText() + "'"))  +
                ", '" + passportField.getText() + "'" +
                ", '" + model.dateToString(hireDate) + "'" +
                ", " + (dismissDate == null ? "NULL" : ("'" + model.dateToString(dismissDate) + "'")) +
                ")";
    }

    @Override
    protected String formatMaxIdSQL() {
        return "SELECT MAX(ID) FROM MANAGERS";
    }

    @Override
    protected String formatDeleteSQL() {
        return "DELETE FROM MANAGERS WHERE ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatSelectSQL() {
        return "SELECT * FROM MANAGERS ORDER BY ID";
    }

    @Override
    protected String formatUpdateSQL() throws ParseException {
        Date dismiss = null;
        Date hire = model.stringToDate(hireField.getText());
        if (dismissField.getText() != null && !(dismissField.getText().equals("")))
            dismiss = model.stringToDate(dismissField.getText());
        return "UPDATE MANAGERS SET LNAME = '" + lastNameField.getText() + "'" +
                ", FNAME = '" + firstNameField.getText() + "'" +
                ", MNAME = " + ((middleNameField.getText() == null) ? ("NULL") : ("'" + middleNameField.getText())) + "'" +
                ", PASSPORT = '" + passportField.getText() + "'" +
                ", HIRE = " + "'" + model.dateToString(hire) + "'" +
                ", DISMISS = " + (dismiss == null ? "NULL" : ("'" + model.dateToString(dismiss) + "'")) +
                " WHERE ID = " + records.get(recordNum).getIdStr() +";";
    }

    @Override
    protected void removeCurrentDataRecord() {
        records.remove(recordNum);
    }

    @Override
    protected int getCurrentDataId() {
        return records.get(recordNum).getId();
    }

    @Override
    protected int getDataId(int pos) {
        return records.get(pos).getId();
    }

    @Override
    protected void clearDataList() {
        records = new ArrayList<>();
    }

    @Override
    protected void insertDataRecord(ResultSet rs) throws SQLException {
        String middleName = rs.getString("MNAME");
        if (middleName != null && middleName.equals("NULL")) middleName = null;
        records.add(new Manager(rs.getInt("ID"),
                rs.getString("LNAME"),
                rs.getString("FNAME"),
                (middleName == null) ? "" : middleName,
                rs.getString("PASSPORT"),
                rs.getDate("HIRE"),
                rs.getDate("DISMISS")
        ));
    }

    @Override
    protected void updateDataRecord() {
        records.get(recordNum).setLastName(lastNameField.getText());
        records.get(recordNum).setFirstName(firstNameField.getText());
        records.get(recordNum).setMiddleName(middleNameField.getText());
        records.get(recordNum).setPassportData(passportField.getText());
        records.get(recordNum).setHireDate(hireField.getText());
        records.get(recordNum).setDismissDate(dismissField.getText());
    }

    protected void checkData() throws DataException, ParseException {
        if (lastNameField.getText() == null || lastNameField.getText().equals("")) throw new DataException();
        if (firstNameField.getText() == null || firstNameField.getText().equals("")) throw new DataException();
        if (passportField.getText() == null || passportField.getText().equals("")) throw new DataException();
        if (hireField.getText() == null) throw new DataException();
        Date hireDate = model.stringToDate(hireField.getText());
        Date dismissDate = null;
        if (dismissField.getText() != null && !(dismissField.getText().equals("")))
            dismissDate = model.stringToDate(dismissField.getText());
    }

    @Override
    protected void showData() {
        if (records.size() > 0) {
            idField.setText(records.get(recordNum).getIdStr());
            lastNameField.setText(records.get(recordNum).getLastName());
            firstNameField.setText(records.get(recordNum).getFirstName());
            if (records.get(recordNum).getMiddleName() == null) middleNameField.setText(null);
            else middleNameField.setText(records.get(recordNum).getMiddleName());
            passportField.setText(records.get(recordNum).getPassportData());
            hireField.setText(records.get(recordNum).getHireDateStr());
            dismissField.setText(records.get(recordNum).getDismissDateStr());
            recordModified = false;
        }
    }

    @Override
    protected void setElementsEmpty() {
        idField.setText(null);
        lastNameField.setText(null);
        firstNameField.setText(null);
        middleNameField.setText(null);
        passportField.setText(null);
        hireField.setText(null);
        dismissField.setText(null);
    }

    @Override
    protected void setElementsEditable(boolean b) {
        lastNameField.setEditable(b);
        firstNameField.setEditable(b);
        middleNameField.setEditable(b);
        passportField.setEditable(b);
        hireField.setEditable(b);
        dismissField.setEditable(b);
    }
}
