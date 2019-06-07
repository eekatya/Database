package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class PrivateBuyersSelectDialog extends SelectDataDialog {
    private static final String ID_LABEL_TEXT = "Идентификатор";
    private static final String LNAME_LABEL_TEXT = "Фамилия";
    private static final String FNAME_LABEL_TEXT = "Имя";
    private static final String MNAME_LABEL_TEXT = "Отчество";
    private static final String PASSPORT_LABEL_TEXT = "Паспортные данные";
    private static final String REGISTRATION_LABEL_TEXT = "Адрес регистрации";

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
    private static final Point REGISTRATION_LABEL_POSITION = new Point(6, 192);
    private static final Dimension REGISTRATION_LABEL_SIZE = new Dimension(128, 24);

    private static final Point ID_TEXT_POSITION = new Point(140, 32);
    private static final Dimension ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point LNAME_TEXT_POSITION = new Point(140, 64);
    private static final Dimension LNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point FNAME_TEXT_POSITION = new Point(140, 96);
    private static final Dimension FNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point MNAME_TEXT_POSITION = new Point(140, 128);
    private static final Dimension MNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point PASSPORT_TEXT_POSITION = new Point(140, 160);
    private static final Dimension PASSPORT_TEXT_SIZE = new Dimension(318, 24);
    private static final Point REGISTRATION_TEXT_POSITION = new Point(140, 192);
    private static final Dimension REGISTRATION_TEXT_SIZE = new Dimension(318, 24);

    private static final Point SELECT_BUTTON_POSITION = new Point(113, 223);
    private static final Point CANCEL_BUTTON_POSITION = new Point(261, 223);
    private static final Dimension DIALOG_DIMENSION = new Dimension(480, 304);

    private JLabel idLabel;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel passportLabel;
    private JLabel registrationLabel;
    private JTextField idField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField passportField;
    private JTextField registrationField;

    private ArrayList<PrivateBuyerItem> records;

    public PrivateBuyersSelectDialog(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        btnSelect.setLocation(SELECT_BUTTON_POSITION);
        btnSelect.setEnabled(false);
        records = null;
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
        registrationLabel = new JLabel(REGISTRATION_LABEL_TEXT);
        registrationLabel.setSize(REGISTRATION_LABEL_SIZE);
        registrationLabel.setLocation(REGISTRATION_LABEL_POSITION);

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
        registrationField = new JTextField();
        registrationField.setSize(REGISTRATION_TEXT_SIZE);
        registrationField.setLocation(REGISTRATION_TEXT_POSITION);

        idField.setEditable(false);
        add(idLabel);
        add(lastNameLabel);
        add(firstNameLabel);
        add(middleNameLabel);
        add(passportLabel);
        add(registrationLabel);

        add(idField);
        add(lastNameField);
        add(firstNameField);
        add(middleNameField);
        add(passportField);
        add(registrationField);

        addModifyListener(lastNameField);
        addModifyListener(firstNameField);
        addModifyListener(middleNameField);
        addModifyListener(passportField);
        addModifyListener(registrationField);

        btnClose.setLocation(CANCEL_BUTTON_POSITION);
    }

    @Override
    protected String formatCheckLinksSQL() {
        return "SELECT COUNT(*) AS COUNT FROM BUYERS WHERE BUYERS.BUYER_ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatInsertSQL(int id) throws ParseException {
        return "INSERT INTO PRIVATE_BUYERS (ID, LNAME, FNAME, MNAME, PASSPORT, REGISTRATION) VALUES (" +
                id +
                ", '" + lastNameField.getText() + "'" +
                ", '" + firstNameField.getText() + "'" +
                ", " + ((middleNameField.getText() == null || middleNameField.getText().equals("")) ? ("NULL") : ("'" + middleNameField.getText() + "'")) +
                ", '" + passportField.getText() + "'" +
                ", " + ((registrationField.getText() == null || registrationField.getText().equals("")) ? ("NULL") : ("'" + registrationField.getText() + "'")) +
                ")";
    }

    @Override
    protected String formatDeleteSQL() {
        return "DELETE FROM PRIVATE_BUYERS WHERE ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatSelectSQL() {
        return "SELECT * FROM PRIVATE_BUYERS ORDER BY ID";
    }

    @Override
    protected String formatUpdateSQL() throws ParseException {
        return "UPDATE PRIVATE_BUYERS SET LNAME = '" + lastNameField.getText() + "'" +
                ", FNAME = '" + firstNameField.getText() + "'" +
                ", MNAME = " + ((middleNameField.getText() == null || middleNameField.getText().equals("")) ? ("NULL") : ("'" + middleNameField.getText() + "'")) +
                ", PASSPORT = '" + passportField.getText() + "'" +
                ", REGISTRATION = " + ((registrationField.getText() == null || registrationField.getText().equals("")) ? ("NULL") : ("'" + registrationField.getText() + "'")) +
                " WHERE ID = " + records.get(recordNum).getIdStr() +";";
    }

    @Override
    protected String formatMaxIdSQL() {
        return "SELECT MAX(ID) FROM PRIVATE_BUYERS";
    }

    @Override
    protected int getDataSize() {
        return records.size();
    }

    @Override
    protected void removeCurrentDataRecord() {
        records.remove(recordNum);
        if (records.size() == 0) btnSelect.setEnabled(false);
    }

    @Override
    protected void clearDataList() {
        records = new ArrayList<>();
        btnSelect.setEnabled(false);
    }

    @Override
    protected void insertDataRecord(ResultSet rs) throws SQLException {
        records.add(new PrivateBuyerItem(rs.getInt("ID"),
                rs.getString("LNAME"),
                rs.getString("FNAME"),
                rs.getString("MNAME"),
                rs.getString("PASSPORT"),
                rs.getString("REGISTRATION")
        ));
        btnSelect.setEnabled(true);
    }

    @Override
    protected void updateDataRecord() {
        records.get(recordNum).setFirstName(firstNameField.getText());
        records.get(recordNum).setLastName(lastNameField.getText());
        records.get(recordNum).setMiddleName(middleNameField.getText());
        records.get(recordNum).setPassportData(passportField.getText());
        records.get(recordNum).setRegistrationData(registrationField.getText());
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
    protected void checkData() throws DataException, ParseException {
        if (lastNameField.getText() == null || lastNameField.getText().equals("")) throw new DataException();
        if (firstNameField.getText() == null || firstNameField.getText().equals("")) throw new DataException();
        if (passportField.getText() == null || passportField.getText().equals("")) throw new DataException();
    }

    @Override
    protected void showData() {
        if (records.size() > 0) {
            idField.setText(records.get(recordNum).getIdStr());
            lastNameField.setText(records.get(recordNum).getLastName());
            firstNameField.setText(records.get(recordNum).getFirstName());
            middleNameField.setText(records.get(recordNum).getMiddleName());
            passportField.setText(records.get(recordNum).getPassportData());
            registrationField.setText(records.get(recordNum).getRegistrationData());
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
        registrationField.setText(null);
    }

    @Override
    protected void setElementsEditable(boolean b) {
        lastNameField.setEditable(b);
        firstNameField.setEditable(b);
        middleNameField.setEditable(b);
        passportField.setEditable(b);
        registrationField.setEditable(b);
    }

    @Override
    protected void saveSelectedId() {
        selectedId = records.get(recordNum).getId();
    }
}
