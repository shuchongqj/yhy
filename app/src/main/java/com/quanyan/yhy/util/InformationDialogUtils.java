package com.quanyan.yhy.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹框（公用） on 2015/11/14.
 */
public final class InformationDialogUtils {

    public static int choosePostion = 0;

    public interface OnAlertSelectId {
        void onClick(int whichButton);
    }

    public interface OnMenuPickLisener {
        void onClick(int whichButton);
    }

    InformationDialogUtils() {
    }


    public static Dialog showAlert(final Context context, final String title,
                                   final String[] items, String exit, final OnAlertSelectId alertDo, final int choosePostion) {
        return showAlert(context, title, items, exit, alertDo, null, choosePostion);
    }




    /**
     * @param context Context.
     * @param title   The title of this AlertDialog can be null .
     * @param items   button name list.
     * @param alertDo methods call Id:Button + cancel_Button.
     * @param exit    Name can be null.It will be Red Color
     * @return A AlertDialog
     */
    public static Dialog showAlert(final Context context, final String title,
                                   final String[] items, String exit, final OnAlertSelectId alertDo,
                                   DialogInterface.OnCancelListener cancelListener, int selectPostion) {
        //  String cancel = context.getString(R.string.cancel);
        choosePostion = selectPostion;
        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.alert_dialog_menu_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        final ListView list = (ListView) layout.findViewById(R.id.content_list);
        final AlertAdapter adapter = new AlertAdapter(context, title, items, exit,
                null);
        list.setAdapter(adapter);
        list.setDividerHeight(0);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!(title == null || title.equals("")) && position - 1 >= 0) {
                    alertDo.onClick(position - 1);
                    dlg.dismiss();
                    list.requestFocus();
                    choosePostion = position;
                } else {
                    alertDo.onClick(position);
                    dlg.dismiss();
                    list.requestFocus();
                    choosePostion = position;
                }
                adapter.notifyDataSetChanged();
            }
        });
        // set a large value put it in bottom
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        if (cancelListener != null)
            dlg.setOnCancelListener(cancelListener);

        dlg.setContentView(layout);
        dlg.show();

        return dlg;
    }

    static class AlertAdapter extends BaseAdapter {
        public static final int TYPE_BUTTON = 0;
        public static final int TYPE_TITLE = 1;
        public static final int TYPE_EXIT = 2;
        public static final int TYPE_CANCEL = 3;
        private List<String> items;
        private int[] types;
        private boolean isTitle = false;
        private Context context;

        public AlertAdapter(Context context, String title, String[] items,
                            String exit, String cancel) {
            if (items == null || items.length == 0) {
                this.items = new ArrayList<String>();
            } else {
                this.items = StringUtil.stringsToList(items);
            }
            this.types = new int[this.items.size() + 3];
            this.context = context;
            if (title != null && !title.equals("")) {
                types[0] = TYPE_TITLE;
                this.isTitle = true;
                this.items.add(0, title);
            }

            if (exit != null && !exit.equals("")) {
                // this.isExit = true;
                types[this.items.size()] = TYPE_EXIT;
                this.items.add(exit);
            }

            if (cancel != null && !cancel.equals("")) {
                // this.isSpecial = true;
                types[this.items.size()] = TYPE_CANCEL;
                this.items.add(cancel);
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0 && isTitle) {
                return false;
            } else {
                return super.isEnabled(position);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String textString = (String) getItem(position);
            ViewHolder holder;
            int type = types[position];
            if (convertView == null
                    || ((ViewHolder) convertView.getTag()).type != type) {
                holder = new ViewHolder();
                if (type == TYPE_CANCEL) {
                    convertView = View.inflate(context,
                            R.layout.alert_dialog_menu_list_layout_cancel, null);
                } else if (type == TYPE_BUTTON) {
                    convertView = View.inflate(context,
                            R.layout.alert_dialog_menu_list_layout, null);
                } else if (type == TYPE_TITLE) {
                    convertView = View.inflate(context,
                            R.layout.ac_dialog_information, null);
                } else if (type == TYPE_EXIT) {
                    convertView = View.inflate(context,
                            R.layout.alert_dialog_menu_list_layout_cancel, null);
                }

                holder.text = (TextView) convertView.findViewById(R.id.popup_text);
                holder.type = type;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(textString);
            if (position == 0) {
                holder.text.setTextColor(Color.parseColor("#333333"));
            } else {
                if (position == choosePostion) {
                    holder.text.setTextColor(Color.parseColor("#fa4619"));
                } else {
                    holder.text.setTextColor(Color.parseColor("#999999"));
                }
            }
            return convertView;
        }

        class ViewHolder {
            TextView text;
            int type;
        }
    }

    class MenuAdapter extends BaseAdapter {
        private List<String> mItems;
        private Context mContext;
        private int mChoosedItem;

        public MenuAdapter(Context context, String[] items,
                           int choosedIndex) {
            if (items == null || items.length == 0) {
                this.mItems = new ArrayList<String>();
            } else {
                this.mItems = StringUtil.stringsToList(items);
            }
            mChoosedItem = choosedIndex;
            this.mContext = context;
        }

        public void setChoosedItem(int itemIndex) {
            mChoosedItem = itemIndex;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String textString = (String) getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.menu_list_item, null);

                holder.text = (TextView) convertView.findViewById(R.id.popup_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(textString);
            if (mChoosedItem == position) {
                holder.text.setBackgroundResource(R.drawable.menu_single_selected);
            } else {
                holder.text.setBackgroundResource(R.drawable.menu_single_selector);
            }
            return convertView;
        }

    }

    static class ViewHolder {
        TextView text;
        int type;
    }
}
