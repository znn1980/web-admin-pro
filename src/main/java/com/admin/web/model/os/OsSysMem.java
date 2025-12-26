package com.admin.web.model.os;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * @author znn
 */
public class OsSysMem extends Os {
    private final OperatingSystemMXBean os;

    public OsSysMem() {
        this.os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public long getTotalMemorySize() {
        return this.os.getTotalMemorySize();
    }

    public long getFreeMemorySize() {
        return this.os.getFreeMemorySize();
    }

    public long getUsedMemorySize() {
        return this.getTotalMemorySize() - this.getFreeMemorySize();
    }

    public long getCommittedVirtualMemorySize() {
        return this.os.getCommittedVirtualMemorySize();
    }

    @Override
    public String toString() {
        return "内存容量：" + asBytes(this.getTotalMemorySize())
                + ",内存可用容量：" + asBytes(this.getFreeMemorySize())
                + ",内存已用容量：" + asBytes(this.getUsedMemorySize())
                + ",虚拟内存容量：" + asBytes(this.getCommittedVirtualMemorySize());
    }

}
