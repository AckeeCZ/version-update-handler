package com.ackee.versioupdatehandler

import androidx.fragment.app.FragmentManager
import com.ackee.versioupdatehandler.model.DialogSettings
import com.ackee.versioupdatehandler.model.VersionStatus
import com.ackee.versioupdatehandler.model.VersionsConfiguration

/**
 * Class that contains the version comparing and update handling logic.
 */
class VersionStatusResolver(private val fetcher: VersionFetcher) {

    suspend fun checkVersionStatus(version: Int): VersionStatus {
        return checkVersionStatus(version.toLong())
    }

    suspend fun checkVersionStatus(version: Long): VersionStatus {
        val versionConfig = fetcher.fetch()
        return resolveStatus(version, versionConfig)
    }

    private fun resolveStatus(version: Long, configuration: VersionsConfiguration): VersionStatus {
        var status = VersionStatus.UP_TO_DATE
        if (version < configuration.minimalVersion()) {
            status = VersionStatus.UPDATE_REQUIRED
        } else if (version < configuration.currentVersion()) {
            status = VersionStatus.UPDATE_AVAILABLE
        }
        return status
    }

    /**
     * Check for version and show dialog with visual settings defined within [DialogSettings]
     */
    suspend fun checkVersionStatusAndOpenDialog(
        version: Int,
        fragmentManager: FragmentManager,
        settings: DialogSettings = DialogSettings()
    ) {
        val versionStatus = checkVersionStatus(version)
        if (versionStatus != VersionStatus.UP_TO_DATE) {
            showDialog(versionStatus == VersionStatus.UPDATE_REQUIRED, fragmentManager, settings)
        }
    }

    /**
     * Show dialog with update info
     */
    private fun showDialog(
        forceUpdate: Boolean,
        fragmentManager: FragmentManager,
        dialogSettings: DialogSettings
    ) {
        UpdateDialog.newInstance(forceUpdate, dialogSettings).show(fragmentManager, UpdateDialog::class.java.name)
    }
}