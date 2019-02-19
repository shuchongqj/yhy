package com.newyhy.wsy;

import com.chinanetcenter.wcs.android.utils.DateUtil;
import com.google.gson.Gson;
import com.yhy.common.types.AppDebug;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 生成 网宿云 需要的 上传凭证
 * Created by Jiervs on 2018/5/24.
 */

public class WsyUtils {

    static String wsy_upload_url = "http://qywlkj.up20.v1.wcsapi.com";
    private static String ak = "9da78911feddd13c244ebf06a34ec5d72a8365a4";
    private static String sk = "a0ac6f65ba12dc57e7d97dbcb3cd3bf812901297";
    private static String bucketName = AppDebug.IS_ONLINE?"qywlkj":"qywlkj-test001";

    private static String getPutPolicyJson() {
        WsyPutPolicy putPolicy = new WsyPutPolicy();
        putPolicy.setScope(bucketName);
        Date date = new Date();
        Long time = DateUtil.parseDate("2050-01-01 12:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
        putPolicy.setDeadline(String.valueOf(time));
        String result = new Gson().toJson(putPolicy);
        return result;
    }

    /**
     * Base64 编码
     * @param src
     * @return
     */
    private static byte[] encodeBase64Ex(byte[] src) {
        byte[] b64 = Base64.encodeBase64(src);

        for (int i = 0; i < b64.length; i++) {
            if (b64[i] == '/') {
                b64[i] = '_';
            } else if (b64[i] == '+') {
                b64[i] = '-';
            }
        }
        return b64;
    }

    private static byte[] urlSafeEncodeBytes(byte[] src) {
        if (src.length % 3 == 0) return encodeBase64Ex(src);

        byte[] b = encodeBase64Ex(src);
        if (b.length % 4 == 0) return b;

        int pad = 4 - b.length % 4;
        byte[] b2 = new byte[b.length + pad];
        System.arraycopy(b, 0, b2, 0, b.length);
        b2[b.length] = '=';
        if (pad > 1) b2[b.length + 1] = '=';
        return b2;
    }

    /**
     * 进行 hmac-sha1签名数据
     * @param data
     * @param key
     * @return
     */
    public static String getSignatureHmacSHA1(byte[] data, String key) {
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac;
        StringBuffer sb = new StringBuffer();
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            for (byte b : rawHmac) {
                int v = b& 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    sb.append(0);
                }
                sb.append(hv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取 网宿云上传凭证
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String generateUpLoadToken()  {
        String uploadToken = "";
        try {
            String policy = getPutPolicyJson();
            String enCodePolicy = new String(urlSafeEncodeBytes(policy.getBytes("utf-8")), "utf-8");
            String sign = getSignatureHmacSHA1(enCodePolicy.getBytes("utf-8"), sk);
            String enCodeSign = new String(urlSafeEncodeBytes(sign.getBytes("utf-8")), "utf-8");
            uploadToken = String.format("%s:%s:%s", ak, enCodeSign, enCodePolicy);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadToken;
    }
}
