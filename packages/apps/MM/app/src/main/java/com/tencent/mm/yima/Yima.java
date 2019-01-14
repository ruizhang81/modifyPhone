package com.tencent.mm.yima;

import com.tencent.mm.http.HttpHelp;

public class Yima {

    private static Yima yima;
    private int code = 13665;
    private String mToken;

    public static Yima getInstanse() {
        if (yima == null) {
            yima = new Yima();
        }
        return yima;
    }

    public void getPhone(final HttpHelp.OnCallBackListener onCallBackListener) {
        login(new HttpHelp.OnCallBackListener() {
            @Override
            public void onCallBack(String token) {
                mToken = token;
                http("http://api.fxhyd.cn/UserInterface.aspx?action=getmobile&token=" + mToken + "&itemid=" + code + "&excludeno=", onCallBackListener);
            }
        });
    }

    public void freePhone(String phone, final HttpHelp.OnCallBackListener onCallBackListener) {
        http("http://api.fxhyd.cn/UserInterface.aspx?action=release&token=" + mToken + "&itemid=" + code + "&mobile=" + phone, onCallBackListener);
    }

    public void getResult(final String phone, final HttpHelp.OnCallBackListener onCallBackListener) {
        http("http://api.fxhyd.cn/UserInterface.aspx?action=getsms&token=" + mToken + "&itemid=" + code + "&mobile=" + phone + "&release=1", onCallBackListener);
    }

    private void login(HttpHelp.OnCallBackListener onCallBackListener) {
        String user = "ruizhang81";
        String pass = "123456aaa";
        http("http://api.fxhyd.cn/UserInterface.aspx?action=login&username=" + user + "&password=" + pass, onCallBackListener);
    }

    private void http(String url,final HttpHelp.OnCallBackListener onCallBackListener){
        HttpHelp.http(url,new HttpHelp.OnCallBackListener(){

            @Override
            public void onCallBack(String bufferStr) {
                if (bufferStr.contains("success") &&
                        bufferStr.contains("|")) {
                    String[] result = bufferStr.split("\\|");
                    if (result.length > 1) {
                        String back = bufferStr.split("\\|")[1];
                        onCallBackListener.onCallBack(back);
                    } else {
                        onCallBackListener.onCallBack(null);
                    }
                } else {
                    onCallBackListener.onCallBack(null);
                }
            }
        });
    }

}
