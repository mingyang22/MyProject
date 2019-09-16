package baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yangming.myproject.R;


/**
 * @author yangming on 2018/11/14
 * 打造主流item布局样式
 */
public class SuperTextView extends RelativeLayout {

    private Context mContext;

    private TextView leftTextView, centerTextView, rightTextView;
    private LayoutParams leftTextViewParams, centerTextViewParams, rightTextViewParams;

    private ImageView leftIconIV, rightIconIV;
    private LayoutParams leftImgParams, rightImgParams;

    private int leftIconWidth;//左边图标的宽
    private int leftIconHeight;//左边图标的高

    private int rightIconWidth;//右边图标的宽
    private int rightIconHeight;//右边图标的高

    private int leftIconMarginLeft;//左边图标的左边距
    private int rightIconMarginRight;//右边图标的右边距

    private Drawable leftIconRes;//左边图标资源
    private Drawable rightIconRes;//右边图标资源

    private int defaultColor = 0xFF666666;//文字默认颜色
    private int defaultSize = 15;//默认字体大小
    private int defaultMargin = 15;
    private int centerDefaultMargin = 5;
    private int defaultGravity = 1;

    private String mLeftTextString;
    private String mCenterTextString;
    private String mRightTextString;
    private String mRightHintString;

    private int mLeftTextColor;
    private int mCenterTextColor;
    private int mRightTextColor;
    private int mRightHintColor;

    private int mLeftTextSize;
    private int mCenterTextSize;
    private int mRightTextSize;

    private int mCenterTextGravity;
    private static final int textGravityLeft = 0;
    private static final int textGravityCenter = 1;
    private static final int textGravityRight = 2;

    private int mLeftViewMarginLeft;
    private int mRightViewMarginRight;
    private int mCenterViewMarginLeft;
    private int mCenterViewMarginRight;

    private View topDividerLineView, bottomDividerLineView;

    private LayoutParams topDividerLineParams, bottomDividerLineParams;
    private int mTopDividerLineMarginLeft;
    private int mTopDividerLineMarginRight;

    private int mBottomDividerLineMarginLeft;
    private int mBottomDividerLineMarginRight;

    private int mDividerLineType;
    private int mDividerLineColor;
    private int mDividerLineHeight;

    private int mDefaultDividerLineColor = 0xFFDADFED;//分割线默认颜色

    /**
     * 分割线的类型
     */
    private static final int NONE = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int BOTH = 3;
    private static final int defaultDivider = NONE;

    private OnSuperTextViewClickListener superTextViewClickListener;
    private OnLeftTvClickListener leftTvClickListener;
    private OnCenterTvClickListener centerTvClickListener;
    private OnRightTvClickListener rightTvClickListener;
    private OnLeftImageViewClickListener leftImageViewClickListener;
    private OnRightImageViewClickListener rightImageViewClickListener;

    public SuperTextView(Context context) {
        this(context, null);
    }

    public SuperTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        defaultSize = sp2px(context, defaultSize);
        defaultMargin = dip2px(context, defaultMargin);

        getAttr(attrs);
        initView();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SuperTextView);

        mLeftTextString = typedArray.getString(R.styleable.SuperTextView_sLeftTextString);
        mCenterTextString = typedArray.getString(R.styleable.SuperTextView_sCenterTextString);
        mRightTextString = typedArray.getString(R.styleable.SuperTextView_sRightTextString);

        mRightHintString = typedArray.getString(R.styleable.SuperTextView_sRightHintString);
        mRightHintColor = typedArray.getColor(R.styleable.SuperTextView_sRightHintColor, defaultColor);

        mLeftTextColor = typedArray.getColor(R.styleable.SuperTextView_sLeftTextColor, defaultColor);
        mCenterTextColor = typedArray.getColor(R.styleable.SuperTextView_sCenterTextColor, defaultColor);
        mRightTextColor = typedArray.getColor(R.styleable.SuperTextView_sRightTextColor, defaultColor);

        mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sLeftTextSize, defaultSize);
        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sCenterTextSize, defaultSize);
        mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sRightTextSize, defaultSize);

        mCenterTextGravity = typedArray.getInt(R.styleable.SuperTextView_sCenterTextGravity, defaultGravity);
        mCenterViewMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sCenterViewMarginLeft, centerDefaultMargin);
        mCenterViewMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sCenterViewMarginRight, centerDefaultMargin);

        mLeftViewMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sLeftViewMarginLeft, defaultMargin);
        mRightViewMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sRightViewMarginRight, defaultMargin);

        leftIconWidth = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sLeftIconWidth, 0);
        leftIconHeight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sLeftIconHeight, 0);

        rightIconWidth = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sRightIconWidth, 0);
        rightIconHeight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sRightIconHeight, 0);

        leftIconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sLeftIconMarginLeft, defaultMargin);
        rightIconMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sRightIconMarginRight, defaultMargin);

        leftIconRes = typedArray.getDrawable(R.styleable.SuperTextView_sLeftIconRes);
        rightIconRes = typedArray.getDrawable(R.styleable.SuperTextView_sRightIconRes);

        mTopDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sTopDividerLineMarginLeft, 0);
        mTopDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sTopDividerLineMarginRight, 0);

        mBottomDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sBottomDividerLineMarginLeft, 0);
        mBottomDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sBottomDividerLineMarginRight, 0);

        mDividerLineType = typedArray.getInt(R.styleable.SuperTextView_sDividerLineType, defaultDivider);
        mDividerLineColor = typedArray.getColor(R.styleable.SuperTextView_sDividerLineColor, mDefaultDividerLineColor);
        mDividerLineHeight = typedArray.getDimensionPixelSize(R.styleable.SuperTextView_sDividerLineHeight, dip2px(mContext, 0.5f));


        typedArray.recycle();
    }

    /**
     * 初始化Params
     *
     * @param params params
     * @return params
     */
    private LayoutParams getParams(LayoutParams params) {
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        return params;
    }

    private void initView() {
        initLeftIcon();
        initRightIcon();

        initLeftTextView();
        initCenterTextView();
        initRightTextView();

        initDividerLineView();
    }

    /**
     * 初始化左边图标
     */
    private void initLeftIcon() {
        if (leftIconIV == null) {
            leftIconIV = initImageView(R.id.sLeftImgId);
        }
        leftImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftImgParams.addRule(RelativeLayout.ALIGN_PARENT_START, TRUE);
        leftImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        if (leftIconHeight != 0 && leftIconWidth != 0) {
            leftImgParams.width = leftIconWidth;
            leftImgParams.height = leftIconHeight;
        }
        leftIconIV.setLayoutParams(leftImgParams);
        if (leftIconRes != null) {
            leftImgParams.setMargins(leftIconMarginLeft, 0, 0, 0);
            leftIconIV.setImageDrawable(leftIconRes);
        }
        addView(leftIconIV);
    }

    /**
     * 初始化右边图标
     */
    private void initRightIcon() {
        if (rightIconIV == null) {
            rightIconIV = initImageView(R.id.sRightImgId);
        }
        rightImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        rightImgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        if (rightIconHeight != 0 && rightIconWidth != 0) {
            rightImgParams.width = rightIconWidth;
            rightImgParams.height = rightIconHeight;
        }
        rightIconIV.setLayoutParams(rightImgParams);
        if (rightIconRes != null) {
            rightImgParams.setMargins(0, 0, rightIconMarginRight, 0);
            rightIconIV.setImageDrawable(rightIconRes);
        }
        addView(rightIconIV);
    }

    /**
     * 初始化LeftTextView
     */
    private void initLeftTextView() {
        if (leftTextView == null) {
            leftTextView = initTextView(R.id.sLeftViewId);
        }
        leftTextViewParams = getParams(leftTextViewParams);
        leftTextViewParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftImgId);
        leftTextViewParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        leftTextViewParams.setMargins(mLeftViewMarginLeft, 0, 0, 0);

        leftTextView.setLayoutParams(leftTextViewParams);
        leftTextView.setTextColor(mLeftTextColor);
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        leftTextView.setText(mLeftTextString);
        setDefaultGravity(leftTextView, defaultGravity);

        addView(leftTextView);
    }

    /**
     * 初始化CenterTextView
     */
    private void initCenterTextView() {
        if (centerTextView == null) {
            centerTextView = initTextView(R.id.sCenterViewId);
        }
        centerTextViewParams = getParams(centerTextViewParams);
        centerTextViewParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        centerTextViewParams.addRule(RIGHT_OF, R.id.sLeftViewId);
        centerTextViewParams.addRule(LEFT_OF, R.id.sRightViewId);
        centerTextViewParams.setMargins(mCenterViewMarginLeft, 0, mCenterViewMarginRight, 0);

        centerTextView.setLayoutParams(centerTextViewParams);
        centerTextView.setTextColor(mCenterTextColor);
        centerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCenterTextSize);
        centerTextView.setText(mCenterTextString);
        setDefaultGravity(centerTextView, mCenterTextGravity);

        addView(centerTextView);
    }

    /**
     * 初始化RightTextView
     */
    private void initRightTextView() {
        if (rightTextView == null) {
            rightTextView = initTextView(R.id.sRightViewId);
        }
        rightTextViewParams = getParams(rightTextViewParams);
        rightTextViewParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);

        rightTextViewParams.addRule(RelativeLayout.LEFT_OF, R.id.sRightImgId);
        rightTextViewParams.setMargins(0, 0, mRightViewMarginRight, 0);

        rightTextView.setLayoutParams(rightTextViewParams);
        rightTextView.setTextColor(mRightTextColor);
        rightTextView.setHintTextColor(mRightHintColor);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        rightTextView.setHint(mRightHintString);
        rightTextView.setText(mRightTextString);
        setDefaultGravity(rightTextView, defaultGravity);

        addView(rightTextView);
    }

    /**
     * 初始化分割线
     */
    private void initDividerLineView() {
        switch (mDividerLineType) {
            case NONE:
                break;
            case TOP:
                initTopDividerLineView(mTopDividerLineMarginLeft, mTopDividerLineMarginRight);
                break;
            case BOTTOM:
                initBottomDividerLineView(mBottomDividerLineMarginLeft, mBottomDividerLineMarginRight);
                break;
            case BOTH:
                initTopDividerLineView(mTopDividerLineMarginLeft, mTopDividerLineMarginRight);
                initBottomDividerLineView(mBottomDividerLineMarginLeft, mBottomDividerLineMarginRight);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化上边分割线view
     *
     * @param marginLeft  左间距
     * @param marginRight 右间距
     */
    private void initTopDividerLineView(int marginLeft, int marginRight) {
        if (topDividerLineView == null) {
            if (topDividerLineParams == null) {
                topDividerLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            topDividerLineParams.addRule(ALIGN_PARENT_TOP, TRUE);
            topDividerLineParams.setMargins(marginLeft, 0, marginRight, 0);
            topDividerLineView = new View(mContext);
            topDividerLineView.setLayoutParams(topDividerLineParams);
            topDividerLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(topDividerLineView);
    }

    /**
     * 初始化底部分割线view
     *
     * @param marginLeft  左间距
     * @param marginRight 右间距
     */
    private void initBottomDividerLineView(int marginLeft, int marginRight) {
        if (bottomDividerLineView == null) {
            if (bottomDividerLineParams == null) {
                bottomDividerLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            bottomDividerLineParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            bottomDividerLineParams.setMargins(marginLeft, 0, marginRight, 0);

            bottomDividerLineView = new View(mContext);
            bottomDividerLineView.setLayoutParams(bottomDividerLineParams);
            bottomDividerLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(bottomDividerLineView);
    }

    /**
     * 初始化TextView
     *
     * @param id id
     * @return TextView
     */
    private TextView initTextView(int id) {
        TextView textView = new TextView(mContext);
        textView.setId(id);
        return textView;
    }

    /**
     * 初始化ImageView
     *
     * @param id id
     * @return ImageView
     */
    private ImageView initImageView(int id) {
        ImageView imageView = new ImageView(mContext);
        imageView.setId(id);
        return imageView;
    }

    /**
     * 设置文字对其方式
     *
     * @param textView textView
     * @param gravity  对其方式
     */
    private void setDefaultGravity(TextView textView, int gravity) {
        if (textView != null) {
            switch (gravity) {
                case textGravityLeft:
                    textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    break;
                case textGravityCenter:
                    textView.setGravity(Gravity.CENTER);
                    break;
                case textGravityRight:
                    textView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置左字符串
     *
     * @param string 字符串
     * @return 方便链式调用
     */
    public SuperTextView setLeftString(CharSequence string) {
        if (leftTextView != null) {
            leftTextView.setText(string);
        }
        return this;
    }

    /**
     * 设置中字符串
     *
     * @param string 字符串
     * @return 方便链式调用
     */
    public SuperTextView setCenterString(CharSequence string) {
        if (centerTextView != null) {
            centerTextView.setText(string);
        }
        return this;
    }

    /**
     * 设置右字符串
     *
     * @param string 字符串
     * @return 方便链式调用
     */
    public SuperTextView setRightString(CharSequence string) {
        if (rightTextView != null) {
            rightTextView.setText(string);
        }
        return this;
    }

    /**
     * 设置左文字颜色
     *
     * @param color 颜色值
     * @return SuperTextView
     */
    public SuperTextView setLeftTextColor(int color) {
        if (leftTextView != null) {
            leftTextView.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置中文字颜色
     *
     * @param color 颜色值
     * @return SuperTextView
     */
    public SuperTextView setCenterTextColor(int color) {
        if (centerTextView != null) {
            centerTextView.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置右文字颜色
     *
     * @param color 颜色值
     * @return SuperTextView
     */
    public SuperTextView setRightTextColor(int color) {
        if (rightTextView != null) {
            rightTextView.setTextColor(color);
        }
        return this;
    }

    /**
     * 获取左字符串
     *
     * @return 返回字符串
     */
    public String getLeftString() {
        return leftTextView != null ? leftTextView.getText().toString().trim() : "";
    }

    /**
     * 获取中字符串
     *
     * @return 返回字符串
     */
    public String getCenterString() {
        return centerTextView != null ? centerTextView.getText().toString().trim() : "";
    }

    /**
     * 获取右字符串
     *
     * @return 返回字符串
     */
    public String getRightString() {
        return rightTextView != null ? rightTextView.getText().toString().trim() : "";
    }

    /**
     * 获取左边ImageView
     *
     * @return ImageView
     */
    public ImageView getLeftIconIV() {
        return leftIconIV;
    }

    /**
     * 获取右边ImageView
     *
     * @return ImageView
     */
    public ImageView getRightIconIV() {
        return rightIconIV;
    }

    /**
     * 设置左边图标
     *
     * @param leftIcon 左边图标
     * @return 返回对象
     */
    public SuperTextView setLeftIcon(Drawable leftIcon) {
        if (leftIconIV != null) {
            leftImgParams.setMargins(leftIconMarginLeft, 0, 0, 0);
            leftIconIV.setImageDrawable(leftIcon);
        }
        return this;
    }

    /**
     * 设置左边图标
     *
     * @param resId 左边图标资源id
     * @return 返回对象
     */
    public SuperTextView setLeftIcon(int resId) {
        if (leftIconIV != null) {
            leftImgParams.setMargins(leftIconMarginLeft, 0, 0, 0);
            leftIconIV.setImageResource(resId);
        }
        return this;
    }

    /**
     * 设置右边图标
     *
     * @param rightIcon 右边图标
     * @return 返回对象
     */
    public SuperTextView setRightIcon(Drawable rightIcon) {
        if (rightIconIV != null) {
            rightImgParams.setMargins(0, 0, rightIconMarginRight, 0);
            rightIconIV.setImageDrawable(rightIcon);
        }
        return this;
    }

    /**
     * 设置右边图标资源Id
     *
     * @param resId 右边图标
     * @return 返回对象
     */
    public SuperTextView setRightIcon(int resId) {
        if (rightIconIV != null) {
            rightImgParams.setMargins(0, 0, rightIconMarginRight, 0);
            rightIconIV.setImageResource(resId);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param drawable 背景资源
     * @return 对象
     */
    public SuperTextView setSBackground(Drawable drawable) {
        if (drawable != null) {
            this.setBackground(drawable);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param resId 背景资源Id
     * @return 对象
     */
    public SuperTextView setSBackground(int resId) {
        if (resId != 0) {
            this.setBackgroundResource(resId);
        }
        return this;
    }

    /**
     * 设置上边分割线显示状态
     *
     * @param visibility visibility
     * @return superTextView
     */
    public SuperTextView setTopDividerLineVisibility(int visibility) {
        if (topDividerLineView == null) {
            initTopDividerLineView(mTopDividerLineMarginLeft, mTopDividerLineMarginRight);
        }
        topDividerLineView.setVisibility(visibility);
        return this;
    }

    /**
     * 设置下边分割线显示状态
     *
     * @param visibility visibility
     * @return superTextView
     */
    public SuperTextView setBottomDividerLineVisibility(int visibility) {
        if (bottomDividerLineView == null) {
            initBottomDividerLineView(mBottomDividerLineMarginLeft, mBottomDividerLineMarginRight);
        }
        bottomDividerLineView.setVisibility(visibility);
        return this;
    }

    /**
     * 获取左的TextView
     *
     * @return textView
     */
    public TextView getLeftTextView() {
        return leftTextView;
    }

    /**
     * 获取中的TextView
     *
     * @return textView
     */
    public TextView getCenterTextView() {
        return centerTextView;
    }

    /**
     * 获取右的TextView
     *
     * @return textView
     */
    public TextView getRightTextView() {
        return rightTextView;
    }

    /**
     * 点击事件
     *
     * @param onSuperTextViewClickListener ClickListener
     * @return SuperTextView
     */
    public SuperTextView setOnSuperTextViewClickListener(OnSuperTextViewClickListener onSuperTextViewClickListener) {
        this.superTextViewClickListener = onSuperTextViewClickListener;
        setOnClickListener(v -> {
            if (superTextViewClickListener != null) {
                superTextViewClickListener.onClickListener(SuperTextView.this);
            }
        });
        return this;
    }

    public SuperTextView setLeftTvClickListener(OnLeftTvClickListener leftTvClickListener) {
        this.leftTvClickListener = leftTvClickListener;
        if (leftTextView != null && leftTvClickListener != null) {
            leftTextView.setOnClickListener(v -> leftTvClickListener.onClickListener());
        }
        return this;
    }

    public SuperTextView setCenterTvClickListener(OnCenterTvClickListener centerTvClickListener) {
        this.centerTvClickListener = centerTvClickListener;
        if (centerTextView != null && centerTvClickListener != null) {
            centerTextView.setOnClickListener(v -> centerTvClickListener.onClickListener());
        }
        return this;
    }

    public SuperTextView setRightTvClickListener(OnRightTvClickListener rightTvClickListener) {
        this.rightTvClickListener = rightTvClickListener;
        if (rightTextView != null && rightTvClickListener != null) {
            rightTextView.setOnClickListener(v -> rightTvClickListener.onClickListener());
        }
        return this;
    }

    public SuperTextView setLeftImageViewClickListener(OnLeftImageViewClickListener listener) {
        this.leftImageViewClickListener = listener;

        if (leftIconIV != null) {
            leftIconIV.setOnClickListener(v -> leftImageViewClickListener.onClickListener(leftIconIV));
        }
        return this;
    }

    public SuperTextView setRightImageViewClickListener(final OnRightImageViewClickListener listener) {
        this.rightImageViewClickListener = listener;
        if (rightIconIV != null) {
            rightIconIV.setOnClickListener(v -> rightImageViewClickListener.onClickListener(rightIconIV));
        }
        return this;
    }

    /**
     * 点击接口
     */
    public interface OnSuperTextViewClickListener {
        void onClickListener(SuperTextView superTextView);
    }

    public interface OnLeftTvClickListener {
        void onClickListener();
    }

    public interface OnCenterTvClickListener {
        void onClickListener();
    }

    public interface OnRightTvClickListener {
        void onClickListener();
    }

    public interface OnLeftImageViewClickListener {
        void onClickListener(ImageView imageView);
    }

    public interface OnRightImageViewClickListener {
        void onClickListener(ImageView imageView);
    }

    /**
     * 单位转换工具类
     *
     * @param context 上下文对象
     * @param spValue 值
     * @return 返回值
     */
    private int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 单位转换工具类
     *
     * @param context  上下文对象
     * @param dipValue 值
     * @return 返回值
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
