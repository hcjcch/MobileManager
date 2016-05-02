package com.hcjcch.garbage.contract;


/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/30 15:22
 */

public interface GarbageCleanContract {
    interface View {
        void setGarbageCleanBg(int R, int G, int B);

        void setCleanText(String text);

        void setGarbageNumberText(long number);

        void setCleaningText();

        void setFinishCleaningText();

        void setScanText();

        void setFinishScanText();

        void setGarbageNumberVisible(boolean visible);

        void resetText();
    }

    interface Presenter {
        void init();

        void onCreate();

        void startClean();

    }
}