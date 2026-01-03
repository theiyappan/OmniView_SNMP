package com.omniview.ui;

// NEW IMPORT: SNMP4J Constants
import org.snmp4j.mp.SnmpConstants;
import com.omniview.model.SnmpResult;
import com.omniview.service.ScanWorker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private ScanWorker currentWorker;
    private final TreeHelper treeHelper;

    private final JTextField ipField;
    private final JComboBox<String> verCombo;
    private final JButton btnV3;
    private final JTextField oidField;
    private final JComboBox<String> opCombo;
    private final JTextField valueField;
    private final JTextArea logArea;
    private final JButton btnGo;
    private final JButton btnStop;

    private String v3User = "initial";
    private String v3AuthPass = "";
    private String v3PrivPass = "";

    public MainFrame() {
        setTitle("OmniView - SNMP4J Manager");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        ipField = new JTextField("127.0.0.1:1024", 12);

        String[] versions = {"v2c", "v1", "v3"};
        verCombo = new JComboBox<>(versions);

        btnV3 = new JButton("v3 Auth");
        btnV3.setEnabled(false);

        oidField = new JTextField("1.3.6.1", 15);
        valueField = new JTextField(10);
        valueField.setEnabled(false);

        String[] ops = {"WALK", "GET", "GET_NEXT", "GET_BULK", "SET"};
        opCombo = new JComboBox<>(ops);
        opCombo.setSelectedItem("WALK");

        btnGo = new JButton("Go");
        btnStop = new JButton("Stop");
        btnStop.setEnabled(false);

        topPanel.add(new JLabel("IP:"));
        topPanel.add(ipField);
        topPanel.add(new JLabel("Ver:"));
        topPanel.add(verCombo);
        topPanel.add(btnV3);
        topPanel.add(new JLabel("OID:"));
        topPanel.add(oidField);
        topPanel.add(new JLabel("Op:"));
        topPanel.add(opCombo);
        topPanel.add(new JLabel("Val:"));
        topPanel.add(valueField);
        topPanel.add(btnGo);
        topPanel.add(btnStop);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTree tree = new JTree();
        tree.setCellRenderer(new com.omniview.ui.SnmpTreeCellRenderer());
        this.treeHelper = new TreeHelper(tree);

        logArea = new JTextArea("Ready (SNMP4J Loaded).\n");
        logArea.setEditable(false);

        splitPane.setLeftComponent(new JScrollPane(tree));
        splitPane.setRightComponent(new JScrollPane(logArea));
        splitPane.setDividerLocation(400);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        verCombo.addActionListener(e -> {
            boolean isV3 = "v3".equals(verCombo.getSelectedItem());
            btnV3.setEnabled(isV3);
        });

        opCombo.addActionListener(e -> {
            String selected = (String) opCombo.getSelectedItem();
            valueField.setEnabled("SET".equals(selected));
        });
        btnV3.addActionListener(e -> showV3Dialog());

        btnGo.addActionListener(e -> {
            String ip = ipField.getText().trim();
            String oid = oidField.getText().trim();
            String val = valueField.getText().trim();
            String opStr = (String) opCombo.getSelectedItem();
            ScanWorker.Operation op = ScanWorker.Operation.valueOf(opStr);
            String community = "public";

            String verStr = (String) verCombo.getSelectedItem();
            int snmpVersion;

            if ("v1".equals(verStr)) snmpVersion = SnmpConstants.version1;
            else if ("v3".equals(verStr)) snmpVersion = SnmpConstants.version3;
            else snmpVersion = SnmpConstants.version2c;

            if (op == ScanWorker.Operation.SET && val.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a value to SET.");
                return;
            }

            if (op == ScanWorker.Operation.WALK) {
                logArea.setText("");
                treeHelper.clear();
            }

            btnGo.setEnabled(false);
            btnStop.setEnabled(true);
            currentWorker = new ScanWorker(ip, snmpVersion, oid, community, val, op, logArea, v3User, v3AuthPass, v3PrivPass) {
                @Override
                protected void process(List<SnmpResult> chunks) {
                    SnmpResult lastResult = null;
                    for (SnmpResult res : chunks) {
                        lastResult = res;
                        logArea.append(res.toString() + "\n");
                        try {
                            treeHelper.addResult(res);
                        } catch (Exception ex) { /* Ignore */ }
                    }
                    if (lastResult != null) {
                        if (op == ScanWorker.Operation.GET_NEXT || op == ScanWorker.Operation.GET_BULK) {
                            oidField.setText(lastResult.getOid());
                        }
                    }
                }
                @Override
                protected void done() {
                    btnGo.setEnabled(true);
                    btnStop.setEnabled(false);
                    oidField.repaint();
                }
            };

            currentWorker.execute();
        });

        btnStop.addActionListener(e -> {
            if (currentWorker != null) {
                currentWorker.cancel(true);
            }
        });
    }

    private void showV3Dialog() {
        JTextField userField = new JTextField(v3User);
        JPasswordField authField = new JPasswordField(v3AuthPass);
        JPasswordField privField = new JPasswordField(v3PrivPass);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username (Principal):"));
        panel.add(userField);
        panel.add(new JLabel("Auth Password (Optional):"));
        panel.add(authField);
        panel.add(new JLabel("Priv Password (Optional):"));
        panel.add(privField);
        int result = JOptionPane.showConfirmDialog(this, panel, "SNMP v3 Configuration",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            this.v3User = userField.getText();
            this.v3AuthPass = new String(authField.getPassword());
            this.v3PrivPass = new String(privField.getPassword());
            JOptionPane.showMessageDialog(this, "v3 Credentials Saved!");
        }
    }
}