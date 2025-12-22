package com.admin.web.model.os;

/**
 * @author znn
 */
public class OsMem extends Os {
    private final Runtime mem;

    public OsMem() {
        mem = Runtime.getRuntime();
    }

    public long getMaxMemory() {
        return mem.maxMemory();
    }

    public long getTotalMemory() {
        return mem.totalMemory();
    }

    public long getFreeMemory() {
        return mem.freeMemory();
    }

    public long getUsedMemory() {
        return this.getTotalMemory() - this.getFreeMemory();
    }

    public long getUsableMemory() {
        return this.getMaxMemory() - this.getTotalMemory() + this.getFreeMemory();
    }

    @Override
    public String toString() {
        return "Java虚拟机内存最大容量：" + asBytes(this.getMaxMemory())
                + ",Java虚拟机内存最大可用容量：" + asBytes(this.getUsableMemory())
                + ",Java虚拟机内存容量：" + asBytes(this.getTotalMemory())
                + ",Java虚拟机内存可用容量：" + asBytes(this.getFreeMemory())
                + ",Java虚拟机内存已用容量：" + asBytes(this.getUsedMemory());
    }
}
