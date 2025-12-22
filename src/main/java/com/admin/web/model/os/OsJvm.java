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

    public String getVmName() {
        return vm.getVmName();
    }

    public String getVmVersion() {
        return vm.getVmVersion();
    }

    public String getVmVendor() {
        return vm.getVmVendor();
    }

    public String getSpecName() {
        return vm.getSpecName();
    }

    public String getSpecVersion() {
        return vm.getSpecVersion();
    }

    public String getSpecVendor() {
        return vm.getSpecVendor();
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
        return "Java虚拟机名称：" + this.getVmName()
                + ",Java虚拟机版本：" + this.getVmVersion()
                + ",Java虚拟机厂商：" + this.getVmVendor()
                + ",Java虚拟机规范名称：" + this.getSpecName()
                + ",Java虚拟机规范版本：" + this.getSpecVersion()
                + ",Java虚拟机规范厂商：" + this.getSpecVendor()
                + ",Java虚拟机启动时间：" + this.getStartTime()
                + ",Java虚拟机运行时间：" + this.getUptime()
                + ",Java虚拟机运行参数：" + this.getInputArguments();
    }
}
