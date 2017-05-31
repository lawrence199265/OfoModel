package com.zhuangbudong.ofo.event;

import com.hwangjr.rxbus.RxBus;

/**
 * Created by xxx on 17/5/11.
 */

public class InputPriceEvent implements InputEvent {
    private String input;
    private int checkIndex;

    @Override
    public void input(String input) {
        this.setInput(input);
        RxBus.get().post(this);
    }

    public void input(String input, int checkIndex) {
        this.setInput(input);
        this.setCheckIndex(checkIndex);
        RxBus.get().post(BusAction.TAG_INPUT_PRICE, this);
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setCheckIndex(int checkIndex) {
        this.checkIndex = checkIndex;
    }

    public String getInput() {
        return input;
    }

    public int getCheckIndex() {
        return checkIndex;
    }
}
