package com.yhy.common.types;

public class ErrorCode {
	//成功
	public static final int STATUS_OK = 0;
	public static final int STATUS_IO_EXCEPTION = 0x1001;
	public static final int STATUS_NETWORK_EXCEPTION = 0x1003;
	//Device Token missing
	public static final int DEVICE_TOKEN_MISSING = 0x1004;
	//network unavailable
	public static final int NETWORK_UNAVAILABLE = 0x1005;
	//网络异常，网络错误
	public static final int NETWORK_ERROR_OR_EXCEPTION = 0x1007;
	//没有数据
	public static final int STATUS_NODATA = 0x1008;

	//not login
	public static final int NOT_LOGIN = 0x1006;
	/**
	 * upload picture failed
	 */
	public static final int UPLOAD_PICTURE_FAILED = 0x1009;
	/**
	 * socket time out
	 */
	public static final int CONNECTTION_TIME_OUT = 0x100a;
    public static final int SDCARD_UNAVAILABLE = 0x100b;
    
    public static final int DATA_FORMAT_ERROR = 0x100c;
}
