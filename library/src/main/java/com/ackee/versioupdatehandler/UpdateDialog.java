package com.ackee.versioupdatehandler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ackee.versioupdatehandler.model.DialogSettings;

/**
 * Dialog indicating that user should update application.
 * <p>
 * Texts and package name are customizable via {@link DialogSettings} class
 **/
public class UpdateDialog extends DialogFragment {

    private static final String FORCE_UPDATE_KEY = "force_update";
    private static final String DIALOG_SETTINGS_KEY = "dialog_settings";

    public static UpdateDialog newInstance(boolean forceUpdate, DialogSettings dialogSettings) {
        Bundle args = new Bundle();
        args.putBoolean(FORCE_UPDATE_KEY, forceUpdate);
        args.putParcelable(DIALOG_SETTINGS_KEY, dialogSettings);
        UpdateDialog updateDialog = new UpdateDialog();
        updateDialog.setArguments(args);
        return updateDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final DialogSettings settings = getArguments().getParcelable(DIALOG_SETTINGS_KEY);
        if (settings == null) {
            throw new IllegalArgumentException("Dialog settings cannot be null");
        }
        final boolean isForceUpdate = getArguments().getBoolean(FORCE_UPDATE_KEY, false);
        if(isForceUpdate) {
            setCancelable(false);
            builder.setCancelable(false);
        }
        String title = "Update app";
        if (settings.getTitle() != null) {
            title = settings.getTitle();
        }
        if (settings.getTitleRes() > 0) {
            title = getString(settings.getTitleRes());
        }
        builder.setTitle(title);
        String message = "Your application is outdated. Please update to the newest version";
        if (settings.getMessage() != null) {
            message = settings.getMessage();
        }
        if (settings.getMessageRes() > 0) {
            message = getString(settings.getMessageRes());
        }
        builder.setMessage(message);

        String posButton = "Update";
        if (settings.getPositiveButton() != null) {
            posButton = settings.getPositiveButton();
        }
        if (settings.getPositiveButtonRes() > 0) {
            posButton = getString(settings.getPositiveButtonRes());
        }

        builder.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    getActivity().finish();
                }
                final String appPackageName = settings.getPackageName() == null ? getActivity().getPackageName() : settings.getPackageName(); // getPackageName() from Context or Activity object
                openPlayStore(appPackageName);
            }
        });
        String negButton = "Cancel";
        if (settings.getNegativeButton() != null) {
            negButton = settings.getNegativeButton();
        }
        if (settings.getNegativeButtonRes() > 0) {
            negButton = getString(settings.getNegativeButtonRes());
        }
        builder.setNegativeButton(negButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    getActivity().moveTaskToBack(true);
                }
            }
        });
        return builder.create();
    }

    /**
     * Open Play Store on application detail
     *
     * @param appPackageName package name of application
     */
    private void openPlayStore(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
