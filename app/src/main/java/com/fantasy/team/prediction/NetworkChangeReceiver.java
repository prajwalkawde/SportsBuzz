package com.fantasy.team.prediction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

 

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean isConnected = isNetworkConnected(context);

            // Handle the connectivity change here
            if (isConnected) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Internet is connected
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);

                if(dialog != null)
                dialog.dismiss();

            } else {
                showCustomDialog(context);

                // Internet is not connected
            }
        }
    }

    private void showCustomDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the LayoutInflater from the current context
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout using the obtained LayoutInflater
        View dialogView = inflater.inflate(R.layout.no_internet_design, null);

        dialogView.findViewById(R.id.restart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(context,SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(i);

            }
        });


        // Set the custom layout to the dialog
        builder.setView(dialogView);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    // Optional: You can define an onClick method for the dialog button
    public void dismissDialog(View view) {
        AlertDialog dialog = (AlertDialog) view.getParent().getParent();
        dialog.dismiss();
    }


    // Check if the device is connected to the internet
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
