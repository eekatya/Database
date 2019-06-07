package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Базовый класс для отчётов
 */
public abstract class ReportDialog extends JDialog implements ActionListener {
    private static final String CLOSE_BUTTON_CMD = "Close";
    private static final Dimension CLOSE_BUTTON_SIZE = new Dimension(90, 32);
    private static final String CLOSE_BUTTON_NAME = "Закрыть";

    protected static final String[][] TABLE_DATA = {
            {""}
    };
    protected static final String[] TABLE_COLUMNS = {
            ""
    };

    protected JTable reportTable;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;
    protected JLabel reportNameLabel;
    protected JButton btnClose;
    protected ShopModel model;

    public ReportDialog(ShopModel model, Frame owner, String title, Dimension dim) {
        super(owner, title, true);
        this.model = model;
        reportTable = null;
        scrollPane = null;
        reportNameLabel = null;
        setSize(dim);
        setLocationRelativeTo(getOwner());
        setLayout(null);
        setResizable(false);
        initParentControls();
        initControls();
    }

    private void initParentControls() {
        btnClose = new JButton(CLOSE_BUTTON_NAME);
        btnClose.setSize(CLOSE_BUTTON_SIZE);
        btnClose.setActionCommand(CLOSE_BUTTON_CMD);
        btnClose.addActionListener(this);

        reportNameLabel = new JLabel();
        reportNameLabel.setFont(model.getDefaultBoldFont());

        reportTable = new JTable();
        tableModel = new DefaultTableModel(TABLE_DATA, TABLE_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells false => table is readonly
            }
        };
        reportTable.setModel(tableModel);
        scrollPane = new JScrollPane(reportTable);

        add(btnClose);
        add(reportNameLabel);
        add(scrollPane);
    }

    public void showDialog() {
        onStart();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    public void closeDialog() {
        setVisible(false);
    }

    protected abstract void onStart();
    protected abstract void updateReport();
    protected abstract void initControls();

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case CLOSE_BUTTON_CMD:
                closeDialog();
                break;
        }
    }
}
