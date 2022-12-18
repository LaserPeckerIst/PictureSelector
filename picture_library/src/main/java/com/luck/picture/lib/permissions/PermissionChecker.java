package com.luck.picture.lib.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author：luck
 * @date：2019-11-20 19:07
 * @describe：权限检查
 */
public class PermissionChecker {

    /**
     * 检查是否有某个权限
     *
     * @param ctx
     * @param permission
     * @return
     */
    public static boolean checkSelfPermission(Context ctx, String permission) {
        String[] permissions = new String[]{permission};
        if ((Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) ||
                Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //android 13
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO};
        }
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(ctx.getApplicationContext(), p)
                    == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 动态申请多个权限
     *
     * @param activity
     * @param code
     */
    public static void requestPermissions(Activity activity, @NonNull String[] permissions, int code) {
        List<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String p : permissions) {
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(p) ||
                        Manifest.permission.READ_EXTERNAL_STORAGE.equals(p)) {
                    list.add(Manifest.permission.READ_MEDIA_IMAGES);
                    list.add(Manifest.permission.READ_MEDIA_VIDEO);
                } else {
                    list.add(p);
                }
            }
        } else {
            list.addAll(Arrays.asList(permissions));
        }
        ActivityCompat.requestPermissions(activity, list.toArray(new String[list.size()]), code);
    }


    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings(Context context) {
        Context applicationContext = context.getApplicationContext();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + applicationContext.getPackageName()));
        if (!isIntentAvailable(context, intent)) return;
        applicationContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private static boolean isIntentAvailable(Context context, final Intent intent) {
        return context.getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
}
