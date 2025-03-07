package com.fantasy.team.prediction;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NotificationPermissionHelper {

    // Check if notification permission is granted
    public static boolean isNotificationPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            // For versions below Android 13, notifications are enabled by default
            return true;
        }
    }

    // Request notification permission
    public static void requestNotificationPermission(final Context context) {
        if (!isNotificationPermissionGranted(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Use system-generated dialog for Android 13 and above
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101 // Request code
                );
            } else {
                // For older versions, show a custom dialog to guide users to app settings
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enable Notifications");
                builder.setMessage("Please enable notifications to receive updates.");

                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openNotificationSettings(context);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        }
    }

    // Open notification settings
    private static void openNotificationSettings(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }
}