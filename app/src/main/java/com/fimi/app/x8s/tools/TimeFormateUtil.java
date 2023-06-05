package com.fimi.app.x8s.tools;

/* loaded from: classes.dex */
public class TimeFormateUtil {
    public static String getRecordTime(int time) {
        if (time == 0) {
            return "00:00";
        }
        if (time < 60) {
            if (time < 10) {
                return "00:0" + time;
            }
            return "00:" + time;
        } else if (time >= 60) {
            int min = time / 60;
            int sencode = time % 60;
            if (min < 10) {
                if (sencode < 10) {
                    return "0" + min + ":0" + sencode;
                }
                return "0" + min + ":" + sencode;
            } else if (sencode < 10) {
                return min + ":0" + sencode;
            } else {
                return min + ":" + sencode;
            }
        } else {
            return "00:00";
        }
    }

    public static String getRecordTime(int hour, int min, int second) {
        if (hour <= 0) {
            if (min > 0) {
                if (min < 10) {
                    if (second >= 10) {
                        String recordTime = "0" + min + ":" + second;
                        return recordTime;
                    }
                    String recordTime2 = "0" + min + ":0" + second;
                    return recordTime2;
                } else if (second >= 10) {
                    String recordTime3 = min + ":" + second;
                    return recordTime3;
                } else {
                    String recordTime4 = min + ":0" + second;
                    return recordTime4;
                }
            } else if (second >= 10) {
                String recordTime5 = "00:" + second;
                return recordTime5;
            } else {
                String recordTime6 = "00:0" + second;
                return recordTime6;
            }
        } else if (min > 0) {
            if (min < 10) {
                if (second >= 10) {
                    String recordTime7 = hour + ":0" + min + ":" + second;
                    return recordTime7;
                }
                String recordTime8 = hour + ":0" + min + ":0" + second;
                return recordTime8;
            } else if (second >= 10) {
                String recordTime9 = hour + ":" + min + ":" + second;
                return recordTime9;
            } else {
                String recordTime10 = hour + ":" + min + ":0" + second;
                return recordTime10;
            }
        } else if (second >= 10) {
            String recordTime11 = hour + ":00:" + second;
            return recordTime11;
        } else {
            String recordTime12 = hour + ":00:0" + second;
            return recordTime12;
        }
    }

    public static void main(String[] args) {
        System.out.println(getRecordTime(0, 59, 30));
    }
}
