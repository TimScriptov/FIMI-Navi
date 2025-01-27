package com.fimi.x8sdk.common;

import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.connect.ConnectType;
import com.fimi.x8sdk.map.MapType;


public final class GlobalConfig {
    private static GlobalConfig globalConfig = new GlobalConfig();
    boolean isRectification;
    int lowPowerOperation;
    int lowPowerValue;
    int mapStyle;
    int seriousLowPowerOperation;
    int seriousLowPowerValue;
    MapType mapType = null;
    ConnectType connectType = ConnectType.Aoa;
    private boolean isLowLanding;
    private boolean isLowReturn;
    private boolean isMap;
    private boolean isNewHand;
    private boolean isShowLog;
    private int mGridLine = 0;
    private boolean isShowmMtric = true;

    public static GlobalConfig getInstance() {
        return globalConfig;
    }

    public static void setGlobalConfig(GlobalConfig globalConfig2) {
        globalConfig = globalConfig2;
    }

    public boolean isAOAConnect() {
        return ConnectType.Aoa.equals(this.connectType);
    }

    public boolean isLowReturn() {
        return this.isLowReturn;
    }

    public void setLowReturn(boolean lowReturn) {
        this.isLowReturn = lowReturn;
        SPStoreManager.getInstance().saveBoolean(Constants.X8_LOW_POWER_RETURN, this.isLowReturn);
    }

    public boolean isLowLanding() {
        return this.isLowLanding;
    }

    public void setLowLanding(boolean lowLanding) {
        this.isLowLanding = lowLanding;
        SPStoreManager.getInstance().saveBoolean(Constants.X8_LOW_POWER_LANDING, this.isLowLanding);
    }

    public void init(Builder builder) {
        this.mapType = builder.mapType;
        this.mapStyle = builder.mapStyle;
        this.lowPowerValue = builder.lowPowerValue;
        this.seriousLowPowerValue = builder.seriousLowPowerValue;
        this.lowPowerOperation = builder.lowPowerOperation;
        this.seriousLowPowerOperation = builder.seriousLowPowerOperation;
        this.isRectification = builder.isRectification;
        this.isShowLog = builder.isShowLog;
        this.mGridLine = builder.mGridLine;
        this.isShowmMtric = builder.isShowmMtric;
        this.isLowLanding = builder.isLowLanding;
        this.isLowReturn = builder.isLowReturn;
        SessionManager.initInstance();
        NoticeManager.initInstance();
    }

    public boolean isRectification() {
        return this.isRectification;
    }

    public void setRectification(boolean rectification) {
        this.isRectification = rectification;
    }

    public MapType getMapType() {
        return this.mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public boolean isShowLog() {
        return this.isShowLog;
    }

    public void setShowLog(boolean showLog) {
        this.isShowLog = showLog;
    }

    public int getGridLine() {
        return this.mGridLine;
    }

    public void setGridLine(int gridLine) {
        this.mGridLine = gridLine;
        SPStoreManager.getInstance().saveInt(Constants.X8_GLINE_LINE_OPTION, this.mGridLine);
    }

    public boolean isShowmMtric() {
        return this.isShowmMtric;
    }

    public void setShowmMtric(boolean showmMtric) {
        this.isShowmMtric = showmMtric;
    }

    public int getMapStyle() {
        return this.mapStyle;
    }

    public void setMapStyle(int mapStyle) {
        this.mapStyle = mapStyle;
    }

    public void setMap(boolean map) {
        this.isMap = map;
    }

    public int getLowPowerValue() {
        return this.lowPowerValue;
    }

    public void setLowPowerValue(int lowPowerValue) {
        this.lowPowerValue = lowPowerValue;
    }

    public int getSeriousLowPowerValue() {
        return this.seriousLowPowerValue;
    }

    public void setSeriousLowPowerValue(int seriousLowPowerValue) {
        this.seriousLowPowerValue = seriousLowPowerValue;
    }

    public int getLowPowerOperation() {
        return this.lowPowerOperation;
    }

    public void setLowPowerOperation(int lowPowerOperation) {
        this.lowPowerOperation = lowPowerOperation;
    }

    public int getSeriousLowPowerOperation() {
        return this.seriousLowPowerOperation;
    }

    public void setSeriousLowPowerOperation(int seriousLowPowerOperation) {
        this.seriousLowPowerOperation = seriousLowPowerOperation;
    }

    public boolean isNewHand() {
        return this.isNewHand;
    }

    public void setNewHand(boolean newHand) {
        this.isNewHand = newHand;
    }


    public static class Builder {
        int lowPowerOperation;
        int lowPowerValue;
        int seriousLowPowerOperation;
        int seriousLowPowerValue;
        private boolean isLowLanding;
        private boolean isLowReturn;
        private boolean isRectification;
        private boolean isShowLog;
        private boolean isShowmMtric;
        private int mGridLine;
        private int mapStyle;
        private MapType mapType;

        public boolean isLowReturn() {
            return this.isLowReturn;
        }

        public void setLowReturn(boolean lowReturn) {
            this.isLowReturn = lowReturn;
        }

        public boolean isLowLanding() {
            return this.isLowLanding;
        }

        public void setLowLanding(boolean lowLanding) {
            this.isLowLanding = lowLanding;
        }

        public Builder setShowmMtric(boolean showmMtric) {
            this.isShowmMtric = showmMtric;
            return this;
        }

        public Builder setGridLine(int gridLine) {
            this.mGridLine = gridLine;
            return this;
        }

        public Builder setShowLog(boolean showLog) {
            this.isShowLog = showLog;
            return this;
        }

        public Builder setMapType(MapType mapType) {
            this.mapType = mapType;
            return this;
        }

        public Builder setMapStyle(int mapStyle) {
            this.mapStyle = mapStyle;
            return this;
        }

        public Builder setRectification(boolean isRectification) {
            this.isRectification = isRectification;
            return this;
        }

        public Builder setLowPowerValue(int lowPowerValue) {
            this.lowPowerValue = lowPowerValue;
            return this;
        }

        public Builder setSeriousLowPowerValue(int seriousLowPowerValue) {
            this.seriousLowPowerValue = seriousLowPowerValue;
            return this;
        }

        public Builder setLowPowerOpration(int lowPowerOperation) {
            this.lowPowerOperation = lowPowerOperation;
            return this;
        }

        public Builder setSeriousLowPowerOperation(int seriousLowPowerOperation) {
            this.seriousLowPowerOperation = seriousLowPowerOperation;
            return this;
        }
    }
}
