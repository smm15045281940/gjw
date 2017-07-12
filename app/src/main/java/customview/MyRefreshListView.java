package customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gangjianwang.www.gangjianwang.R;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class MyRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private View headView;
    private ImageView headImageView;
    private TextView headTextView;
    private int headViewHeight;//头布局高度
    private ObjectAnimator animator;//刷新动画

    private View footView;
    private ImageView footImageView;
    private int footViewHeight;
    private ObjectAnimator footAnimator;
    private boolean isScrolltoBottom;
    private boolean isLoadingMore;

    private int firstVisibleItemPosition;//屏幕显示在第一个的item的索引
    private int downY;//按下时Y轴的偏移量

    private final int DOWN_PULL_REFRESH = 0;
    private final int RELEASE_REFRESH = 1;
    private final int REFRESHING = 2;
    private int currentState = DOWN_PULL_REFRESH;

    private OnRefreshListener mOnRefreshListener;

    public MyRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadView();//初始化头布局
        initFootView();
        initAnim();//初始化动画
        this.setOnScrollListener(this);
    }

    private void initHeadView() {
        headView = View.inflate(getContext(), R.layout.head_myrefreshlistview, null);
        headImageView = (ImageView) headView.findViewById(R.id.iv_head_myrefreshlistview);
        headTextView = (TextView) headView.findViewById(R.id.tv_head_myrefreshlistview);
        headView.measure(0, 0);//系统测量headView高度
        headViewHeight = headView.getMeasuredHeight();
        headView.setPadding(0, -headViewHeight, 0, 0);
        this.addHeaderView(headView);
    }

    public void hideHeadView() {
        animator.cancel();
        headView.setPadding(0, -headViewHeight, 0, 0);
        headTextView.setText("下拉可刷新");
        headImageView.setImageResource(R.mipmap.img_default);
        currentState = DOWN_PULL_REFRESH;
    }

    private void initFootView() {
        footView = View.inflate(getContext(), R.layout.foot_myrefreshlistview, null);
        footImageView = (ImageView) footView.findViewById(R.id.iv_foot_myrefreshlistview);
        footView.measure(0, 0);
        footViewHeight = footView.getMeasuredHeight();
        footView.setPadding(0, -footViewHeight, 0, 0);
        this.addFooterView(footView);
    }

    public void hideFootView() {
        footAnimator.cancel();
        footView.setPadding(0, -footViewHeight, 0, 0);
        footImageView.setImageResource(R.mipmap.img_default);
        isLoadingMore = false;
    }

    private void initAnim() {
        animator = ObjectAnimator.ofFloat(headImageView, "rotation", 0.0F, 359.0F);
        animator.setDuration(500);
        animator.setRepeatCount(-1);

        footAnimator = ObjectAnimator.ofFloat(footImageView, "rotation", 0.0F, 359.0F);
        footAnimator.setDuration(500);
        footAnimator.setRepeatCount(-1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int diff = (moveY - downY) / 2;//间距
                int paddingTop = -headViewHeight + diff;
                if (firstVisibleItemPosition == 0 && -headViewHeight < paddingTop) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) {//完全显示
                        currentState = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0 && currentState == RELEASE_REFRESH) {//部分显示
                        currentState = DOWN_PULL_REFRESH;
                        refreshHeaderView();
                    }
                    headView.setPadding(0, paddingTop, 0, 0);//下拉头布局
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState == RELEASE_REFRESH) {
                    headView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    refreshHeaderView();
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onDownPullRefresh();
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    headView.setPadding(0, -headViewHeight, 0, 0);//隐藏头布局
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH://下拉刷新状态
                headTextView.setText("下拉可刷新");
                break;
            case RELEASE_REFRESH://松开刷新状态
                headTextView.setText("松开以刷新");
                break;
            case REFRESHING://正在刷新状态
                animator.start();
                headTextView.setText("正在刷新");
                break;
            default:
                break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (isScrolltoBottom && !isLoadingMore) {
                isLoadingMore = true;
                footView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());
                if (mOnRefreshListener != null) {
                    footAnimator.start();
                    mOnRefreshListener.onLoadingMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrolltoBottom = true;
        } else {
            isScrolltoBottom = false;
        }
    }
}
