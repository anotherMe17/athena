package io.github.anotherme17.common.util.zip.enums;

/**
 * @author lirenhao
 * date: 2018/9/11 下午8:16
 */
public enum CompressionMethod {
    /**
     * 无压缩。
     */
    STORE,

    /**
     * DEFLATE（压缩等级1 =压缩比：最小，压缩率：最高）。
     */
    DEFLATE_FASTEST,

    /**
     * DEFLATE（压缩级别2 =压缩率：小，压缩率：高）。
     */
    DEFLATE_FASTER,

    /**
     * DEFLATE（压缩等级3 =压缩率：小，压缩率：高）。
     */
    DEFLATE_FAST,

    /**
     * DEFLATE（压缩等级4 =压缩率：小，压缩率：高）。
     */
    DEFLATE_NORMAL_FAST,

    /**
     * DEFLATE（压缩等级5 =压缩率：中等，压缩率：中等）。
     */
    DEFLATE_NORMAL,

    /**
     * DEFLATE（压缩等级6 =压缩率：大，压缩率：低）。
     */
    DEFLATE_NORMAL_HIGH,

    /**
     * DEFLATE（压缩等级7 =压缩比：大，压缩率：低）。
     */
    DEFLATE_HIGH,

    /**
     * DEFLATE（压缩等级8 =压缩比：大，压缩率：低）。
     */
    DEFLATE_HIGHER,

    /**
     * DEFLATE（压缩等级9 =压缩比：最大，压缩率：最低）。
     */
    DEFLATE_HIGHEST;
}
