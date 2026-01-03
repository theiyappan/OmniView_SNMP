package com.omniview.model;

public class SnmpResult {
    private final String oid;
    private final String value;
    private final String type;
    public SnmpResult(String oid, String value, String type) {
        this.oid = oid;
        this.value = value;
        this.type = type;
    }
    public String getOid() { return oid; }
    public String getValue() { return value; }
    public String getType() { return type; }
    @Override
    public String toString() {
        return oid + " = " + value + " [" + type + "]";
    }
}