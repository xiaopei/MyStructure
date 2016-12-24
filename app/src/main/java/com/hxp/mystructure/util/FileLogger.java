package com.hxp.mystructure.util;


import com.hxp.mystructure.app.ApplicationWithCrashHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hxp on 2016/12/23.
 * debug模式时才会往
 */
public class FileLogger {

    public static BufferedWriter mWriter;

    public static final boolean LOG_TO_FILE = ILog.DEBUG;
    public static final String PATH = CacheFileUtils.getInstance().getExternalSdPath(false) + "/"+ ApplicationWithCrashHandler.LABLE+"_log/";
    public static synchronized void init() {
        if (!LOG_TO_FILE) {
            return;
        }
        try {
            File dir = new File(PATH);
            ILog.i("PATH",PATH+"");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            String logName = PATH + date + ".txt";
            mWriter = new BufferedWriter(new FileWriter(logName, true));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static synchronized void writeLine(String tag, String text) {
        if (mWriter == null) {
            return;
        }
        try {
            String time = new SimpleDateFormat("MM-dd hh:mm:ss:SSS").format(new Date(System.currentTimeMillis()));
            text = time + " >>> " + tag + " : " + text;
            mWriter.write(text);
            mWriter.newLine();
            mWriter.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static synchronized void close() {
        if (mWriter != null) {
            try {
                mWriter.close();
            } catch (IOException e) {}
            mWriter = null;
        }
    }

}
