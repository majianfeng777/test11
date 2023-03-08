package com.huaweicloud.sdk.iot.waterqualitytesting.list;

public class listViewInfor_item {
    private String text_infor;
    private String text_infornum;
    public listViewInfor_item(String text_infor, String text_infornum){
        this.text_infor=text_infor;
        this.text_infornum=text_infornum;
    }

    public String getText_infor() {
        return text_infor;
    }

    public String getText_infornum() {
        return text_infornum;
    }
}
