package com.admin.web.model.os;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.stream.StreamSupport;

/**
 * @author znn
 */
public class OsDisk extends Os {
    private final Iterable<FileStore> disks;

    public OsDisk() {
        this.disks = FileSystems.getDefault().getFileStores();
    }

    public long getTotalSpace() {
        return StreamSupport.stream(this.disks.spliterator(), false).mapToLong((fileStore) -> {
            try {
                return fileStore.getTotalSpace();
            } catch (IOException e) {
                return 0;
            }
        }).sum();
    }

    public long getUsableSpace() {
        return StreamSupport.stream(this.disks.spliterator(), false).mapToLong((fileStore) -> {
            try {
                return fileStore.getUsableSpace();
            } catch (IOException e) {
                return 0;
            }
        }).sum();
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
