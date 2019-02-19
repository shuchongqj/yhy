package com.yhy.network.req.log;

import com.yhy.network.annotations.Param;
import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseLogRequest;
import com.yhy.network.req.BaseRequest;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class LogUploadReq extends BaseLogRequest {

    @Param(ignore = true)
    private List<Log> logs;

    public LogUploadReq(List<Log> logs) {
        this.logs = logs;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public static class Log{
        private String eventCode;

        private Map<String, String> param;

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

        public Map<String, String> getParam() {
            return param;
        }

        public void setParam(Map<String, String> param) {
            this.param = param;
        }
    }
}
