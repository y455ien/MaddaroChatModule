package com.example.maddarochatmodule.chat_module.chat;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class ChatUtils {

    private ChatUtils() {
        // non-instantiable class
    }

    /*
    * Check if time is today returns time, otherwise returns date
    * */
    public static String formatChatRoomTime(Long timestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(date.before(calendar.getTime()) ? "yyyy-MM-dd" : "hh:mm a", StringUtils.getLanguage().equals("ar") ? new Locale("ar") : Locale.US);
        return dateFormat.format(date);
    }

    public static void setChatBubbleBackground(View bubbleView, boolean isIncoming) {
        GradientDrawable backgroundShape = new GradientDrawable();

        backgroundShape.setShape(GradientDrawable.RECTANGLE);

        float radius = 12f;
        if (isIncoming)
            backgroundShape.setCornerRadii(StringUtils.getLanguage().equals("ar")
                    ? new float[]{radius, radius, radius, radius, radius, radius, 0, 0}
                    : new float[]{radius, radius, radius, radius, 0, 0, radius, radius});
        else
            backgroundShape.setCornerRadii(StringUtils.getLanguage().equals("ar")
                    ? new float[]{radius, radius, radius, radius, 0, 0, radius, radius}
                    : new float[]{radius, radius, radius, radius, radius, radius, 0, 0});

        backgroundShape.setColor(bubbleView.getContext().getResources()
                .getColor(isIncoming ? R.color.textGrey : R.color.colorPrimary));

        bubbleView.setBackground(backgroundShape);
    }

    public static void setTimeTextView(@NonNull Date date, @NonNull TextView textView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", StringUtils.getLanguage().equals("ar") ? new Locale("ar") : Locale.US);
        textView.setText(dateFormat.format(date));
    }

    public static String getHumanDurationText(long milliseconds) {
        return String.format(Locale.US, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
