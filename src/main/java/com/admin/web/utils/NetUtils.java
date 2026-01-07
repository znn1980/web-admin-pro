package com.admin.web.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.StringJoiner;

/**
 * @author znn
 */
public class NetUtils {

    public static InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String getLocalIp() {
        try {
            return getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalName() {
        try {
            return getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalMac() {
        return getLocalMac("-");
    }

    public static String getLocalMac(String delimiter) {
        try {
            StringJoiner mac = new StringJoiner(delimiter == null ? "" : delimiter);
            for (byte b : NetworkInterface.getByInetAddress(getLocalHost()).getHardwareAddress()) {
                mac.add(Integer.toHexString(b & 0xFF));
            }
            return mac.toString().toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
