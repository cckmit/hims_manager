package com.cmpay.lemon.monitor.enums;

/**
 * 菜单枚举值
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
public enum MenuEnum {
    /**
     * 目录
     */
    CATALOG(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    private int value;

    MenuEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
