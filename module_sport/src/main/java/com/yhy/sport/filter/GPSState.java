package com.yhy.sport.filter;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Created by zhaolei.yang 2018-07-09 19:06
 */
public class GPSState {

    private State mPreState;
    private State mCurState;

    private List<StateChangeListener> mStateChangeList = new ArrayList<>();

    public GPSState() {
        mCurState = State.GPS_LOSS;
    }

    /**
     * 定位管理返回的精度策略，支持注册回调和直接传入回调
     *
     * @param location
     */
    public void check(Location location) {
        check(location, null);
    }

    /**
     * 定位管理返回的精度策略，支持注册回调和直接传入回调
     *
     * @param location
     * @param l
     */
    public void check(Location location, StateChangeListener l) {
        State state;
        float accuracy = location.getAccuracy();

        if (accuracy < 0 || accuracy > 100) {
            state = State.GPS_LOSS;
        } else if (accuracy <= 10 && accuracy > 0) {
            state = State.GPS_GOOD;
        } else if (accuracy < 60) {
            state = State.GPS_NORMAL;
        } else {
            state = State.GPS_WEEK;
        }

        mCurState = state;

        if (mPreState == null) {
            mPreState = state;
            onStateChanged(state, l);
        } else {
            if (mPreState.getCode() != state.getCode()) {
                onStateChanged(state, l);
            }
        }

        mPreState = state;
    }

    public State obtainState() {
        return mCurState;
    }

    private void onStateChanged(State state, StateChangeListener l) {
        if (mStateChangeList != null && mStateChangeList.size() > 0) {
            for (StateChangeListener listener : mStateChangeList) {
                if (listener != null)
                    listener.onStateChanged(state);
            }
        }

        if (l != null) {
            l.onStateChanged(state);
        }
    }

    public void registerListener(StateChangeListener stateChangeListener) {
        mStateChangeList.add(stateChangeListener);
    }

    public interface StateChangeListener {
        void onStateChanged(State state);
    }

    public enum State {
        GPS_LOSS("no gps signal", -1),
        GPS_WEEK("gps signal is week", 1),
        GPS_NORMAL("gps signal is normal", 2),
        GPS_GOOD("gps signal is good", 3);

        String msg;
        int code;

        State(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public long getCode() {
            return code;
        }
    }
}
