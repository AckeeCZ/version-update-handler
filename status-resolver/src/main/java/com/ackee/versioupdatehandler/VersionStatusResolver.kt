package com.ackee.versioupdatehandler

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.ackee.versioupdatehandler.model.DialogSettings
import com.ackee.versioupdatehandler.model.VersionStatus
import com.ackee.versioupdatehandler.model.VersionsConfiguration

/**
 * Class that contains the version comparing and update handling logic.
 */
class VersionStatusResolver(private val fetcher: VersionFetcher) {

    suspend fun checkVersionStatus(version: Int): VersionStatus {
        val versionConfig = fetcher.fetch()
        return resolveStatus(version, versionConfig)
    }

    private fun resolveStatus(version: Int, configuration: VersionsConfiguration): VersionStatus {
        var status = VersionStatus.UP_TO_DATE
        if (version < configuration.minimalVersion()) {
            status = VersionStatus.UPDATE_REQUIRED
        } else if (version < configuration.currentVersion()) {
            status = VersionStatus.UPDATE_AVAILABLE
        }
        Log.d("VersionStatusResolver", "Resolving version status... App version:" + version +
            " minimal version: " + configuration.minimalVersion() +
            " current version: " + configuration.minimalVersion() + " Status resolved:  " + status)
        return status
    }
    /**
     * Check for version and show dialog with customized visual settings via [DialogSettings]
     *
     * @param version         current version
     * @param fragmentManager fragment manager used for showing dialog fragment
     */
    /**
     * Check for version and show dialog with default settings
     *
     * @param version         current version
     * @param fragmentManager fragment manager used for showing dialog fragment
     */
    suspend fun checkVersionStatusAndOpenDefault(
        version: Int,
        fragmentManager: FragmentManager,
        settings: DialogSettings = DialogSettings.Builder().build()
    ) {
        val versionStatus = checkVersionStatus(version)
        if (versionStatus != VersionStatus.UP_TO_DATE) {
            showDialog(versionStatus == VersionStatus.UPDATE_REQUIRED, fragmentManager, settings)
        }
    }

    /**
     * Show dialog with update info
     *
     * @param forceUpdate indicator if update is mandatory
     */
    private fun showDialog(
        forceUpdate: Boolean,
        fragmentManager: FragmentManager,
        dialogSettings: DialogSettings
    ) {
        UpdateDialog.newInstance(forceUpdate, dialogSettings).show(fragmentManager, UpdateDialog::class.java.name)
    }
}