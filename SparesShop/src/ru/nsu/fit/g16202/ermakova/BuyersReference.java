package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

/*
Форма для работы с таблицей покупателей
 */
public class BuyersReference extends TableDataDialog {
    private static final String PRIVATE_BUYERS_REFERENCE_DIALOG_TITLE = "Справочник покупателей физ. лиц";
    private static final String EXACT_BUYER_BUTTON_CMD = "Exact Buyer";
    private static final String ID_LABEL_TEXT = "Идентификатор";
    private static final String PHONE_LABEL_TEXT = "Телефон";
    private static final String EMAIL_LABEL_TEXT = "Email";
    private static final String ADDRESS_LABEL_TEXT = "Фактический адрес";
    private static final String TYPE_LABEL_TEXT = "Тип покупателя";

    private static final String PB_ID_LABEL_TEXT = "Идентификатор";
    private static final String PB_LNAME_LABEL_TEXT = "Фамилия";
    private static final String PB_FNAME_LABEL_TEXT = "Имя";
    private static final String PB_MNAME_LABEL_TEXT = "Отчество";
    private static final String PB_PASSPORT_LABEL_TEXT = "Паспортные данные";
    private static final String PB_REGISTRATION_LABEL_TEXT = "Адрес регистрации";

    private static final String LB_ID_LABEL_TEXT = "Идентификатор";
    private static final String LB_NAME_LABEL_TEXT = "Наименование";
    private static final String LB_ADDRESS_LABEL_TEXT = "Юридический адрес";
    private static final String LB_INN_LABEL_TEXT = "ИНН";
    private static final String LB_CONTACT_LABEL_TEXT = "Контактное лицо";
    private static final String LB_DETAILS_LABEL_TEXT = "Банковские реквизиты";

    private static final String SELECT_BUTTON_TEXT = "Данные покупателя...";

    private static final Point ID_LABEL_POSITION = new Point(6, 32);
    private static final Dimension ID_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PHONE_LABEL_POSITION = new Point(6, 64);
    private static final Dimension PHONE_LABEL_SIZE = new Dimension(128, 24);
    private static final Point EMAIL_LABEL_POSITION = new Point(6, 96);
    private static final Dimension EMAIL_LABEL_SIZE = new Dimension(128, 24);
    private static final Point ADDRESS_LABEL_POSITION = new Point(6, 128);
    private static final Dimension ADDRESS_LABEL_SIZE = new Dimension(128, 24);
    private static final Point TYPE_LABEL_POSITION = new Point(6, 160);
    private static final Dimension TYPE_LABEL_SIZE = new Dimension(128, 24);

    private static final Point ID_TEXT_POSITION = new Point(140, 32);
    private static final Dimension ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point PHONE_TEXT_POSITION = new Point(140, 64);
    private static final Dimension PHONE_TEXT_SIZE = new Dimension(160, 24);
    private static final Point EMAIL_TEXT_POSITION = new Point(140, 96);
    private static final Dimension EMAIL_TEXT_SIZE = new Dimension(160, 24);
    private static final Point ADDRESS_TEXT_POSITION = new Point(140, 128);
    private static final Dimension ADDRESS_TEXT_SIZE = new Dimension(318, 24);
    private static final Point TYPE_COMBO_POSITION = new Point(140, 160);
    private static final Dimension TYPE_COMBO_SIZE = new Dimension(160, 24);

    private static final Point PB_ID_LABEL_POSITION = new Point(6, 192);
    private static final Dimension PB_ID_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PB_LNAME_LABEL_POSITION = new Point(6, 224);
    private static final Dimension PB_LNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PB_FNAME_LABEL_POSITION = new Point(6, 256);
    private static final Dimension PB_FNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PB_MNAME_LABEL_POSITION = new Point(6, 288);
    private static final Dimension PB_MNAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PB_PASSPORT_LABEL_POSITION = new Point(6, 320);
    private static final Dimension PB_PASSPORT_LABEL_SIZE = new Dimension(128, 24);
    private static final Point PB_REGISTRATION_LABEL_POSITION = new Point(6, 352);
    private static final Dimension PB_REGISTRATION_LABEL_SIZE = new Dimension(128, 24);

    private static final Point PB_ID_TEXT_POSITION = new Point(140, 192);
    private static final Dimension PB_ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point PB_LNAME_TEXT_POSITION = new Point(140, 224);
    private static final Dimension PB_LNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point PB_FNAME_TEXT_POSITION = new Point(140, 256);
    private static final Dimension PB_FNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point PB_MNAME_TEXT_POSITION = new Point(140, 288);
    private static final Dimension PB_MNAME_TEXT_SIZE = new Dimension(160, 24);
    private static final Point PB_PASSPORT_TEXT_POSITION = new Point(140, 320);
    private static final Dimension PB_PASSPORT_TEXT_SIZE = new Dimension(318, 24);
    private static final Point PB_REGISTRATION_TEXT_POSITION = new Point(140, 352);
    private static final Dimension PB_REGISTRATION_TEXT_SIZE = new Dimension(318, 24);

    private static final Point LB_ID_LABEL_POSITION = new Point(6, 192);
    private static final Dimension LB_ID_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LB_NAME_LABEL_POSITION = new Point(6, 224);
    private static final Dimension LB_NAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LB_ADDRESS_LABEL_POSITION = new Point(6, 256);
    private static final Dimension LB_ADDRESS_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LB_INN_LABEL_POSITION = new Point(6, 288);
    private static final Dimension LB_INN_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LB_CONTACT_LABEL_POSITION = new Point(6, 320);
    private static final Dimension LB_CONTACT_LABEL_SIZE = new Dimension(128, 24);
    private static final Point LB_DETAILS_LABEL_POSITION = new Point(6, 352);
    private static final Dimension LB_DETAILS_LABEL_SIZE = new Dimension(128, 24);

    private static final Point LB_ID_TEXT_POSITION = new Point(140, 192);
    private static final Dimension LB_ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point LB_NAME_TEXT_POSITION = new Point(140, 224);
    private static final Dimension LB_NAME_TEXT_SIZE = new Dimension(318, 24);
    private static final Point LB_ADDRESS_TEXT_POSITION = new Point(140, 256);
    private static final Dimension LB_ADDRESS_TEXT_SIZE = new Dimension(318, 24);
    private static final Point LB_INN_TEXT_POSITION = new Point(140, 288);
    private static final Dimension LB_INN_TEXT_SIZE = new Dimension(318, 24);
    private static final Point LB_CONTACT_TEXT_POSITION = new Point(140, 320);
    private static final Dimension LB_CONTACT_TEXT_SIZE = new Dimension(318, 24);
    private static final Point LB_DETAILS_TEXT_POSITION = new Point(140, 352);
    private static final Dimension LB_DETAILS_TEXT_SIZE = new Dimension(318, 24);

    private static final Point CLOSE_BUTTON_POSITION = new Point(306, 384);
    private static final Point SELECT_BUTTON_POSITION = new Point(80, 384);
    private static final Dimension SELECT_BUTTON_SIZE = new Dimension(150, 32);
    private static final Dimension DIALOG_DIMENSION = new Dimension(480, 464);

    private JLabel idLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel addressLabel;
    private JLabel typeLabel;

    private JLabel pbIdLabel;
    private JLabel pbLNameLabel;
    private JLabel pbFNameLabel;
    private JLabel pbMNameLabel;
    private JLabel pbPassportLabel;
    private JLabel pbRegistrationLabel;

    private JLabel lbIdLabel;
    private JLabel lbNameLabel;
    private JLabel lbAddressLabel;
    private JLabel lbINNLabel;
    private JLabel lbContactLabel;
    private JLabel lbDetailsLabel;

    private JTextField idField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox typeField;

    private JTextField pbIdField;
    private JTextField pbLNameField;
    private JTextField pbFNameField;
    private JTextField pbMNameField;
    private JTextField pbPassportField;
    private JTextField pbRegistrationField;

    private JTextField lbIdField;
    private JTextField lbNameField;
    private JTextField lbAddressField;
    private JTextField lbINNField;
    private JTextField lbContactField;
    private JTextField lbDetailsField;

    private JButton exactBuyerButton;

    private ArrayList<BuyerItem> records;
    private ArrayList<BuyerTypeItem> buyerTypes;
    private int selectedType;
    PrivateBuyersSelectDialog privateBuyersSelectDialog;

    public BuyersReference(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        records = null;
        privateBuyersSelectDialog = null;
        loadBuyerTypes();
    }

    private void loadBuyerTypes() {
        try {
            Statement stmt;
            buyerTypes = new ArrayList<>();
            model.setAutoCommit(false);
            stmt = model.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM BUYER_TYPES ORDER BY ID");
            while (rs.next()) {
                buyerTypes.add(new BuyerTypeItem(rs.getInt("ID"),
                        rs.getString("NAME")
                ));
            }
            rs.close();
            stmt.close();
            model.commitConn();
            typeField.removeAllItems();
            for (int i = 0; i < buyerTypes.size(); i++)
                typeField.addItem(buyerTypes.get(i).getTypeName());
            selectedType = 0;
            typeField.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
            return;
        }
    }

    private void showPrivateControls(boolean b) {
        pbIdLabel.setVisible(b);
        pbIdField.setVisible(b);
        pbLNameLabel.setVisible(b);
        pbLNameField.setVisible(b);
        pbFNameLabel.setVisible(b);
        pbFNameField.setVisible(b);
        pbMNameLabel.setVisible(b);
        pbMNameField.setVisible(b);
        pbPassportLabel.setVisible(b);
        pbPassportField.setVisible(b);
        pbRegistrationLabel.setVisible(b);
        pbRegistrationField.setVisible(b);
    }

    private void showLegalControls(boolean b) {
        lbIdLabel.setVisible(b);
        lbIdField.setVisible(b);
        lbNameLabel.setVisible(b);
        lbNameField.setVisible(b);
        lbAddressLabel.setVisible(b);
        lbAddressField.setVisible(b);
        lbINNLabel.setVisible(b);
        lbINNField.setVisible(b);
        lbContactLabel.setVisible(b);
        lbContactField.setVisible(b);
        lbDetailsLabel.setVisible(b);
        lbDetailsField.setVisible(b);
    }

    private void clearPrivateControls() {
        pbIdField.setText(null);
        pbLNameField.setText(null);
        pbFNameField.setText(null);
        pbMNameField.setText(null);
        pbPassportField.setText(null);
        pbRegistrationField.setText(null);
    }

    private void clearLegalControls() {
        lbIdField.setText(null);
        lbNameField.setText(null);
        lbAddressField.setText(null);
        lbINNField.setText(null);
        lbContactField.setText(null);
        lbDetailsField.setText(null);
    }

    protected void selectBuyer() {
        if (selectedType == 0) {
            if (privateBuyersSelectDialog == null) {
                privateBuyersSelectDialog = new PrivateBuyersSelectDialog(model, model.getFrame(), PRIVATE_BUYERS_REFERENCE_DIALOG_TITLE);
            }
            privateBuyersSelectDialog.setPositionedId(records.get(recordNum).getLinkedId());
            privateBuyersSelectDialog.showDialog();
            if (privateBuyersSelectDialog.isDataSelected()) {
                try {
                    model.setAutoCommit(false);
                    Statement stmt = model.createStatement();
                    int type = typeField.getSelectedIndex() + 1;
                    String sql = "UPDATE BUYERS SET BUYER_ID = " + privateBuyersSelectDialog.getSelectedId() +
                            ", TYPE = " + type +
                            ", PHONE = '" + phoneField.getText() + "'" +
                            ", EMAIL = " + ((emailField.getText() == null || emailField.getText().equals("")) ? ("NULL") : ("'" + emailField.getText() + "'")) +
                            ", ADDRESS = " + ((addressField.getText() == null || addressField.getText().equals("")) ? ("NULL") : ("'" + addressField.getText() + "'")) +
                            " WHERE ID = " + records.get(recordNum).getIdStr() +";";
                    stmt.executeUpdate(sql);
                    stmt.close();
                    model.commitConn();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
                    return;
                }
                records.get(recordNum).setLinkedBuyerId(privateBuyersSelectDialog.getSelectedId());
                showData();
            }
        } else {
            DBMessages.showInformationMessage(model.getFrame(), DBMessages.DB_UNSUPPORTED_OPERATION_MSG);
        }
    }

    @Override
    protected void initControls() {
        idLabel = new JLabel(ID_LABEL_TEXT);
        idLabel.setSize(ID_LABEL_SIZE);
        idLabel.setLocation(ID_LABEL_POSITION);
        phoneLabel = new JLabel(PHONE_LABEL_TEXT);
        phoneLabel.setSize(PHONE_LABEL_SIZE);
        phoneLabel.setLocation(PHONE_LABEL_POSITION);
        emailLabel = new JLabel(EMAIL_LABEL_TEXT);
        emailLabel.setSize(EMAIL_LABEL_SIZE);
        emailLabel.setLocation(EMAIL_LABEL_POSITION);
        addressLabel = new JLabel(ADDRESS_LABEL_TEXT);
        addressLabel.setSize(ADDRESS_LABEL_SIZE);
        addressLabel.setLocation(ADDRESS_LABEL_POSITION);
        typeLabel = new JLabel(TYPE_LABEL_TEXT);
        typeLabel.setSize(TYPE_LABEL_SIZE);
        typeLabel.setLocation(TYPE_LABEL_POSITION);

        pbIdLabel = new JLabel(PB_ID_LABEL_TEXT);
        pbIdLabel.setSize(PB_ID_LABEL_SIZE);
        pbIdLabel.setLocation(PB_ID_LABEL_POSITION);
        pbLNameLabel = new JLabel(PB_LNAME_LABEL_TEXT);
        pbLNameLabel.setSize(PB_LNAME_LABEL_SIZE);
        pbLNameLabel.setLocation(PB_LNAME_LABEL_POSITION);
        pbFNameLabel = new JLabel(PB_FNAME_LABEL_TEXT);
        pbFNameLabel.setSize(PB_FNAME_LABEL_SIZE);
        pbFNameLabel.setLocation(PB_FNAME_LABEL_POSITION);
        pbMNameLabel = new JLabel(PB_MNAME_LABEL_TEXT);
        pbMNameLabel.setSize(PB_MNAME_LABEL_SIZE);
        pbMNameLabel.setLocation(PB_MNAME_LABEL_POSITION);
        pbPassportLabel = new JLabel(PB_PASSPORT_LABEL_TEXT);
        pbPassportLabel.setSize(PB_PASSPORT_LABEL_SIZE);
        pbPassportLabel.setLocation(PB_PASSPORT_LABEL_POSITION);
        pbRegistrationLabel = new JLabel(PB_REGISTRATION_LABEL_TEXT);
        pbRegistrationLabel.setSize(PB_REGISTRATION_LABEL_SIZE);
        pbRegistrationLabel.setLocation(PB_REGISTRATION_LABEL_POSITION);

        lbIdLabel = new JLabel(LB_ID_LABEL_TEXT);
        lbIdLabel.setSize(LB_ID_LABEL_SIZE);
        lbIdLabel.setLocation(LB_ID_LABEL_POSITION);
        lbNameLabel = new JLabel(LB_NAME_LABEL_TEXT);
        lbNameLabel.setSize(LB_NAME_LABEL_SIZE);
        lbNameLabel.setLocation(LB_NAME_LABEL_POSITION);
        lbAddressLabel = new JLabel(LB_ADDRESS_LABEL_TEXT);
        lbAddressLabel.setSize(LB_ADDRESS_LABEL_SIZE);
        lbAddressLabel.setLocation(LB_ADDRESS_LABEL_POSITION);
        lbINNLabel = new JLabel(LB_INN_LABEL_TEXT);
        lbINNLabel.setSize(LB_INN_LABEL_SIZE);
        lbINNLabel.setLocation(LB_INN_LABEL_POSITION);
        lbContactLabel = new JLabel(LB_CONTACT_LABEL_TEXT);
        lbContactLabel.setSize(LB_CONTACT_LABEL_SIZE);
        lbContactLabel.setLocation(LB_CONTACT_LABEL_POSITION);
        lbDetailsLabel = new JLabel(LB_DETAILS_LABEL_TEXT);
        lbDetailsLabel.setSize(LB_DETAILS_LABEL_SIZE);
        lbDetailsLabel.setLocation(LB_DETAILS_LABEL_POSITION);

        idField = new JTextField();
        idField.setSize(ID_TEXT_SIZE);
        idField.setLocation(ID_TEXT_POSITION);
        phoneField = new JTextField();
        phoneField.setSize(PHONE_TEXT_SIZE);
        phoneField.setLocation(PHONE_TEXT_POSITION);
        emailField = new JTextField();
        emailField.setSize(EMAIL_TEXT_SIZE);
        emailField.setLocation(EMAIL_TEXT_POSITION);
        addressField = new JTextField();
        addressField.setSize(ADDRESS_TEXT_SIZE);
        addressField.setLocation(ADDRESS_TEXT_POSITION);
        typeField = new JComboBox();
        typeField.setSize(TYPE_COMBO_SIZE);
        typeField.setLocation(TYPE_COMBO_POSITION);

        pbIdField = new JTextField();
        pbIdField.setSize(PB_ID_TEXT_SIZE);
        pbIdField.setLocation(PB_ID_TEXT_POSITION);
        pbLNameField = new JTextField();
        pbLNameField.setSize(PB_LNAME_TEXT_SIZE);
        pbLNameField.setLocation(PB_LNAME_TEXT_POSITION);
        pbFNameField = new JTextField();
        pbFNameField.setSize(PB_FNAME_TEXT_SIZE);
        pbFNameField.setLocation(PB_FNAME_TEXT_POSITION);
        pbMNameField = new JTextField();
        pbMNameField.setSize(PB_MNAME_TEXT_SIZE);
        pbMNameField.setLocation(PB_MNAME_TEXT_POSITION);
        pbPassportField = new JTextField();
        pbPassportField.setSize(PB_PASSPORT_TEXT_SIZE);
        pbPassportField.setLocation(PB_PASSPORT_TEXT_POSITION);
        pbRegistrationField = new JTextField();
        pbRegistrationField.setSize(PB_REGISTRATION_TEXT_SIZE);
        pbRegistrationField.setLocation(PB_REGISTRATION_TEXT_POSITION);

        lbIdField = new JTextField();
        lbIdField.setSize(LB_ID_TEXT_SIZE);
        lbIdField.setLocation(LB_ID_TEXT_POSITION);
        lbNameField = new JTextField();
        lbNameField.setSize(LB_NAME_TEXT_SIZE);
        lbNameField.setLocation(LB_NAME_TEXT_POSITION);
        lbAddressField = new JTextField();
        lbAddressField.setSize(LB_ADDRESS_TEXT_SIZE);
        lbAddressField.setLocation(LB_ADDRESS_TEXT_POSITION);
        lbINNField = new JTextField();
        lbINNField.setSize(LB_INN_TEXT_SIZE);
        lbINNField.setLocation(LB_INN_TEXT_POSITION);
        lbContactField = new JTextField();
        lbContactField.setSize(LB_CONTACT_TEXT_SIZE);
        lbContactField.setLocation(LB_CONTACT_TEXT_POSITION);
        lbDetailsField = new JTextField();
        lbDetailsField.setSize(LB_DETAILS_TEXT_SIZE);
        lbDetailsField.setLocation(LB_DETAILS_TEXT_POSITION);

        idField.setEditable(false);

        pbIdField.setEditable(false);
        pbLNameField.setEditable(false);
        pbFNameField.setEditable(false);
        pbMNameField.setEditable(false);
        pbPassportField.setEditable(false);
        pbRegistrationField.setEditable(false);

        lbIdField.setEditable(false);
        lbNameField.setEditable(false);
        lbAddressField.setEditable(false);
        lbINNField.setEditable(false);
        lbContactField.setEditable(false);
        lbDetailsField.setEditable(false);

        add(idLabel);
        add(phoneLabel);
        add(emailLabel);
        add(addressLabel);
        add(typeLabel);

        add(pbIdLabel);
        add(pbLNameLabel);
        add(pbFNameLabel);
        add(pbMNameLabel);
        add(pbPassportLabel);
        add(pbRegistrationLabel);

        add(lbIdLabel);
        add(lbNameLabel);
        add(lbAddressLabel);
        add(lbINNLabel);
        add(lbContactLabel);
        add(lbDetailsLabel);

        add(idField);
        add(phoneField);
        add(emailField);
        add(addressField);
        add(typeField);

        add(pbIdField);
        add(pbLNameField);
        add(pbFNameField);
        add(pbMNameField);
        add(pbPassportField);
        add(pbRegistrationField);

        add(lbIdField);
        add(lbNameField);
        add(lbAddressField);
        add(lbINNField);
        add(lbContactField);
        add(lbDetailsField);

        addModifyListener(phoneField);
        addModifyListener(emailField);
        addModifyListener(addressField);
        typeField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (selectedType != typeField.getSelectedIndex()) {
                    selectedType = typeField.getSelectedIndex();
                    boolean privateSelected = (selectedType == 0);
                    clearPrivateControls();
                    clearLegalControls();
                    showPrivateControls(privateSelected);
                    showLegalControls(!privateSelected);
                    recordModified = true;
                }
            }
        });
        btnClose.setLocation(CLOSE_BUTTON_POSITION);
        exactBuyerButton = new JButton(SELECT_BUTTON_TEXT);
        exactBuyerButton.setLocation(SELECT_BUTTON_POSITION);
        exactBuyerButton.setSize(SELECT_BUTTON_SIZE);
        exactBuyerButton.setActionCommand(EXACT_BUYER_BUTTON_CMD);
        exactBuyerButton.addActionListener(this);
        add(exactBuyerButton);
    }

    @Override
    protected String formatCheckLinksSQL() {
        String sql;
        if (records.get(recordNum).getBuyerType() == 1) sql = "SELECT COUNT(*) AS COUNT FROM PRIVATE_BUYERS WHERE PRIVATE_BUYERS.ID = " + records.get(recordNum).getLinkedId();
        else sql = "SELECT COUNT(*) AS COUNT FROM LEGAL_BUYERS WHERE LEGAL_BUYERS.ID = " + records.get(recordNum).getLinkedId();
        return sql;
    }

    @Override
    protected String formatInsertSQL(int id) throws ParseException {
        int buyerId = Integer.parseInt((typeField.getSelectedIndex() == 0) ? pbIdField.getText() : lbIdField.getText());
        int type = typeField.getSelectedIndex() + 1;
        return "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (" +
                id +
                ", " + buyerId +
                ", " + type +
                ", '" + phoneField.getText() + "'" +
                ", " + ((emailField.getText() == null || emailField.getText().equals("")) ? ("NULL") : ("'" + emailField.getText() + "'")) +
                ", " + ((addressField.getText() == null || addressField.getText().equals("")) ? ("NULL") : ("'" + addressField.getText() + "'")) +
                ")";
    }

    @Override
    protected String formatDeleteSQL() {
        return "DELETE FROM BUYERS WHERE ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatSelectSQL() {
        return "SELECT * FROM BUYERS ORDER BY ID";
    }

    @Override
    protected String formatUpdateSQL() throws ParseException {
        int buyerId = Integer.parseInt((typeField.getSelectedIndex() == 0) ? pbIdField.getText() : lbIdField.getText());
        int type = typeField.getSelectedIndex() + 1;
        return "UPDATE BUYERS SET BUYER_ID = " + buyerId +
                ", TYPE = " + type +
                ", PHONE = '" + phoneField.getText() + "'" +
                ", EMAIL = " + ((emailField.getText() == null || emailField.getText().equals("")) ? ("NULL") : ("'" + emailField.getText() + "'")) +
                ", ADDRESS = " + ((addressField.getText() == null || addressField.getText().equals("")) ? ("NULL") : ("'" + addressField.getText() + "'")) +
                " WHERE ID = " + records.get(recordNum).getIdStr() +";";
    }

    @Override
    protected String formatMaxIdSQL() {
        return "SELECT MAX(ID) FROM BUYERS";
    }

    @Override
    protected int getDataSize() {
        return records.size();
    }

    @Override
    protected void removeCurrentDataRecord() {
        records.remove(recordNum);
    }

    @Override
    protected void clearDataList() {
        records = new ArrayList<>();
    }

    @Override
    protected void insertDataRecord(ResultSet rs) throws SQLException {
        records.add(new BuyerItem(rs.getInt("ID"),
                rs.getInt("BUYER_ID"),
                rs.getInt("TYPE"),
                rs.getString("PHONE"),
                rs.getString("EMAIL"),
                rs.getString("ADDRESS")
        ));
    }

    @Override
    protected void updateDataRecord() {
        records.get(recordNum).setBuyerId(Integer.parseInt(pbIdField.getText()));
        records.get(recordNum).setBuyerType(typeField.getSelectedIndex() + 1);
        records.get(recordNum).setPhoneNumber(phoneField.getText());
        records.get(recordNum).setEmailAddress(emailField.getText());
        records.get(recordNum).setBuyerAddress(addressField.getText());
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
        if (phoneField.getText() == null || phoneField.getText().equals("")) throw new DataException();
        if (emailField.getText() == null || emailField.getText().equals("")) throw new DataException();
        int selectedIndex = typeField.getSelectedIndex();
        if (selectedIndex != 0 || selectedIndex != 1) throw new DataException();
        try {
            int id;
            if (selectedIndex == 0) id = Integer.parseInt(pbIdField.getText());
            else id = Integer.parseInt(lbIdField.getText());
        } catch (Exception e) {
            throw new ParseException("", 0);
        }
    }

    @Override
    protected void showData() {
        if (records.size() > 0) {
            idField.setText(records.get(recordNum).getIdStr());
            int type = records.get(recordNum).getBuyerType();
            typeField.setSelectedIndex(type - 1);
            phoneField.setText(records.get(recordNum).getPhoneNumber());
            emailField.setText(records.get(recordNum).getEmailAddress());
            addressField.setText(records.get(recordNum).getBuyerAddress());
            boolean privateSelected = (type == 1);

            showPrivateControls(privateSelected);
            showLegalControls(!privateSelected);

            try {
                ResultSet rs = null;
                model.setAutoCommit(false);
                Statement stmt = model.createStatement();
                if (type == 1) {
                    pbIdField.setText(records.get(recordNum).getLinkedIdStr());
                    rs = stmt.executeQuery("SELECT * FROM PRIVATE_BUYERS WHERE ID = " + records.get(recordNum).getLinkedId());
                    if (rs.next()) {
                        pbLNameField.setText(rs.getString("LNAME"));
                        pbFNameField.setText(rs.getString("FNAME"));
                        pbMNameField.setText(rs.getString("MNAME"));
                        pbPassportField.setText(rs.getString("PASSPORT"));
                        pbRegistrationField.setText(rs.getString("REGISTRATION"));
                    } else {
                        clearPrivateControls();
                    }
                } else {
                    lbIdField.setText(records.get(recordNum).getLinkedIdStr());
                    rs = stmt.executeQuery("SELECT * FROM LEGAL_BUYERS WHERE ID = " + records.get(recordNum).getLinkedId());
                    if (rs.next()) {
                        lbNameField.setText(rs.getString("NAME"));
                        lbAddressField.setText(rs.getString("ADDRESS"));
                        lbINNField.setText(rs.getString("INN"));
                        lbContactField.setText(rs.getString("CONTACT"));
                        lbDetailsField.setText(rs.getString("BANK_DETAILS"));
                    } else {
                        clearLegalControls();
                    }
                }
                rs.close();
                stmt.close();
                model.commitConn();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                DBMessages.showErrorMessage(this, DBMessages.DB_REQUEST_ERROR_MSG);
                return;
            }
            recordModified = false;
        }
    }

    @Override
    protected void setElementsEmpty() {
        idField.setText(null);
        typeField.setSelectedIndex(0);
        phoneField.setText(null);
        emailField.setText(null);
        addressField.setText(null);

        showPrivateControls(true);
        showLegalControls(false);

        clearPrivateControls();
        clearLegalControls();
    }

    @Override
    protected void setElementsEditable(boolean b) {
        phoneField.setEditable(b);
        emailField.setEditable(b);
        addressField.setEditable(b);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        String comStr = e.getActionCommand();
        switch (comStr) {
            case EXACT_BUYER_BUTTON_CMD:
                selectBuyer();
                break;
        }
    }
}
