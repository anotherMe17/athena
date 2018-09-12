package io.github.anotherme17.common.util.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

import java.io.File;
import java.io.IOException;

/**
 * 文件解压缩工具
 *
 * @author lirenhao
 * date: 2018/9/12 上午11:46
 */
public class ZipExtractor {

    private String encoding = InternalZipConstants.CHARSET_DEFAULT;

    private boolean autoCreateDirectory = false;

    /**
     * 解压缩文件
     *
     * @param directory 解压后文件路径
     * @param zipPath   压缩文件路径
     * @throws IOException
     * @throws ZipException
     */
    public void execute(String directory, String zipPath) throws IOException, ZipException {
        execute(directory, zipPath, null);
    }

    /**
     * 解压缩文件
     *
     * @param directory 解压后文件路径
     * @param zipFile   压缩文件路径
     * @param password  解压密码
     * @throws IOException
     * @throws ZipException
     */
    public void execute(String directory, String zipFile, String password) throws IOException, ZipException {
        execute(new File(directory), new File(zipFile), password);
    }

    /**
     * 解压缩文件
     *
     * @param directory 解压后文件路径
     * @param zipFile   压缩文件路径
     * @throws IOException
     * @throws ZipException
     */
    public void execute(File directory, File zipFile) throws IOException, ZipException {
        execute(directory, zipFile, null);
    }

    /**
     * 解压缩文件
     *
     * @param directory 解压后文件路径
     * @param zipFile   压缩文件路径
     * @param password  解压密码
     * @throws IOException
     * @throws ZipException
     */
    public void execute(File directory, File zipFile, String password) throws IOException, ZipException {
        if (autoCreateDirectory) {
            String path = directory.getPath();
            if (!path.endsWith("\\") && !path.endsWith("/")) {
                path += InternalZipConstants.FILE_SEPARATOR;
            }
            directory = new File(path + Zip4jUtil.getZipFileNameWithoutExt(zipFile.getName()));
        }

        Zip4jUtil.checkOutputFolder(directory.getPath());

        ZipFile zip = new ZipFile(zipFile);
        zip.setFileNameCharset(encoding);
        if (zip.isEncrypted() && Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
            zip.setPassword(password);
        }
        zip.extractAll(directory.getCanonicalPath());
    }


    public static ZipExtractor build() {
        return new ZipExtractor();
    }

    public String getEncoding() {
        return encoding;
    }

    public ZipExtractor setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public boolean isAutoCreateDirectory() {
        return autoCreateDirectory;
    }

    public ZipExtractor setAutoCreateDirectory(boolean autoCreateDirectory) {
        this.autoCreateDirectory = autoCreateDirectory;
        return this;
    }
}
