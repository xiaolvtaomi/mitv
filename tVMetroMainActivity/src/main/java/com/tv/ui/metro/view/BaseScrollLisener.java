package com.tv.ui.metro.view;

/**
 * Created by lml on 16/12/27.
 */

public interface BaseScrollLisener {
    public void focusMoveToLeft();
    public void focusMoveToRight();
    public void focusMoveToUp();
    public void scrollToLeft(boolean fullscroll);
    public void scrollToRight(boolean fullscroll);
}
