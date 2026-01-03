package com.omniview.ui;

import com.omniview.model.SnmpResult;
import com.omniview.utils.OidDictionary;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.Map;

public class TreeHelper {
    private final JTree tree;
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;
    private final Map<String, DefaultMutableTreeNode> nodeCache = new HashMap<>();

    public TreeHelper(JTree tree) {
        this.tree = tree;
        this.root = new DefaultMutableTreeNode("Root");
        this.model = new DefaultTreeModel(root);
        this.tree.setModel(model);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
    }
    public void addResult(SnmpResult result) {
        String oid = result.getOid();
        String val = result.getValue();
        String[] parts = oid.split("\\.");
        DefaultMutableTreeNode currentNode = root;
        StringBuilder currentPathBuilder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (currentPathBuilder.length() > 0) {
                currentPathBuilder.append(".");
            }
            currentPathBuilder.append(part);
            String currentPathOID = currentPathBuilder.toString();
            DefaultMutableTreeNode cachedNode = nodeCache.get(currentPathOID);

            if (cachedNode == null) {
                boolean isLeaf = (i == parts.length - 1);
                String label;

                if (isLeaf) {
                    String parentPath = currentPathOID.substring(0, currentPathOID.lastIndexOf('.'));
                    String lookupName = OidDictionary.lookup(parentPath, "");

                    label = part + " = " + val;
                } else {
                    label = OidDictionary.lookup(currentPathOID, part);
                }
                cachedNode = new DefaultMutableTreeNode(label);
                nodeCache.put(currentPathOID, cachedNode);
                try {
                    model.insertNodeInto(cachedNode, currentNode, currentNode.getChildCount());
                } catch (Exception e) {
                    System.err.println("Error adding node: " + currentPathOID);
                }
                if (i < 6) {
                    tree.scrollPathToVisible(new TreePath(cachedNode.getPath()));
                }
            }
            currentNode = cachedNode;
        }
    }

    public void clear() {
        root.removeAllChildren();
        nodeCache.clear();
        model.reload();
    }
}