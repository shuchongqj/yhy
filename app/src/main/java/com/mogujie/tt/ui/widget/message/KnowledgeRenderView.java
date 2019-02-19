package com.mogujie.tt.ui.widget.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.im.entity.KnowLedgeItemBean;
import com.yhy.common.beans.im.entity.KnowLedgeMessage;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.beans.net.model.param.WebParams;

import java.util.List;

/**
 * @author : yingmu on 15-1-9.
 * @email : yingmu@mogujie.com.
 * <p/>
 * 样式根据mine 与other不同可以分成两个
 */
public class KnowledgeRenderView extends BaseMsgRenderView implements AdapterView.OnItemClickListener {
    /**
     * 文字消息体
     */
    private TextView messageContent;
    private ListView mListView;
    private KnowledgeAdapter mAdapter;

    public static KnowledgeRenderView inflater(Context context, ViewGroup viewGroup, boolean isMine) {
        int resource = isMine ? R.layout.tt_mine_knowledge_message_item : R.layout.tt_other_knowledge_message_item;

        KnowledgeRenderView textRenderView = (KnowledgeRenderView) LayoutInflater.from(context).inflate(resource, viewGroup, false);
        textRenderView.setMine(isMine);
        textRenderView.setParentView(viewGroup);
        return textRenderView;
    }

    public KnowledgeRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        messageContent = (TextView) findViewById(R.id.message_content);
        mListView = (ListView) findViewById(R.id.base_listview);
        mListView.setAdapter(mAdapter = new KnowledgeAdapter());
        mListView.setOnItemClickListener(this);
    }


    /**
     * 控件赋值
     *
     * @param messageEntity
     * @param userEntity
     */
    @Override
    public void render(MessageEntity messageEntity, UserEntity userEntity, Context context) {
        super.render(messageEntity, userEntity, context);
        if (messageEntity instanceof KnowLedgeMessage) {
            KnowLedgeMessage knowLedgeMessage = (KnowLedgeMessage) messageEntity;
            if (!knowLedgeMessage.isBuildJson()) {
                knowLedgeMessage.buildDisplayMessage();
            }
            messageContent.setText(knowLedgeMessage.getTitle());
            mAdapter.setData(knowLedgeMessage.getBeans());
        }

    }
//    private static final String SCHEMA ="com.mogujie.tt://message_private_url";
//    private static final String PARAM_UID ="uid";
//    private String urlRegex = "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eouw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)";

//    private void extractUrl2Link(TextView v) {
//        java.util.regex.Pattern wikiWordMatcher = java.util.regex.Pattern.compile(urlRegex);
//        String mentionsScheme = String.format("%s/?%s=",SCHEMA, PARAM_UID);
//        Linkify.addLinks(v, wikiWordMatcher, mentionsScheme);
//    }

    @Override
    public void msgFailure(MessageEntity messageEntity) {
        super.msgFailure(messageEntity);
    }

    /**
     * ----------------set/get---------------------------------
     */
    public TextView getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TextView messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        KnowLedgeItemBean bean = (KnowLedgeItemBean) parent.getAdapter().getItem(position);
        WebParams params = new WebParams();
        params.setUrl(bean.getH5Url());
        params.setShowTitle(false);
        NavUtils.openBrowser(getContext(), params);
    }

    class KnowledgeAdapter extends BaseAdapter {

        private List<KnowLedgeItemBean> data;

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_knowledge, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text_view);
            textView.setText(data.get(position).getMsg());

            return convertView;
        }

        public void setData(List<KnowLedgeItemBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }
}
