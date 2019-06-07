package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

/*
Класс для хранения поля даты в отчёте (обновляет отчёт при потере фокуса)
 */
public class ReportDateTextField extends JTextField implements FocusListener {
    private Date dateValue;
    private ReportDialog report;

    public ReportDateTextField(String text, ReportDialog report){
        super(text);
        this.report = report;
        updateDateValue();
        addFocusListener(this);
    }

    private void updateDateValue() {
        if ((getText() == null) || (getText().equals(""))) {
            dateValue = null;
        } else {
            try {
                dateValue = ShopModel.stringToDate(getText());
            } catch (Exception e) {
                dateValue = null;
                setText("");
                DBMessages.showErrorMessage(this, DBMessages.INPUT_DATA_ERROR_MESSAGE);
                return;
            }
        }
    }

    private void updateReport() {
        updateDateValue();
        report.updateReport();
    }

    public Date getDateValue() {
        return dateValue;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        updateReport();
    }
}
