package com.fimi.app.x8s.test;

import androidx.annotation.NonNull;


public class TestJava {
    public static void main(String[] args) {
        byte[] arr = {51, 51, -64, -64};
        System.out.println(byte2float(arr, 0));
        System.out.println(2.0f);
    }

    @NonNull
    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return new byte[]{(byte) (intBits & 255), (byte) ((65280 & intBits) >> 8), (byte) ((16711680 & intBits) >> 16), (byte) (((-16777216) & intBits) >> 24)};
    }

    public static float byteToFloat(@NonNull byte[] arr, int index) {
        int i = ((-16777216) & (arr[index] << 24)) | (16711680 & (arr[index + 1] << 16)) | (65280 & (arr[index + 2] << 8)) | (arr[index + 3] & 255);
        return Float.intBitsToFloat(i);
    }

    public static float byte2float(@NonNull byte[] b, int index) {
        int l = b[index];
        return Float.intBitsToFloat((((((l & 255) | (b[index + 1] << 8)) & 65535) | (b[index + 2] << 16)) & 0x00ffffff) | (b[index + 3] << 24));
    }
}
