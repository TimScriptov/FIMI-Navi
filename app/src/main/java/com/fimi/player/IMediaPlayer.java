package com.fimi.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;


public interface IMediaPlayer {
    int MEDIA_ERROR_IO = -1004;
    int MEDIA_ERROR_MALFORMED = -1007;
    int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    int MEDIA_ERROR_SERVER_DIED = 100;
    int MEDIA_ERROR_TIMED_OUT = -110;
    int MEDIA_ERROR_UNKNOWN = 1;
    int MEDIA_ERROR_UNSUPPORTED = -1010;
    int MEDIA_INFO_BAD_INTERLEAVING = 800;
    int MEDIA_INFO_BUFFERING_END = 702;
    int MEDIA_INFO_BUFFERING_START = 701;
    int MEDIA_INFO_METADATA_UPDATE = 802;
    int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    int MEDIA_INFO_NOT_SEEKABLE = 801;
    int MEDIA_INFO_STARTED_AS_NEXT = 2;
    int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    int MEDIA_INFO_UNKNOWN = 1;
    int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
    int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;

    int getAudioSessionId();

    long getCurrentPosition();

    String getDataSource();

    void setDataSource(FileDescriptor fileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException;

    void setDataSource(String str) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    long getDuration();

    MediaInfo getMediaInfo();

    int getVideoHeight();

    int getVideoSarDen();

    int getVideoSarNum();

    int getVideoWidth();

    @Deprecated
    boolean isPlayable();

    boolean isPlaying();

    void pause() throws IllegalStateException;

    int playerRtmpStart(String str, int i) throws IllegalStateException;

    int playerRtmpStop(int i) throws IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    int recEmergencySave(String str, int i) throws IllegalStateException;

    int recStart(String str, int i, int i2) throws IllegalStateException;

    int recStop() throws IllegalStateException;

    void release();

    void reset();

    void seekTo(long j) throws IllegalStateException;

    void setAudioStreamType(int i);

    void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    @TargetApi(14)
    void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    int setDeviceInfo(int i, int i2) throws IllegalStateException;

    void setDisplay(SurfaceHolder surfaceHolder);

    @Deprecated
    void setKeepInBackground(boolean z);

    @Deprecated
    void setLogEnabled(boolean z);

    void setMediaQualityListener(MediaQualityListener mediaQualityListener);

    void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener);

    void setOnCompletionListener(OnCompletionListener onCompletionListener);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnInfoListener(OnInfoListener onInfoListener);

    void setOnLiveVideoListener(OnLiveVideoListener onLiveVideoListener);

    void setOnPreparedListener(OnPreparedListener onPreparedListener);

    void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener);

    void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener);

    void setPreview(int i) throws IllegalStateException;

    void setScreenOnWhilePlaying(boolean z);

    @TargetApi(14)
    void setSurface(Surface surface);

    void setVolume(float f, float f2);

    @Deprecated
    void setWakeMode(Context context, int i);

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;


    interface MediaQualityListener {
        void showMediaQuality(int i, int i2);
    }


    interface OnBufferingUpdateListener {
        void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i);
    }


    interface OnCompletionListener {
        void onCompletion(IMediaPlayer iMediaPlayer, int i);
    }


    interface OnErrorListener {
        boolean onError(IMediaPlayer iMediaPlayer, int i, int i2);
    }


    interface OnInfoListener {
        boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2);
    }


    interface OnLiveVideoListener {
        void onRtmpStatusCB(int i, int i2, int i3);
    }


    interface OnPreparedListener {
        void onPrepared(IMediaPlayer iMediaPlayer);

        void onStartStream();
    }


    interface OnSeekCompleteListener {
        void onSeekComplete(IMediaPlayer iMediaPlayer);
    }


    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4);
    }
}
