package com.omniview.utils;

import java.util.HashMap;
import java.util.Map;

public class OidDictionary {
    private static final Map<String, String> oidMap = new HashMap<>();

    static {
        oidMap.put("1", "iso");
        oidMap.put("1.3", "org");
        oidMap.put("1.3.6", "dod");
        oidMap.put("1.3.6.1", "internet");
        oidMap.put("1.3.6.1.2", "mgmt");
        oidMap.put("1.3.6.1.2.1", "mib-2");

        oidMap.put("1.3.6.1.2.1.1", "system");
        oidMap.put("1.3.6.1.2.1.2", "interfaces");
        oidMap.put("1.3.6.1.2.1.2.2", "ifTable");
        oidMap.put("1.3.6.1.2.1.4", "ip");
        oidMap.put("1.3.6.1.2.1.5", "icmp");
        oidMap.put("1.3.6.1.2.1.6", "tcp");
        oidMap.put("1.3.6.1.2.1.7", "udp");
        oidMap.put("1.3.6.1.2.1.11", "snmp");

        oidMap.put("1.3.6.1.2.1.25", "host");
        oidMap.put("1.3.6.1.2.1.25.1", "hrSystem");
        oidMap.put("1.3.6.1.2.1.25.2", "hrStorage");
        oidMap.put("1.3.6.1.2.1.25.3", "hrDevice");
        oidMap.put("1.3.6.1.2.1.25.4", "hrSWRun");
        oidMap.put("1.3.6.1.2.1.25.4.2", "hrSWRunTable");
        oidMap.put("1.3.6.1.2.1.25.4.2.1", "hrSWRunEntry");
        oidMap.put("1.3.6.1.2.1.25.5", "hrSWRunPerf");
        oidMap.put("1.3.6.1.2.1.25.6", "hrSWInstalled");
    }
    public static String lookup(String fullPath, String localNumber) {
        if (oidMap.containsKey(fullPath)) {
            return oidMap.get(fullPath) + " (" + localNumber + ")";
        }
        return localNumber;
    }
}