package com.admin.web.model.os;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * @author znn
 */
public class OsSys extends Os {
    private final OperatingSystemMXBean os;

    public OsSys() {
        os = ManagementFactory.getOperatingSystemMXBean();
    }

    public String getName() {
        return os.getName();
    }

    public String getArch() {
        return os.getArch();
    }

    public String getVersion() {
        return os.getVersion();
    }

    public int getAvailableProcessors() {
        return os.getAvailableProcessors();
    }

    public double getSystemLoadAverage() {
        return os.getSystemLoadAverage() >= 0 ? os.getSystemLoadAverage() : 0;
    }

    @Override
    public String toString() {
        return "系统名称：" + this.getName()
                + ",系统版本：" + this.getVersion()
                + ",系统架构：" + this.getArch()
                + ",系统负载：" + this.getSystemLoadAverage()
                + ",处理器数量：" + this.getAvailableProcessors();
    }
}
