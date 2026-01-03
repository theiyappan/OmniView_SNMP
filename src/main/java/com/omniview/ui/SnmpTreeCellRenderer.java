package com.omniview.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class SnmpTreeCellRenderer extends DefaultTreeCellRenderer {

    private final Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");
    private final Icon fileIcon = UIManager.getIcon("FileView.fileIcon");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String text = (String) node.getUserObject();

        if (leaf) {
            setIcon(fileIcon);
            setForeground(new Color(0, 0, 150));
        } else {
            setIcon(folderIcon);
            setForeground(Color.BLACK);
        }
        if (sel) {
            setForeground(Color.WHITE);
            setBackgroundSelectionColor(new Color(0, 120, 215));
        } else {
            setBackgroundNonSelectionColor(null);
        }
        if (text != null && (text.startsWith("host") || text.startsWith("25"))) {
            setForeground(new Color(200, 100, 0));
            setText("<html><b>" + text + "</b></html>");
        }
        return this;
    }
}