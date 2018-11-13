package com.tencent.mm.action;

public interface ActionBase {

    void run(ActionBaseListener listener, String... args);

}
