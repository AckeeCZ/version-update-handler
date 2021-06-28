package io.github.ackeecz.versionupdatehandler

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.ackeecz.versionupdatehandler.model.DialogSettings

/**
 * Dialog indicating that user should update application.
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
        val builder = AlertDialog.Builder(requireActivity())
        val settings = requireArguments().getParcelable<DialogSettings>(DIALOG_SETTINGS_KEY)
            ?: throw IllegalArgumentException("Dialog settings cannot be null")

        val isForceUpdate = requireArguments().getBoolean(FORCE_UPDATE_KEY, false)
        if (isForceUpdate) {
            isCancelable = false
            builder.setCancelable(false)
        }

        setupTitle(settings, builder)
        setupMessage(settings, builder)
        setupPositiveButton(settings, builder, isForceUpdate)
        setupNegativeButton(settings, builder, isForceUpdate)

        return builder.create()
    }

    private fun setupTitle(settings: DialogSettings, builder: AlertDialog.Builder) {
        var title: String? = "Update app"
        if (settings.title != null) {
            title = settings.title
        }
        if (settings.titleRes > 0) {
            title = getString(settings.titleRes)
        }
        builder.setTitle(title)
    }

    private fun setupMessage(settings: DialogSettings, builder: AlertDialog.Builder) {
        var message: String? = "Your application is outdated. Please update to the newest version"
        if (settings.message != null) {
            message = settings.message
        }
        if (settings.messageRes > 0) {
            message = getString(settings.messageRes)
        }
        builder.setMessage(message)
    }

    private fun setupPositiveButton(
        settings: DialogSettings,
        builder: AlertDialog.Builder,
        isForceUpdate: Boolean
    ) {
        var posButton: String? = "Update"
        if (settings.positiveButton != null) {
            posButton = settings.positiveButton
        }
        if (settings.positiveButtonRes > 0) {
            posButton = getString(settings.positiveButtonRes)
        }
        builder.setPositiveButton(posButton) { _, _ ->
            if (isForceUpdate) {
                requireActivity().finish()
            }
            // getPackageName() from Context or Activity object
            val appPackageName = if (settings.packageName == null) requireActivity().packageName else settings.packageName
            appPackageName?.let { openPlayStore(it) }
        }
    }

    private fun setupNegativeButton(
        settings: DialogSettings,
        builder: AlertDialog.Builder,
        isForceUpdate: Boolean
    ) {
        var negButton: String? = "Cancel"
        if (settings.negativeButton != null) {
            negButton = settings.negativeButton
        }
        if (settings.negativeButtonRes > 0) {
            negButton = getString(settings.negativeButtonRes)
        }
        builder.setNegativeButton(negButton) { _, _ ->
            if (isForceUpdate) {
                requireActivity().moveTaskToBack(true)
            }
        }
    }

    /**
     * Open Play Store on application detail
     */
    private fun openPlayStore(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}
