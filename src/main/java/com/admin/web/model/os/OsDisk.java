package com.admin.web.model.os;

import java.io.File;
import java.util.Arrays;

/**
 * @author znn
 */
public class OsDisk extends Os {
    private final File[] disks;

    public OsDisk() {
        disks = File.listRoots();
    }

    public long getTotalSpace() {
        return Arrays.stream(disks).mapToLong(File::getTotalSpace).sum();
    }

    public long getUsableSpace() {
        return Arrays.stream(disks).mapToLong(File::getUsableSpace).sum();
    }

    public long getUsedSpace() {
        return this.getTotalSpace() - this.getUsableSpace();
    }

    @Override
    public String toString() {
        return "磁盘容量：" + asBytes(this.getTotalSpace())
                + ",磁盘可用容量：" + asBytes(this.getUsableSpace())
                + ",磁盘已用容量：" + asBytes(this.getUsedSpace());

    }
}
