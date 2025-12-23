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
        vm = ManagementFactory.getRuntimeMXBean();
    }

    public String getName() {
        return vm.getVmName();
    }

    public String getVersion() {
        return vm.getVmVersion();
    }

    public String getVendor() {
        return vm.getVmVendor();
    }

    public List<String> getInputArguments() {
        return vm.getInputArguments();
    }

    public long getStartTime() {
        return vm.getStartTime();
    }

    public long getUptime() {
        return vm.getUptime();
    }

    @Override
    public String toString() {
        return "Java虚拟机名称：" + this.getName()
                + ",Java虚拟机版本：" + this.getVersion()
                + ",Java虚拟机厂商：" + this.getVendor()
                + ",Java虚拟机启动时间：" + this.getStartTime()
                + ",Java虚拟机运行时间：" + this.getUptime()
                + ",Java虚拟机运行参数：" + this.getInputArguments();
    }
}
