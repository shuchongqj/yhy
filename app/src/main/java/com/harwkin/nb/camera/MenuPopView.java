package com.harwkin.nb.camera;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity页面右上角菜单封装
 * Created by yulili on 2015/1/20.
 */
public class MenuPopView extends PopupWindow{

    private Context mContext;
    MenuListAdapter mMenuAdapter;
    private View mMenuView;
    private ListView mLvMenus;
    List<MenuMoreItem> menuData = new ArrayList<MenuMoreItem>();

    public MenuPopView(Context ctx, List<MenuMoreItem> menus, AdapterView.OnItemClickListener listener){
        super(ctx);
        this.mContext = ctx;
        this.menuData = menus;
        initMenuList(listener,menuData);
    }

    ImageView imgArrow;
    /**
     * 初始化菜单列表
     * @param lsn 列表单鞋监听
     * @param data 列表数据
     */
    private void initMenuList(AdapterView.OnItemClickListener lsn,List<MenuMoreItem> data){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.view_menu_more, null);
        mLvMenus = (ListView) mMenuView.findViewById(R.id.lv_menus);
        mMenuAdapter = new MenuListAdapter(mContext, data,R.layout.view_menu_more_items);
        mLvMenus.setAdapter(mMenuAdapter);
        mLvMenus.setOnItemClickListener(lsn);

        imgArrow = (ImageView) mMenuView.findViewById(R.id.img_arrow);

        this.setContentView(mMenuView);
        this.setWidth((int) (ScreenUtil.getScreenWidth(mContext) * 0.5));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 没有该行代码则返回键不响应
        this.setBackgroundDrawable(new BitmapDrawable());
        this.update();
    }

    /**
     * 显示右角菜单
     */
    public void showRightMorePop(final View locationView){
        this.showAsDropDown(locationView, 0, 0);
        imgArrow.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 直接移除吧
                        imgArrow.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        // 现在布局全部完成，可以获取到任何View组件的宽度、高度、左边、右边等信息
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgArrow
                                .getLayoutParams();
                        int screenWidth = ScreenUtil.getScreenWidth(mContext);
                        int[] location = new int[2];
                        locationView.getLocationOnScreen(location);
                        int marginRight = screenWidth - (location[0] + locationView.getWidth() / 2 + imgArrow.getWidth() / 2);
                        lp.setMargins(0, 0, marginRight, 0);
                        imgArrow.setLayoutParams(lp);
                    }
                });

    }

    /**
     * 将String数组转化为list
     * @param menus
     * @return
     */
    public static List<MenuMoreItem> arrayToMenuItemList(String[] menus){
        int length = null == menus ? 0 : menus.length;
        if (length < 1) {
            return null;
        }
        List<MenuMoreItem> list = new ArrayList<MenuMoreItem>();
        for (String value : menus){
            MenuMoreItem menuMoreItem = new MenuMoreItem();
            menuMoreItem.itemText = value;
            list.add(menuMoreItem);
        }
        return list;
    }

    /**
     * 将icons,strings转化为list
     * @param icons
     * @param menus
     * @return
     */
    public static List<MenuMoreItem> arrayWithIconToList(int[] icons,String[] menus){
        if (icons.length != menus.length){
            throw new IllegalArgumentException();
        }
        List<MenuMoreItem> list = new ArrayList<MenuMoreItem>();
        for (int i=0;i<icons.length && i<menus.length;i++){
            MenuMoreItem menuMoreItem = new MenuMoreItem();
            menuMoreItem.itemIconId = icons[i];
            menuMoreItem.itemText = menus[i];
            list.add(menuMoreItem);
        }
        return list;
    }

    public void notify(List<MenuMoreItem> data){
        mMenuAdapter.notifyData(data);
    }

    /**
     * 菜单列表Adapter
     */
    public class MenuListAdapter extends BaseAdapter {
        private Context mContext;
        List<MenuMoreItem> data;
        int resource;

        public MenuListAdapter(Context context, List<MenuMoreItem> data, int resource) {
            this.mContext = context;
            this.data = data;
            this.resource = resource;
        }

        public void notifyData(List<MenuMoreItem> data){
            this.data = data;
            mMenuAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        this.resource, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            MenuMoreItem menuItem = data.get(position);
            int iconResourceId = null == menuItem ? 0 : menuItem.itemIconId;
            if (iconResourceId != 0) {
                try {
                    Drawable drawable = mContext.getResources().getDrawable(iconResourceId);
                    vh.tvMenuItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    vh.tvMenuItem.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelSize(R.dimen.margin_10dp));
                }catch (Resources.NotFoundException e){
                    e.printStackTrace();
                }
            }
            vh.tvMenuItem.setText(menuItem.itemText);
            return convertView;
        }

        class ViewHolder {
            TextView tvMenuItem;
            public ViewHolder(View view){
                tvMenuItem = (TextView) view.findViewById(R.id.item_menu);
            }
        }
    }
}
