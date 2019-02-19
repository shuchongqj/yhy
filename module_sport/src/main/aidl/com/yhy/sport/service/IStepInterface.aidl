// ISportStepInterface.aidl
package com.yhy.sport.service;

interface IStepInterface {
     /**
      *
      *行走开始，计数开始
      *
      */
     void walkStart();

     /**
     *
     *行走继续，计数继续
     *
     */
     void walkResume();

     /**
     *
     *行走开始，计数暂停
     *
     */
     void walkPause();

     /**
     *
     *行走停止，计数停止
     *
     */
     void walkStop();

     /**
     *
     *根据当前运动中的步行数
     *
     */
     int getCurrentWalkSteps();
}
