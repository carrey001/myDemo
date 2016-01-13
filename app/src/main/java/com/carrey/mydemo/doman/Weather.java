package com.carrey.mydemo.doman;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2016/1/13 12:06
 */

public class Weather {

    /**
     * city : 北京
     * cityid : 101010100
     * temp : 9
     * WD : 西南风
     * WS : 2级
     * SD : 22%
     * WSE : 2
     * time : 10:45
     * isRadar : 1
     * Radar : JC_RADAR_AZ9010_JB
     * njd : 暂无实况
     * qy : 1014
     */

    public WeatherinfoEntity weatherinfo;

    public static class WeatherinfoEntity {
        public String city;
        public String cityid;
        public String temp;
        public String WD;
        public String WS;
        public String SD;
        public String WSE;
        public String time;
        public String isRadar;
        public String Radar;
        public String njd;
        public String qy;

        @Override
        public String toString() {
            return "WeatherinfoEntity{" +
                    "city='" + city + '\'' +
                    ", cityid='" + cityid + '\'' +
                    ", temp='" + temp + '\'' +
                    ", WD='" + WD + '\'' +
                    ", WS='" + WS + '\'' +
                    ", SD='" + SD + '\'' +
                    ", WSE='" + WSE + '\'' +
                    ", time='" + time + '\'' +
                    ", isRadar='" + isRadar + '\'' +
                    ", Radar='" + Radar + '\'' +
                    ", njd='" + njd + '\'' +
                    ", qy='" + qy + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherinfo=" + weatherinfo +
                '}';
    }
}
