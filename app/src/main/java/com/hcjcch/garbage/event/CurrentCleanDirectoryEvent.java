package com.hcjcch.garbage.event;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 16:23
 */

public class CurrentCleanDirectoryEvent {

    private String absolutePath;

    public CurrentCleanDirectoryEvent(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
}
