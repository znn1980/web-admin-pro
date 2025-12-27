package com.admin.web.model.os;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * @author znn
 */
public class OsJvm extends Os {
    private final RuntimeMXBean vm;

    public OsJvm() {
        this.vm = ManagementFactory.getRuntimeMXBean();
    }

    public String getName() {
        return this.vm.getVmName();
    }

    public String getVersion() {
        return this.vm.getVmVersion();
    }

    public String getVendor() {
        return this.vm.getVmVendor();
    }

    public List<String> getInputArguments() {
        return this.vm.getInputArguments();
    }

    public long getStartTime() {
        return this.vm.getStartTime();
    }

    public long getUptime() {
        return this.vm.getUptime();
    }

    @Override
    public String toString() {
        return "Jvm名称：" + this.getName()
                + ",Jvm版本：" + this.getVersion()
                + ",Jvm厂商：" + this.getVendor()
                + ",Jvm启动时间：" + this.getStartTime()
                + ",Jvm运行时间：" + this.getUptime()
                + ",Jvm运行参数：" + this.getInputArguments();
    }
}
