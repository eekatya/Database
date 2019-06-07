package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/*
Форма для работы с таблицей деталей
 */
public class SparesReference extends TableDataDialog {
    private static final String ID_LABEL_TEXT = "Идентификатор";
    private static final String NAME_LABEL_TEXT = "Наименование детали";
    private static final String ARTICLE_LABEL_TEXT = "Артикул";

    private static final Point ID_LABEL_POSITION = new Point(6, 32);
    private static final Dimension ID_LABEL_SIZE = new Dimension(128, 24);
    private static final Point NAME_LABEL_POSITION = new Point(6, 64);
    private static final Dimension NAME_LABEL_SIZE = new Dimension(128, 24);
    private static final Point ARTICLE_LABEL_POSITION = new Point(6, 96);
    private static final Dimension ARTICLE_LABEL_SIZE = new Dimension(128, 24);

    private static final Point ID_TEXT_POSITION = new Point(140, 32);
    private static final Dimension ID_TEXT_SIZE = new Dimension(96, 24);
    private static final Point NAME_TEXT_POSITION = new Point(140, 64);
    private static final Dimension NAME_TEXT_SIZE = new Dimension(288, 24);
    private static final Point ARTICLE_TEXT_POSITION = new Point(140, 96);
    private static final Dimension ARTICLE_TEXT_SIZE = new Dimension(288, 24);

    private static final Point CLOSE_BUTTON_POSITION = new Point(177, 127);
    private static final Dimension DIALOG_DIMENSION = new Dimension(450, 208);

    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel articleLabel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField articleField;

    private ArrayList<SpareItem> records;

    public SparesReference(ShopModel model, Frame owner, String title) {
        super(model, owner, title, DIALOG_DIMENSION);
        records = null;
    }

    @Override
    protected void initControls() {
        idLabel = new JLabel(ID_LABEL_TEXT);
        idLabel.setSize(ID_LABEL_SIZE);
        idLabel.setLocation(ID_LABEL_POSITION);
        nameLabel = new JLabel(NAME_LABEL_TEXT);
        nameLabel.setSize(NAME_LABEL_SIZE);
        nameLabel.setLocation(NAME_LABEL_POSITION);
        articleLabel = new JLabel(ARTICLE_LABEL_TEXT);
        articleLabel.setSize(ARTICLE_LABEL_SIZE);
        articleLabel.setLocation(ARTICLE_LABEL_POSITION);
        idField = new JTextField();
        idField.setSize(ID_TEXT_SIZE);
        idField.setLocation(ID_TEXT_POSITION);
        nameField = new JTextField();
        nameField.setSize(NAME_TEXT_SIZE);
        nameField.setLocation(NAME_TEXT_POSITION);
        articleField = new JTextField();
        articleField.setSize(ARTICLE_TEXT_SIZE);
        articleField.setLocation(ARTICLE_TEXT_POSITION);

        idField.setEditable(false);
        add(idLabel);
        add(nameLabel);
        add(articleLabel);

        add(idField);
        add(nameField);
        add(articleField);

        addModifyListener(nameField);
        addModifyListener(articleField);

        btnClose.setLocation(CLOSE_BUTTON_POSITION);
    }

    @Override
    protected String formatCheckLinksSQL() {
        return "SELECT COUNT(*) AS COUNT FROM ORDERS_STRUCTURE WHERE ORDERS_STRUCTURE.SPARE_ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatInsertSQL(int id) throws ParseException {
        return "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (" +
                id +
                ", '" + nameField.getText() + "'" +
                ", '" + articleField.getText() + "'" +
                ")";

    }

    @Override
    protected String formatDeleteSQL() {
        return "DELETE FROM SPARES WHERE ID = " + records.get(recordNum).getId();
    }

    @Override
    protected String formatSelectSQL() {
        return "SELECT * FROM SPARES ORDER BY ID";
    }

    @Override
    protected String formatUpdateSQL() throws ParseException {
        return "UPDATE SPARES SET NAME = '" + nameField.getText() + "'" +
                ", ARTICLE = '" + articleField.getText() + "'" +
                " WHERE ID = " + records.get(recordNum).getSpareIdStr() + ";";
    }

    @Override
    protected String formatMaxIdSQL() {
        return "SELECT MAX(ID) FROM SPARES";
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
        records.add(new SpareItem(rs.getInt("ID"),
                rs.getString("NAME"),
                rs.getString("ARTICLE")
        ));
    }

    @Override
    protected void updateDataRecord() {
        records.get(recordNum).setSpareName(nameField.getText());
        records.get(recordNum).setSpareArticle(articleField.getText());
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
        if (nameField.getText() == null || nameField.getText().equals("")) throw new DataException();
        if (articleField.getText() == null || articleField.getText().equals("")) throw new DataException();
    }

    @Override
    protected void showData() {
        if (records.size() > 0) {
            idField.setText(records.get(recordNum).getSpareIdStr());
            nameField.setText(records.get(recordNum).getSpareName());
            articleField.setText(records.get(recordNum).getSpareArticle());
            recordModified = false;
        }
    }

    @Override
    protected void setElementsEmpty() {
        idField.setText(null);
        nameField.setText(null);
        articleField.setText(null);
    }

    @Override
    protected void setElementsEditable(boolean b) {
        nameField.setEditable(b);
        articleField.setEditable(b);
    }
}
