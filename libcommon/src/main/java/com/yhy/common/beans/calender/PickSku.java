package com.yhy.common.beans.calender;

import android.text.TextUtils;

import com.yhy.common.beans.net.model.tm.ItemSkuPVPairVO;
import com.yhy.common.beans.net.model.tm.ItemSkuVO;
import com.yhy.common.utils.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dengmingjia on 2015/11/23.
 * 选择日历需要的bean对象
 */
public class PickSku implements Serializable {
    private static final String SELECTED_TYPE = "SELECTED_TYPE";
    private static final String ADULTS = "成人";
    private static final String PERSON_TYEP = "PERSON_TYPE";
    private static final String DATE = "START_DATE";
    private static final long serialVersionUID = -513546493377957860L;
    /**
     * 库存
     */
    public int stockNum;

    /**
     * 价格
     */
    public long price;

    /**
     * 日期
     */
    public String date;
    public long pid;
    public long vid;
    public long id;

    //    public PickSku(int stockNum, long price, String date) {
//        this.stockNum = stockNum;
//        this.price = price;
//        this.date = date;
//    }
    public PickSku(String date, long pid, long vid) {
        this.date = date;
        this.pid = pid;
        this.vid = vid;
    }


    public static Object[] getPickSkus(long startDate, List<ItemSkuVO> skuList) {
        if (skuList == null || skuList.size() == 0) {
            return null;
        }
        Object[] objects = new Object[2];
        long endDate = 0;
        //获取开始时间后两个月最后一天的timeinmillis
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        calendar.add(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        long tempDate = calendar.getTimeInMillis();
        HashMap<String, PickSku> maps = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < skuList.size(); i++) {
            ItemSkuVO vo = skuList.get(i);
            PickSku sku = getSkuItemValue(vo.itemSkuPVPairList, DATE);
            if (sku == null) continue;
            long dateTime = Long.valueOf(sku.date);
            if (dateTime > tempDate) {
                objects[0] = endDate;
                objects[1] = maps;
                return objects;
            }
            String dateString = dateFormat.format(dateTime);
            if (maps.get(dateString) != null) continue;
            endDate = endDate > dateTime ? endDate : dateTime;
            sku.stockNum = vo.stockNum;
            sku.price = vo.price;
            maps.put(dateString, sku);
        }
        objects[0] = endDate;
        objects[1] = maps;
        return objects;
    }

    /**
     * 获取itemValue值
     */
    public static PickSku getSkuItemValue(List<ItemSkuPVPairVO> items, String type) {
        if (items == null || items.size() == 0) return null;
        for (int i = 0; i < items.size(); i++) {
            ItemSkuPVPairVO vo = items.get(i);
            if (PERSON_TYEP.equals(vo.pType)) {
                if (!ADULTS.equals(vo.vTxt)) return null;
            } else if (type.equals(vo.pType)) {
                return new PickSku(vo.vTxt, vo.pId, vo.vId);
            }
        }
        return null;
    }

    public static PickSku getSku(HashMap<String, PickSku> pickSkus, String dateString) {
        if (pickSkus == null || TextUtils.isEmpty(dateString)) {
            return null;
        }
        return pickSkus.get(dateString);
    }

    public static PickSku getSku(HashMap<String, PickSku> pickSkus, Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return getSku(pickSkus, DateUtils.getyyyymmdd(calendar.getTimeInMillis(), "-"));
    }

}
