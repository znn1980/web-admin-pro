package com.admin.web.model.os;

/**
 * @author znn
 */
public class OsJvmMem extends Os {
    private final Runtime mem;

    public OsJvmMem() {
        this.mem = Runtime.getRuntime();
    }

    public long getMaxMemory() {
        return this.mem.maxMemory();
    }

    public long getTotalMemory() {
        return this.mem.totalMemory();
    }

    public long getFreeMemory() {
        return this.mem.freeMemory();
    }

    public long getUsedMemory() {
        return this.getTotalMemory() - this.getFreeMemory();
    }

    public long getUsableMemory() {
        return this.getMaxMemory() - this.getTotalMemory() + this.getFreeMemory();
    }

    @Override
    public String toString() {
        return "Jvm内存容量：" + asBytes(this.getTotalMemory())
                + ",Jvm内存可用容量：" + asBytes(this.getFreeMemory())
                + ",Jvm内存已用容量：" + asBytes(this.getUsedMemory())
                + ",Jvm内存最大容量：" + asBytes(this.getMaxMemory())
                + ",Jvm内存最大可用容量：" + asBytes(this.getUsableMemory());
    }
}
