
/**   
 * @Title: StepReceiver.java 
 * @Package: com.pajk.pedometer.receiver 
 * @Description: TODO
 * @author xiezhidong@pajk.cn  
 * @date 2014-12-16 下午2:34:21 
 */


package com.quanyan.pedometer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.newpedometer.StepService;


/** 
 * @Description 
 * @author xiezhidong@pajk.cn
 * @date 2014-12-16 下午2:34:21 
 */

public class StepReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		LogUtils.d("loza?StepReceiver -- onReceive");
		if(arg0 == null){
			return;
		}
		LogUtils.d("loza?StepReceiver -- onReceive2");
		Intent intentSer = new Intent(arg0,StepService.class);
		arg0.startService(intentSer);
	}

}