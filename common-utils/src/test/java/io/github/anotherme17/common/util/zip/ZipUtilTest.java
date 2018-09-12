package io.github.anotherme17.common.util.zip;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author lirenhao
 * date: 2018/9/11 下午5:36
 */
public class ZipUtilTest {

    @Test
    public void testZipUtil() throws ZipException, IOException {
        ZipCompressor.builder()
                .build()
                .execute(new File("/Users/D/Desktop/ziptest/test.zip"),
                        new File("/Users/D/Desktop/8cd5f61a-b5c4-11e8-ace1-00163e0c310d"));;
    }

    @Test
    public void testUnZip() throws IOException, ZipException {
        ZipExtractor.build()
                .setAutoCreateDirectory(false)
                .setEncoding("GBK")
                .execute(new File("/Users/D/Desktop/ziptest/unzip"),
                new File("/Users/D/Downloads/2088012632607250-20180906-030104600-买入交易.csv.zip"),
                null);
    }
}
