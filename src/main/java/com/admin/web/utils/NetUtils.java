package com.admin.web.utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.StringJoiner;

/**
 * @author znn
 */
public class NetUtils {


    /**
     * 获取所有满足过滤条件的本地IP地址对象
     *
     * @param networkInterfaceFilter 过滤器，null表示不过滤，获取所有网卡
     * @param inetAddressFilter      过滤器，null表示不过滤，获取所有地址
     * @return 过滤后的地址对象列表
     * @throws IOException \
     */
    public static LinkedHashSet<InetAddress> filter(Filter<NetworkInterface> networkInterfaceFilter, Filter<InetAddress> inetAddressFilter) throws IOException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        LinkedHashSet<InetAddress> localHosts = new LinkedHashSet<>();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterfaceFilter != null && !networkInterfaceFilter.accept(networkInterface)) {
                continue;
            }
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddressFilter == null || inetAddressFilter.accept(inetAddress)) {
                    localHosts.add(inetAddress);
                }
            }
        }
        return localHosts;
    }

    public static InetAddress getLocalhost() throws IOException {
        InetAddress localHost = null;
        for (InetAddress inetAddress : filter(null, inetAddress ->
                !inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)) {
            if (!inetAddress.isSiteLocalAddress()) {
                return inetAddress;
            } else if (localHost == null) {
                localHost = inetAddress;
            }
        }
        return localHost != null ? localHost : InetAddress.getLocalHost();
    }

    public static String getHostIp() {
        try {
            return getLocalhost().getHostAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostName() {
        try {
            return getLocalhost().getHostName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostMac(String delimiter) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(getLocalhost());
            StringJoiner mac = new StringJoiner(delimiter == null ? "" : delimiter);
            for (byte b : networkInterface.getHardwareAddress()) {
                mac.add(Integer.toHexString(b & 0xFF));
            }
            return mac.toString().toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Filter<T> {
        /**
         * 是否接受对象
         *
         * @param t 检查的对象
         * @return 是否接受对象
         */
        boolean accept(T t);
    }
}
