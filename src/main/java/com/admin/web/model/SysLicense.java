package com.admin.web.model;

import com.admin.web.exception.SysLicenseException;
import com.admin.web.utils.NetUtils;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * @author znn
 */
public record SysLicense(String num, LocalDate from, LocalDate to) implements Serializable {

    @Override
    public String toString() {
        return String.format("许可证{编号=%s, 有效期=[%s, %s]}", this.num(), this.from(), this.to());
    }

    /**
     * 许可证编号
     */
    public static String asSysLicenseNumber() {
        return UUID.nameUUIDFromBytes(NetUtils.getLocalMac().getBytes()).toString().toUpperCase();
    }

    /**
     * 许可证加密
     *
     * @param license 许可证
     * @return 密文
     */
    public static byte[] asSysLicense(SysLicense license) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(DigestUtils.md5Digest(license.num().getBytes()), "AES"));
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                    oos.writeObject(license);
                    oos.flush();
                    return cipher.doFinal(bos.toByteArray());
                }
            }
        } catch (Exception e) {
            throw new SysLicenseException("许可证加密异常！", e);
        }
    }

    /**
     * 许可证解密
     *
     * @param bytes 密文
     * @return 许可证
     */
    public static SysLicense asSysLicense(byte[] bytes) {
        return asSysLicense(asSysLicenseNumber(), bytes);
    }

    /**
     * 许可证解密
     *
     * @param key   密钥
     * @param bytes 密文
     * @return 许可证
     */
    public static SysLicense asSysLicense(String key, byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(DigestUtils.md5Digest(key.getBytes()), "AES"));
            try (ByteArrayInputStream bis = new ByteArrayInputStream(cipher.doFinal(bytes))) {
                try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                    return (SysLicense) ois.readObject();
                }
            }
        } catch (Exception e) {
            throw new SysLicenseException("许可证解密异常！", e);
        }
    }


    /**
     * 读取许可证文件内容
     *
     * @return 许可证文件内容
     */
    public static byte[] readSysLicense(Path file) {
        if (Files.notExists(file)) {
            throw new SysLicenseException("许可证不存在！");
        }
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new SysLicenseException("许可证读取失败！", e);
        }
    }

    /**
     * 验证许可证文件
     *
     * @param license 许可证
     */
    public static void hasSysLicense(SysLicense license) {
        if (!Objects.equals(asSysLicenseNumber(), license.num())) {
            throw new SysLicenseException(String.format("许可证签名无效！(%s)", license.num()));
        }
        if (license.from().compareTo(LocalDate.now()) > 0
                || license.to().compareTo(LocalDate.now()) < 0) {
            throw new SysLicenseException(String.format("许可证已到期！(%s ~ %s)", license.from(), license.to()));
        }
    }

}
