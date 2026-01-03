package com.omniview.utils;

public class SmartFormatter {
    public static String format(String type, String rawValue) {
        if (type == null || !type.equalsIgnoreCase("OctetString")) {
            return rawValue;
        }

        if (rawValue == null || rawValue.isEmpty()) return "";
        if (rawValue.contains(":")) {
            try {
                String[] parts = rawValue.split(":");
                byte[] bytes = new byte[parts.length];
                int printableCount = 0;

                for (int i = 0; i < parts.length; i++) {
                    int val = Integer.parseInt(parts[i], 16);
                    bytes[i] = (byte) val;
                    if ((val >= 32 && val <= 126) || val == 9 || val == 10 || val == 13) {
                        printableCount++;
                    } else if (val == 0 && i == parts.length - 1) {
                        printableCount++;
                    }
                }

                if (bytes.length > 0 && ((double) printableCount / bytes.length) > 0.85) {
                    String result = new String(bytes);
                    if (result.endsWith("\0")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    return result;
                }
            } catch (Exception e) {
                return rawValue;
            }
        } else if (rawValue.contains(" ")) {
            try {
                String[] parts = rawValue.split(" ");
                byte[] bytes = new byte[parts.length];
                int printableCount = 0;
                for (int i = 0; i < parts.length; i++) {
                    int val = Integer.parseInt(parts[i], 16);
                    bytes[i] = (byte) val;
                    if ((val >= 32 && val <= 126) || val == 9 || val == 10 || val == 13) {
                        printableCount++;
                    }
                }
                if (bytes.length > 0 && ((double) printableCount / bytes.length) > 0.85) {
                    return new String(bytes).trim();
                }
            } catch (Exception e) {
                return rawValue;
            }
        }
        return rawValue;
    }
}