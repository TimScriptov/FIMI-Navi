package com.fimi.kernel.dataparser.milink;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ByteArrayToIntArray {
    static int[] CRC32_Table = new int[256];
    private static boolean initTable;

    public static int CRC32Software(byte[] pData, int Length) {
        if (!initTable) {
            Table_Init();
            initTable = true;
        }
        int crc = -1;
        int offset = Length / 4;
        for (int i = 0; i < offset; i++) {
            byte[] nn = {pData[i * 4], pData[(i * 4) + 1], pData[(i * 4) + 2], pData[(i * 4) + 3]};
            int tmp = bytesToInt(nn, 0);
            for (int j = 3; j >= 0; j--) {
                int abyte = (crc >> 24) ^ ((tmp >> (j * 8)) & 255);
                int a = abyte & 255;
                crc = CRC32_Table[a] ^ (crc << 8);
            }
        }
        int k = Length % 4;
        if (k > 0) {
            byte[] t = new byte[4];
            for (int j2 = 0; j2 < k; j2++) {
                t[j2] = pData[(offset * 4) + j2];
            }
            int tmp2 = bytesToInt(t, 0);
            for (int j3 = 3; j3 >= 0; j3--) {
                int abyte2 = (byte) ((crc >> 24) ^ ((tmp2 >> (j3 * 8)) & 255));
                int a2 = abyte2 & 255;
                crc = CRC32_Table[a2] ^ (crc << 8);
            }
        }
        return crc ^ 0;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value = (src[offset] & 255) | ((src[offset + 1] & 255) << 8) | ((src[offset + 2] & 255) << 16) | ((src[offset + 3] & 255) << 24);
        return value;
    }

    public static void Table_Init() {
        for (int i32 = 0; i32 < 256; i32++) {
            int nData32 = i32 << 24;
            int CRC_Reg = 0;
            for (int j32 = 0; j32 < 8; j32++) {
                if (((nData32 ^ CRC_Reg) & Integer.MIN_VALUE) != 0) {
                    CRC_Reg = (CRC_Reg << 1) ^ 79764919;
                } else {
                    CRC_Reg <<= 1;
                }
                nData32 <<= 1;
            }
            CRC32_Table[i32] = CRC_Reg;
        }
    }

    public static int[] getInt(byte[] bylength) {
        int length = bylength.length;
        List<byte[]> listbyte = new ArrayList<>();
        byte[] item = new byte[4];
        for (int y = 0; y < bylength.length; y += 4) {
            if (bylength.length - y < 4 && bylength.length % 4 == 1) {
                item[0] = bylength[y];
                item[1] = 0;
                item[2] = 0;
                item[3] = 0;
            } else if (bylength.length - y < 4 && bylength.length % 4 == 2) {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = 0;
                item[3] = 0;
            } else if (bylength.length - y < 4 && bylength.length % 4 == 3) {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = bylength[y + 2];
                item[3] = 0;
            } else {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = bylength[y + 2];
                item[3] = bylength[y + 3];
            }
            listbyte.add(trans(item));
            item = new byte[4];
        }
        int[] arrayitem = new int[listbyte.size()];
        for (int x = 0; x < listbyte.size(); x++) {
            arrayitem[x] = byteArrayToInt(listbyte.get(x));
        }
        return arrayitem;
    }

    public static byte[] trans(byte[] by) {
        byte[] bx = new byte[4];
        for (int x = 0; x < by.length; x++) {
            bx[x] = by[(by.length - x) - 1];
        }
        return bx;
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[3] & 255) | ((b[2] & 255) << 8) | ((b[1] & 255) << 16) | ((b[0] & 255) << 24);
    }
}
