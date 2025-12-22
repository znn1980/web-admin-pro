package com.admin.web.model.os;

import java.io.Serializable;

/**
 * @author znn
 */
public class Os implements Serializable {

    public String getTips() {
        return this.toString();
    }

    public static String asBytes(long bytes) {
        long k = 1024L, m = k * k, g = m * k, t = g * k;
        if (bytes >= t) {
            return String.format("%.2fTB", (double) bytes / t);
        } else if (bytes >= g) {
            return String.format("%.2fGB", (double) bytes / g);
        } else if (bytes >= m) {
            return String.format("%.2fMB", (double) bytes / m);
        } else if (bytes >= k) {
            return String.format("%.2fKB", (double) bytes / k);
        } else {
            return String.format("%dB", bytes);
        }
    }
}
