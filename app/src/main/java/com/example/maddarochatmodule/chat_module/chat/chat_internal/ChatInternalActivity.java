package com.example.maddarochatmodule.chat_module.chat.chat_internal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.devlomi.record_view.OnRecordActionListener;
import com.example.maddarochatmodule.MyApp;
import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.base.BaseActivity;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.CustomMessageInput;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.CustomMessagesListAdapter;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.document_messages.CustomIncomingDocumentMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.document_messages.CustomOutcomingDocumentMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.document_messages.DocumentMessageListener;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.image_messages.CustomIncomingImageMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.image_messages.CustomOutcomingImageMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.text_messages.CustomIncomingTextMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.text_messages.CustomOutcomingTextMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.voice_messages.CustomIncomingVoiceMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.voice_messages.CustomOutcomingVoiceMessageViewHolder;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.voice_messages.VoiceMessagesListener;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.document_view.DocumentViewActivity;
import com.example.maddarochatmodule.data_model.ChatBuddyModel;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.data_model.annotation.MessageType;
import com.example.maddarochatmodule.util.PicassoHelper;
import com.example.maddarochatmodule.util.StringUtils;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.intcore.statefullayout.StatefulLayout;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.miguelbcr.ui.rx_paparazzo2.entities.Response;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatInternalActivity extends BaseActivity
        implements VoiceMessagesListener, DocumentMessageListener {

    private static final String DATA = "DATA";

    public static Intent getChatInternalActivity(Context context, ChatRoomModel model) {
        Intent intent = new Intent(context, ChatInternalActivity.class);
        intent.putExtra(DATA, model);
        return intent;
    }

    @BindView(R.id.layout_stateful)
    StatefulLayout statefulLayout;
    @BindView(R.id.rv_messages_list)
    MessagesList rvMessagesList;
    @BindView(R.id.pb_loadMore)
    ProgressBar pbLoadMore;
    @BindView(R.id.layout_message_input)
    CustomMessageInput layoutMessageInput;
    @BindView(R.id.attachment_sheet)
    View attachmentSheet;
    @BindView(R.id.tv_conversation_ended)
    TextView tvConversationEnded;
    @BindView(R.id.view_pending_send_status)
    View viewPendingSendStatus;

    @Inject
    UserPref userPref;

    private ChatInternalViewModel viewModel;

    private CustomMessagesListAdapter adapter;
    private ChatRoomModel currentRoom;
    private ChatBuddyModel currentUser, currentBuddy;

    private AudioRecorder audioRecorder;
    private RxAudioPlayer audioPlayer;
    private RxPermissions rxPermissions;
    private File recordDirectory, lastRecordedAudioFile;
    private long lastRecordDuration;
    private boolean hasMaxDurationLimitPendingRecord = false;
    private boolean distroyed = false;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable recordDisposable;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat_internal;
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatInternalViewModel.class);
    }

    @Override
    protected void onError(ErrorModel errorModel) {
        super.onError(errorModel);
        if (errorModel.isNetworkError())
            statefulLayout.showConnectionError();
    }

    @Override
    public void showLoading() {
        if (adapter.isEmpty()) {
            statefulLayout.showLoading();
        } else {
            pbLoadMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        pbLoadMore.setVisibility(View.GONE);
    }

    private void hideSheet() {
        attachmentSheet.setVisibility(View.GONE);
    }

    @Override
    protected void onViewCreated() {
        if (!getDataFromIntent())
            return;

        viewModel.getErrorLiveData().observe(this, this::onError);

        viewModel.getHistoryChatMessagesLiveData().observe(this, this::onHistoryMessagesReceived);
        viewModel.getIncomingChatRoomActivationsLiveData().observe(this, this::onReceiveIncomingChatRoomActivations);
        viewModel.getIncomingChatMessagesLiveData().observeForever(messageObserver);

        viewModel.getHasPendingMessagesToSendLD().observe(this,
                hasPendingMessagesToSend -> viewPendingSendStatus.setVisibility(hasPendingMessagesToSend ? View.VISIBLE : View.GONE));

        setUpViews();

        getHistoryMessages();
    }

    private boolean getDataFromIntent() {
        currentRoom = (ChatRoomModel) getIntent().getSerializableExtra(DATA);
        if (currentRoom == null)
            return false;
        //Todo: Needs to be changed to actual current User Id <-------------------------------------
        currentBuddy = currentRoom.getOtherBuddy("6110fbe54edd90bb229684f0");
        if (currentBuddy == null)
            return false;
        currentUser = currentRoom.getOtherBuddy("6110fbe54edd90bb229684f0");
        return currentUser != null;
    }

    private void setUpViews() {
        setTitleWithBack(currentBuddy.getName());

        statefulLayout.setClickListener(this::getHistoryMessages);

        MessageHolders holders = new MessageHolders();

        /*
         * Registering message types ...
         * First add your type to the content checker
         * Then register it with the MessageHolders::registerContentType method,
         * Passing the incoming and outgoing cells ViewHolders with accompanied with their layout Ids
         * And optionally passing a custom payload to each of the two ViewHolders
         * And finally passing the common contentChecker
         * */

        MessageHolders.ContentChecker<ChatMessageModel> contentChecker = (message, type) ->
                (type == Integer.valueOf(MessageType.TEXT).byteValue() && message.getType() == MessageType.TEXT)
                        || (type == Integer.valueOf(MessageType.IMAGE).byteValue() && message.getType() == MessageType.IMAGE)
                        || (type == Integer.valueOf(MessageType.VOICE).byteValue() && message.getType() == MessageType.VOICE)
                        || (type == Integer.valueOf(MessageType.DOCUMENT).byteValue() && message.getType() == MessageType.DOCUMENT);

        holders.registerContentType(Integer.valueOf(MessageType.TEXT).byteValue(),
                CustomIncomingTextMessageViewHolder.class, R.layout.item_custom_message_incoming_text,
                CustomOutcomingTextMessageViewHolder.class, R.layout.item_custom_message_outcoming_text,
                contentChecker);

        holders.registerContentType(Integer.valueOf(MessageType.IMAGE).byteValue(),
                CustomIncomingImageMessageViewHolder.class, this, R.layout.item_custom_message_incoming_image,
                CustomOutcomingImageMessageViewHolder.class, this, R.layout.item_custom_message_outcoming_image,
                contentChecker);

        holders.registerContentType(Integer.valueOf(MessageType.VOICE).byteValue(),
                CustomIncomingVoiceMessageViewHolder.class, this, R.layout.item_custom_message_incoming_voice,
                CustomOutcomingVoiceMessageViewHolder.class, this, R.layout.item_custom_message_outcoming_voice,
                contentChecker);

        holders.registerContentType(Integer.valueOf(MessageType.DOCUMENT).byteValue(),
                CustomIncomingDocumentMessageViewHolder.class, this, R.layout.item_custom_message_incoming_document,
                CustomOutcomingDocumentMessageViewHolder.class, this, R.layout.item_custom_message_outcoming_document,
                contentChecker);

        adapter = new CustomMessagesListAdapter(currentUser.getId(), holders,
                (imageView, url, payload) -> PicassoHelper.loadImageWithCache(url, imageView, PicassoHelper.CENTER_CROP, null, null, null));

        rvMessagesList.setAdapter(adapter);

        adapter.setLoadMoreListener((page, totalItemsCount) -> getHistoryMessages());

        // Audio Recording & Playing

        audioRecorder = AudioRecorder.getInstance();

        audioPlayer = RxAudioPlayer.getInstance();

        recordDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + "VoiceMessages");

        //noinspection ResultOfMethodCallIgnored
        recordDirectory.mkdirs();

        rxPermissions = new RxPermissions(this);

        // Message Input

        layoutMessageInput.setInputListener(this::createTextMessage);

        layoutMessageInput.setAttachmentsListener(
                () -> attachmentSheet.setVisibility(attachmentSheet.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        layoutMessageInput.setRecordPermissionListener(() -> checkAudioRecordingPermissions(true));

        layoutMessageInput.setRecordPermissionGranted(checkAudioRecordingPermissions(false));

        layoutMessageInput.setRecordDurationBoundsInSeconds(1, 60);

        layoutMessageInput.setOnRecordActionListener(new OnRecordActionListener() {
            @Override
            public void onStart() {
                startAudioRecording();
            }

            @Override
            public void onCancel() {
                stopAudioRecording(false);
                deleteRecordedFile(lastRecordedAudioFile);
                hasMaxDurationLimitPendingRecord = false;
            }

            @Override
            public void onMaxDurationReached() {
                stopAudioRecording(false);
                hasMaxDurationLimitPendingRecord = true;
                Toasty.warning(ChatInternalActivity.this,
                        StringUtils.getString(R.string.max_record_duration_reached), Toasty.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(long recordTime) {
                if (hasMaxDurationLimitPendingRecord)
                    createVoiceMessage();
                else
                    stopAudioRecording(true);
                hasMaxDurationLimitPendingRecord = false;
            }

            @Override
            public void onLessThanMinimumDuration() {
                stopAudioRecording(false);
                deleteRecordedFile(lastRecordedAudioFile);
                hasMaxDurationLimitPendingRecord = false;
            }
        });

        // Attachment sheet

        attachmentSheet.findViewById(R.id.attachment_camera).setOnClickListener(v -> captureCameraImage());

        attachmentSheet.findViewById(R.id.attachment_gallery).setOnClickListener(v -> chooseImageFromGallery());

        attachmentSheet.findViewById(R.id.attachment_document).setOnClickListener(v -> chooseDocumentFromPicker());

        // Ended conversation Text view

//        tvConversationEnded.setVisibility(currentRoom.isActive() ? View.GONE : View.VISIBLE);
//        layoutMessageInput.setVisibility(currentRoom.isActive() ? View.VISIBLE : View.INVISIBLE);
    }

    // Messages Fetching

    private void getHistoryMessages() {
        viewModel.getHistoryChatMessages(currentRoom.getId());
    }

    private void onHistoryMessagesReceived(List<ChatMessageModel> messagesList) {
        adapter.addToEnd(messagesList, true);
        statefulLayout.showContentAndLockConnectionErrors();
    }

    private void onReceiveIncomingChatRoomActivations(ChatRoomModel chatRoomModel) {
        if (chatRoomModel.getId().equals(currentRoom.getId())) {
            currentRoom.setActive(chatRoomModel.isActive());
            tvConversationEnded.setVisibility(currentRoom.isActive() ? View.GONE : View.VISIBLE);
            layoutMessageInput.setVisibility(currentRoom.isActive() ? View.VISIBLE : View.INVISIBLE);
            getIntent().putExtra(DATA, currentRoom);
        }
    }

    Observer<ChatMessageModel> messageObserver = new Observer<ChatMessageModel>() {
        @Override
        public void onChanged(ChatMessageModel chatMessageModel) {
            if (chatMessageModel == null || !chatMessageModel.getRoomId().equals(currentRoom.getId()))
                return;

            if (distroyed) {
                viewModel.getIncomingChatMessagesLiveData().removeObserver(this);
            } else {
                onReceiveIncomingChatMessage(chatMessageModel);
            }
        }
    };

    private void onReceiveIncomingChatMessage(ChatMessageModel chatMessageModel) {
        if (chatMessageModel.getRoomId().equals(currentRoom.getId())) {
            adapter.addToStart(chatMessageModel, true);
            viewModel.sendSeenMessage(currentRoom.getId());


            if (!chatMessageModel.getSenderUser().getId().equals(userPref.getChatToken()))
                makeSound();
        }
    }

    public void makeSound() {
//        MediaPlayer mediaPlayer = MediaPlayer.create(MyApp.getInstance().getApplicationContext(), R.raw.twitter_sound);
//        mediaPlayer.start();
    }
    // Attachments

    @SuppressLint("CheckResult")
    private void captureCameraImage() {
        //noinspection ResultOfMethodCallIgnored
        RxPaparazzo.single(this)
                .size(new CustomMaxSize(500))
                .crop()
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::createImageMessage, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void chooseImageFromGallery() {
        //noinspection ResultOfMethodCallIgnored
        RxPaparazzo.single(this)
                .size(new CustomMaxSize(500))
                .crop()
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::createImageMessage, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void chooseDocumentFromPicker() {
        //noinspection ResultOfMethodCallIgnored
        RxPaparazzo.single(this)
                .setMimeType("application/pdf")
                .usingFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::createDocumentMessage, Throwable::printStackTrace);
    }

    // Audio Recording

    private boolean checkAudioRecordingPermissions(boolean shouldRequestIfNotGranted) {
        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO))
            return true;
        if (shouldRequestIfNotGranted)
            compositeDisposable.add(rxPermissions
                    .request(Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        layoutMessageInput.setRecordPermissionGranted(granted);
                        if (!granted)
                            showErrorMsg(StringUtils.getString(R.string.permission_must_be_granted));
                    }, Throwable::printStackTrace));
        return false;
    }

    private void startAudioRecording() {
        onRequestingStop();

        lastRecordedAudioFile = new File(recordDirectory, System.currentTimeMillis() + ".m4a");

        recordDisposable = Observable.fromCallable(() ->
                audioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC, MediaRecorder.OutputFormat.AAC_ADTS, MediaRecorder.AudioEncoder.AAC, lastRecordedAudioFile))
                .doOnComplete(() -> audioRecorder.startRecord())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {

                }, Throwable::printStackTrace);
    }

    private void stopAudioRecording(boolean resumeToMessageCreating) {
        if (recordDisposable != null && !recordDisposable.isDisposed()) {
            recordDisposable.dispose();
            recordDisposable = null;
        }
        compositeDisposable.add(Observable.fromCallable(() ->
                audioRecorder.stopRecord())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordDurationInSeconds -> {
                    if (recordDurationInSeconds == -1)
                        return;
                    lastRecordDuration = recordDurationInSeconds * 1000;
                    if (resumeToMessageCreating)
                        createVoiceMessage();
                }, Throwable::printStackTrace));
    }

    private void deleteRecordedFile(File file) {
        if (file != null && file.exists())
            //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    // Audio Playing

    @Override
    public void onMessageRequestingPlay(ChatMessageModel message) {
        super.showLoading();
        compositeDisposable.add(
                audioPlayer.play(PlayConfig
                        .url("http://3.126.221.243:8080/" + message.getVoiceUrl())
                        .streamType(AudioManager.STREAM_VOICE_CALL)
                        .looping(false)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                aBoolean -> {
                                    hideLoading();
                                    resetVoiceMessagesStates(message);
                                },
                                throwable -> {
                                    hideLoading();
                                    showErrorMsg(StringUtils.getString(R.string.no_internet_connection));
                                },
                                this::onRequestingStop));
    }

    @Override
    public void onRequestingStop() {
        audioPlayer.stopPlay();
        resetVoiceMessagesStates(null);
    }

    private void resetVoiceMessagesStates(@Nullable ChatMessageModel messageToPlay) {
        int position = 0;
        for (CustomMessagesListAdapter.Wrapper wrapper : adapter.getAllItemsList()) {
            if (wrapper.item instanceof ChatMessageModel) {
                ChatMessageModel message = (ChatMessageModel) wrapper.item;
                if (message.getType() == MessageType.VOICE) {
                    message.setVoicePlaying(message.equals(messageToPlay));
                    adapter.notifyItemChanged(position);
                }
            }
            position++;
        }
    }

    // Document Viewing

    @Override
    public void onMessageRequestingPreview(ChatMessageModel message) {
        startActivity(DocumentViewActivity.getIntent(this, "http://3.126.221.243:8080"+ message.getDocumentUrl()));
    }

    // Message Creating

    private boolean createTextMessage(CharSequence input) {
        hideSheet();
        String body = input.toString().trim();
        if (!TextUtils.isEmpty(body))
            viewModel.sendChatMessage(currentRoom.getId(), body, MessageType.TEXT, null, null)
                    .observe(this, ack -> {
                        if (ack)
                            layoutMessageInput.clearInputEditText();
                        else
                            showErrorMsg(StringUtils.getString(R.string.an_error_has_occurred));
                    });

        return false;
    }

    private void createImageMessage(Response<ChatInternalActivity, FileData> response) {
        hideSheet();
        if (response.resultCode() != RESULT_OK)
            return;
        super.showLoading();
        viewModel.uploadMultimediaFile(response.data().getFile()).observe(this, fileModel ->
                viewModel.sendChatMessage(currentRoom.getId(), fileModel.getPath(), MessageType.IMAGE, null, null)
                        .observe(this, ack -> {
                            hideLoading();
                            if (!ack)
                                showErrorMsg(StringUtils.getString(R.string.an_error_has_occurred));
                        }));
    }

    private void createVoiceMessage() {
        hideSheet();
        super.showLoading();
        viewModel.uploadMultimediaFile(lastRecordedAudioFile).observe(this, fileModel ->
                viewModel.sendChatMessage(currentRoom.getId(), fileModel.getPath(), MessageType.VOICE, lastRecordDuration, null)
                        .observe(this, ack -> {
                            hideLoading();
                            if (!ack)
                                showErrorMsg(StringUtils.getString(R.string.an_error_has_occurred));
                        }));
    }

    private void createDocumentMessage(Response<ChatInternalActivity, FileData> response) {
        hideSheet();
        if (response.resultCode() != RESULT_OK)
            return;
        super.showLoading();
        viewModel.uploadMultimediaFile(response.data().getFile()).observe(this, fileModel ->
                viewModel.sendChatMessage(currentRoom.getId(), fileModel.getPath(), MessageType.DOCUMENT, null, response.data().getFilename())
                        .observe(this, ack -> {
                            hideLoading();
                            if (!ack)
                                showErrorMsg(StringUtils.getString(R.string.an_error_has_occurred));
                        }));
    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stopPlay();
        }
        compositeDisposable.dispose();
        viewModel.getIncomingChatMessagesLiveData().removeObserver(messageObserver);
        distroyed = true;
        super.onDestroy();
    }
}
