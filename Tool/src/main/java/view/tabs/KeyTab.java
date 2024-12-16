package view.tabs;

import api.API;
import api.ConnectException;
import model.Order;
import model.PublicKeyItem;
import view.BaseUI;
import view.MainApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KeyTab extends JPanel implements BaseUI {
    MainApp mainApp;
    final String[] columnNames = {"ID", "Khóa công khai", "Ngày tạo", "Trạng thái", "Số đơn hàng xác thực"};
    Object[][] data = {};
    DefaultTableModel model;
    JTable table;
    JButton btnGenKey,btnReportKey;

    public KeyTab(MainApp mainApp) {
        this.mainApp = mainApp;
        this.init();
        this.setOnClick();
    }
    @Override
    public void init() {
        this.setLayout(new BorderLayout());
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(1,1));

        try {
            refresh();
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(mainApp,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        setSizeCol();
        btnGenKey = new JButton("Tạo khóa");
        btnReportKey = new JButton("Báo cáo khóa");
        btnGenKey.setForeground(Color.WHITE);
        btnReportKey.setForeground(Color.WHITE);
        btnGenKey.setBackground(new Color(13,110,253));
        btnReportKey.setBackground(new Color(250,53,69));

        JPanel panelSouth = new JPanel();
        panelSouth.setPreferredSize(new Dimension(0,50));
        panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));
        panelSouth.add(btnGenKey);
        panelSouth.add(Box.createHorizontalGlue());
        panelSouth.add(btnReportKey);

        this.add(scrollPane,BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);

    }
    public void refresh() throws ConnectException{
        List<PublicKeyItem> orders = API.getPublicKeys(mainApp.getUsername());
        if (orders == null) return;
        model.setRowCount(0); // Xóa tất cả hàng
        for (PublicKeyItem publicKeyItem : orders) {
            model.addRow(new Object[]{publicKeyItem.getId(), publicKeyItem.getPublicKey(), publicKeyItem.getCreatedAt(), publicKeyItem.getStatus(), publicKeyItem.getCountOrderSignature()}); // Thêm hàng mới
        }
    }

    @Override
    public void setOnClick() {

    }
    private void setSizeCol(){
        //"ID", "khóa công khai", "Ngày tạo", "trạng thía", "số đơn"
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
    }
}
