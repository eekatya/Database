package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;

/*
Класс для вывода сообщений (информационых или об ошибках)
 */
public class DBMessages {
    private static final String ERROR_TITLE = "Ошибка";
    private static final String INFORMATION_TITLE = "Сообщение";

    public static final String INPUT_DATA_ERROR_MESSAGE = "Данные введены не верно";
    public static final String DB_REQUEST_ERROR_MSG = "Ошибка запроса к базе данных";
    public static final String DB_CONNECT_ERROR_MSG = "Ошибка подключения к базе данных";
    public static final String DB_DISCONNECT_ERROR_MSG = "Ошибка отключения от базы данных";
    public static final String DB_CREATE_ERROR_MSG = "Ошибка создания базы данных";
    public static final String DB_CREATED_INFORMATION_MSG = "База данных создана";
    public static final String DB_UNSUPPORTED_OPERATION_MSG = "Данная операция не поддерживается (пока)";
    public static final String NULLS_NOT_ALLOWED_MSG = "Пустые поля недопустимы";
    public static final String LINKS_TO_KEY_EXISTS_MSG = "На ключ удаляемой записи есть ссылки в других таблицах";

    public static void showErrorMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent,
                msg,
                ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
    }
    public static void showInformationMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent,
                msg,
                INFORMATION_TITLE,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
