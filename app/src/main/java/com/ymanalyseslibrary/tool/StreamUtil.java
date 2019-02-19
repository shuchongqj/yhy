package com.ymanalyseslibrary.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/22
 * Time:10:07
 * Version 1.0
 */
public class StreamUtil {

    public static byte[] getInputStreamData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean flag = false;

        int length;
        while(-1 != (length = inputStream.read(buffer))) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static void closeInputStream(InputStream inputStream) {
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception var2) {
                ;
            }
        }

    }

    public static void closeOutputStream(OutputStream outputStream) {
        if(outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception var2) {
                ;
            }
        }

    }
}
