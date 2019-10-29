package com.zgdj.project;

import java.util.List;

public class Test {

    /**
     * title : 电机参数
     * data : [{"name":"电机转速","value":10.5,"unit":"r/min"},{"name":"传动轴温","value":32,"unit":"℃"},{"name":"非传动轴温","value":32.7,"unit":"℃"},{"name":"U相温度1","value":42.6,"unit":"℃"},{"name":"V相温度1","value":41.6,"unit":"℃"},{"name":"W相温度1","value":42.5,"unit":"℃"},{"name":"U相温度2","value":41.9,"unit":"℃"},{"name":"V相温度2","value":43.5,"unit":"℃"},{"name":"W相温度2","value":41.4,"unit":"℃"},{"name":"测漏仪","value":46.3,"unit":"℃"}]
     */

    private String title;
    private List<DataBean> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 电机转速
         * value : 10.5
         * unit : r/min
         */

        private String name;
        private double value;
        private String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
