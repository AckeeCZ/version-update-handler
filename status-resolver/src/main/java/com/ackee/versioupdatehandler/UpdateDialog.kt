package com.ackee.versioupdatehandler

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ackee.versioupdatehandler.model.DialogSettings

/**
 * Dialog indicating that user should update application.
 *
 *
 * Texts and package name are customizable via [DialogSettings] class
 */
class UpdateDialog : DialogFragment() {

    companion object {
        private const val FORCE_UPDATE_KEY = "force_update"
        private const val DIALOG_SETTINGS_KEY = "dialog_settings"
        fun newInstance(forceUpdate: Boolean, dialogSettings: DialogSettings?): UpdateDialog {
            val args = Bundle()
            args.putBoolean(FORCE_UPDATE_KEY, forceUpdate)
            args.putParcelable(DIALOG_SETTINGS_KEY, dialogSettings)
            val updateDialog = UpdateDialog()
            updateDialog.arguments = args
            return updateDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val settings: DialogSettings =
            arguments!!.getParcelable(DIALOG_SETTINGS_KEY) ?: throw IllegalArgumentException("Dialog settings cannot be null")
        val isForceUpdate = arguments!!.getBoolean(FORCE_UPDATE_KEY, false)
        if (isForceUpdate) {
            isCancelable = false
            builder.setCancelable(false)
        }
        var title: String? = "Update app"
        if (settings.title != null) {
            title = settings.title
        }
        if (settings.titleRes > 0) {
            title = getString(settings.titleRes)
        }
        builder.setTitle(title)
        var message: String? = "Your application is outdated. Please update to the newest version"
        if (settings.message != null) {
            message = settings.message
        }
        if (settings.messageRes > 0) {
            message = getString(settings.messageRes)
        }
        builder.setMessage(message)
        var posButton: String? = "Update"
        if (settings.positiveButton != null) {
            posButton = settings.positiveButton
        }
        if (settings.positiveButtonRes > 0) {
            posButton = getString(settings.positiveButtonRes)
        }
        builder.setPositiveButton(posButton) { _, _ ->
            if (isForceUpdate) {
                activity!!.finish()
            }
            val appPackageName =
                if (settings.packageName == null) activity!!.packageName else settings.packageName // getPackageName() from Context or Activity object
            appPackageName?.let { openPlayStore(it) }
        }
        var negButton: String? = "Cancel"
        if (settings.negativeButton != null) {
            negButton = settings.negativeButton
        }
        if (settings.negativeButtonRes > 0) {
            negButton = getString(settings.negativeButtonRes)
        }
        builder.setNegativeButton(negButton) { _, _ ->
            if (isForceUpdate) {
                activity!!.moveTaskToBack(true)
            }
        }
        return builder.create()
    }

    /**
     * Open Play Store on application detail
     *
     * @param appPackageName package name of application
     */
    private fun openPlayStore(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}