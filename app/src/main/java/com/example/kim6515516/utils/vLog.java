package com.example.kim6515516.utils;

import android.util.Log;

/**
 * Created by kim6515516 on 2016-03-27.
 */
public class vLog  {

    public static void trace(final String msg)
    {
        if (ApplicationConstants.DEBUG)
        {
            final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.d(ApplicationConstants.TAG, "#" + lineNumber + " " + className + "." + methodName + "() : " + msg);
        }
    }
}
