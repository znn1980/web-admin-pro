package com.admin.web.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringJoiner;

/**
 * @author znn
 */
public class NetUtils {

    public static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalIp() {
        return getLocalHost().getHostAddress();
    }

    public static String getLocalName() {
        return getLocalHost().getHostName();
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
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
