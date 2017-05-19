package com.adida.akashjpro.activity.utils;

/**
 * Created by Aka on 1/11/2017.
 */

public class untilsTime {
    public static String getTime(String date, String dateCurren){
        String time = date;

        String nam   = time.substring(6, 8);
        String thang = time.substring(3, 5);
        String ngay  = time.substring(0, 2);
        String gio   = time.substring(9, 11);
        String phut  = time.substring(12, 14);
        String giay  = time.substring(15, 17);


        String timeCurrent =dateCurren;


        String namCurent   = timeCurrent.substring(6, 8);
        String thangCurent = timeCurrent.substring(3, 5);
        String ngayCurent  = timeCurrent.substring(0, 2);
        String gioCurent   = timeCurrent.substring(9, 11);
        String phutCurrent = timeCurrent.substring(12, 14);
        String giayCurrent = timeCurrent.substring(15, 17);

        if (Integer.parseInt(namCurent) - Integer.parseInt(nam) > 0) {

            if (Integer.parseInt(thangCurent) - Integer.parseInt(thang) >=0
                    |Integer.parseInt(namCurent) - Integer.parseInt(nam) >=2 ) {
                return String.valueOf(Integer.parseInt(namCurent)
                        - Integer.parseInt(nam)) + " năm trước";
            }else {
                return String.valueOf(12 - Integer.parseInt(thang)
                        + Integer.parseInt(thangCurent)) + " tháng trước";
            }

        }

        if (Integer.parseInt(thangCurent) - Integer.parseInt(thang) > 0) {
            if (Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) >= 0
                    |Integer.parseInt(thangCurent) - Integer.parseInt(thang) >= 2) {
                    return String.valueOf(Integer.parseInt(thangCurent)
                            - Integer.parseInt(thang)) + " tháng trước";
            }else {
                return String.valueOf(30 - Integer.parseInt(ngay)
                        + Integer.parseInt(ngayCurent)) + " ngày trước";
            }
        }

        if (Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) > 0) {
            if (Integer.parseInt(gioCurent) - Integer.parseInt(gio) >= 0
                    |Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) >= 2) {
                return String.valueOf(Integer.parseInt(ngayCurent)
                        - Integer.parseInt(ngay)) + " ngày trước";
            }else {
                return String.valueOf(24 - Integer.parseInt(gio)
                        + Integer.parseInt(gioCurent)) + " giờ trước";
            }
        }

        if (Integer.parseInt(gioCurent) - Integer.parseInt(gio) > 0) {
            if (Integer.parseInt(phutCurrent) - Integer.parseInt(phut) >= 0
                    |Integer.parseInt(gioCurent) - Integer.parseInt(gio) >= 2) {
                return String.valueOf(Integer.parseInt(gioCurent)
                        - Integer.parseInt(gio)) + " giờ trước";
            }else {
                return String.valueOf(60 - Integer.parseInt(phut)
                        + Integer.parseInt(phutCurrent)) + " phút trước";
            }
        }

        if (Integer.parseInt(phutCurrent) - Integer.parseInt(phut) > 0) {
            if (Integer.parseInt(giayCurrent) - Integer.parseInt(giay) >= 0
                    | Integer.parseInt(phutCurrent) - Integer.parseInt(phut) >= 2) {
                return String.valueOf(Integer.parseInt(phutCurrent)
                        - Integer.parseInt(phut)) + " phút trước";
            }else {
                return String.valueOf(60 - Integer.parseInt(giay)
                        + Integer.parseInt(giayCurrent)) + " giây trước";
            }
        }


        return String.valueOf(Integer.parseInt(giayCurrent)
                - Integer.parseInt(giay))+ " giây trước";
    }


    public static String getTimComment(String date, String dateCurrent){
        String time = date;

        String nam   = time.substring(6, 8);
        String thang = time.substring(3, 5);
        String ngay  = time.substring(0, 2);
        String gio   = time.substring(9, 11);
        String phut  = time.substring(12, 14);
        String giay  = time.substring(15, 17);


        String timeCurrent = dateCurrent;

        String timeCommet = time.substring(9, 14);


        String namCurent   = timeCurrent.substring(6, 8);
        String thangCurent = timeCurrent.substring(3, 5);
        String ngayCurent  = timeCurrent.substring(0, 2);
        String gioCurent   = timeCurrent.substring(9, 11);
        String phutCurrent = timeCurrent.substring(12, 14);
        String giayCurrent = timeCurrent.substring(15, 17);

        if (Integer.parseInt(namCurent) - Integer.parseInt(nam) > 0) {

            if (Integer.parseInt(thangCurent) - Integer.parseInt(thang) >=0
                    |Integer.parseInt(namCurent) - Integer.parseInt(nam) >=2 ) {
                return String.valueOf(Integer.parseInt(namCurent)
                        - Integer.parseInt(nam)) + " năm trước"+ " - " + timeCommet;
            }else {
                return String.valueOf(12 - Integer.parseInt(thang)
                        + Integer.parseInt(thangCurent)) + " tháng trước"+ " - " + timeCommet;
            }

        }

        if (Integer.parseInt(thangCurent) - Integer.parseInt(thang) > 0) {
            if (Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) >= 0
                    |Integer.parseInt(thangCurent) - Integer.parseInt(thang) >= 2) {
                return String.valueOf(Integer.parseInt(thangCurent)
                        - Integer.parseInt(thang)) + " tháng trước"+ " - " + timeCommet;
            }else {
                return String.valueOf(30 - Integer.parseInt(ngay)
                        + Integer.parseInt(ngayCurent)) + " ngày trước"  + " - " + timeCommet;
            }
        }

        if (Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) > 0) {
            if (Integer.parseInt(gioCurent) - Integer.parseInt(gio) >= 0
                    |Integer.parseInt(ngayCurent) - Integer.parseInt(ngay) >= 2) {
                return String.valueOf(Integer.parseInt(ngayCurent)
                        - Integer.parseInt(ngay)) + " ngày trước" + " - " + timeCommet;
            }else {
                return String.valueOf(24 - Integer.parseInt(gio)
                        + Integer.parseInt(gioCurent)) + " giờ trước";
            }
        }

        if (Integer.parseInt(gioCurent) - Integer.parseInt(gio) > 0) {
            if (Integer.parseInt(phutCurrent) - Integer.parseInt(phut) >= 0
                    |Integer.parseInt(gioCurent) - Integer.parseInt(gio) >= 2) {
                return String.valueOf(Integer.parseInt(gioCurent)
                        - Integer.parseInt(gio)) + " giờ trước";
            }else {
                return String.valueOf(60 - Integer.parseInt(phut)
                        + Integer.parseInt(phutCurrent)) + " phút trước";
            }
        }

        if (Integer.parseInt(phutCurrent) - Integer.parseInt(phut) > 0) {
            if (Integer.parseInt(giayCurrent) - Integer.parseInt(giay) >= 0
                    | Integer.parseInt(phutCurrent) - Integer.parseInt(phut) >= 2) {
                return String.valueOf(Integer.parseInt(phutCurrent)
                        - Integer.parseInt(phut)) + " phút trước";
            }else {
                return String.valueOf(60 - Integer.parseInt(giay)
                        + Integer.parseInt(giayCurrent)) + " giây trước";
            }
        }


        return String.valueOf(Integer.parseInt(giayCurrent)
                - Integer.parseInt(giay))+ " giây trước";
    }
}
