package com.yhy.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.yhy.common.utils.JSONUtils;

import java.lang.reflect.Type;

@Route(path = "/default/serialization")
public class SerializationServiceImpl implements SerializationService {
    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        return JSONUtils.convertToObject(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return JSONUtils.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return JSONUtils.convertToObject(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}
