package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/*
Класс модели приложения
 */
public class ShopModel {
    public static final String CURRENCY_TEXT = " руб.";
    private static final String DEFAULT_FONT_NAME = "Segoe UI";
    private static final int DEFAULT_FONT_SIZE = 13;
    private static final String DB_USER_NAME = "postgres";
    private static final String DB_PASSWORD = "1";
    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/spare_shop";
    private static final String DB_DRIVER_NAME = "org.postgresql.Driver";
    private static final String MANAGERS_REFERENCE_DIALOG_TITLE = "Справочник менеджеров";
    private static final String SPARES_REFERENCE_DIALOG_TITLE = "Справочник запасных частей";
    private static final String BUYERS_REFERENCE_DIALOG_TITLE = "Справочник покупателей";
    private static final String SUPPLIERS_BY_SPARE_REPORT_DIALOG_TITLE = "Отчёт 1. Список поставщиков по выбранной детали";
    private static final String SPARES_INFO_REPORT_DIALOG_TITLE = "Отчёт 2. Сведения по деталям";
    private static final String BUYERS_INFO_REPORT_DIALOG_TITLE = "Отчёт 3. Перечень покупателей";
    private static final String WAREHOUSE_INFO_REPORT_DIALOG_TITLE = "Отчёт 4. Перечень деталей на складе";
    private static final String TOP_INFO_REPORT_DIALOG_TITLE = "Отчёт 5. Топ деталей и поставщиков";
    private static final String AVERAGE_MONTH_SELLS_REPORT_DIALOG_TITLE = "Отчёт 6. Среднее число продаж на месяц";
    private static final String INVENTORY_REPORT_TITLE = "Отчёт 7. Кассовый отчёт";
    private static final String NUMBER_OF_CELLS_TITLE = "Отчет 8. Количество свободных ячеек на складе" ;
    private static final String MANAGERS_ORDERS_TITLE = "Отчет 9. Менеджеры для товаров, побывавших на складе" ;
    private static final String WARRANTY_SPARES_TITLE = "Отчет 10. Товары с гарантией на складе" ;
    private static final String SPARES_SEARCH_TITLE = "Поиск деталей по наименованию и стоимости";

    private ShopFrame frame;
    private Font defaultFont;
    private Font defaultBoldFont;
    private Connection conn;
    ManagersReference managersReference;
    SparesReference sparesReference;
    BuyersReference customersReference;
    SuppliersBySpareReport suppliersBySpareReport;
    SparesInfoReport sparesInfoReport;
    BuyersInfoReport buyersInfoReport;
    WarehouseInfoReport warehouseInfoReport;
    TopSparesSuppliersReport topReport;
    AverageMonthSellsReport averageMonthSellsReport;
    SparesSearchDialog sparesSearchDialog;

    public ShopModel() {
        defaultFont = new Font(DEFAULT_FONT_NAME, Font.PLAIN, DEFAULT_FONT_SIZE);
        defaultBoldFont = new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE - 1);
        conn = null;
        managersReference = null;
        sparesReference = null;
        customersReference = null;
        suppliersBySpareReport = null;
        sparesInfoReport = null;
        buyersInfoReport = null;
        warehouseInfoReport = null;
        topReport = null;
        averageMonthSellsReport = null;
        sparesSearchDialog = null;
        frame = new ShopFrame(this);
        frame.initMenu();
        frame.setConnected(false);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public Font getDefaultBoldFont() {
        return defaultBoldFont;
    }

    public void exitApplication() {
        if (conn != null)
            disconnectDatabase();
    }

    public static String dateToString(Date date) {
        String str = null;
        str = new SimpleDateFormat("dd.MM.yyyy").format(date);
        return str;
    }

    public static Date stringToDate(String str) throws ParseException {
        Date date = null;
        date = new SimpleDateFormat("dd.MM.yyyy").parse(str);
        return date;
    }

    public static int parseCost(String cost) {
        String temp = cost.substring(0, cost.length() - 2);
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < temp.length(); i++) {
            char ch = temp.charAt(i);
            int n = (int) ch;
            if (ch != ' ' && ch != ',' && ch != 160)
                sb.append(ch);
        }
        int result = Integer.parseInt(sb.toString());
        return result;
    }

    public static int parseTextCost(String cost) {
        int result = 0;
        try {
            result = (int)(Double.parseDouble(cost) * 100.0);
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    public static String formatCost(int cost, String delim, String currency) {
        int whole = cost / 100;
        int fract = cost - whole * 100;
        StringBuilder sb = new StringBuilder("");
        sb.append(whole);
        sb.append(delim);
        if (fract == 0) sb.append("00");
        else sb.append(fract);
        sb.append(currency);
        return sb.toString();
    }

    public String firstDayOfYear(int year) {
        if (year < 1901 || year > 2099) return new String("");
        return new String("01.01." + year);
    }

    public String lastDayOfYear(int year) {
        if (year < 1901 || year > 2099) return new String("");
        return new String("31.12." + year);
    }

    public void connectDatabase() {
        try {
            Class.forName(DB_DRIVER_NAME);
            conn = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER_NAME, DB_PASSWORD);
            System.err.println("Connected to database successfully");
        } catch (Exception e) {
            System.err.println("Exception in connection to database");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_CONNECT_ERROR_MSG);
            return;
        }
        frame.setConnected(true);
    }

    public void disconnectDatabase() {
        try {
            conn.close();
            conn = null;
            System.err.println("Disconnected from database successfully");
        } catch (Exception e) {
            System.err.println("Exception in disconnection from database");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_DISCONNECT_ERROR_MSG);
            return;
        }
        frame.setConnected(false);
    }

    private void dropDatabase() throws Exception {
        Statement stmt;
        String sql;
        Class.forName(DB_DRIVER_NAME);
        conn = DriverManager.getConnection(CONNECTION_URL, DB_USER_NAME, DB_PASSWORD);
        System.err.println("Connected to postgreSQL successfully");
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
        sql = "DROP DATABASE IF EXISTS spare_shop";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
        conn = null;
        System.err.println("Drop database completed successfully");
    }

    private void createDatabase() throws Exception {
        Statement stmt;
        String sql;
        Class.forName(DB_DRIVER_NAME);
        conn = DriverManager.getConnection(CONNECTION_URL, DB_USER_NAME, DB_PASSWORD);
        System.err.println("Connected to postgreSQL successfully");
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
        sql = "CREATE DATABASE spare_shop";
        stmt.execute(sql);
        stmt.close();
        conn.close();
        conn = null;
        System.err.println("Create database completed successfully");
    }

    private void createAllTables() throws Exception {
        Statement stmt;
        String sql;
        Class.forName(DB_DRIVER_NAME);
        conn = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER_NAME, DB_PASSWORD);
        System.err.println("Connected to database successfully");
        conn.setAutoCommit(false);

        stmt = conn.createStatement();
        sql = "CREATE TABLE MANAGERS " + // Таблица 1. "Менеджеры"
                "(ID INT PRIMARY KEY    NOT NULL," +
                " LNAME VARCHAR(50)     NOT NULL," +
                " FNAME VARCHAR(50)     NOT NULL," +
                " MNAME VARCHAR(50)," +
                " PASSPORT VARCHAR(100) NOT NULL," +
                " HIRE DATE             NOT NULL," +
                " DISMISS DATE" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table MANAGERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE SPARES " + // Таблица 2. "Детали"
                "(ID INT PRIMARY KEY  NOT NULL," +
                " NAME VARCHAR(100)   NOT NULL," +
                " ARTICLE VARCHAR(50) NOT NULL" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table SPARES completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE ORDER_STATUSES " + // Таблица 4. "Статусы заказа"
                "(ID INT PRIMARY KEY NOT NULL," +
                " NAME VARCHAR(50)   NOT NULL" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table ORDER_STATUSES completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE BUYER_TYPES" + // Таблица "Типы покупателей"
                " (ID INT PRIMARY KEY NOT NULL," +
                " NAME VARCHAR(50)    NOT NULL" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table BUYER_TYPES completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE BUYERS " + // Таблица 5. "Покупатели"
                "(ID INT PRIMARY KEY   NOT NULL," +
                " BUYER_ID INT         NOT NULL," +
                " TYPE INT             NOT NULL," +
                " PHONE CHAR(12)       NOT NULL," +
                " EMAIL VARCHAR(40)    NOT NULL," +
                " ADDRESS VARCHAR(100) NOT NULL," +
                " FOREIGN KEY (TYPE) REFERENCES BUYER_TYPES (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table BUYERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE PRIVATE_BUYERS" + // Таблица "Покупатели физ. лица"
                " (ID INT PRIMARY KEY       NOT NULL," +
                " LNAME VARCHAR(50)         NOT NULL," +
                " FNAME VARCHAR(50)         NOT NULL," +
                " MNAME VARCHAR(50)," +
                " PASSPORT VARCHAR(100)     NOT NULL," +
                " REGISTRATION VARCHAR(100)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table PRIVATE_BUYERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE LEGAL_BUYERS" + // Таблица "Покупатели юр. лица"
                " (ID INT PRIMARY KEY  NOT NULL," +
                " NAME VARCHAR(80)     NOT NULL," +
                " ADDRESS VARCHAR(100) NOT NULL," +
                " INN CHAR(12)         NOT NULL," +
                " CONTACT VARCHAR(80)  NOT NULL," +
                " BANK_DETAILS VARCHAR(150)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table LEGAL_BUYERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE SUPPLIER_TYPES" + // Таблица "Типы поставщиков"
                " (ID INT PRIMARY KEY NOT NULL," +
                " NAME VARCHAR(50)    NOT NULL" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table SUPPLIER_TYPES completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE SUPPLIERS" + // Таблица "Поставщики"
                " (ID INT PRIMARY KEY       NOT NULL," +
                " TYPE INT                  NOT NULL," +
                " NAME VARCHAR(80)          NOT NULL," +
                " ADDRESS VARCHAR(100)      NOT NULL," +
                " INN CHAR(12)              NOT NULL," +
                " CONTACT VARCHAR(80)       NOT NULL," +
                " BANK_DETAILS VARCHAR(150)," +
                " STATUS BOOLEAN            NOT NULL," +
                " FOREIGN KEY (TYPE) REFERENCES SUPPLIER_TYPES (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table SUPPLIERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE SUPPLIER_SPARE" + // Таблица "Запчасти поставляемые поставщиками"
                " (ID INT PRIMARY KEY NOT NULL," +
                " SPARE_ID INT        NOT NULL," +
                " SUPPLIER_ID INT     NOT NULL," +
                " TIME INT            NOT NULL," +
                " COST MONEY          NOT NULL," +
                " FOREIGN KEY (SPARE_ID) REFERENCES SPARES (ID)," +
                " FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIERS (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table SUPPLIER_SPARE completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE ORDERS " + // Таблица 3. "Заказы"
                "(ID INT PRIMARY KEY NOT NULL," +
                " NUMBER VARCHAR(20) NOT NULL," +
                " ORDER_DATE DATE    NOT NULL," +
                " RECEIVE_DATE DATE CONSTRAINT rcv_date_less_ord_date CHECK (RECEIVE_DATE = NULL OR RECEIVE_DATE >= ORDER_DATE)," +
                " STATUS INT         NOT NULL," +
                " SUPPLIER INT       NOT NULL," +
                " WARRANTY BOOLEAN   NOT NULL," +
                " MANAGER INT        NOT NULL," +
                " FOREIGN KEY (STATUS) REFERENCES ORDER_STATUSES (ID)," +
                " FOREIGN KEY (SUPPLIER) REFERENCES SUPPLIERS (ID)," +
                " FOREIGN KEY (MANAGER) REFERENCES MANAGERS (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table BUYERS completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE ORDERS_STRUCTURE " + // Таблица 9. "Состав заказов"
                "(ID INT PRIMARY KEY NOT NULL," +
                " ORDER_ID INT NOT NULL," +
                " SPARE_ID INT NOT NULL," +
                " SPARES_NUM INT," +
                " COST MONEY         NOT NULL," +
                " FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID)," +
                " FOREIGN KEY (SPARE_ID) REFERENCES SPARES (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table ORDERS_STRUCTURE completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE WAREHOUSE_STRUCTURE " + // Таблица 17. "Состав склада"
                "(ID INT PRIMARY KEY NOT NULL," +
                " PLACE INT          NOT NULL CHECK (PLACE > 0)," +
                " ORDER_ID INT       NOT NULL," +
                " ACTIVE BOOLEAN     NOT NULL," +
                " FOREIGN KEY (ORDER_ID) REFERENCES ORDERS_STRUCTURE (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table WAREHOUSE_STRUCTURE completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE SALES " + // Таблица 18. "Продажи"
                "(ID INT PRIMARY KEY NOT NULL," +
                " BUYER_ID INT       NOT NULL," +
                " SALE_DATE DATE     NOT NULL," +
                " FOREIGN KEY (BUYER_ID) REFERENCES BUYERS (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table SALES completed successfully");

        stmt = conn.createStatement();
        sql = "CREATE TABLE WAREHOUSE_SALES " + // Таблица 19. "Продажи со склада"
                "(ID INT PRIMARY KEY NOT NULL," +
                " WAREHOUSE_ID INT   NOT NULL," +
                " SALE_ID INT        NOT NULL," +
                " COST MONEY         NOT NULL," +
                " FOREIGN KEY (WAREHOUSE_ID) REFERENCES WAREHOUSE_STRUCTURE (ID)," +
                " FOREIGN KEY (SALE_ID) REFERENCES SALES (ID)" +
                ")";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Create table WAREHOUSE_SALES completed successfully");
        // TODO: complete other tables
    }

    private void fillStaticTables() throws Exception {
        Statement stmt;
        String sql;
        conn.setAutoCommit(false);

        try {
            // Таблица 4
            stmt = conn.createStatement();
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (1, 'Заказан');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (2, 'Оплачен поставщику');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (3, 'Пришёл на таможню');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (4, 'Оформлен на таможне');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (5, 'Оплачены таможенные пошлины');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ORDER_STATUSES (ID, NAME) VALUES (6, 'Поступил на склад');";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            System.err.println("Table ORDER_STATUSES filled");
        } catch (Exception e) {
            conn.rollback();
            throw new Exception();
        }

        // Таблица 6
        stmt = conn.createStatement();
        sql = "INSERT INTO BUYER_TYPES (ID, NAME) VALUES (1, 'Физическое лицо');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYER_TYPES (ID, NAME) VALUES (2, 'Юридическое лицо');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table BUYER_TYPES filled");

        // Таблица 10
        String query = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (?,?)";
        PreparedStatement pStatement = conn.prepareStatement(query);
        pStatement.setInt(1, 1);
        pStatement.setString(2, "Фирма");
        pStatement.addBatch();
        pStatement.setInt(1, 2);
        pStatement.setString(2, "Дилер");
        pStatement.addBatch();
        pStatement.setInt(1, 3);
        pStatement.setString(2, "Небольшое производство");
        pStatement.addBatch();
        pStatement.setInt(1, 4);
        pStatement.setString(2, "Мелкий поставщик");
        pStatement.addBatch();
        pStatement.setInt(1, 5);
        pStatement.setString(2, "Магазин");
        pStatement.addBatch();

        pStatement.executeBatch();
        pStatement.close();

//        stmt = conn.createStatement();
//        sql = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (1, 'Фирма');";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (2, 'Дилер');";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (3, 'Небольшое производство');";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (4, 'Мелкий поставщик');";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO SUPPLIER_TYPES (ID, NAME) VALUES (5, 'Магазин');";
//        stmt.executeUpdate(sql);
//        stmt.close();
//        conn.commit();
        System.err.println("Table SUPPLIER_TYPES filled");

        // TODO: complete other tables
    }

    private class OrdersStructure {
        public int orderId;
        public int sparesNum;
        public Date receiveDate;
        public OrdersStructure(int id, int num, Date date) {
            orderId = id;
            sparesNum = num;
            receiveDate = new Date(date.getTime());
        }
    }

    private int getWarehouseEmptyPlace() throws SQLException {
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "SELECT PLACE, ACTIVE FROM WAREHOUSE_STRUCTURE" +
                " WHERE ACTIVE = TRUE" +
                " ORDER BY PLACE;";
        System.err.println("getWarehouseEmptyPlace: " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        int place = 1;
        while (rs.next()) {
            int id = rs.getInt("PLACE");
            if (place < id) break;
            place++;
        }
        rs.close();
        stmt.close();
        conn.commit();
        return place;
    }

    private int getNextMaxValue(String colum, String table) throws SQLException {
        Statement stmt;
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        String sql = "SELECT MAX(" + colum + ") FROM " + table;
        System.err.println("getNextMaxValue: " + sql);
        ResultSet rs = stmt.executeQuery( sql);
        int max = -1;
        if (rs.next()) {
            max = rs.getInt("MAX");
        }
        max++;
        rs.close();
        stmt.close();
        conn.commit();
        return max;
    }

    private void outputWarehouse() throws SQLException {
        System.err.println("W: ID, PLACE, ORDER_ID, EMPTY");
        Statement stmt;
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        String sql = "SELECT * FROM WAREHOUSE_STRUCTURE ORDER BY PLACE;";
        ResultSet rs = stmt.executeQuery( sql);
        while (rs.next()) {
            int id = rs.getInt("ID");
            int place = rs.getInt("PLACE");
            int order = rs.getInt("ORDER_ID");
            boolean active = rs.getBoolean("ACTIVE");
            System.err.println(id + " " + place + " " + order + " " + active);
        }
        rs.close();
        stmt.close();
        conn.commit();
    }

    private void putSpareToWarehouse(int placeId, int ordersStructureId) throws SQLException {
        outputWarehouse();
        Statement stmt;
        String sql;
        stmt = conn.createStatement();
        int maxID = getNextMaxValue("ID", "WAREHOUSE_STRUCTURE");
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        sql = "INSERT INTO WAREHOUSE_STRUCTURE (ID, PLACE, ORDER_ID, ACTIVE)" +
                " VALUES (" + maxID + ", " +
                placeId + ", " +
                ordersStructureId + ", " +
                "TRUE);";
        System.err.println("putSpareToWarehouse: " + sql);
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        outputWarehouse();
    }

    private int getRandomBuyerId() throws SQLException {
        Statement stmt;
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        String sql = "SELECT COUNT(*) FROM BUYERS";
        System.err.println("getRandomBuyerId: " + sql);
        ResultSet rs = stmt.executeQuery( sql);
        int buyersCount = -1;
        if (rs.next()) {
            buyersCount = rs.getInt("COUNT");
        }
        rs.close();
        stmt.close();
        conn.commit();

        stmt = conn.createStatement();
        System.err.println("getRandomBuyerId: " + sql);
        rs = stmt.executeQuery( "SELECT ID FROM BUYERS ORDER BY ID");
        int buyerId = -1;
        while (rs.next()) {
            if (buyersCount == 0) {
                buyerId = rs.getInt("ID");
                break;
            }
            buyersCount--;
        }
        rs.close();
        stmt.close();
        conn.commit();
        return buyerId;
    }

    private void putSparesToWarehouse(int ordersStructureId, int sparesNum) throws SQLException {
        for (int i = 0; i < sparesNum; i++) {
            int place = getWarehouseEmptyPlace();
            putSpareToWarehouse(place, ordersStructureId);
        }
    }

    private class SellInfo {
        public int buyer;
        public Date date;

        public SellInfo(int buyer, Date date) {
            this.buyer = buyer;
            this.date = new Date(date.getTime());
        }
    }

    private ArrayList<OrdersStructure> getOrdersList() throws SQLException {
        ArrayList<OrdersStructure> orders = new ArrayList<>();
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "SELECT ORDERS_STRUCTURE.ID AS ID, ORDERS_STRUCTURE.ORDER_ID AS ORDER_ID, ORDERS_STRUCTURE.SPARES_NUM AS SPARES_NUM, ORDERS.RECEIVE_DATE AS RECEIVE_DATE" +
                " FROM ORDERS_STRUCTURE, ORDERS, SPARES" +
                " WHERE ORDERS_STRUCTURE.ORDER_ID = ORDERS.ID" +
                " AND ORDERS_STRUCTURE.SPARE_ID = SPARES.ID" +
                " AND ORDERS.STATUS = 6" +
                " ORDER BY ORDERS.RECEIVE_DATE;";
        System.err.println("getOrdersList: " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            orders.add(new OrdersStructure(rs.getInt("ID"),
                    rs.getInt("SPARES_NUM"),
                    rs.getDate("RECEIVE_DATE")
            ));
        }
        rs.close();
        stmt.close();
        conn.commit();
        return orders;
    }

    private ArrayList<Integer> getRandomSparesFromWarehouse(int percent) throws SQLException {
        ArrayList<Integer> sparesList = new ArrayList<>();
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "SELECT COUNT(ID) FROM WAREHOUSE_STRUCTURE WHERE ACTIVE = TRUE";
        System.err.println("getRandomSparesFromWarehouse: " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        int totalCount = 0;
        if (rs.next()) {
            totalCount = rs.getInt("COUNT");
        }
        rs.close();
        stmt.close();
        conn.commit();
        int count = (totalCount * percent) / 100;
        Random rnd = new Random((new Date()).getTime());
        for (int i = 0; i < count; i++) {
            int spare = rnd.nextInt(totalCount);
            stmt = conn.createStatement();
            sql = "SELECT ID FROM WAREHOUSE_STRUCTURE WHERE ACTIVE = TRUE ORDER BY ID;";
            System.err.println("getRandomSparesFromWarehouse: " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (spare == 0) {
                    sparesList.add(rs.getInt("ID"));
                    break;
                }
                spare--;
            }
            rs.close();
            stmt.close();
            conn.commit();
        }
        return sparesList;
    }

    private void processDeferredSells(ArrayList<SellInfo> deferredSells, Date beforeDate) throws SQLException {
        conn.setAutoCommit(false);
        Random rnd = new Random((new Date()).getTime());
        Iterator<SellInfo> iterator = deferredSells.iterator();
        while (iterator.hasNext()) {
            SellInfo info = iterator.next();
            if (beforeDate == null || (info.date.getTime() < beforeDate.getTime())) {
                int percent = rnd.nextInt(15) + 10; // Выкупаем от 10 до 25% со склада
                ArrayList<Integer> sparesList = getRandomSparesFromWarehouse(percent);
                int saleId = getNextMaxValue("ID", "SALES");
                Statement stmt;
                String sql = "INSERT INTO SALES (ID, BUYER_ID, SALE_DATE)" +
                        " VALUES(" + saleId +
                        ", " + info.buyer +
                        ", '" + dateToString(info.date) + "');";
                stmt = conn.createStatement();
                System.err.println("processDeferredSells: " + sql);
                stmt.executeUpdate(sql); // Добавляем информацию о новой покупке
                stmt.close();
                conn.commit();
                for (int i = 0; i < sparesList.size(); i++) {
                    int warehouseSaleId = getNextMaxValue("ID", "WAREHOUSE_SALES");
                    stmt = conn.createStatement();
                    sql = "SELECT ORDERS_STRUCTURE.ID AS ORDER_STRUCTURE_ID, ORDERS_STRUCTURE.ORDER_ID AS ORDER_ID, ORDERS_STRUCTURE.SPARE_ID AS SPARE_ID, ORDERS_STRUCTURE.SPARES_NUM AS SPARES_NUM, ORDERS_STRUCTURE.COST AS COST, WAREHOUSE_STRUCTURE.ID AS WAREHOUSE_ID, WAREHOUSE_STRUCTURE.ORDER_ID AS WAREHOUSE_ORDER_ID" +
                            " FROM ORDERS_STRUCTURE, WAREHOUSE_STRUCTURE" +
                            " WHERE WAREHOUSE_STRUCTURE.ID = " + sparesList.get(i) +
                            " ORDER BY ORDER_STRUCTURE_ID";
                    System.err.println("processDeferredSells: " + sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    if (!rs.next()) {
                        System.err.println("No records here: " + sql);
                        throw new SQLException();
                    }
                    int cost = parseCost(rs.getString("COST")) * (100 + 20) / 100 / rs.getInt("SPARES_NUM"); // Получаем стоимость продажи детали (+20%)
                    rs.close();
                    stmt.close();
                    conn.commit();
                    sql = "INSERT INTO WAREHOUSE_SALES (ID, WAREHOUSE_ID, SALE_ID, COST)" +
                            " VALUES(" + warehouseSaleId +
                            ", " + sparesList.get(i) +
                            ", " + saleId +
                            ", " + formatCost(cost, ".", "") + ");";
                    stmt = conn.createStatement();
                    System.err.println("processDeferredSells: " + sql);
                    stmt.executeUpdate(sql); // Добавляем покупку детали на складе
                    stmt.close();
                    conn.commit();
                    outputWarehouse();
                    sql = "UPDATE WAREHOUSE_STRUCTURE SET ACTIVE = FALSE WHERE ID = " + sparesList.get(i);
                    stmt = conn.createStatement();
                    System.err.println("processDeferredSells: " + sql);
                    stmt.executeUpdate(sql); // Освобождаем мнсто на складе
                    stmt.close();
                    conn.commit();
                    outputWarehouse();
                }
                iterator.remove();
            }
        }
    }

    private Date getRandomDate(Date startDate, int maxDaysShift) {
        Random rnd = new Random((new Date()).getTime());
        int daysShift = rnd.nextInt(maxDaysShift);
        Calendar instance = Calendar.getInstance();
        instance.setTime(startDate); // Устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, daysShift);// Прибавляем random дней к запрошенной дате
        Date newDate = instance.getTime(); // получаем измененную дату
        return newDate;
    }

    private int getRandomBuyer() throws SQLException {
        Random rnd = new Random((new Date()).getTime());
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "SELECT COUNT(*) FROM BUYERS;";
        System.err.println("getRandomBuyer: " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        int count = 0;
        if (rs.next())
            count = rs.getInt("COUNT");
        if (count == 0) return -1;
        rs.close();
        stmt.close();
        conn.commit();
        int byerPosition = rnd.nextInt(count);
        stmt = conn.createStatement();
        sql = "SELECT ID FROM BUYERS ORDER BY ID;";
        System.err.println("getRandomBuyer: " + sql);
        rs = stmt.executeQuery(sql);
        int buyer = -1;
        while (rs.next()) {
            if (byerPosition == 0) {
                buyer = rs.getInt("ID");
                break;
            }
            byerPosition--;
        }
        rs.close();
        stmt.close();
        conn.commit();
        return buyer;
    }

    private void addSell(ArrayList<SellInfo> deferredSells, Date dateFrom) throws SQLException {
        int buyer = getRandomBuyer();
        Date sellDate = getRandomDate(dateFrom, 20);
        deferredSells.add(new SellInfo(buyer, sellDate));
    }

    /*
    1. Идём по заказам в хронологическом порядке.
    2. Распределяем детали на скад.
    3. Через каждые 1-3 заказа происходит 1-5 продаж. Выбираем 1-5 покупателей, соответственно. Дата продажи от +0 до +20 дней от поступления заказа.
    4. Каждая продажа выбирает 10-20% склада.
     */
    private void simulateSales() throws Exception {
        ArrayList<OrdersStructure> orders = getOrdersList(); // Получаем все заказанные детали по всем заказам
        ArrayList<SellInfo> deferredSells = new ArrayList<>(); // Отложенные продажи
        int i;
        Random rnd = new Random((new Date()).getTime());
        int startSells = rnd.nextInt(3) + 1; // Через каждые 1...3 заказа производим покупки
        for (i = 0; i < orders.size(); i++) {
            processDeferredSells(deferredSells, orders.get(i).receiveDate); // Выполняем продажи, которые были до получения деталей по заказу
            putSparesToWarehouse(orders.get(i).orderId, orders.get(i).sparesNum); // Помещаем детали из заказа на склад
            if (startSells == 0) {
                int sellsCount = rnd.nextInt(5) + 1; // Количество продаж равно 1...5
                for (int j = 0; j < sellsCount; j++)
                    addSell(deferredSells, orders.get(i).receiveDate); // Добавляем произвольную продажу в множество отложенных продаж
                startSells = rnd.nextInt(3) + 1;
            }
            startSells--;
        }
        processDeferredSells(deferredSells, null); // Выполняем все оставшиеся продажи
    }

    private void fillSampleTables() throws Exception {
        Statement stmt;
        String sql;
        // Таблица 1
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        sql = "INSERT INTO MANAGERS (ID, LNAME, FNAME, MNAME, PASSPORT, HIRE, DISMISS) VALUES (1, 'Иванов', 'Иван', 'Иванович', 'УВД г. Челябинска 01.01.1990 г.', '23.11.2009', NULL);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO MANAGERS (ID, LNAME, FNAME, MNAME, PASSPORT, HIRE, DISMISS) VALUES (2, 'Петров', 'Пётр', 'Петрович', 'УВД Казахстана', '03.08.2012', '12.12.2018');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO MANAGERS (ID, LNAME, FNAME, MNAME, PASSPORT, HIRE, DISMISS) VALUES (3, 'Калехашвили', 'Годердзи', NULL, 'УВД Кыргызтана', '19.02.2014', NULL);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO MANAGERS (ID, LNAME, FNAME, MNAME, PASSPORT, HIRE, DISMISS) VALUES (4, 'Сергеев', 'Сергей', 'Сергеевич', 'не известно', '17.01.2011', NULL);";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table MANAGERS filled");

        // Таблица 2
        stmt = conn.createStatement();
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (1, 'Втулка для Lexus 570', 'LX-570-VTULKA');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (2, 'Колесо 22\"для Lexus 570', 'LX-570-KOLESO22');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (3, 'Колесо 23\"для Lexus 570', 'LX-570-KOLESO23');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (4, 'Руль (золото) для Cadillac Escalade', 'CE-RUL-GOLD');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (5, 'Руль (серебро) для Cadillac Escalade', 'CE-RUL-SILVER');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (6, 'Руль (бронза) для Cadillac Escalade', 'CE-RUL-BRONZE');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (7, 'Втулка для Cadillac Escalade', 'CE-VTULKA');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (8, 'Колесо № 1 для Cadillac Escalade', 'CE-KOLESO-1');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (9, 'Колесо № 2 для Cadillac Escalade', 'CE-KOLESO-2');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SPARES (ID, NAME, ARTICLE) VALUES (10, 'Кузов Запорожец (зелёный)', 'KUZOV-ZAPOR-GREEN');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table SPARES filled");

        // Таблица 5
        stmt = conn.createStatement();
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (1, 1, 1, '+17894561230', 'myrussianemail@mail.ru', 'г. Москва, ул. Новосибирская');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (2, 2, 1, '+71114447770', 'myusaemail@gmail.ru', 'г. Лос-Анджелес, ул. GTA V');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (3, 3, 1, '+75124856214', 'i@hotmail.com', 'г. Новосибирск, ул. Московская');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (4, 4, 1, '+42087452158', 'my777@inbox.ru', 'г. Урюпинск, д. 1');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (5, 1, 2, '+73256987245', 'ermakova99@gmail.com', 'г. Екатеринбург, ул. им. НГУ');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (6, 2, 2, '+19999999991', 'mail@yandex.ru', 'г. Нефтянск, ул. Бензиновая');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO BUYERS (ID, BUYER_ID, TYPE, PHONE, EMAIL, ADDRESS) VALUES (7, 3, 2, '+11112223339', 'qqq@mail.ru', '-без адреса-');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table BUYERS filled");

        // Таблица 7
        stmt = conn.createStatement();
        sql = "INSERT INTO PRIVATE_BUYERS (ID, LNAME, FNAME, MNAME, PASSPORT, REGISTRATION) VALUES (1, 'Баширов', 'Руслан', NULL, 'Лондон', 'г. Томск, ул. Петрова');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO PRIVATE_BUYERS (ID, LNAME, FNAME, MNAME, PASSPORT, REGISTRATION) VALUES (2, 'Петров', 'Александр', NULL, 'Солсбери', 'г. Новосибирск, ул. Баширова');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO PRIVATE_BUYERS (ID, LNAME, FNAME, MNAME, PASSPORT, REGISTRATION) VALUES (3, 'Константинопольский', 'Константин', 'Константинович', '7500 УВД г. Константинополя', NULL);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO PRIVATE_BUYERS (ID, LNAME, FNAME, MNAME, PASSPORT, REGISTRATION) VALUES (4, 'Серов', 'Иван', 'Васильевич', '-', 'г. Ханты-Мансийск');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table PRIVATE_BUYERS filled");

        // Таблица 8
        stmt = conn.createStatement();
        sql = "INSERT INTO LEGAL_BUYERS (ID, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS) VALUES (1, 'Тойота-Центр', 'проспект Ильича', '12345', 'Звеницковский Всеволод', 'Альфа Банк № 263');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO LEGAL_BUYERS (ID, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS) VALUES (2, 'Лексус-Центр', 'улица Ленина', '67890', 'Потапов Иван', 'Банк ВТБ № 892, счёт 1234567890');";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO LEGAL_BUYERS (ID, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS) VALUES (3, 'Кадиллак-Центр', 'улица Пирогова', '1111111199', 'Некрасов Владимир Исаевич', 'Сбербанк, основное подразделение, счёт 999999999999');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table LEGAL_BUYERS filled");

        // Таблица 11
        stmt = conn.createStatement();
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (1, 1, 'Главный производитель', 'Малайзия', '123098', 'Эльф Петрович', 'Банк Малайзии № 1', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (2, 1, 'Самый Главный производитель', 'Кыргызтан, ул. Джугашвили', '777', 'Хафиз', 'Банковские реквизиты 123', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (3, 1, 'Запчасти \"К Тойоте\" прошлого века', 'Японские острова', '1', 'Хатико Иван', 'Japan Bank', FALSE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (4, 2, 'Дилер Мерседес', 'Москва, Рублёвка', '999777', 'Абрамович', 'Сбербанк 000', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (5, 2, 'Дилер Тойота', 'СПб', '00000001', 'Матвиенко М.М.', 'Газпромбанк', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (6, 2, 'Шкода ЗАПЧАСТИ', 'ул. Чешская', '333', 'Кыржишек Милан', 'European Bank 1', FALSE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (7, 3, 'Apple в гараже', 'г. Калифорнийский ул. Лос-Анджелевская', '123456789', 'Джобс Стивенов', 'Schwarzenegger Bank # 6', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (8, 3, 'Маленький поставщик', 'Урюпинск', '2', 'Петя Петров', 'Банк Югра, счёт № 1', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (9, 3, 'Производство в Иванове', 'ул. Большая', '222', 'Иванов Иван', 'Банк ВТБ', FALSE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (10, 4, 'Альпина - запчасти', 'Московский мкр.', '24578525', 'Пётр Грызлов', 'Банк ВТБ № 2', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (11, 4, 'Поставкин', 'г. Новосибирск', '745874', 'Шамишев Антон Власович', 'Банк ВТБ № 3', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (12, 4, 'Доставкин', 'г. Н-ск', '111444777', 'Антонов Артём', 'Альфа Банк, счёт лучший', FALSE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (13, 5, 'Магазин продукты', 'ул. Синяя', '987654', 'Тойотов Вячеслав', 'Газпромбанк, № 15', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (14, 5, 'Магазин за углом', 'ул. Красная', '555', 'Красин Иван', 'Газпромбанк, № 15', TRUE);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIERS (ID, TYPE, NAME, ADDRESS, INN, CONTACT, BANK_DETAILS, STATUS) VALUES (15, 5, 'Магазин Мелкий', 'ул. Зелёная', '666', 'Митрофан Петрович', 'Газпромбанк, № 18', FALSE);";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        System.err.println("Table SUPPLIERS filled");

        // Таблица 16
        stmt = conn.createStatement();
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (1, 1, 1, 10, 1500.88);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (2, 3, 1, 8, 256);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (3, 7, 1, 3, 500.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (4, 8, 1, 7, 221.80);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (5, 9, 1, 5, 119.41);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (6, 10, 1, 15, 18.23);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (7, 2, 2, 2, 1520.88);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (8, 3, 2, 11, 25.6);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (9, 4, 2, 6, 515.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (10, 5, 2, 3, 200.80);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (11, 7, 2, 14, 19.42);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (12, 8, 2, 13, 180.41);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (13, 9, 2, 19, 190.41);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (14, 10, 2, 10, 180.24);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (15, 1, 3, 10, 243.88);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (16, 7, 3, 11, 1089.6);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (17, 9, 3, 5, 11.18);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (18, 10, 3, 5, 201);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (19, 7, 4, 8, 500.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (20, 8, 4, 11, 221.80);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (21, 9, 4, 9, 119.41);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (22, 10, 4, 9, 18.23);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (23, 2, 5, 6, 100.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (24, 6, 5, 3, 201.80);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (25, 7, 5, 14, 190.4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (26, 8, 5, 13, 186.42);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (27, 9, 5, 19, 191);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (28, 10, 5, 10, 1800);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (29, 1, 6, 4, 1900);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (30, 2, 6, 5, 180);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (31, 1, 7, 9, 256);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (32, 3, 7, 7, 510.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (33, 6, 7, 4, 201.80);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (34, 7, 7, 14, 190.42);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (35, 8, 7, 13, 130.41);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (36, 10, 7, 15, 140.55);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (37, 4, 8, 1, 2.56);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (38, 6, 8, 9, 51.77);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (39, 1, 9, 4, 250.6);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (40, 2, 9, 3, 515.77);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (41, 3, 9, 8, 210.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (42, 6, 9, 12, 19.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (43, 7, 9, 11, 181.47);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (44, 8, 9, 12, 242.40);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (45, 9, 9, 7, 131.24);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (46, 1, 10, 5, 550);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (47, 3, 10, 4, 211);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (48, 8, 10, 3, 190.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (49, 9, 10, 5, 146.55);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (50, 5, 11, 6, 551.2);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (51, 6, 11, 4, 211);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (52, 7, 11, 5, 19.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (53, 8, 11, 6, 14.65);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (54, 1, 12, 4, 211);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (55, 3, 12, 4, 190.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (56, 5, 12, 5, 121.47);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (57, 7, 12, 5, 241.45);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (58, 9, 12, 10, 130);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (59, 1, 13, 12, 210.99);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (60, 2, 13, 4, 547);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (61, 3, 13, 5, 315.22);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (62, 6, 14, 3, 519.02);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (63, 7, 14, 9, 879);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (64, 8, 14, 15, 298.45);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (65, 10, 14, 12, 130);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (66, 9, 15, 11, 520);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO SUPPLIER_SPARE (ID, SPARE_ID, SUPPLIER_ID, TIME, COST) VALUES (67, 10, 15, 11, 522);";
        stmt.executeUpdate(sql);

        stmt.close();
        conn.commit();
        System.err.println("Table SUPPLIER_SPARE filled");

        // Таблица 3
        stmt = conn.createStatement();
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (1, 'ЕГВ-12', '23.08.2018', '01.09.18', 6, 1, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (2, 'ЕГВ-13', '24.08.2018', '03.09.2018', 6, 1, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (3, 'ЕГВ-15', '24.08.2018', '10.09.2018', 6, 1, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (4, 'НЕЦБК-1', '01.09.2018', '15.09.2018', 6, 1, TRUE, 2);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (5, 'НЕЦБК-2', '01.09.2018', '16.09.2018', 6, 1, TRUE, 2);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (6, 'НЕЦБК-3', '11.11.2018', '30.12.2018', 6, 1, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (7, 'НЕЦБК-4', '12.12.2018', '31.12.2018', 6, 1, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (8, 'НЕЦБК-5', '15.12.2018', '10.01.2019', 6, 1, TRUE, 4);";

        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (9, 'УМБЦ-646', '01.01.2019', '23.01.2019', 6, 2, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (10, 'УМБЦ-2548', '05.12.2018', '31.12.2018', 6, 2, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (11, 'УМБЦ-7854', '11.07.2018', '15.09.2018', 6, 2, TRUE, 2);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (12, 'КЛМН-2225', '01.02.2019', '15.02.2019', 6, 2, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (13, 'КЛМН-117', '20.01.2019', '16.02.2019', 6, 2, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (14, 'КЛМН-4478', '22.02.2019', '30.03.2019', 6, 2, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (15, 'КЛМН-119', '12.03.2019', '04.04.2019', 6, 2, TRUE, 4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (16, 'КЛМН-258', '15.12.2018', '05.01.2019', 6, 2, TRUE, 4);";
        stmt.executeUpdate(sql);


        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (17, 'НЕКМ-001', '01.03.2019', '16.03.2019', 6, 3, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (18, 'НЕКМ-002', '06.03.2019', '14.04.2019', 6, 3, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (19, 'НЕКМ-003', '15.04.2019', NULL, 5, 3, TRUE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (20, 'ЙЦУКЕН-201', '01.04.2019', '19.04.2019', 6, 4, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (21, 'ЙЦУКЕН-302', '06.04.2019', '15.04.2019', 6, 4, TRUE, 4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (22, 'ЙЦУКЕН-403', '09.05.2019', NULL, 3, 4, TRUE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (23, 'ASDF-119', '20.12.2018', '16.01.2019', 6, 5, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (24, 'ASDF-4318', '22.01.2019', '31.01.2019', 6, 5, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (25, 'ASDF-2190', '12.10.2018', '12.12.2018', 6, 5, TRUE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (26, 'ASDF-2058', '15.12.2018', '05.02.2019', 6, 5, TRUE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (27, 'ASDF-119', '20.12.2018', '16.01.2019', 6, 6, TRUE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (28, 'ASDF-4318', '22.01.2019', '31.01.2019', 6, 6, TRUE, 3);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (29, 'QWERTY-1', '21.12.2018', '16.02.2019', 6, 7, FALSE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (30, 'QWERTY-2', '22.01.2019', '28.02.2019', 6, 7, FALSE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (31, 'QWERTY-3', '12.11.2018', '12.01.2019', 6, 7, FALSE, 4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (32, 'QWERTY-4', '18.04.2019', NULL, 4, 7, FALSE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (33, 'ЗАКАЗ-25', '03.04.2019', NULL, 4, 8, FALSE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (34, ' ЗАКАЗ-50', '29.04.2019', '10.05.2019', 6, 8, FALSE, 3);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (35, 'ЗАКАЗ-15-25', '03.05.2019', '09.05.2019', 6, 9, FALSE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (36, 'ЗАКАЗ-15-50', '01.03.2019', '01.05.2019', 6, 9, FALSE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (37, 'ORDER-1', '25.03.2019', '10.04.2019', 6, 10, FALSE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (38, 'ORDER-2', '02.02.2019', '01.03.2019', 6, 10, FALSE, 1);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (39, 'ORDER-10', '26.03.2019', '12.04.2019', 6, 11, FALSE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (40, 'ORDER-20', '05.02.2019', '20.03.2019', 6, 11, FALSE, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (41, 'ORDER-30', '10.02.2019', '22.03.2019', 6, 11, FALSE, 1);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (42, 'ORDER-100', '26.04.2019', '13.05.2019', 6, 12, FALSE, 1);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (43, 'ORDER-200', '05.03.2019', '20.05.2019', 6, 12, FALSE, 4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (44, 'ORDER-300', '10.03.2019', NULL, 5, 12, FALSE, 3);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (45, 'ORDER-101', '10.05.2019', NULL, 3, 13, FALSE, 4);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (46, 'ORDER-102', '03.05.2019', NULL, 3, 14, FALSE, 1);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (47, 'ORDER-103', '03.05.2019', NULL, 3, 14, FALSE, 4);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (48, 'ORDER-104', '01.04.2018', '01.08.2018', 6, 14, FALSE, 2);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS (ID, NUMBER, ORDER_DATE, RECEIVE_DATE, STATUS, SUPPLIER, WARRANTY, MANAGER) VALUES (49, 'ORDER-104', '05.03.2019', '04.04.2019', 6, 15, FALSE, 3);";
        stmt.executeUpdate(sql);

        stmt.close();
        conn.commit();
        System.err.println("Table ORDERS filled");

        // Таблица 9
        stmt = conn.createStatement();
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (1, 1, 3, 1, 250);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (2, 1, 7, 2, 1000);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (3, 2, 8, 3, 660);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (4, 9, 4, 3, 1500);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (5, 9, 9, 1, 180);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (6, 9, 10, 2, 360);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (7, 10, 3, 2, 50);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (8, 17, 7, 1, 1000);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (9, 17, 9, 1, 11);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (10, 18, 1, 1, 240);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (11, 20, 10, 1, 18);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (12, 21, 9, 2, 230);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (13, 21, 7, 1, 500);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (14, 23, 2, 2, 200);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (15, 23, 6, 2, 400);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (16, 23, 7, 1, 180);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (17, 24, 8, 1, 180);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (18, 27, 1, 1, 1800);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (19, 27, 2, 2, 300);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (20, 27, 2, 1, 150);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (21, 29, 6, 1, 200);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (22, 30, 10, 1, 130);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (23, 33, 4, 2, 3);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (24, 33, 6, 3, 150);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (25, 34, 6, 4, 199);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (26, 35, 1, 2, 500);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (27, 35, 2, 3, 1500);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (28, 35, 3, 1, 200);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (29, 36, 9, 3, 300);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (30, 37, 8, 1, 180);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (31, 39, 5, 2, 1000);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (32, 39, 8, 2, 25);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (33, 39, 7, 3, 55);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (34, 40, 6, 1, 200);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (35, 40, 7, 1, 19);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (36, 42, 1, 4, 800);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (37, 42, 3, 1, 180);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (38, 42, 9, 2, 240);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (39, 43, 7, 2, 420);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (40, 45, 1, 1, 200);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (41, 45, 2, 1, 540);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (42, 45, 3, 1, 310);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (43, 46, 6, 3, 1500);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (44, 47, 10, 3, 350);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (45, 47, 8, 1, 290);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (46, 48, 6, 1, 510);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (47, 48, 8, 1, 290);";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO ORDERS_STRUCTURE (ID, ORDER_ID, SPARE_ID, SPARES_NUM, COST) VALUES (48, 49, 10, 2, 1000);";
        stmt.executeUpdate(sql);

        stmt.close();
        conn.commit();
        System.err.println("Table ORDERS_STRUCTURE filled");

        simulateSales();
        // TODO: complete other tables
    }

    public void createEmptyDatabase() {
        try {
            dropDatabase();
            createDatabase();
            createAllTables();
            fillStaticTables();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_CREATE_ERROR_MSG);
            return;
        }
        DBMessages.showInformationMessage(frame, DBMessages.DB_CREATED_INFORMATION_MSG);
    }

    public void createFilledDatabase() {
        try {
            dropDatabase();
            createDatabase();
            createAllTables();
            fillStaticTables();
            fillSampleTables();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_CREATE_ERROR_MSG);
            return;
        }
        DBMessages.showInformationMessage(frame, DBMessages.DB_CREATED_INFORMATION_MSG);
    }

    public Statement createStatement() throws Exception {
        return conn.createStatement();
    }

    public void commitConn() throws Exception {
        conn.commit();
    }

    public void setAutoCommit(boolean b) throws Exception {
        conn.setAutoCommit(b);
    }

    public void closeConn() throws Exception {
        conn.close();
    }

    public void managersReference() {
        if (managersReference == null) {
            managersReference = new ManagersReference(this, frame, MANAGERS_REFERENCE_DIALOG_TITLE);
        }
        managersReference.showDialog();
    }

    public void sparesReference() {
        if (sparesReference == null) {
            sparesReference = new SparesReference(this, frame, SPARES_REFERENCE_DIALOG_TITLE);
        }
        sparesReference.showDialog();
    }

    public void customersReference() {
        if (customersReference == null) {
            customersReference = new BuyersReference(this, frame, BUYERS_REFERENCE_DIALOG_TITLE);
        }
        customersReference.showDialog();
    }

    public void privateCustomersReference() {

    }

    public void legalCustomersReference() {

    }

    public void suppliersBySpareReport() {
        if (suppliersBySpareReport == null) {
            suppliersBySpareReport = new SuppliersBySpareReport(this, frame, SUPPLIERS_BY_SPARE_REPORT_DIALOG_TITLE);
        }
        suppliersBySpareReport.showDialog();
    }

    public void sparesInfoReport() {
        if (sparesInfoReport == null) {
            sparesInfoReport = new SparesInfoReport(this, frame, SPARES_INFO_REPORT_DIALOG_TITLE);
        }
        sparesInfoReport.showDialog();
    }

    public void buyersInfoReport() {
        if (buyersInfoReport == null) {
            buyersInfoReport = new BuyersInfoReport(this, frame, BUYERS_INFO_REPORT_DIALOG_TITLE);
        }
        buyersInfoReport.showDialog();
    }

    public void warehouseInfoReport() {
        if (warehouseInfoReport == null) {
            warehouseInfoReport = new WarehouseInfoReport(this, frame, WAREHOUSE_INFO_REPORT_DIALOG_TITLE);
        }
        warehouseInfoReport.showDialog();
    }

    public void topInfoReport() {
        if (topReport == null) {
            topReport = new TopSparesSuppliersReport(this, frame, TOP_INFO_REPORT_DIALOG_TITLE);
        }
        topReport.showDialog();
    }

    public void averageMonthSells() {
        if (averageMonthSellsReport == null) {
            averageMonthSellsReport = new AverageMonthSellsReport(this, frame, AVERAGE_MONTH_SELLS_REPORT_DIALOG_TITLE);
        }
        averageMonthSellsReport.showDialog();
    }

    public void inventoryStatementReport() {
        Statement stmt;
        int cost;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "SELECT SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE, WAREHOUSE_SALES.COST AS COST" +
                    " FROM SPARES" +
                    " JOIN ORDERS_STRUCTURE ON SPARES.ID = ORDERS_STRUCTURE.SPARE_ID" +
                    " JOIN WAREHOUSE_STRUCTURE ON ORDERS_STRUCTURE.ID = WAREHOUSE_STRUCTURE.ORDER_ID" +
                    " JOIN WAREHOUSE_SALES ON WAREHOUSE_STRUCTURE.ID = WAREHOUSE_SALES.WAREHOUSE_ID" +
                    " JOIN SALES ON WAREHOUSE_SALES.SALE_ID = SALES.ID AND SALES.SALE_DATE = '15.05.2019'" +
//                    " GROUP BY NAME, ARTICLE" +
                    " ORDER BY NAME";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(INVENTORY_REPORT_TITLE);
            while(rs.next())
            {
                cost = parseCost(rs.getString("COST"))/100;
                System.out.println(rs.getString("NAME") +
                        rs.getString("ARTICLE") + " " +
                        cost + " руб.");
            }
            rs.close();
            stmt.close();
            commitConn();
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_REQUEST_ERROR_MSG);
        }
    }

    public void numberOfCellsReport() {
        Statement stmt;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "SELECT MAX(WAREHOUSE_STRUCTURE.PLACE) FROM SPARES CROSS JOIN ORDERS_STRUCTURE CROSS JOIN WAREHOUSE_STRUCTURE WHERE WAREHOUSE_STRUCTURE.ACTIVE";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(NUMBER_OF_CELLS_TITLE);
            while(rs.next())
            {
                System.out.println(rs.getInt("MAX"));
            }
            rs.close();
            stmt.close();
            commitConn();
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_REQUEST_ERROR_MSG);
        }
    }

    public void managersOrdersReport() {
        Statement stmt;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "SELECT MANAGERS.LNAME, MANAGERS.FNAME, MANAGERS.MNAME, MANAGERS.PASSPORT, MANAGERS.HIRE, MANAGERS.DISMISS, MIN(ORDERS.STATUS), ORDERS.NUMBER, ORDERS.ORDER_DATE FROM MANAGERS LEFT OUTER JOIN ORDERS ON MANAGERS.ID = ORDERS.MANAGER" +
                    " GROUP BY MANAGERS.LNAME, MANAGERS.FNAME, MANAGERS.MNAME, MANAGERS.PASSPORT, MANAGERS.HIRE, MANAGERS.DISMISS, ORDERS.NUMBER, ORDERS.ORDER_DATE" +
                    " HAVING MIN(ORDERS.STATUS)>3" +
                    " ORDER BY MANAGERS.LNAME";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(MANAGERS_ORDERS_TITLE);
            while(rs.next())
            {
                System.out.println(rs.getString("LNAME") + " " + rs.getString("FNAME")+ " " + rs.getString("MNAME")+ " " + rs.getString("PASSPORT")+ " " + rs.getDate("HIRE")+ " " + rs.getDate("DISMISS")+ " " + rs.getInt("MIN")+ " " + rs.getString("NUMBER")+ " " + rs.getDate("ORDER_DATE"));
            }
            rs.close();
            stmt.close();
            commitConn();
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_REQUEST_ERROR_MSG);
        }

    }

    public void warrantySparesReport() {
        Statement stmt;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "SELECT SPARES.ID AS ID, SPARES.NAME AS NAME, SPARES.ARTICLE AS ARTICLE, WAREHOUSE_STRUCTURE.PLACE AS PLACE" +
                    " FROM SPARES, WAREHOUSE_STRUCTURE, ORDERS_STRUCTURE, ORDERS" +
                    " WHERE WAREHOUSE_STRUCTURE.ACTIVE = TRUE AND WAREHOUSE_STRUCTURE.ORDER_ID = ORDERS_STRUCTURE.ID AND ORDERS_STRUCTURE.SPARE_ID = SPARES.ID" +
                    " AND ORDERS_STRUCTURE.ORDER_ID = ORDERS.ID AND ORDERS.WARRANTY = FALSE" +
                    " ORDER BY WAREHOUSE_STRUCTURE.PLACE";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(WARRANTY_SPARES_TITLE);
            System.out.println("ID\tNAME\tARTICLE\tPLACE");
            System.out.println("--\t----\t-------\t-----");
            while(rs.next())
            {
                System.out.println(rs.getInt("ID") + " " + rs.getString("NAME")+ " " + rs.getString("ARTICLE")+ " " + rs.getInt("PLACE"));
            }
            rs.close();
            stmt.close();
            commitConn();
        } catch (Exception e) {
            System.err.println("Exception while updating report");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            DBMessages.showErrorMessage(frame, DBMessages.DB_REQUEST_ERROR_MSG);
        }
    }

    public void sparesSearch() {
        if (sparesSearchDialog == null) {
            sparesSearchDialog = new SparesSearchDialog(this, frame, SPARES_SEARCH_TITLE);
        }
        sparesSearchDialog.showDialog();
    }
}
