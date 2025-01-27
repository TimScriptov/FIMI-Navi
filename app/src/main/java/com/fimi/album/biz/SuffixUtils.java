package com.fimi.album.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SuffixUtils {
    private static SuffixUtils mSuffixUtils;
    private final List<String> videoSuffixList = new ArrayList();
    private final List<String> photoSuffixList = new ArrayList();
    private final List<String> videoThumSuffixList = new ArrayList();
    public String fileFormatRlv = ".RLV";
    public String fileFormatJpg = ".JPG";
    public String fileFormatMp4 = ".MP4";
    public String fileFormatThm = ".THM";
    String[] videoStrings = {this.fileFormatThm};
    String[] photoStrings = {".jpg", this.fileFormatJpg, ".png", "PNG"};

    private SuffixUtils() {
        this.videoSuffixList.addAll(Arrays.asList(this.videoStrings));
        this.photoSuffixList.addAll(Arrays.asList(this.photoStrings));
    }

    public static SuffixUtils obtain() {
        if (mSuffixUtils == null) {
            synchronized (SuffixUtils.class) {
                if (mSuffixUtils == null) {
                    mSuffixUtils = new SuffixUtils();
                }
            }
        }
        return mSuffixUtils;
    }

    public boolean judgeFileType(String filePathSuffix) {
        if (filePathSuffix.contains(".")) {
            String filePathSuffix2 = filePathSuffix.substring(filePathSuffix.lastIndexOf("."));
            if (this.videoSuffixList.contains(filePathSuffix2)) {
                return true;
            }
            if (this.photoSuffixList.contains(filePathSuffix2)) {
            }
            return false;
        }
        return false;
    }

    public boolean judgeVideo(String filePathSuffix) {
        if (filePathSuffix.contains(".")) {
            return this.videoSuffixList.contains(filePathSuffix.substring(filePathSuffix.lastIndexOf(".")));
        }
        return false;
    }

    public boolean judgePhotho(String filePathSuffix) {
        if (filePathSuffix.contains(".")) {
            return this.photoSuffixList.contains(filePathSuffix.substring(filePathSuffix.lastIndexOf(".")));
        }
        return false;
    }
}
