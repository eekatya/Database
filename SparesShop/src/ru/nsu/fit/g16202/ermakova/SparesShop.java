package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;

/*
Класс приложения
 */
public class SparesShop {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                new ShopModel();
            }
        });
    }
}
