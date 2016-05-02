package com.hcjcch.garbage.event;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 18:20
 */

public class DeleteFileEvent {
    private String absolutePath;

    public DeleteFileEvent(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
}
