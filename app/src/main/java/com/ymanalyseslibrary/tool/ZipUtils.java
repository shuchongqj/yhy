package com.ymanalyseslibrary.tool;



import com.ymanalyseslibrary.log.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class ZipUtils {
    private static final String LOG_TAG = "ZipUtils";

    /*
     * 用标准的Zip包压缩数据
     */
    public  static String compress(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "IOException,Failed to compress1 message.", e);
        }
        return null;
    }

    // 解压缩
    public static String uncompress(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(
                    str.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "IOException,Failed to uncompress1 message.", e);
        }
        return null;
    }
}
