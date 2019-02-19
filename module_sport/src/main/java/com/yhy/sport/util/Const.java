package com.yhy.sport.util;

/**
 * @Description: 统一对外公开的常量入口
 * @Created by zhaolei.yang 2018-07-09 13:50
 */
public interface Const {

    float REDUCER_THRESHOLD = 30f;//抽稀阈值

    float NOISE_THRESHOLD = 100f;//噪点阈值

    int KALMAN_INTENSITY = 3;//卡尔曼滤波强度值

    String KLMAN_FILTER_PROVIDER = "klman_filter_provider";//标记是处理过的数据

    int REQUEST_READ_PHONE_STATE = 1;//动态询问手机状态权限的Request code

    String TAG = "sport";//Log日志tag

    String STEP_COUNTER_TAG = "step_counter_tag";//计步传感器Log日志tag

    int DATA_TASK_DELTA = 1000;//异步线程处理GPS信号数据的循环间隔，单位ms

    int AMAP_INTERVAL = 2000;//高德地图GPS信号回调间隔，单位ms
}
