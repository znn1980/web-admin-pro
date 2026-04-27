package com.admin.web.model;

import com.admin.web.model.os.*;
import com.admin.web.utils.NetUtils;

/**
 * @author znn
 */
public class SysMonitor {
    private final OsSys osSys;
    private final OsSysMem osSysMem;
    private final OsJvm osJvm;
    private final OsJvmMem osJvmMem;
    private final OsDisk osDisk;

    public SysMonitor() {
        this.osSys = new OsSys();
        this.osSysMem = new OsSysMem();
        this.osJvm = new OsJvm();
        this.osJvmMem = new OsJvmMem();
        this.osDisk = new OsDisk();
    }

    public String getOsIp() {
        return NetUtils.getLocalIp();
    }

    public String getOsMac() {
        return NetUtils.getLocalMac();
    }

    public String getOsName() {
        return NetUtils.getLocalName();
    }

    public OsSys getOsSys() {
        return this.osSys;
    }

    public OsSysMem getOsSysMem() {
        return this.osSysMem;
    }

    public OsJvm getOsJvm() {
        return this.osJvm;
    }

    public OsJvmMem getOsJvmMem() {
        return this.osJvmMem;
    }

    public OsDisk getOsDisk() {
        return this.osDisk;
    }

    @Override
    public String toString() {
        return "SysMonitor{" +
                "osIp=" + this.getOsIp() +
                ", osMac=" + this.getOsMac() +
                ", osName=" + this.getOsName() +
                ", osSys=" + this.getOsSys() +
                ", osSysMem=" + this.getOsSysMem() +
                ", osJvm=" + this.getOsJvm() +
                ", osJvmMem=" + this.getOsJvmMem() +
                ", osDisk=" + this.getOsDisk() +
                '}';
    }
}
