package com.admin.web.model;

import com.admin.web.model.os.OsDisk;
import com.admin.web.model.os.OsJvm;
import com.admin.web.model.os.OsMem;
import com.admin.web.model.os.OsSys;
import com.admin.web.utils.NetUtils;

import java.io.Serializable;

/**
 * @author znn
 */
public class SysMonitor implements Serializable {
    private final OsSys osSys;
    private final OsJvm osJvm;
    private final OsMem osMem;
    private final OsDisk osDisk;

    public SysMonitor() {
        this.osSys = new OsSys();
        this.osJvm = new OsJvm();
        this.osMem = new OsMem();
        this.osDisk = new OsDisk();
    }

    public String getOsIp() {
        return NetUtils.getLocalIp();
    }

    public String getOsMac() {
        return NetUtils.getLocalMac("-");
    }

    public String getOsName() {
        return NetUtils.getLocalName();
    }

    public OsSys getOsSys() {
        return osSys;
    }

    public OsJvm getOsJvm() {
        return osJvm;
    }

    public OsMem getOsMem() {
        return osMem;
    }

    public OsDisk getOsDisk() {
        return osDisk;
    }

    @Override
    public String toString() {
        return "SysMonitor{" +
                "osIp=" + this.getOsIp() +
                ", osMac=" + this.getOsMac() +
                ", osName=" + this.getOsName() +
                ", osSys=" + this.getOsSys() +
                ", osJvm=" + this.getOsJvm() +
                ", osMem=" + this.getOsMem() +
                ", osDisk=" + this.getOsDisk() +
                '}';
    }
}
