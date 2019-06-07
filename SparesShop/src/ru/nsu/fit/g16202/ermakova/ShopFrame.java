package ru.nsu.fit.g16202.ermakova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
Класс главного окна
 */
public class ShopFrame extends JFrame implements ActionListener {
    private static final Dimension MAIN_FRAME_DEFAULT_SIZE = new Dimension(800, 600);
    private static final Dimension MAIN_FRAME_MINIMAL_SIZE = new Dimension(800, 600);
    private static final String MAIN_FRAME_HEAD_TEXT = "Магазин автозапчастей";

    private static final String MENU_DATADASE_NAME = "База данных";
    private static final String MENU_CONNECT_NAME = "Подключиться";
    private static final String MENU_DISCONNECT_NAME = "Отключиться";
    private static final String MENU_CREATE_EMPTY_NAME = "Создать пустую базу";
    private static final String MENU_CREATE_FILLED_NAME = "Создать заполненную базу";
    private static final String MENU_EXIT_NAME = "Выход";

    private static final String MENU_REFERENCE_NAME = "Справочники";
    private static final String MENU_MANAGERS_NAME = "Менеджеры";
    private static final String MENU_SPARES_NAME = "Запасные части";
    private static final String MENU_CUSTOMERS_NAME = "Покупатели";
    private static final String MENU_PRIVATE_CUSTOMERS_NAME = "Покупатели физические лица";
    private static final String MENU_LEGAL_CUSTOMERS_NAME = "Покупатели юридические лица";

    private static final String MENU_REPORTS_NAME = "Отчёты";
    private static final String MENU_SUPPLIERS_BY_SPARE_NAME = "Список поставщиков по выбранной детали";
    private static final String MENU_SPARES_INFO_NAME = "Сведения по деталям";
    private static final String MENU_BUYERS_SPARES_INFO_NAME = "Перечень покупателей";
    private static final String MENU_WAREHOUSE_SPARES_INFO_NAME = "Перечень деталей на складе";
    private static final String MENU_TOP_INFO_NAME = "Топ поставщиков и деталей";
    private static final String MENU_AVERAGE_SELLS_NAME = "Среднее число продаж за месяц";
    private static final String MENU_INVENTORY_STATEMENT_NAME = "Кассовый отчёт";
    private static final String MENU_NUMBER_OF_CELLS = "Количество свободных ячеек на складе";
    private static final String MENU_MANAGERS_ORDERS = "Менеджеры для товаров, побывавших на складе";
    private static final String MENU_WARRANTY_SPARES = "Товары с гарантией на складе";

    private static final String MENU_ANALYSIS_NAME = "Анализ";
    private static final String MENU_SPARES_SEARCH_NAME = "Поиск деталей";

    private ShopModel model;
    private JMenuBar menuBar;
    private JMenu jmDatabase;
    private JMenuItem jmiConnect;
    private JMenuItem jmiDisconnect;
    private JMenuItem jmiCreateEmpty;
    private JMenuItem jmiCreateFilled;
    private JMenuItem jmiExit;
    private JMenu jmReference;
    private JMenuItem jmiManagers;
    private JMenuItem jmiSpares;
    private JMenuItem jmiCustomers;
    private JMenuItem jmiPrivateCustomers;
    private JMenuItem jmiLegalCustomers;
    private JMenu jmReports;
    private JMenuItem jmiSuppliersBySpare;
    private JMenuItem jmiSparesInfo;
    private JMenuItem jmiBuyersSparesInfo;
    private JMenuItem jmiWarehouseSparesInfo;
    private JMenuItem jmiTopInfo;
    private JMenuItem jmiAverageMonthSells;
    private JMenuItem jmiInventoryStatement;
    private JMenuItem jmiNumberOfCells;
    private JMenuItem jmiManagersOrders;
    private JMenuItem jmiWarrantySpares;
    private JMenu jmAnalysis;
    private JMenuItem jmiSparesSearch;

    public ShopFrame(ShopModel model) {
        super();
        this.model = model;
        setSize(MAIN_FRAME_DEFAULT_SIZE);
        setMinimumSize(MAIN_FRAME_MINIMAL_SIZE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                exitApplication();
            }
        });
//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MAIN_FRAME_ICON)));
        setLocationRelativeTo(null);
        setResizable(true);
        setTitle(MAIN_FRAME_HEAD_TEXT);
    }

    public void initMenu() {
        menuBar = new JMenuBar();

        jmDatabase = new JMenu(MENU_DATADASE_NAME);
        jmDatabase.setFont(model.getDefaultFont());
        jmiConnect = new JMenuItem(MENU_CONNECT_NAME);
        jmiConnect.setFont(model.getDefaultFont());
        jmiDisconnect = new JMenuItem(MENU_DISCONNECT_NAME);
        jmiDisconnect.setFont(model.getDefaultFont());
        jmiCreateEmpty = new JMenuItem(MENU_CREATE_EMPTY_NAME);
        jmiCreateEmpty.setFont(model.getDefaultFont());
        jmiCreateFilled = new JMenuItem(MENU_CREATE_FILLED_NAME);
        jmiCreateFilled.setFont(model.getDefaultFont());
        jmiExit = new JMenuItem(MENU_EXIT_NAME);
        jmiExit.setFont(model.getDefaultFont());

        jmDatabase.add(jmiConnect);
        jmDatabase.add(jmiDisconnect);
        jmDatabase.add(jmiCreateEmpty);
        jmDatabase.add(jmiCreateFilled);
        jmDatabase.addSeparator();
        jmDatabase.add(jmiExit);

        jmReference = new JMenu(MENU_REFERENCE_NAME);
        jmReference.setFont(model.getDefaultFont());
        jmiManagers = new JMenuItem(MENU_MANAGERS_NAME);
        jmiManagers.setFont(model.getDefaultFont());
        jmiSpares = new JMenuItem(MENU_SPARES_NAME);
        jmiSpares.setFont(model.getDefaultFont());
        jmiCustomers = new JMenuItem(MENU_CUSTOMERS_NAME);
        jmiCustomers.setFont(model.getDefaultFont());
        jmiPrivateCustomers = new JMenuItem(MENU_PRIVATE_CUSTOMERS_NAME);
        jmiPrivateCustomers.setFont(model.getDefaultFont());
        jmiLegalCustomers = new JMenuItem(MENU_LEGAL_CUSTOMERS_NAME);
        jmiLegalCustomers.setFont(model.getDefaultFont());

        jmReference.add(jmiManagers);
        jmReference.add(jmiSpares);
        jmReference.add(jmiCustomers);
        jmReference.add(jmiPrivateCustomers);
        jmReference.add(jmiLegalCustomers);

        jmReports = new JMenu(MENU_REPORTS_NAME);
        jmReports.setFont(model.getDefaultFont());
        jmiSuppliersBySpare = new JMenuItem(MENU_SUPPLIERS_BY_SPARE_NAME);
        jmiSuppliersBySpare.setFont(model.getDefaultFont());
        jmiSparesInfo = new JMenuItem(MENU_SPARES_INFO_NAME);
        jmiSparesInfo.setFont(model.getDefaultFont());
        jmiBuyersSparesInfo = new JMenuItem(MENU_BUYERS_SPARES_INFO_NAME);
        jmiBuyersSparesInfo.setFont(model.getDefaultFont());
        jmiWarehouseSparesInfo = new JMenuItem(MENU_WAREHOUSE_SPARES_INFO_NAME);
        jmiWarehouseSparesInfo.setFont(model.getDefaultFont());
        jmiTopInfo = new JMenuItem(MENU_TOP_INFO_NAME);
        jmiTopInfo.setFont(model.getDefaultFont());
        jmiAverageMonthSells = new JMenuItem(MENU_AVERAGE_SELLS_NAME);
        jmiAverageMonthSells.setFont(model.getDefaultFont());
        jmiInventoryStatement = new JMenuItem(MENU_INVENTORY_STATEMENT_NAME);
        jmiInventoryStatement.setFont(model.getDefaultFont());
        jmiNumberOfCells = new JMenuItem(MENU_NUMBER_OF_CELLS);
        jmiNumberOfCells.setFont(model.getDefaultFont());
        jmiManagersOrders = new JMenuItem(MENU_MANAGERS_ORDERS);
        jmiManagersOrders.setFont(model.getDefaultFont());
        jmiWarrantySpares = new JMenuItem(MENU_WARRANTY_SPARES);
        jmiWarrantySpares.setFont(model.getDefaultFont());

        jmReports.add(jmiSuppliersBySpare);
        jmReports.add(jmiSparesInfo);
        jmReports.add(jmiBuyersSparesInfo);
        jmReports.add(jmiWarehouseSparesInfo);
        jmReports.add(jmiTopInfo);
        jmReports.add(jmiAverageMonthSells);
        jmReports.add(jmiInventoryStatement);
        jmReports.add(jmiNumberOfCells);
        jmReports.add(jmiManagersOrders);
        jmReports.add(jmiWarrantySpares);

        jmAnalysis = new JMenu(MENU_ANALYSIS_NAME);
        jmAnalysis.setFont(model.getDefaultFont());
        jmiSparesSearch = new JMenuItem(MENU_SPARES_SEARCH_NAME);
        jmiSparesSearch.setFont(model.getDefaultFont());

        jmAnalysis.add(jmiSparesSearch);

        jmiConnect.addActionListener(this);
        jmiDisconnect.addActionListener(this);
        jmiCreateEmpty.addActionListener(this);
        jmiCreateFilled.addActionListener(this);
        jmiExit.addActionListener(this);

        jmiManagers.addActionListener(this);
        jmiSpares.addActionListener(this);
        jmiCustomers.addActionListener(this);
        jmiPrivateCustomers.addActionListener(this);
        jmiLegalCustomers.addActionListener(this);

        jmiSuppliersBySpare.addActionListener(this);
        jmiSparesInfo.addActionListener(this);
        jmiBuyersSparesInfo.addActionListener(this);
        jmiWarehouseSparesInfo.addActionListener(this);
        jmiTopInfo.addActionListener(this);
        jmiAverageMonthSells.addActionListener(this);
        jmiInventoryStatement.addActionListener(this);
        jmiNumberOfCells.addActionListener(this);
        jmiManagersOrders.addActionListener(this);
        jmiWarrantySpares.addActionListener(this);

        jmiSparesSearch.addActionListener(this);

        menuBar.add(jmDatabase);
        menuBar.add(jmReference);
        menuBar.add(jmReports);
        menuBar.add(jmAnalysis);

        setJMenuBar(menuBar);
    }

    public void setConnected(boolean b) {
        jmiConnect.setEnabled(!b);
        jmiDisconnect.setEnabled(b);
        jmiCreateEmpty.setEnabled(!b);
        jmiCreateFilled.setEnabled(!b);
        jmiManagers.setEnabled(b);
        jmiSpares.setEnabled(b);
        jmiCustomers.setEnabled(b);
        jmiPrivateCustomers.setEnabled(b);
        jmiLegalCustomers.setEnabled(b);
        jmiSuppliersBySpare.setEnabled(b);
        jmiSparesInfo.setEnabled(b);
        jmiBuyersSparesInfo.setEnabled(b);
        jmiWarehouseSparesInfo.setEnabled(b);
        jmiTopInfo.setEnabled(b);
        jmiAverageMonthSells.setEnabled(b);
        jmiInventoryStatement.setEnabled(b);
        jmiNumberOfCells.setEnabled(b);
        jmiManagersOrders.setEnabled(b);
        jmiWarrantySpares.setEnabled(b);
        jmiSparesSearch.setEnabled(b);
    }

    private void exitApplication() {
        setVisible(false);
        model.exitApplication();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case MENU_CONNECT_NAME:
                model.connectDatabase();
                break;
            case MENU_DISCONNECT_NAME:
                model.disconnectDatabase();
                break;
            case MENU_CREATE_EMPTY_NAME:
                model.createEmptyDatabase();
                break;
            case MENU_CREATE_FILLED_NAME:
                model.createFilledDatabase();
                break;
            case MENU_MANAGERS_NAME:
                model.managersReference();
                break;
            case MENU_SPARES_NAME:
                model.sparesReference();
                break;
            case MENU_CUSTOMERS_NAME:
                model.customersReference();
                break;
            case MENU_PRIVATE_CUSTOMERS_NAME:
                model.privateCustomersReference();
                break;
            case MENU_LEGAL_CUSTOMERS_NAME:
                model.legalCustomersReference();
                break;
            case MENU_SUPPLIERS_BY_SPARE_NAME:
                model.suppliersBySpareReport();
                break;
            case MENU_SPARES_INFO_NAME:
                model.sparesInfoReport();
                break;
            case MENU_BUYERS_SPARES_INFO_NAME:
                model.buyersInfoReport();
                break;
            case MENU_WAREHOUSE_SPARES_INFO_NAME:
                model.warehouseInfoReport();
                break;
            case MENU_TOP_INFO_NAME:
                model.topInfoReport();
                break;
            case MENU_AVERAGE_SELLS_NAME:
                model.averageMonthSells();
                break;
            case MENU_INVENTORY_STATEMENT_NAME:
                model.inventoryStatementReport();
                break;
            case MENU_NUMBER_OF_CELLS:
                model.numberOfCellsReport();
                break;
            case MENU_MANAGERS_ORDERS:
                model.managersOrdersReport();
                break;
            case MENU_WARRANTY_SPARES:
                model.warrantySparesReport();
                break;
            case MENU_SPARES_SEARCH_NAME:
                model.sparesSearch();
                break;
            case MENU_EXIT_NAME:
                exitApplication();
                break;
        }
    }
}
