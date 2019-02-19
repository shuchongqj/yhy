package com.quanyan.yhy.ui.sport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.sport.model.HealthInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class HealthAdapter extends RecyclerView.Adapter implements HealthListener {

    private Context context = null;
    private ArrayList<HealthInfo> list = null;

    public HealthAdapter(Context context) {
        this.context = context;
        list = new ArrayList<HealthInfo>();
    }

    public HealthAdapter(Context context, ArrayList<HealthInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<HealthInfo> list) {
        this.list = list;
//        if (list != null && !list.isEmpty()) {
//            for (HealthInfo i : list) {
//                this.list.add(i);
//            }
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_health_list_item, parent, false);
        HealthHolder mHealthHolder = new HealthHolder(view);
        return mHealthHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HealthHolder mHealthHolder = (HealthHolder) holder;
        if (list != null && !list.isEmpty()) {
            HealthInfo i = list.get(position);
            String name = i.getName();
            if (name != null) {
                mHealthHolder.tvName.setText(name);
            }
            String positional = i.getPositional();
            if (positional != null) {
                mHealthHolder.tvPositional.setText(positional);
            }
            String avatar = i.getAvatar();
            if (avatar != null) {
                mHealthHolder.sdHead.setTag(null);
                avatar = ImageUtils.getImageFullUrl(avatar);
//                mHealthHolder.sdHead.setImageURI(avatar);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(avatar), mHealthHolder.sdHead);
            }
            mHealthHolder.btnChat.setTag(position);
            mHealthHolder.sdHead.setTag(position);
            mHealthHolder.setListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onChatItemClick(int position) {
        if (list != null && !list.isEmpty()) {
            HealthInfo i = list.get(position);
            long id = i.getId();
            Analysis.pushEvent(context, AnEvent.HEALTH_ADVICE,String.valueOf(id));
            // 修改  现在是跳转个人主页  定位到服务
            NavUtils.gotoMasterHomepage(context, id, true);
            Analysis.pushEvent(context, AnEvent.CONSULTANT_HOMEPAGE,String.valueOf(id));
        }
    }
}
