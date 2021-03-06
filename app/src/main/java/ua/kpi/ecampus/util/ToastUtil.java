package ua.kpi.ecampus.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtil contains utility methods related with the Toast class.
 *
 * Created by Administrator on 09.02.2016.
 */
public class ToastUtil {

    private ToastUtil() {}

    public static void showError(final String message, final Context context) {
        getToast(message, context).show();
    }

    public static void showShortMessage(String message, Context context) {
        getToast(message, context, Toast.LENGTH_SHORT).show();
    }

    private static Toast getToast(String message, Context context) {
        return getToast(message, context, Toast.LENGTH_LONG);
    }

    private static Toast getToast(String message, Context context, int length) {
        return Toast.makeText(context, message, length);
    }
}
