package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
Базовый класс формы выбора записи из одной таблицей
 */
public abstract class SelectDataDialog extends TableDataDialog {
    private static final String SELECT_BUTTON_NAME = "Выбрать";
    private static final String CANCEL_BUTTON_NAME = "Закрыть";
    private static final String SELECT_BUTTON_CMD = "Select";
    private static final Dimension SELECT_BUTTON_SIZE = new Dimension(90, 32);
    private static final Point SELECT_BUTTON_FIRST_POSITION = new Point(200, 400);
    protected boolean dataSelected;
    protected int selectedId;

    protected JButton btnSelect;

    public SelectDataDialog(ShopModel model, Frame owner, String title, Dimension dim) {
        super(model, owner, title, dim);
        btnClose.setText(CANCEL_BUTTON_NAME);
        dataSelected = false;
        selectedId = -1;
        initSelectButton();
    }

    private void initSelectButton() {
        btnSelect = new JButton(SELECT_BUTTON_NAME);
        btnSelect.setSize(SELECT_BUTTON_SIZE);
        btnSelect.setLocation(SELECT_BUTTON_FIRST_POSITION);
        btnSelect.setActionCommand(SELECT_BUTTON_CMD);
        btnSelect.addActionListener(this);
        add(btnSelect);
    }

    public boolean isDataSelected() {
        return dataSelected;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void showDialog() {
        dataSelected = false;
        super.showDialog();
    }

    protected abstract void saveSelectedId();

    private void selectRecord() {
        dataSelected = true;
        saveSelectedId();
        closeDialog();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        String comStr = e.getActionCommand();
        switch (comStr) {
            case SELECT_BUTTON_CMD:
                selectRecord();
                break;
        }
    }
}
