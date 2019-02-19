package com.yhy.network.utils

import android.os.Process
import com.yhy.common.base.YHYBaseApplication
import java.io.File
import java.util.*

class TempLog {

    companion object {


        fun log(log : String){
            if (!YHYBaseApplication.getInstance().yhyEnvironment.isOnline){
                File("/sdcard/log.txt").apply {
                    if (exists()){
                        createNewFile()
                    }
                    try {
                        appendText("web log --- ${Date()} (process id : ${Process.myPid()}) ----> $log\r\n")

                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }

        }
    }
}