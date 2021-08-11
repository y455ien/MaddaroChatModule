package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.devlomi.record_view.OnRecordActionListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.example.maddarochatmodule.R;

public class CustomMessageInput extends ConstraintLayout
        implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    protected ConstraintLayout clTextInput;
    protected ConstraintLayout clVoiceInput;
    protected EditText messageInput;
    protected ImageView messageSendButton;
    protected ImageView attachmentButton;
    protected RecordButton recordButton;
    protected RecordView recordView;

    private CharSequence input;

    private InputListener inputListener;
    private AttachmentsListener attachmentsListener;
    private RecordPermissionListener recordPermissionListener;
    private OnRecordActionListener onRecordActionListener;

    private boolean isTyping;
    private TypingListener typingListener;
    private int delayTypingStatusMillis = 1500;
    private boolean lastFocus;
    private Runnable typingTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTyping) {
                isTyping = false;
                if (typingListener != null) typingListener.onStopTyping();
            }
        }
    };

    private boolean isRecordPermissionGranted = false;

    // constructors

    public CustomMessageInput(Context context) {
        super(context);
        init(context);
    }

    public CustomMessageInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMessageInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // private initiators

    private void init(Context context) {
        inflate(context, R.layout.layout_chat_message_input, this);

        clTextInput = findViewById(R.id.cl_text_input);
        clVoiceInput = findViewById(R.id.cl_voice_input);
        messageInput = findViewById(R.id.et_message_input);
        messageSendButton = findViewById(R.id.iv_send);
        attachmentButton = findViewById(R.id.iv_add_media);
        recordButton = findViewById(R.id.record_button);
        recordView = findViewById(R.id.record_view);

        messageSendButton.setOnClickListener(this);
        attachmentButton.setOnClickListener(this);
        messageInput.addTextChangedListener(this);
        messageInput.setText("");
        messageInput.setOnFocusChangeListener(this);

        initRecordView();
    }

    private void initRecordView() {
        recordButton.setRecordView(recordView);

        recordButton.setRecordActionListeningEnabled(isRecordPermissionGranted);
        recordButton.setOnRecordButtonClickListener(v -> {
            if(recordPermissionListener != null)
                recordPermissionListener.onRequestPermission();
        });

        recordView.setSlideToCancelTextColor(getContext().getResources().getColor(R.color.textBlue));
        recordView.setSlideToCancelArrowColor(getContext().getResources().getColor(R.color.textBlue));
        recordView.setCounterTimeColor(getContext().getResources().getColor(R.color.errorColor));
        recordView.setOnRecordActionListener(new OnRecordActionListener() {
            @Override
            public void onStart() {
                clTextInput.setVisibility(INVISIBLE);
                if (onRecordActionListener != null)
                    onRecordActionListener.onStart();
            }

            @Override
            public void onCancel() {
                if (onRecordActionListener != null)
                    onRecordActionListener.onCancel();
            }

            @Override
            public void onMaxDurationReached() {
                if (onRecordActionListener != null)
                    onRecordActionListener.onMaxDurationReached();
            }

            @Override
            public void onFinish(long recordTime) {
                clTextInput.setVisibility(VISIBLE);
                if (onRecordActionListener != null)
                    onRecordActionListener.onFinish(recordTime);
            }

            @Override
            public void onLessThanMinimumDuration() {
                clTextInput.setVisibility(VISIBLE);
                if (onRecordActionListener != null)
                    onRecordActionListener.onLessThanMinimumDuration();
            }

        });
        recordView.setOnBasketAnimationEndListener(() -> clTextInput.setVisibility(VISIBLE));
    }

    // public setters

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public void setAttachmentsListener(AttachmentsListener attachmentsListener) {
        this.attachmentsListener = attachmentsListener;
    }

    public void setTypingListener(TypingListener typingListener) {
        this.typingListener = typingListener;
    }

    public void setRecordPermissionListener(RecordPermissionListener recordPermissionListener) {
        this.recordPermissionListener = recordPermissionListener;
    }

    public void setOnRecordActionListener(OnRecordActionListener onRecordActionListener) {
        this.onRecordActionListener = onRecordActionListener;
    }

    public void setDelayTypingStatusMillis(int delayTypingStatusMillis) {
        this.delayTypingStatusMillis = delayTypingStatusMillis;
    }

    public void setRecordDurationBoundsInSeconds(int minDuration, int maxDuration){
        recordView.setRecordDurationBoundsInSeconds(minDuration, maxDuration);
    }

    public void setRecordPermissionGranted(boolean isRecordPermissionGranted) {
        this.isRecordPermissionGranted = isRecordPermissionGranted;
        recordButton.setRecordActionListeningEnabled(isRecordPermissionGranted);
    }

    public void clearInputEditText(){
        messageInput.setText("");
    }

    // callbacks

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        input = s;
        clVoiceInput.setVisibility(input.length() > 0 ? INVISIBLE : VISIBLE);
        if (s.length() > 0) {
            if (!isTyping) {
                isTyping = true;
                if (typingListener != null) typingListener.onStartTyping();
            }
            removeCallbacks(typingTimerRunnable);
            postDelayed(typingTimerRunnable, delayTypingStatusMillis);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_send) {
            boolean isSubmitted = onSubmit();
            if (isSubmitted) {
                messageInput.setText("");
            }
            removeCallbacks(typingTimerRunnable);
            post(typingTimerRunnable);
        } else if (id == R.id.iv_add_media) {
            onAddAttachments();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (lastFocus && !hasFocus && typingListener != null) {
            typingListener.onStopTyping();
        }
        lastFocus = hasFocus;
    }

    private boolean onSubmit() {
        return inputListener != null && inputListener.onSubmit(input);
    }

    private void onAddAttachments() {
        if (attachmentsListener != null) attachmentsListener.onAddAttachments();
    }

    // Listeners

    public interface InputListener {
        boolean onSubmit(CharSequence input);
    }

    public interface AttachmentsListener {
        void onAddAttachments();
    }

    public interface TypingListener {
        void onStartTyping();

        void onStopTyping();
    }

    public interface RecordPermissionListener {
        void onRequestPermission();
    }

}

