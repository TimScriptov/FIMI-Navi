package com.fimi.soul.media.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.fimi.kernel.utils.DateUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class FermiPlayerUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    public static Bitmap createVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath.replace("file://", ""), 1);
    }

    public static boolean createRemoteVideoThumbnail(String remotePath, String localPath) {
        FimiMediaPlayer fimiMediaPlayer = FimiMediaPlayer.getIntance();
        return fimiMediaPlayer.videoToJpg(remotePath, localPath);
    }

    public static Bitmap createVideoThumbnail(String filePath, int offsetMillSecond) {
        String filePath2 = filePath.replace("file://", "");
        if (Build.VERSION.SDK_INT >= 14) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath2);
            Bitmap bitmap = retriever.getFrameAtTime(offsetMillSecond * 1000L, 2);
            return bitmap;
        }
        Bitmap bitmap2 = createVideoThumbnail(filePath2);
        return bitmap2;
    }

    public static Bitmap createVideoThumbnail(String filePath, int width, int height, int offsetMillSecond) {
        Bitmap bitmap = createVideoThumbnail(filePath, offsetMillSecond);
        if (bitmap != null) {
            return ThumbnailUtils.extractThumbnail(bitmap, width, height);
        }
        return bitmap;
    }

    public static Bitmap createVideoThumbnail(String filePath, int width, int height) {
        Bitmap bp = createVideoThumbnail(filePath);
        if (bp != null) {
            return ThumbnailUtils.extractThumbnail(bp, width, height);
        }
        return bp;
    }

    public static long getVideoDuration(Context context, String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            try {
                long time = MediaPlayer.create(context, Uri.fromFile(file)).getDuration();
                Log.d("Good", "time:" + time);
                return time;
            } catch (Exception e) {
            }
        }
        return 0L;
    }

    public static String getVideoDurationString(Context context, String path) {
        return getTimelineString(getVideoDuration(context, path));
    }

    public static String getVideoDurationString(Context context, String path, String formate) {
        return getTimelineString(getVideoDuration(context, path), formate);
    }

    public static String getTimelineString(long millSecond) {
        return getTimelineString(millSecond, DateUtil.dateFormatHMS);
    }

    public static String getTimelineString(long millsecond, String formate) {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.applyPattern(formate);
        return sdf.format(new Date(millsecond));
    }
}
