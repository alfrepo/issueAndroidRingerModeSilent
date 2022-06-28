package com.example.ringermodesilenttest.permissions;

import android.Manifest;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ringermodesilenttest.MainActivity;

import java.util.function.BiConsumer;

public class PermissionController {

    public static final String TAG =  PermissionController.class.toString();

    static AlertDialog alertDialog;

    public boolean checkPermissions() {
        return Permission.get(context()).haveRequiredPermissions();
    }


    public void requestPermissions() {
        final Permission p = Permission.get(context());

        // Ask the user for permission to use their location, if it wasn't granted already
        if (!p.haveRequiredPermissions()) {
            // Permission is not granted (yet)
            // We ask the user for permission


            if (!p.haveDnDPermission) {
                showPermissionsDialogue( "Do not disturb control. To regulate the phone volume, when the phone is in \"Do Not Disturb Mode\" the app requires your permissions." ,
                        (DialogInterface d, Integer id) -> {
                            final Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context().startActivity(intent);

                            // close the dialogue
                            d.dismiss();
                        }
                );
                return;
            }
        }
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Context context() {
        return activity();
    }

    private MainActivity activity() {
        return MainActivity.getMainActivity();
    }

    private void showPermissionsDialogue(String message, BiConsumer<DialogInterface, Integer> functionOnOk) {
        if (PermissionController.alertDialog != null) {
            Log.i(TAG, "Asked to show a permission dialogue, but one is already open. Skip request.");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity());
        builder.setMessage(message);

        builder.setCancelable(false);

        // Add the buttons
        builder.setPositiveButton("Grant permissions", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                functionOnOk.accept(dialog, id);
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // forget the dialog when it is removed
                PermissionController.alertDialog = null;
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // remember the new dialog, to avoid double opening
                PermissionController.alertDialog = alertDialog;
            }
        });

        alertDialog.show();
    }


    public static class Permission {
        public final boolean haveFineGraned;

        public final boolean haveBackground;
        /*
            permission ACCESS_BACKGROUND_LOCATION is required on a device that runs Android 11 (API level 30) or higher
            https://developer.android.com/about/versions/oreo/background-location-limits
         */
        public final boolean platformRequiresBackground;

        public final boolean haveCoarse;
        public final boolean haveDnDPermission;

        public Permission(Context context) {

            haveFineGraned = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;

            haveBackground = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;

            haveCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;

            platformRequiresBackground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;

            haveDnDPermission = isNotificationPolicyAccessGranted(context);
        }

        public static Permission get(Context context) {
            return new Permission(context);
        }

        public boolean haveRequiredPermissions() {
            // Ask the user for permission to use their location, if it wasn't granted already
            return haveFineGraned &&
                    (haveBackground || !platformRequiresBackground) &&
                    haveCoarse &&
                    haveDnDPermission;
        }
    }

    private static boolean isNotificationPolicyAccessGranted(Context context) {

        // TODO - try disabling the DnD check for the instrumentation tests,
        // as @Rule in InstrumentationTest doesnt seem to work for "Do Not Disturb"
        if (isTesting() ) {
            return true;
        }

        return getNotificationManager(context).isNotificationPolicyAccessGranted();
    }

    // TODO - port it to ussing some environment variable, which the tests themselfs will set
    private static boolean isTesting() {
        try {
            Class.forName("digital.alf.geosound.JustOpenAllWindows");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
