package com.quanyan.yhy.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;


public class MyTextActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.post_comment_edittext)
	private EditText post_comment_edittext;

	@ViewInject(R.id.tv_max_size)
	private TextView tv_max_size;

	@ViewInject(R.id.tv_need_text)
	private TextView tv_need_text;

	@ViewInject(R.id.img_clear)
	private ImageView mClearImg;

	public final static int MAX_LENGTH_NAME = 10;
	public final static int MAX_LENGTH_SIGN = 30;
	public final static int MAX_LENGTH_DEST = 100;
	private int limitsize;

	private final static int MAX_NICK_LENGTH  = 15;
	private final static int MIN_NICK_LENGTH  = 2;

	private int mSelectType;
	private String mTitle;
	private String mText;

	@Override
	public View onLoadContentView() {
		return View.inflate(this,R.layout.ac_my_text, null);
	}

	private BaseNavView mBaseNavView;

	@Override
	public View onLoadNavView() {
		mBaseNavView = new BaseNavView(this);
		mBaseNavView.setRightText(getString(R.string.label_btn_finish));
		mBaseNavView.setRightTextClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onfinish();
			}
		});
		return mBaseNavView;
	}

	@Override
	public boolean isTopCover() {
		return false;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		mSelectType = getIntent().getIntExtra(SPUtils.EXTRA_SELECT_TYPE, -1);
		mTitle = getIntent().getStringExtra(SPUtils.EXTRA_TITLE);
		mText = getIntent().getStringExtra(SPUtils.EXTRA_SELECT_CURRENT_VALUE);
		if (mTitle != null) {
			mBaseNavView.setTitleText(mTitle);
		}
		initViews();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
		super.onCreate(savedInstanceState);
	}

	private void initViews() {
		mClearImg.setOnClickListener(this);
		loadData();
		changeButton();
	}

	private void changeButton() {
		String edit = post_comment_edittext.getText().toString();
		if(edit.length() >= 2){
			mBaseNavView.setRightTextEnable(true);
			mBaseNavView.setRightTextColor(R.color.neu_333333);
		}else {
			mBaseNavView.setRightTextEnable(false);
			mBaseNavView.setRightTextColor(R.color.neu_cccccc);
		}
	}

	/**
	 * 选择文本
	 * @param context
	 */
	public static void gotoSeclectText(Activity context,String title,int type,String value){
		Intent intent = new Intent(context,MyTextActivity.class);
		intent.putExtra(SPUtils.EXTRA_TITLE,title);
		intent.putExtra(SPUtils.EXTRA_SELECT_TYPE,type);
		intent.putExtra(SPUtils.EXTRA_SELECT_CURRENT_VALUE,value);
		context.startActivityForResult(intent, UserInfoUpdateActivity.REQ_TEXT_INPUT);
	}

	private void loadData() {
		limitText();

		if(!StringUtil.isEmpty(mText)){
			tv_max_size.setText(mText.length() + "/" + limitsize);
			post_comment_edittext.setText(mText);
		}else {
			tv_max_size.setText(0 + "/" + limitsize);
			post_comment_edittext.setText("");
			mClearImg.setVisibility(View.INVISIBLE);
		}
		post_comment_edittext.addTextChangedListener(mTextWatcher);
		post_comment_edittext.setSelection(post_comment_edittext.length()); // 将光标移动最后一个字符后面
	}

	private void doPostComment() {
		String suggestText = post_comment_edittext.getText().toString();

		if (mContentIsLeagal && !TextUtils.isEmpty(suggestText)) {
			Intent data = new Intent();
			data.putExtra(SPUtils.EXTRA_SELECT_CURRENT_VALUE, suggestText);
			data.putExtra(SPUtils.EXTRA_SELECT_TYPE, mSelectType);
			setResult(Activity.RESULT_OK, data);
			finish();
		}

	}

	private void limitText() {
		if (mSelectType == ValueConstants.SELECT_TYPE_USER_NAME) {
			limitsize = MAX_LENGTH_NAME;
			//post_comment_edittext.setSingleLine();
			post_comment_edittext.setHint(getString(R.string.name_limit));
			post_comment_edittext.setSingleLine();

		}else if(mSelectType == ValueConstants.SELECT_TYPE_NICK_NAME){

			limitsize = MAX_NICK_LENGTH;
			//post_comment_edittext.setSingleLine();
			post_comment_edittext.setHint(getString(R.string.nick_limit));
			post_comment_edittext.setSingleLine();

		} else if (mSelectType == ValueConstants.SELECT_TYPE_SIGN) {
			limitsize = MAX_LENGTH_SIGN;
			post_comment_edittext.setHint(getString(R.string.declaration_limit));
		}

		post_comment_edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limitsize)});//设置最大长度

		//checkEditContent();
	}


	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable editable) {
			tv_max_size.setText(post_comment_edittext.getText().toString().length() + "/" + limitsize);
			if(post_comment_edittext.getText().toString().length() > 0){
				mClearImg.setVisibility(View.VISIBLE);
			}else {
				mClearImg.setVisibility(View.INVISIBLE);
			}
			changeButton();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	};

	/**
	 * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
	 * 
	 * @param c
	 * @return 返回字节个数
	 */
	private int calculateLength(CharSequence c) {
		int len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 1;
			} else {
				len += 2;
			}
		}
		return len;
	}

	private boolean mContentIsLeagal = true;

	/**
	 * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
	 */
	/*private void checkEditContent() {
		mContentIsLeagal = true;
//		int inputLen = getInputCount();
		int currentLength = post_comment_edittext.getText().toString().length();
		if (currentLength == 0) {
			mClearImg.setVisibility(View.GONE);
			mContentIsLeagal = false;
		} else {
			mClearImg.setVisibility(View.VISIBLE);
		}
		
		tv_hint_post_comment.setText(String.format(getString(R.string.feed_back_left_char),
				String.valueOf(MAX_NICK_LENGTH - currentLength)));

		
		if(post_comment_edittext.getText().toString().equals(mText)){
			mContentIsLeagal = false;
		}

		if(mSelectType == UserInfoUpdateActivity.SELECT_TYPE_USER_NAME){
			if (currentLength < MIN_NICK_LENGTH) {
				mTvErrorHint.setText(String.format(getString(R.string.error_nick_must_bigger_than_two),mTitle,MAX_NICK_LENGTH));
				mContentIsLeagal = false;
			} else if (MAX_NICK_LENGTH - currentLength < 0) {
				mTvErrorHint.setText(String.format(getString(R.string.error_must_litter_than_tweety),MAX_NICK_LENGTH,mTitle));
				mContentIsLeagal = false;
			} else {
				String content = post_comment_edittext.getText().toString();

				if(!RegexTool.isName(content)){
					mTvErrorHint.setText(getString(R.string.name_error));
				}

				*//*String REG = "^[a-zA-Z0-9\u4e00-\u9fa5_-]+$";
				if (!content.matches(REG)) { // 不符合规则
					mTvErrorHint.setText(getString(R.string.error_must_not_contain_spec_char));
					mContentIsLeagal = false;
				}*//*
			}
		} if(mSelectType == UserInfoUpdateActivity.SELECT_TYPE_NICK_NAME){
			if (currentLength < MIN_NICK_LENGTH) {
				mTvErrorHint.setText(String.format(getString(R.string.error_nick_must_bigger_than_two),mTitle,MAX_NICK_LENGTH));
				mContentIsLeagal = false;
			} else if (MAX_NICK_LENGTH - currentLength < 0) {
				mTvErrorHint.setText(String.format(getString(R.string.error_must_litter_than_tweety),MAX_NICK_LENGTH,mTitle));
				mContentIsLeagal = false;
			} else {
				String content = post_comment_edittext.getText().toString();

				String REG = "^[a-zA-Z0-9\u4e00-\u9fa5_-]+$";
				if (!content.matches(REG)) { // 不符合规则
					mTvErrorHint.setText(getString(R.string.error_must_not_contain_spec_char));
					mContentIsLeagal = false;
				}
			}
		} else if(mSelectType == UserInfoUpdateActivity.SELECT_TYPE_SIGN){
			if (currentLength < MIN_NICK_LENGTH) {
				mTvErrorHint.setText(String.format(getString(R.string.error_nick_must_bigger_than_two),mTitle,MAX_LENGTH_SIGN));
				mContentIsLeagal = false;
			} else if (MAX_LENGTH_SIGN - currentLength < 0) {
				mTvErrorHint.setText(String.format(getString(R.string.error_must_litter_than_tweety),MAX_LENGTH_SIGN,mTitle));
				mContentIsLeagal = false;
			} *//*else {
				String content = post_comment_edittext.getText().toString();

				String REG = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
				if (!content.matches(REG)) { // 不符合规则
					mTvErrorHint.setText(getString(R.string.error_must_not_contain_spec_char));
					mContentIsLeagal = false;
				}
			}*//*
		}


		if (mContentIsLeagal && mTvErrorHint != null) {
			mTvErrorHint.setText("");
		}
	}*/

	/**
	 * 获取用户输入的分享内容字数
	 * 
	 * @return
	 *//*
	private int getInputCount() {
		return calculateLength(post_comment_edittext.getText().toString());
	}*/

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_clear:
				post_comment_edittext.setText("");
				break;
		}
	}

	private void onfinish() {
		String edit = post_comment_edittext.getText().toString().trim();
		//姓名
		if(mSelectType == ValueConstants.SELECT_TYPE_USER_NAME){
			if(checkName(edit)){
				doPostComment();
			}
		}
		//昵称
		if(mSelectType == ValueConstants.SELECT_TYPE_NICK_NAME){
			if(checkNickName(edit)){
				doPostComment();
			}
		}
		//宣言
		if(mSelectType == ValueConstants.SELECT_TYPE_SIGN){
			if(checkDeclaration(edit)){
				doPostComment();
			}
		}

	}

	//检查宣言
	private boolean checkDeclaration(String edit) {
		if(edit.length() < 2 || edit.length() > 30){
			ToastUtil.showToast(this, getString(R.string.declaration_limit));
			return false;
		}
		return true;
	}

	//检查昵称
	private boolean checkNickName(String edit) {
		if(edit.length() < 2 || !RegexUtil.isNick(edit)){
			ToastUtil.showToast(this, getString(R.string.nick_limit));
			return false;
		}

		return true;
	}

	//检查姓名
	private boolean checkName(String edit) {
		if(edit.length() < 2){
			ToastUtil.showToast(this, getString(R.string.name_error_limit));
			return false;
		}

		if(!RegexUtil.isName(edit) || RegexUtil.isBeforOrEnd(edit)){
			ToastUtil.showToast(this, getString(R.string.name_error_limit));
			return false;
		}

		return true;
	}


}