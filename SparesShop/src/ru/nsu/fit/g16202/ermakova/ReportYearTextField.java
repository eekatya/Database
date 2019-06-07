package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

public class ReportYearTextField extends JTextField implements FocusListener {
    private Integer yearValue;
    private ReportDialog report;
    private int yearFrom;
    private int yearTo;

    public ReportYearTextField(String text, ReportDialog report, int constraintFrom, int constraintTo){
        super(text);
        this.report = report;
        yearFrom = constraintFrom;
        yearTo = constraintTo;
        updateDateValue();
        addFocusListener(this);
    }

    private void updateDateValue() {
        if ((getText() == null) || (getText().equals(""))) {
            yearValue = null;
        } else {
            try {
                int result = Integer.parseInt(getText());
                if (result < yearFrom || result > yearTo) throw new Exception();
                yearValue = result;
            } catch (Exception e) {
                yearValue = null;
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

    public Integer getYearValue() {
        return yearValue;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        updateReport();
    }
}
