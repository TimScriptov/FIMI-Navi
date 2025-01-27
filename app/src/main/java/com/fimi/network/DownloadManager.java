package com.fimi.network;

import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.network.entity.X8PlaybackLogEntity;


public class DownloadManager {
    public void downLoadFw(UpfirewareDto dto, DisposeDataHandle disposeDataHandle) {
        disposeDataHandle.mSource = DirectoryPath.getFwPath(dto.getSysName());
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(dto.getFileUrl()), disposeDataHandle);
    }

    public void downLoadFlightPlayback(X8PlaybackLogEntity dto, DisposeDataHandle disposeDataHandle) {
        String logFileUrl = dto.getLogFileUrl();
        String[] fileNames = logFileUrl.split(".zip")[0].split("/");
        String fileName = fileNames[fileNames.length - 1] + ".zip";
        dto.setFlieName(fileName);
        disposeDataHandle.mSource = DirectoryPath.getX8LoginFlightPlaybackPath("") + "/" + fileName;
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(dto.getLogFileUrl()), disposeDataHandle);
    }

    public void downLoadX9MediaDescriptionFile(String url, String path, DisposeDataHandle disposeDataHandle) {
        disposeDataHandle.mSource = path;
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url), disposeDataHandle);
    }

    public void downloadApk(String url, String path, DisposeDataHandle disposeDataHandle) {
        disposeDataHandle.mSource = path;
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url), disposeDataHandle);
    }
}
