package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.fmlink4.LinkPayLoad4;
import com.fimi.x8sdk.entity.FLatLng;

import java.util.ArrayList;
import java.util.List;


public class AckNoFlyNormal extends X8BaseMessage {
    FLatLng a1;
    FLatLng a2;
    short attributeByte;
    FLatLng b1;
    FLatLng b2;
    FLatLng c1;
    FLatLng c2;
    FLatLng center;
    FLatLng d1;
    FLatLng d2;
    FLatLng endDirAngle;
    double endDirAngleLat;
    double endDirAngleLon;
    int forbiddenType;
    int heightLimit;
    int heightLimitRadius;
    double nfzPointA1Lat;
    double nfzPointA1Lon;
    double nfzPointA2Lat;
    double nfzPointA2lon;
    double nfzPointB1Lat;
    double nfzPointB1Lon;
    double nfzPointB2Lat;
    double nfzPointB2Lon;
    double nfzPointC1Lat;
    double nfzPointC1Lon;
    double nfzPointC2Lat;
    double nfzPointC2Lon;
    double nfzPointD1Lat;
    double nfzPointD1Lon;
    double nfzPointD2Lat;
    double nfzPointD2Lon;
    double nfzPointLat;
    double nfzPointLon;
    int noflyRadius;
    int numEudges;
    List<FLatLng> points = new ArrayList();
    int polygonShape;
    FLatLng startDirAngle;
    double startDirAngleLat;
    double startDirAngleLon;
    int userType;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        LinkPayLoad4 payLoad4 = packet.getPayLoad4();
        this.attributeByte = payLoad4.getShort();
        this.forbiddenType = this.attributeByte & 3;
        this.userType = (this.attributeByte >> 2) & 3;
        this.polygonShape = (this.attributeByte >> 4) & 7;
        this.numEudges = (this.attributeByte >> 7) & 31;
        this.heightLimit = payLoad4.getShort();
        this.nfzPointLat = payLoad4.getFloat();
        this.nfzPointLon = payLoad4.getFloat();
        this.center = new FLatLng(this.nfzPointLat, this.nfzPointLon);
        if ((this.polygonShape == 1) | (this.polygonShape == 0)) {
            this.heightLimitRadius = payLoad4.getShort() * 10;
            this.noflyRadius = payLoad4.getShort() * 10;
            this.startDirAngleLon = payLoad4.getFloat();
            this.startDirAngleLat = payLoad4.getFloat();
            this.startDirAngle = new FLatLng(this.startDirAngleLat, this.startDirAngleLon);
            this.endDirAngleLon = payLoad4.getFloat();
            this.endDirAngleLat = payLoad4.getFloat();
            this.endDirAngle = new FLatLng(this.endDirAngleLat, this.endDirAngleLon);
        }
        if (this.polygonShape == 2) {
            this.nfzPointB1Lat = payLoad4.getFloat();
            this.nfzPointB1Lon = payLoad4.getFloat();
            this.b1 = new FLatLng(this.nfzPointB1Lat, this.nfzPointB1Lon);
            this.nfzPointC1Lat = payLoad4.getFloat();
            this.nfzPointC1Lon = payLoad4.getFloat();
            this.c1 = new FLatLng(this.nfzPointC1Lat, this.nfzPointC1Lon);
            this.nfzPointA1Lat = payLoad4.getFloat();
            this.nfzPointA1Lon = payLoad4.getFloat();
            this.a1 = new FLatLng(this.nfzPointA1Lat, this.nfzPointA1Lon);
            this.nfzPointA2Lat = payLoad4.getFloat();
            this.nfzPointA2lon = payLoad4.getFloat();
            this.a2 = new FLatLng(this.nfzPointA2Lat, this.nfzPointA2lon);
            this.nfzPointC2Lat = payLoad4.getFloat();
            this.nfzPointC2Lon = payLoad4.getFloat();
            this.c2 = new FLatLng(this.nfzPointC2Lat, this.nfzPointC2Lon);
            this.nfzPointB2Lat = payLoad4.getFloat();
            this.nfzPointB2Lon = payLoad4.getFloat();
            this.b2 = new FLatLng(this.nfzPointB2Lat, this.nfzPointB2Lon);
            this.nfzPointD1Lat = payLoad4.getFloat();
            this.nfzPointD1Lon = payLoad4.getFloat();
            this.d1 = new FLatLng(this.nfzPointD1Lat, this.nfzPointD1Lon);
            this.nfzPointD2Lat = payLoad4.getFloat();
            this.nfzPointD2Lon = payLoad4.getFloat();
            this.d2 = new FLatLng(this.nfzPointD2Lat, this.nfzPointD2Lon);
        }
        if (this.polygonShape == 3 && this.numEudges > 0) {
            for (int i = 0; i < this.numEudges; i++) {
                FLatLng point = new FLatLng();
                point.latitude = payLoad4.getFloat();
                point.longitude = payLoad4.getFloat();
                this.points.add(point);
            }
        }
    }

    public int getForbiddenType() {
        return this.forbiddenType;
    }

    public void setForbiddenType(int forbiddenType) {
        this.forbiddenType = forbiddenType;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getPolygonShape() {
        return this.polygonShape;
    }

    public void setPolygonShape(int polygonShape) {
        this.polygonShape = polygonShape;
    }

    public int getNumEudges() {
        return this.numEudges;
    }

    public void setNumEudges(int numEudges) {
        this.numEudges = numEudges;
    }

    public short getAttributeByte() {
        return this.attributeByte;
    }

    public void setAttributeByte(short attributeByte) {
        this.attributeByte = attributeByte;
    }

    public double getNfzPointLat() {
        return this.nfzPointLat;
    }

    public void setNfzPointLat(double nfzPointLat) {
        this.nfzPointLat = nfzPointLat;
    }

    public double getNfzPointLon() {
        return this.nfzPointLon;
    }

    public void setNfzPointLon(double nfzPointLon) {
        this.nfzPointLon = nfzPointLon;
    }

    public int getHeightLimitRadius() {
        return this.heightLimitRadius;
    }

    public void setHeightLimitRadius(int heightLimitRadius) {
        this.heightLimitRadius = heightLimitRadius;
    }

    public int getNoflyRadius() {
        return this.noflyRadius;
    }

    public void setNoflyRadius(int noflyRadius) {
        this.noflyRadius = noflyRadius;
    }

    public FLatLng getStartDirAngle() {
        return this.startDirAngle;
    }

    public void setStartDirAngle(FLatLng startDirAngle) {
        this.startDirAngle = startDirAngle;
    }

    public FLatLng getEndDirAngle() {
        return this.endDirAngle;
    }

    public void setEndDirAngle(FLatLng endDirAngle) {
        this.endDirAngle = endDirAngle;
    }

    public double getNfzPointD1Lat() {
        return this.nfzPointD1Lat;
    }

    public void setNfzPointD1Lat(double nfzPointD1Lat) {
        this.nfzPointD1Lat = nfzPointD1Lat;
    }

    public double getNfzPointD1Lon() {
        return this.nfzPointD1Lon;
    }

    public void setNfzPointD1Lon(double nfzPointD1Lon) {
        this.nfzPointD1Lon = nfzPointD1Lon;
    }

    public double getNfzPointB1Lat() {
        return this.nfzPointB1Lat;
    }

    public void setNfzPointB1Lat(double nfzPointB1Lat) {
        this.nfzPointB1Lat = nfzPointB1Lat;
    }

    public double getNfzPointB1Lon() {
        return this.nfzPointB1Lon;
    }

    public void setNfzPointB1Lon(double nfzPointB1Lon) {
        this.nfzPointB1Lon = nfzPointB1Lon;
    }

    public double getNfzPointC1Lat() {
        return this.nfzPointC1Lat;
    }

    public void setNfzPointC1Lat(double nfzPointC1Lat) {
        this.nfzPointC1Lat = nfzPointC1Lat;
    }

    public double getNfzPointC1Lon() {
        return this.nfzPointC1Lon;
    }

    public void setNfzPointC1Lon(double nfzPointC1Lon) {
        this.nfzPointC1Lon = nfzPointC1Lon;
    }

    public double getNfzPointA1Lat() {
        return this.nfzPointA1Lat;
    }

    public void setNfzPointA1Lat(double nfzPointA1Lat) {
        this.nfzPointA1Lat = nfzPointA1Lat;
    }

    public double getNfzPointA1Lon() {
        return this.nfzPointA1Lon;
    }

    public void setNfzPointA1Lon(double nfzPointA1Lon) {
        this.nfzPointA1Lon = nfzPointA1Lon;
    }

    public double getNfzPointA2Lat() {
        return this.nfzPointA2Lat;
    }

    public void setNfzPointA2Lat(double nfzPointA2Lat) {
        this.nfzPointA2Lat = nfzPointA2Lat;
    }

    public double getNfzPointA2lon() {
        return this.nfzPointA2lon;
    }

    public void setNfzPointA2lon(double nfzPointA2lon) {
        this.nfzPointA2lon = nfzPointA2lon;
    }

    public double getNfzPointC2Lat() {
        return this.nfzPointC2Lat;
    }

    public void setNfzPointC2Lat(double nfzPointC2Lat) {
        this.nfzPointC2Lat = nfzPointC2Lat;
    }

    public double getNfzPointC2Lon() {
        return this.nfzPointC2Lon;
    }

    public void setNfzPointC2Lon(double nfzPointC2Lon) {
        this.nfzPointC2Lon = nfzPointC2Lon;
    }

    public double getNfzPointB2Lat() {
        return this.nfzPointB2Lat;
    }

    public void setNfzPointB2Lat(double nfzPointB2Lat) {
        this.nfzPointB2Lat = nfzPointB2Lat;
    }

    public double getNfzPointB2Lon() {
        return this.nfzPointB2Lon;
    }

    public void setNfzPointB2Lon(double nfzPointB2Lon) {
        this.nfzPointB2Lon = nfzPointB2Lon;
    }

    public double getNfzPointD2Lat() {
        return this.nfzPointD2Lat;
    }

    public void setNfzPointD2Lat(double nfzPointD2Lat) {
        this.nfzPointD2Lat = nfzPointD2Lat;
    }

    public double getNfzPointD2Lon() {
        return this.nfzPointD2Lon;
    }

    public void setNfzPointD2Lon(double nfzPointD2Lon) {
        this.nfzPointD2Lon = nfzPointD2Lon;
    }

    public List<FLatLng> getPoints() {
        return this.points;
    }

    public void setPoints(List<FLatLng> points) {
        this.points = points;
    }

    public FLatLng getCenter() {
        return this.center;
    }

    public void setCenter(FLatLng center) {
        this.center = center;
    }

    public FLatLng getD1() {
        return this.d1;
    }

    public void setD1(FLatLng d1) {
        this.d1 = d1;
    }

    public FLatLng getB1() {
        return this.b1;
    }

    public void setB1(FLatLng b1) {
        this.b1 = b1;
    }

    public FLatLng getC1() {
        return this.c1;
    }

    public void setC1(FLatLng c1) {
        this.c1 = c1;
    }

    public FLatLng getA1() {
        return this.a1;
    }

    public void setA1(FLatLng a1) {
        this.a1 = a1;
    }

    public FLatLng getA2() {
        return this.a2;
    }

    public void setA2(FLatLng a2) {
        this.a2 = a2;
    }

    public FLatLng getC2() {
        return this.c2;
    }

    public void setC2(FLatLng c2) {
        this.c2 = c2;
    }

    public FLatLng getB2() {
        return this.b2;
    }

    public void setB2(FLatLng b2) {
        this.b2 = b2;
    }

    public FLatLng getD2() {
        return this.d2;
    }

    public void setD2(FLatLng d2) {
        this.d2 = d2;
    }

    public int getHeightLimit() {
        return this.heightLimit;
    }

    public void setHeightLimit(int heightLimit) {
        this.heightLimit = heightLimit;
    }

    @Override
    public String toString() {
        return "AckNoFlyNormal{, forbiddenType=" + this.forbiddenType + ", userType=" + this.userType + ", polygonShape=" + this.polygonShape + ", numEudges=" + this.numEudges + ", attributeByte=" + ((int) this.attributeByte) + ", heightLimit=" + this.heightLimit + ", nfzPointLat=" + this.nfzPointLat + ", nfzPointLon=" + this.nfzPointLon + ", heightLimitRadius=" + this.heightLimitRadius + ", noflyRadius=" + this.noflyRadius + ", startDirAngleLat=" + this.startDirAngleLat + ", startDirAngleLon=" + this.startDirAngleLon + ", endDirAngleLat=" + this.endDirAngleLat + ", endDirAngleLon=" + this.endDirAngleLon + ", nfzPointB1Lat=" + this.nfzPointB1Lat + ", nfzPointB1Lon=" + this.nfzPointB1Lon + ", nfzPointC1Lat=" + this.nfzPointC1Lat + ", nfzPointC1Lon=" + this.nfzPointC1Lon + ", nfzPointA1Lat=" + this.nfzPointA1Lat + ", nfzPointA1Lon=" + this.nfzPointA1Lon + ", nfzPointA2Lat=" + this.nfzPointA2Lat + ", nfzPointA2lon=" + this.nfzPointA2lon + ", nfzPointC2Lat=" + this.nfzPointC2Lat + ", nfzPointC2Lon=" + this.nfzPointC2Lon + ", nfzPointB2Lat=" + this.nfzPointB2Lat + ", nfzPointB2Lon=" + this.nfzPointB2Lon + ", nfzPointD1Lat=" + this.nfzPointD1Lat + ", nfzPointD1Lon=" + this.nfzPointD1Lon + ", nfzPointD2Lat=" + this.nfzPointD2Lat + ", nfzPointD2Lon=" + this.nfzPointD2Lon + '}';
    }
}
