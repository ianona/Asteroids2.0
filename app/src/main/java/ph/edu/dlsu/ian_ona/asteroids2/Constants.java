package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.graphics.Typeface;

public class Constants {
    // singleton for constant values
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static Context MAIN_CONTEXT;
    public static long INIT_TIME;
    public static Typeface PIXEL_FONT;
    public static int HIGHSCORES;
    public static boolean MUTE;

    public static String getTAG(Object o) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        int position = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getFileName().contains(o.getClass().getSimpleName())
                    && !elements[i+1].getFileName().contains(o.getClass().getSimpleName())){
                position = i;
                break;
            }
        }

        StackTraceElement element = elements[position];
        String className = element.getFileName().replace(".java", "");
        return "[" + className + "](" + element.getMethodName() + ":" + element.getLineNumber() + ")";
    }
}
