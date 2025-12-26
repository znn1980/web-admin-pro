package com.admin.web.model.os;

import java.lang.management.ManagementFactory;


/**
 * @author znn
 */
public class OsSys extends Os {
    private final java.lang.management.OperatingSystemMXBean os;
    private final com.sun.management.OperatingSystemMXBean cpu;

    public OsSys() {
        this.os = ManagementFactory.getOperatingSystemMXBean();
        this.cpu = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public String getName() {
        return this.os.getName();
    }

    public String getArch() {
        return this.os.getArch();
    }

    public String getVersion() {
        return this.os.getVersion();
    }

    public int getAvailableProcessors() {
        return this.os.getAvailableProcessors();
    }

    public double getSystemLoadAverage() {
        return this.os.getSystemLoadAverage() >= 0 ? this.os.getSystemLoadAverage() : 0;
    }

    public double getCpuLoad() {
        return this.cpu.getCpuLoad() >= 0 ? this.cpu.getCpuLoad() : 0;
    }

    @Override
    public String toString() {
        return "系统名称：" + this.getName()
                + ",系统版本：" + this.getVersion()
                + ",系统架构：" + this.getArch()
                + ",系统负载：" + String.format("%.2f%%", this.getSystemLoadAverage() * 100)
                + ",处理器数量：" + this.getAvailableProcessors()
                + ",CPU使用率：" + String.format("%.2f%%", this.getCpuLoad() * 100);
    }
}
