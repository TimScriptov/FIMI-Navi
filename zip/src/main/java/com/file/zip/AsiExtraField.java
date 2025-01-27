package com.file.zip;

import androidx.annotation.NonNull;

import java.util.zip.CRC32;
import java.util.zip.ZipException;

public class AsiExtraField implements ZipExtraField, UnixStat, Cloneable {
    private static final ZipShort HEADER_ID = new ZipShort(30062);
    private static final int WORD = 4;
    private int mode = 0;
    private int uid = 0;
    private int gid = 0;
    private String link = "";
    private boolean dirFlag = false;
    private CRC32 crc = new CRC32();

    @Override
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    @Override
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(getLinkedFile().getBytes().length + 14);
    }

    @Override
    public ZipShort getCentralDirectoryLength() {
        return getLocalFileDataLength();
    }

    @Override
    public byte[] getLocalFileDataData() {
        byte[] data = new byte[getLocalFileDataLength().getValue() - 4];
        System.arraycopy(ZipShort.getBytes(getMode()), 0, data, 0, 2);
        byte[] linkArray = getLinkedFile().getBytes();
        System.arraycopy(ZipLong.getBytes(linkArray.length), 0, data, 2, 4);
        System.arraycopy(ZipShort.getBytes(getUserId()), 0, data, 6, 2);
        System.arraycopy(ZipShort.getBytes(getGroupId()), 0, data, 8, 2);
        System.arraycopy(linkArray, 0, data, 10, linkArray.length);
        this.crc.reset();
        this.crc.update(data);
        long checksum = this.crc.getValue();
        byte[] result = new byte[data.length + 4];
        System.arraycopy(ZipLong.getBytes(checksum), 0, result, 0, 4);
        System.arraycopy(data, 0, result, 4, data.length);
        return result;
    }

    @Override
    public byte[] getCentralDirectoryData() {
        return getLocalFileDataData();
    }

    public int getUserId() {
        return this.uid;
    }

    public void setUserId(int uid) {
        this.uid = uid;
    }

    public int getGroupId() {
        return this.gid;
    }

    public void setGroupId(int gid) {
        this.gid = gid;
    }

    public String getLinkedFile() {
        return this.link;
    }

    public void setLinkedFile(String name) {
        this.link = name;
        this.mode = getMode(this.mode);
    }

    public boolean isLink() {
        return getLinkedFile().length() != 0;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = getMode(mode);
    }

    public boolean isDirectory() {
        return this.dirFlag && !isLink();
    }

    public void setDirectory(boolean dirFlag) {
        this.dirFlag = dirFlag;
        this.mode = getMode(this.mode);
    }

    @Override
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        long givenChecksum = ZipLong.getValue(data, offset);
        byte[] tmp = new byte[length - 4];
        System.arraycopy(data, offset + 4, tmp, 0, length - 4);
        this.crc.reset();
        this.crc.update(tmp);
        long realChecksum = this.crc.getValue();
        if (givenChecksum != realChecksum) {
            throw new ZipException("bad CRC checksum " + Long.toHexString(givenChecksum) + " instead of " + Long.toHexString(realChecksum));
        }
        int newMode = ZipShort.getValue(tmp, 0);
        byte[] linkArray = new byte[(int) ZipLong.getValue(tmp, 2)];
        this.uid = ZipShort.getValue(tmp, 6);
        this.gid = ZipShort.getValue(tmp, 8);
        if (linkArray.length == 0) {
            this.link = "";
        } else {
            System.arraycopy(tmp, 10, linkArray, 0, linkArray.length);
            this.link = new String(linkArray);
        }
        setDirectory((newMode & 16384) != 0);
        setMode(newMode);
    }

    protected int getMode(int mode) {
        int type = 32768;
        if (isLink()) {
            type = UnixStat.LINK_FLAG;
        } else if (isDirectory()) {
            type = 16384;
        }
        return (mode & UnixStat.PERM_MASK) | type;
    }

    @NonNull
    public Object clone() {
        try {
            AsiExtraField cloned = (AsiExtraField) super.clone();
            cloned.crc = new CRC32();
            return cloned;
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
}
