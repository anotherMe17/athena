package io.github.anotherme17.common.util.zip;

import com.sun.tools.javac.util.Assert;
import io.github.anotherme17.common.util.zip.enums.CompressionMethod;
import io.github.anotherme17.common.util.zip.enums.EncryptionType;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lirenhao
 * date: 2018/9/11 下午7:49
 */
public class ZipCompressor {

    private ZipParameters zipParameters = null;

    private ZipModel zipModel = null;

    public static ZipCompressor.Builder builder() {
        return new Builder();
    }

    /**
     * 压缩文件
     *
     * @param zipFile 压缩后文件名
     * @param files   被压缩文件
     * @throws ZipException
     * @throws IOException
     */
    public void execute(String zipFile, String... files) throws ZipException, IOException {
        Assert.checkNonNull(files, "被压缩文件不能为空");
        List<File> tmpFiles = new ArrayList<>();
        for (String file : files) {
            tmpFiles.add(new File(file));
        }
        execute(new File(zipFile), tmpFiles.toArray(new File[files.length]));
    }

    /**
     * 压缩文件
     *
     * @param zipFile 压缩后文件名
     * @param files   被压缩文件
     * @throws ZipException
     * @throws IOException
     */
    public void execute(File zipFile, File... files) throws ZipException, IOException {
        Assert.checkNonNull(zipParameters, "参数不能为空");
        Assert.checkNonNull(zipModel, "参数不能为空");
        /* 检查目录是否存在,如不存在则创建 */
        Zip4jUtil.checkOutputFolder(zipFile.getParent());

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile), zipModel);
            write(zos, zipParameters, files);
            zos.finish();
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }

    private void write(ZipOutputStream zos, ZipParameters parameter, File... files) throws IOException, ZipException {
        for (File file : files) {
            File[] childFiles = file.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                String parentPath = parameter.getRootFolderInZip();
                parameter.setRootFolderInZip(parentPath + file.getName());
                write(zos, parameter, childFiles);
                parameter.setRootFolderInZip(parentPath);
            } else {
                zos.putNextEntry(file, parameter);
                if (!file.isDirectory()) {
                    BufferedInputStream bis = null;
                    try {
                        bis = new BufferedInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[8096];
                        int len;
                        while ((len = bis.read(buffer)) >= 0) {
                            zos.write(buffer, 0, len);
                        }
                    } finally {
                        if (bis != null) {
                            bis.close();
                        }
                    }
                }
                zos.closeEntry();
            }
        }
    }

    public ZipParameters getZipParameters() {
        return zipParameters;
    }

    public void setZipParameters(ZipParameters zipParameters) {
        this.zipParameters = zipParameters;
    }

    public ZipModel getZipModel() {
        return zipModel;
    }

    public void setZipModel(ZipModel zipModel) {
        this.zipModel = zipModel;
    }

    public static class Builder {

        /**
         * 文件名编码
         */
        private String encoding = InternalZipConstants.CHARSET_DEFAULT;

        /**
         * 压缩方式
         */
        private CompressionMethod compressionMethod = CompressionMethod.DEFLATE_NORMAL_HIGH;

        /**
         * 加密方式
         */
        private EncryptionType encryptionType = EncryptionType.NO_ENCRYPTION;

        /**
         * 加密的密码
         */
        private String password = null;

        /**
         * Zip条目的根路径。
         */
        private String rootPathOfZipEntry = "";

        /**
         * 设置文件名的编码方式
         *
         * @param encoding 文件名的编码方式
         */
        public ZipCompressor.Builder setFileNameCharset(String encoding) {
            Assert.checkNonNull(encoding, "文件名编码不能为空");
            this.encoding = encoding;
            return this;
        }

        /**
         * 设置压缩方式
         *
         * @param compressionMethod 压缩方式
         */
        public ZipCompressor.Builder setCompressionMethod(CompressionMethod compressionMethod) {
            Assert.checkNonNull(compressionMethod, "压缩方式不能为空");
            this.compressionMethod = compressionMethod;
            return this;
        }

        /**
         * 设置加密方式
         *
         * @param encryptionType 加密类型
         */
        public ZipCompressor.Builder setEncryptionType(EncryptionType encryptionType) {
            Assert.checkNonNull(encryptionType, "加密方式不能为空");
            this.encryptionType = encryptionType;
            return this;
        }

        /**
         * 设置密码
         *
         * @param password 加密密码
         * @throws ZipException
         */
        public ZipCompressor.Builder setPassword(String password) throws ZipException {
            if (!Zip4jUtil.isStringNotNullAndNotEmpty(password))
                throw new ZipException("密码不能设为空");
            this.password = password;
            return this;
        }

        /**
         * 设置压缩的根目录
         *
         * @param rootPathOfZipEntry 压缩文件的根目录
         */
        private ZipCompressor.Builder setRootPathOfZipEntry(String rootPathOfZipEntry) {
            if (rootPathOfZipEntry == null) {
                rootPathOfZipEntry = "";
            } else {
                rootPathOfZipEntry = rootPathOfZipEntry.replaceAll("\\\\+", InternalZipConstants.ZIP_FILE_SEPARATOR);
                if (rootPathOfZipEntry.startsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                    rootPathOfZipEntry = rootPathOfZipEntry.substring(InternalZipConstants.ZIP_FILE_SEPARATOR.length());
                }
                if (!rootPathOfZipEntry.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                    rootPathOfZipEntry += InternalZipConstants.ZIP_FILE_SEPARATOR;
                }
                this.rootPathOfZipEntry = rootPathOfZipEntry;
            }
            return this;
        }

        public ZipCompressor build() {
            ZipCompressor zipCompressor = new ZipCompressor();

            ZipParameters zipParameters = new ZipParameters();

            /* 压缩方式 */
            switch (compressionMethod) {
                case STORE:
                    zipParameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
                    break;
                default:
                    zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                    break;
            }
            zipParameters.setCompressionLevel(compressionMethod.ordinal());

            /* 加密方式 */
            switch (encryptionType) {
                case NO_ENCRYPTION:
                    zipParameters.setEncryptFiles(false);
                    zipParameters.setEncryptionMethod(Zip4jConstants.ENC_NO_ENCRYPTION);
                    zipParameters.setAesKeyStrength(-1);
                    zipParameters.setPassword((String) null);
                    break;
                case ZIP_CRYPTO:
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                    zipParameters.setAesKeyStrength(-1);
                    Assert.checkNonNull(password, "密码不能为空");
                    zipParameters.setPassword(password);
                    break;
                case AES_128:
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
                    Assert.checkNonNull(password, "密码不能为空");
                    zipParameters.setPassword(password);
                    break;
                case AES_256:
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                    Assert.checkNonNull(password, "密码不能为空");
                    zipParameters.setPassword(password);
                    break;
                default:
                    break;
            }

            /* 设置Zip根目录 */
            zipParameters.setRootFolderInZip(rootPathOfZipEntry);


            /*  */
            ZipModel zipModel = new ZipModel();
            zipModel.setFileNameCharset(encoding);

            zipCompressor.setZipModel(zipModel);
            zipCompressor.setZipParameters(zipParameters);

            return zipCompressor;
        }
    }
}
