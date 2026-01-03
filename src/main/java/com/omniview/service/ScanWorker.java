package com.omniview.service;

import com.omniview.model.SnmpResult;
import com.omniview.utils.SmartFormatter;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ScanWorker extends SwingWorker<Void, SnmpResult> {

    private static final String ENGINE_ID_HEX = "0x80001f888052710000c6bd536900000000";

    public enum Operation { WALK, GET, GET_NEXT, GET_BULK, SET }

    private final String targetIp;
    private final int snmpVersion;
    private String startOid;
    private final String community;
    private final String writeValue;
    private final Operation operation;
    private final JTextArea logArea;

    private final String v3User;
    private final String v3AuthPass;
    private final String v3PrivPass;

    private int targetPort = 161;
    private static final int MAX_WALK_ROWS = 5000;

    public ScanWorker(String ipInput, int snmpVersion, String startOid, String community, String writeValue,
                      Operation operation, JTextArea logArea,
                      String v3User, String v3AuthPass, String v3PrivPass) {

        this.snmpVersion = snmpVersion;
        this.community = community;
        this.writeValue = writeValue;
        this.operation = operation;
        this.logArea = logArea;
        this.v3User = v3User;
        this.v3AuthPass = v3AuthPass;
        this.v3PrivPass = v3PrivPass;

        if (ipInput.contains(":")) {
            String[] parts = ipInput.split(":");
            this.targetIp = parts[0];
            try {
                this.targetPort = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port, defaulting to 161");
            }
        } else {
            this.targetIp = ipInput;
        }

        if (startOid != null) {
            String clean = startOid.split("=")[0].split(" ")[0].replaceAll("[^0-9.]", "");
            if (!clean.startsWith(".")) clean = "." + clean;
            if (clean.startsWith(".2.1.")) clean = ".1.3.6.1" + clean;
            this.startOid = clean;
        } else {
            this.startOid = "";
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        String vStr = (snmpVersion == SnmpConstants.version3) ? "v3" : (snmpVersion == SnmpConstants.version1) ? "v1" : "v2c";
        publishLog(">>> Initializing (" + operation + ") on " + targetIp + ":" + targetPort + " [" + vStr + "]");

        Snmp snmp = null;
        try {
            SecurityProtocols.getInstance().addDefaultProtocols();
            SecurityProtocols.getInstance().addAuthenticationProtocol(new AuthMD5());
            SecurityProtocols.getInstance().addPrivacyProtocol(new PrivDES());
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);

            if (snmpVersion == SnmpConstants.version3) {
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);

                setupV3User(snmp);
            }

            transport.listen();

            Target<?> target = createTarget();

            switch (operation) {
                case GET:
                    sendRequest(snmp, target, PDU.GET, startOid);
                    break;
                case GET_NEXT:
                    sendRequest(snmp, target, PDU.GETNEXT, startOid);
                    break;
                case SET:
                    performSet(snmp, target);
                    break;
                case GET_BULK:
                    performBulk(snmp, target);
                    break;
                case WALK:
                    performWalk(snmp, target);
                    break;
            }

        } catch (Exception e) {
            publishLog(">>> CRITICAL ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (snmp != null) {
                try { snmp.close(); } catch (IOException ignored) { }
            }
        }
        return null;
    }

    private void setupV3User(Snmp snmp) {
        OID authProtocol = null;
        OID privProtocol = null;
        OctetString authPass = null;
        OctetString privPass = null;

        if (v3AuthPass != null && !v3AuthPass.isEmpty()) {
            authProtocol = AuthMD5.ID;
            authPass = new OctetString(v3AuthPass);

            if (v3PrivPass != null && !v3PrivPass.isEmpty()) {
                privProtocol = PrivDES.ID;
                privPass = new OctetString(v3PrivPass);
                publishLog(">>> v3 Config: AuthPriv (MD5/DES)");
            } else {
                publishLog(">>> v3 Config: AuthNoPriv (MD5)");
            }
        } else {
            publishLog(">>> v3 Config: NoAuthNoPriv");
        }

        // Add user to the USM
        UsmUser user = new UsmUser(
                new OctetString(v3User),
                authProtocol, authPass,
                privProtocol, privPass
        );
        snmp.getUSM().addUser(new OctetString(v3User), user);
    }

    private Target<?> createTarget() {
        Address targetAddress = new UdpAddress(targetIp + "/" + targetPort);

        if (snmpVersion == SnmpConstants.version3) {
            UserTarget target = new UserTarget();
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(5000);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityName(new OctetString(v3User));

            // Set Security Level
            if (v3PrivPass != null && !v3PrivPass.isEmpty()) {
                target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            } else if (v3AuthPass != null && !v3AuthPass.isEmpty()) {
                target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
            } else {
                target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
            }
            if (ENGINE_ID_HEX != null && !ENGINE_ID_HEX.isEmpty()) {
                byte[] engineIdBytes = hexStringToByteArray(ENGINE_ID_HEX);
                target.setAuthoritativeEngineID(engineIdBytes);
            }

            return target;
        } else {
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(5000);
            target.setVersion(snmpVersion);
            return target;
        }
    }

    private void sendRequest(Snmp snmp, Target<?> target, int pduType, String oid) throws IOException {
        PDU pdu = (snmpVersion == SnmpConstants.version3) ? new ScopedPDU() : new PDU();
        pdu.setType(pduType);
        pdu.add(new VariableBinding(new OID(oid)));

        ResponseEvent response = snmp.send(pdu, target);
        processResponse(response);
    }

    private void performSet(Snmp snmp, Target<?> target) throws IOException {
        if (writeValue == null || writeValue.isEmpty()) return;

        PDU pdu = (snmpVersion == SnmpConstants.version3) ? new ScopedPDU() : new PDU();
        pdu.setType(PDU.SET);

        Variable var;
        try {
            var = new Integer32(Integer.parseInt(writeValue));
        } catch (NumberFormatException e) {
            var = new OctetString(writeValue);
        }

        pdu.add(new VariableBinding(new OID(startOid), var));

        ResponseEvent response = snmp.send(pdu, target);
        processResponse(response);
    }

    private void performBulk(Snmp snmp, Target<?> target) throws IOException {
        PDU pdu = (snmpVersion == SnmpConstants.version3) ? new ScopedPDU() : new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(20);
        pdu.setNonRepeaters(0);
        pdu.add(new VariableBinding(new OID(startOid)));

        ResponseEvent response = snmp.send(pdu, target);
        processResponse(response);
    }

    private void performWalk(Snmp snmp, Target<?> target) throws IOException {
        publishLog(">>> Walking tree...");
        OID currentOid = new OID(startOid);

        String boundary = startOid.startsWith(".") ? startOid.substring(1) : startOid;

        boolean finished = false;
        int count = 0;

        while (!finished && !isCancelled() && count < MAX_WALK_ROWS) {
            PDU pdu = (snmpVersion == SnmpConstants.version3) ? new ScopedPDU() : new PDU();
            pdu.setType(PDU.GETNEXT);
            pdu.add(new VariableBinding(currentOid));

            ResponseEvent response = snmp.send(pdu, target);
            if (response == null || response.getResponse() == null) {
                break;
            }

            PDU respPDU = response.getResponse();
            if (respPDU.getErrorStatus() != PDU.noError) {
                publishLog("Error: " + respPDU.getErrorStatusText());
                break;
            }

            List<? extends VariableBinding> vbs = respPDU.getVariableBindings();
            if (vbs.isEmpty()) break;

            VariableBinding vb = vbs.get(0);
            OID nextOid = vb.getOid();
            Variable val = vb.getVariable();

            if (!nextOid.toString().startsWith(boundary) && !nextOid.toString().startsWith("." + boundary)) {
                publishLog(">>> Finished requested subtree.");
                finished = true;
            } else {
                publishResult(nextOid.toString(), val);
                currentOid = nextOid;
                count++;
            }
        }
    }

    private void processResponse(ResponseEvent event) {
        if (event != null && event.getResponse() != null) {
            PDU response = event.getResponse();
            if (response.getErrorStatus() == PDU.noError) {
                for (VariableBinding vb : response.getVariableBindings()) {
                    publishResult(vb.getOid().toString(), vb.getVariable());
                }
            } else {
                publishLog(">>> Error: " + response.getErrorStatusText());
            }
        } else {
            publishLog(">>> Request Timed Out.");
        }
    }

    private void publishResult(String oid, Variable val) {
        if (oid.startsWith(".")) oid = oid.substring(1);

        String type = val.getClass().getSimpleName();
        String valueStr = val.toString();

        if (val instanceof OctetString) {
            valueStr = SmartFormatter.format("OctetString", valueStr);
        }

        publish(new SnmpResult(oid, valueStr, type));
    }

    private byte[] hexStringToByteArray(String s) {
        if (s.startsWith("0x")) s = s.substring(2);
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Override
    protected void process(List<SnmpResult> chunks) {}

    @Override
    protected void done() { publishLog(">>> Operation Complete."); }

    private void publishLog(String msg) { SwingUtilities.invokeLater(() -> logArea.append(msg + "\n")); }
}