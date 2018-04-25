package com.yzsn.adownlader.util.log;

import android.util.Log;

/**
 * 打印工具
 * Created by zc.
 */
public class L {
    private static final String LINE = "------------------------------------------------------------" +
            "------------------------------------------------------------------------------------------------------------------------";
    private static LAdapter mLAdapter;

    public static void initLAdapter(LAdapter mLAdapter) {
        L.mLAdapter = mLAdapter;
    }

    public static void e(String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.e(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.e(mLAdapter.getTag(), content);
            }
        }
    }

    public static void i(String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.i(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.i(mLAdapter.getTag(), content);
            }
        }
    }

    public static void d(String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.d(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.d(mLAdapter.getTag(), content);
            }
        }
    }

    public static void v(String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.v(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.v(mLAdapter.getTag(), content);
            }
        }
    }

    public static void w(String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.w(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.w(mLAdapter.getTag(), content);
            }
        }
    }

    public static void e(boolean isNeedOtherInfo, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (isNeedOtherInfo) {
                Log.e(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.e(mLAdapter.getTag(), content);
            }
        }
    }

    public static void i(boolean isNeedOtherInfo, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (isNeedOtherInfo) {
                Log.i(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.i(mLAdapter.getTag(), content);
            }
        }
    }

    public static void d(boolean isNeedOtherInfo, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (isNeedOtherInfo) {
                Log.d(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.d(mLAdapter.getTag(), content);
            }
        }
    }

    public static void v(boolean isNeedOtherInfo, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (isNeedOtherInfo) {
                Log.v(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.v(mLAdapter.getTag(), content);
            }
        }
    }

    public static void w(boolean isNeedOtherInfo, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (isNeedOtherInfo) {
                Log.w(mLAdapter.getTag(), getDescription() + content);
            } else {
                Log.w(mLAdapter.getTag(), content);
            }
        }
    }


    public static void e(String tag, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.e(tag, getDescription() + content);
            } else {
                Log.e(tag, content);
            }
        }
    }

    public static void i(String tag, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.i(tag, getDescription() + content);
            } else {
                Log.i(tag, content);
            }
        }
    }

    public static void d(String tag, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.d(tag, getDescription() + content);
            } else {
                Log.d(tag, content);
            }
        }
    }

    public static void v(String tag, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.v(tag, getDescription() + content);
            } else {
                Log.v(tag, content);
            }
        }
    }

    public static void w(String tag, String content) {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            if (mLAdapter.isNeedOtherInfo()) {
                Log.w(tag, getDescription() + content);
            } else {
                Log.w(tag, content);
            }
        }
    }

    public static void lineE() {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            Log.e(mLAdapter.getTag(), LINE);
        }
    }

    public static void lineI() {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            Log.i(mLAdapter.getTag(), LINE);
        }
    }

    public static void lineD() {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            Log.d(mLAdapter.getTag(), LINE);
        }
    }

    public static void lineV() {
        if (mLAdapter == null) {
            return;
        }
        if (mLAdapter.isDebug()) {
            Log.v(mLAdapter.getTag(), LINE);
        }
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     */
    private static String getDescription() {
        String[] infos = new String[]{"", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length >= 5) {
//            infos[0] = elements[4].getClassName();      //com.aframe.activity.LaucherActivity$1
            String name = elements[4].getClassName();
            if (name.contains("$")) {
                infos[0] = name.substring(name.lastIndexOf(".") + 1, name.indexOf("$", 0));
            } else {
                infos[0] = name.substring(name.lastIndexOf(".") + 1);
            }
            infos[1] = elements[4].getMethodName();
            infos[2] = "line = " + elements[4].getLineNumber();
        } else {
            Log.e("L.java", "Stack is too shallow!!!");
        }

        return infos[0] + " -> " + infos[1] + "() -> " + infos[2] + "\n";
    }

}
