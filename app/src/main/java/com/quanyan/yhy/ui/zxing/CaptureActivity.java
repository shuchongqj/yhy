package com.quanyan.yhy.ui.zxing;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.zxing.camera.CameraManager;
import com.quanyan.yhy.ui.zxing.decoding.CaptureActivityHandler;
import com.quanyan.yhy.ui.zxing.decoding.InactivityTimer;
import com.quanyan.yhy.ui.zxing.decoding.RGBLuminanceSource;
import com.quanyan.yhy.ui.zxing.view.ViewfinderNoNetView;
import com.quanyan.yhy.ui.zxing.view.ViewfinderView;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Vector;

import static com.yhy.router.RouterPath.ACTIVITY_CAPTURE_QRCODE;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
@Route(path = ACTIVITY_CAPTURE_QRCODE, name = "CaptureActivity")
public class CaptureActivity extends BaseNewActivity implements Callback {

	private static final String TAG = "CaptureActivity";

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	private BaseNavView mBaseNavView;
	private ProgressDialog mProgress;
	private static final int REQUEST_CODE = 0x1002;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private SurfaceHolder surfaceHolder;
	private TextView mTvNetText;
	private ViewfinderNoNetView viewNoNetView;
	private Dialog mPermissionDialog;

	public static void gotoCaptureActivity(Activity context, int reqCode) {
		Intent intent = new Intent(context, CaptureActivity.class);
		context.startActivityForResult(intent, reqCode);
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestPermissions();
	}

	@Override
	protected int setLayoutId() {
		return R.layout.ac_capture_dimension;
	}

	@Override
	protected void initView() {
		super.initView();
		CameraManager.init(getApplication());
		mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewNoNetView = (ViewfinderNoNetView) findViewById(R.id.view_no_net_view);
		mTvNetText = (TextView) findViewById(R.id.tv_net_notic);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.tv_open).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//调取相册
				TCEventHelper.onEvent(CaptureActivity.this, AnalyDataValue.SCANPAGE_IMAGE_CLICK);
				initRightCamera();
			}
		});
		/*mBaseNavView.setRightTextClick(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		if(NetworkUtil.isNetworkAvailable(this)){
			netWorkVisable();
		}else {
			netWorkDisable();
		}

		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}

	//没网操作
	private void netWorkDisable() {
		if(mTvNetText.getVisibility() == View.GONE){
			mTvNetText.setVisibility(View.VISIBLE);
		}
		if(viewNoNetView.getVisibility() == View.GONE){
			viewNoNetView.setVisibility(View.VISIBLE);
		}
		if(viewfinderView.getVisibility() == View.VISIBLE){
			viewfinderView.setVisibility(View.GONE);
		}

		//CameraManager.get().closeDriver();
	}
	//有网
	private void netWorkVisable() {
		if(mTvNetText.getVisibility() == View.VISIBLE){
			mTvNetText.setVisibility(View.GONE);
		}
		if(viewfinderView.getVisibility() == View.GONE){
			viewfinderView.setVisibility(View.VISIBLE);
		}
		if(viewNoNetView.getVisibility() == View.VISIBLE){
			viewNoNetView.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}


	private void initRightCamera() {
		QRCodeUtil.selectOnePicture(this, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(NetworkUtil.isNetworkAvailable(this)){
			netWorkVisable();
			if(resultCode == RESULT_OK){
				switch (requestCode){
					case REQUEST_CODE:
						//识别相册二维码
						showProcess();
						String picPath = getPhotoPath(data);
						doQrPicture(picPath);
						break;
				}
			}
		}else {
			netWorkDisable();
		}

	}

	private void doQrPicture(final String picPath) {
		Runnable requestThread = new Runnable() {
			@Override
			public void run() {
				Result result = DistinguishQR(picPath);
				if (result != null) {
					Message m = mHandler.obtainMessage();
					m.what = PARSE_BARCODE_SUC;
					m.obj = result;
					mHandler.sendMessage(m);
				} else {
					Message m = mHandler.obtainMessage();
					m.what = PARSE_BARCODE_FAIL;
					m.obj = result;
					mHandler.sendMessage(m);
				}
			}
		};
		NetThreadManager.getInstance().execute(requestThread);
	}

	private void showProcess() {
		mProgress = new ProgressDialog(CaptureActivity.this, R.style.pDialog);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setMessage("正在扫描...");
		mProgress.show();
	}

	private void dissmissProcess(){
		if (mProgress != null){
			mProgress.dismiss();
		}
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(NetworkUtil.isNetworkAvailable(this)){
			netWorkVisable();
			switch (msg.what){
				case PARSE_BARCODE_SUC:
					mProgress.dismiss();
					Result result = (Result) msg.obj;
					if(result != null){
						handleDecode(result, null);
					}
					break;
				case PARSE_BARCODE_FAIL:
					Toast.makeText(CaptureActivity.this, "解析二维码出错", Toast.LENGTH_LONG).show();
					dissmissProcess();
					break;
			}

		}else {
			netWorkDisable();
		}

	}

	private String getPhotoPath(Intent data) {
		String picPath = "";
		if (data == null) {
			Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
			return "";
		}
		Uri photoUri = data.getData();
		LogUtils.i(TAG, "photoUri: " + data.getData());
		if (photoUri == null) {
			Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
			return "";
		}

		ContentResolver resolver = getContentResolver();
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = resolver.query(photoUri, pojo, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getColumnNames().length; i++) {
				LogUtils.i(TAG, "cursor = " + cursor.getColumnNames()[i]);
			}
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToNext();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		LogUtils.i(TAG, "photoUri = " + photoUri.getScheme());
		if(photoUri.getScheme().startsWith("file")){
			picPath = photoUri.getPath();
			LogUtils.i(TAG, "imagePath = " + picPath);
		}

		LogUtils.i(TAG, "imagePath = " + picPath);
		if (!StringUtil.isEmpty(picPath) && QRCodeUtil.isPicture(picPath)) {
			return picPath;
		} else {
			LogUtils.i(TAG, "选择图片文件不正确");
		}
		return "";
	}


	//识别相册的二维码
	private Result DistinguishQR(String imgPath) {
		Bitmap scanBitmap;
		if (TextUtils.isEmpty(imgPath)) {
			return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		/*hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);*/
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(imgPath, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 800);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(imgPath, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (com.google.zxing.FormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		LogUtils.d("Harwkin", "result =====" + resultString);
		//FIXME
		if (resultString.equals("")) {
			ToastUtil.showToast(CaptureActivity.this, "Scan failed!");
		}else {
			//解析链接
			parseLocalUrl(resultString);
		}
		CaptureActivity.this.finish();
	}

	private void parseLocalUrl(String resultString) {
		//识别自己的二维码
		if(QRCodeUtil.parseHttp(resultString)){
			String decode = URLDecoder.decode(resultString);
			LogUtils.d("Harwkin", "decode =====" + decode);
			String firstParam = QRCodeUtil.getValueByName(decode, QRCodeUtil.URL_INDEX_NAME);
			if(firstParam.contains(NativeUtils.QUANYAN_SCHEME)){
				//久休的scheme
				//String url = resultString.substring(resultString.indexOf(NativeUtils.QUANYAN_SCHEME), resultString.length());
				LogUtils.d("Harwkin", "splitURL =====" + firstParam);
				Uri uri = Uri.parse(firstParam);
				NativeUtils.parseNativeData(uri, this);
			}else {
					//跳web界面
					WebParams wp = new WebParams();
					wp.setUrl(resultString);
					wp.setShowTitle(!LocalUtils.isNeedFullScreen(resultString));
					wp.setIsSetTitle(!LocalUtils.isNeedFullScreen(resultString));
					NavUtils.openBrowser(CaptureActivity.this, wp);

			}
		}else {
			if (resultString.startsWith("66") && resultString.length() == 18){
				NavUtils.startWebview(this, "", SPUtils.getURL_SCAN_HEXIAO(this) + resultString, 0);
			}else {
				ToastUtil.showToast(CaptureActivity.this, "识别失败,请重试!");
				dissmissProcess();
			}
		}


		/*//识别自己的二维码
		if(QRCodeUtil.parseUrl(this, resultString)){
			//久休的scheme
			if(resultString.contains(NativeUtils.QUANYAN_SCHEME)){
				String url = resultString.substring(resultString.indexOf(NativeUtils.QUANYAN_SCHEME), resultString.length());
				LogUtil.d("Harwkin", "splitURL =====" + url);
				Uri uri = Uri.parse(url);
				NativeUtils.parseNativeData(uri, this);
			}
		}else {
			if(QRCodeUtil.parseHttp(resultString)){
				//跳web界面
				WebParams wp = new WebParams();
				wp.setUrlAndPlayer(resultString);
				NavUtils.openBrowser(CaptureActivity.this, wp);
			}else {
				ToastUtil.showToast(CaptureActivity.this, resultString);
			}
		}*/


	}


	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	@Override
	protected void onNetStateChanged() {
		super.onNetStateChanged();
		netWorkVisable();
	}

	@Override
	protected void onNetStateChanged(boolean hasNet) {
		super.onNetStateChanged(hasNet);
		netWorkDisable();
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};


	/*@Override
	public View onLoadNavView() {
		mBaseNavView = new BaseNavView(this);
		mBaseNavView.setTitleText(R.string.capture_title);
		mBaseNavView.setRightText(R.string.capture_title_right);
		return mBaseNavView;
	}*/


	private boolean checkPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int result = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
//            int result1 = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.FLASHLIGHT);
			int result2 = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);

			return result == PackageManager.PERMISSION_GRANTED
//                    && result1 == PackageManager.PERMISSION_GRANTED
					&& result2 == PackageManager.PERMISSION_GRANTED;
		} else {
			int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
//            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.FLASHLIGHT);
			int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);

			return result == PackageManager.PERMISSION_GRANTED
//                    && result1 == PackageManager.PERMISSION_GRANTED
					&& result2 == PackageManager.PERMISSION_GRANTED;
		}
	}

	/**
	 * 申请权限
	 */
	private void requestPermissions() {
		if (Build.VERSION.SDK_INT>22){
			if (ContextCompat.checkSelfPermission(this,
					android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
					|| ContextCompat.checkSelfPermission(this,
					Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
				//先判断有没有权限 ，没有就在这里进行权限的申请
				ActivityCompat.requestPermissions(this,
						new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case 1:
				if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED
						&& grantResults[1] == PackageManager.PERMISSION_GRANTED){
				}else {
					//这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
					if (mPermissionDialog == null) {
						mPermissionDialog = DialogUtil.showMessageDialog(this, null,
								"打开摄像头失败！请在\"设置\"中确认是否授权使用摄像头", "确认", null,
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										finish();
										mPermissionDialog.dismiss();
									}
								}, null);
					}
					mPermissionDialog.show();
				}
				break;
			default:
				break;
		}
	}
}