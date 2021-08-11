package com.intcore.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class StatefulLayout extends FrameLayout {

    /* ---------------------------------------------------------------------------------------- */
    // Elements to be edited when customization is needed
    /* ---------------------------------------------------------------------------------------- */

    // 1- Add your state view layout file to statefulLayout\src\main\res\layout\

    // 2- Add your new state to StatefulLayout\src\main\res\values\attrs.xml

    // 3- Add your new state here with the same value used in attrs
    @Retention(SOURCE)
    @IntDef({STATE.IN_PROGRESS, STATE.CONNECTION_ERROR, STATE.UNKNOWN_ERROR, STATE.EMPTY_CONTENT, STATE.HAS_CONTENT})
    public @interface STATE {
        int IN_PROGRESS = 0;
        int CONNECTION_ERROR = 1;
        int UNKNOWN_ERROR = 2;
        int EMPTY_CONTENT = 3;
        int HAS_CONTENT = 4;
    }

    // 4- Add view object for your new state
    private View inProgressView;
    private View connectionErrorView;
    private View unKnownErrorView;
    private View emptyContentView;

    // 5- Add your state inflation to this method
    private void inflateLayouts() {
        inProgressView = (inProgressView == null) ? inflateAndAddLayout(R.layout.stf_layout_in_progress) : inProgressView;
        connectionErrorView = (connectionErrorView == null) ? inflateAndAddLayout(R.layout.stf_layout_connection_error) : connectionErrorView;
        unKnownErrorView = (unKnownErrorView == null) ? inflateAndAddLayout(R.layout.stf_layout_unknown_error) : unKnownErrorView;
        emptyContentView = (emptyContentView == null) ? inflateAndAddLayout(R.layout.stf_layout_empty) : emptyContentView;
    }

    // 6- Add your new state click listeners methods (if any)
    public interface ClickListener {
        void onRetryRefreshClicked();
    }

    // 7- Link your listener methods (if any) to the views click listeners
    private void initListenerCallbacks() {
        final View retryView = connectionErrorView.findViewById(R.id.stfRetryButton);
        if (retryView != null)
            retryView.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onRetryRefreshClicked();
            });
    }

    // 8- (Optional) -- Add pre-defined public methods that sets your new state using setState()

    public void showLoading(){
        setState(STATE.IN_PROGRESS);
    }

    public void showConnectionError() {
        if (acceptingConnectionErrors)
            setState(STATE.CONNECTION_ERROR);
    }

    public void showUnKnownError(){
        setState(STATE.UNKNOWN_ERROR);
    }

    public void showEmpty(){
        setState(STATE.EMPTY_CONTENT);
    }

    public void showContentAndLockConnectionErrors(){
        setAcceptingConnectionErrors(false);
        showContent();
    }

    public void showContent(){
        setState(STATE.HAS_CONTENT);
    }

    // 9- (Optional) -- Add methods for you state view customizing, which returns the requested inflated view.

    public View setCustomLoadingView(int layoutID) {
        removeView(inProgressView);
        inProgressView = inflateAndAddLayout(layoutID);
        return inProgressView;
    }

    public View setCustomConnectionErrorView(int layoutID) {
        removeView(connectionErrorView);
        connectionErrorView = inflateAndAddLayout(layoutID);
        return connectionErrorView;
    }

    public View setCustomUnknownErrorView(int layoutID) {
        removeView(unKnownErrorView);
        unKnownErrorView = inflateAndAddLayout(layoutID);
        return unKnownErrorView;
    }

    public View setCustomEmptyContentView(int layoutID) {
        removeView(emptyContentView);
        emptyContentView = inflateAndAddLayout(layoutID);
        return emptyContentView;
    }

    /* ---------------------------------------------------------------------------------------- */
    // Public Methods
    /* ---------------------------------------------------------------------------------------- */

    public StatefulLayout(Context context) {
        this(context, null);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.stfStatefulLayout, 0, 0);
        defaultState = array.getInt(R.styleable.stfStatefulLayout_stfDefaultState, DEFAULT_STATE);
        animationEnabled = array.getBoolean(R.styleable.stfStatefulLayout_stfAnimationEnabled, DEFAULT_ANIM_ENABLED);
        inAnimation = getAnimationRes(array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation, DEFAULT_IN_ANIM));
        outAnimation = getAnimationRes(array.getResourceId(R.styleable.stfStatefulLayout_stfOutAnimation, DEFAULT_OUT_ANIM));
        array.recycle();
    }

    @STATE
    public int getState() {
        return currentState;
    }

    public int getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(int defaultState) {
        this.defaultState = defaultState;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    public Animation getInAnimation() {
        return inAnimation;
    }

    public void setInAnimation(Animation animation) {
        inAnimation = animation;
    }

    public void setInAnimation(@AnimRes int anim) {
        inAnimation = getAnimationRes(anim);
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(Animation animation) {
        outAnimation = animation;
    }

    public void setOutAnimation(@AnimRes int anim) {
        outAnimation = getAnimationRes(anim);
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    @Nullable
    public void setClickListener(@Nullable ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public boolean isAcceptingConnectionErrors() {
        return acceptingConnectionErrors;
    }

    public void setAcceptingConnectionErrors(boolean acceptingConnectionErrors) {
        this.acceptingConnectionErrors = acceptingConnectionErrors;
    }

    public void resetLayoutBehavior(){
        this.acceptingConnectionErrors = true;
    }

    /* ---------------------------------------------------------------------------------------- */
    // Private Methods and objects
    /* ---------------------------------------------------------------------------------------- */


    private static final String MSG_ONE_CHILD = "StatefulLayout must have only one child!";
    private static final int DEFAULT_STATE = STATE.HAS_CONTENT;
    private static final boolean DEFAULT_ANIM_ENABLED = false;
    private static final int DEFAULT_IN_ANIM = android.R.anim.fade_in;
    private static final int DEFAULT_OUT_ANIM = android.R.anim.fade_out;

    @STATE
    private int defaultState;
    @STATE
    private int currentState;
    private boolean acceptingConnectionErrors = true;
    private View contentView;
    private boolean animationEnabled;
    private Animation inAnimation;
    private Animation outAnimation;
    private int animCounter;
    private ClickListener clickListener;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1) throw new IllegalStateException(MSG_ONE_CHILD);

        if (isInEditMode()) return; // hide state views in designer

        contentView = getChildAt(0);
        if (contentView == null)
            contentView = inflateAndAddLayout(R.layout.stf_layout_content);
        else
            contentView.setVisibility(GONE);

        inflateLayouts();
        initListenerCallbacks();

        setState(defaultState);
    }

    // Inflate the layout, and add it as a child to the root view as an invisible layout.
    // returns the inflated layout
    private View inflateAndAddLayout(@LayoutRes int layoutID) {
        View view = LayoutInflater.from(getContext()).inflate(layoutID, null, false);
        view.setVisibility(GONE);
        addView(view);
        return view;
    }

    // Return the first visible layout
    // Note, there will always be one visible layout at a time
    private View findTheVisibleLayout() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null && view.getVisibility() == VISIBLE)
                return view;
        }
        return null;
    }

    private void setState(@STATE int state) {
        View inView = null;
        switch (state) {
            case STATE.IN_PROGRESS:
                inView = inProgressView;
                break;
            case STATE.CONNECTION_ERROR:
                inView = connectionErrorView;
                break;
            case STATE.UNKNOWN_ERROR:
                inView = unKnownErrorView;
                break;
            case STATE.EMPTY_CONTENT:
                inView = emptyContentView;
                break;
            case STATE.HAS_CONTENT:
                inView = contentView;
                break;
        }

        View outView = findTheVisibleLayout();

        if (inView == null || inView.equals(outView))
            return;

        replaceViews(inView, outView);
        this.currentState = state;
    }

    private void replaceViews(@NonNull View inView, @Nullable View outView) {

        if (outView == null) {
            inView.setVisibility(VISIBLE);
            return;
        }

        if (isAnimationEnabled()) {
            inView.clearAnimation();
            outView.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (outView.getVisibility() == VISIBLE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounter != animCounterCopy) return;
                        outView.setVisibility(GONE);
                        inView.setVisibility(VISIBLE);
                        inView.startAnimation(inAnimation);
                    }
                });
                outView.startAnimation(outAnimation);
            }
        } else {
            outView.setVisibility(GONE);
            inView.setVisibility(VISIBLE);
        }
    }

    private Animation getAnimationRes(@AnimRes int resId) {
        return AnimationUtils.loadAnimation(getContext(), resId);
    }

}
