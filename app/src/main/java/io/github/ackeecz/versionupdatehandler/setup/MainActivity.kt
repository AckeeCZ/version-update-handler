package io.github.ackeecz.versionupdatehandler.setup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.ackeecz.versionupdatehandler.sample.R
import io.github.ackeecz.versionupdatehandler.VersionFetcher
import io.github.ackeecz.versionupdatehandler.VersionStatusResolver
import io.github.ackeecz.versionupdatehandler.model.BasicVersionsConfiguration
import io.github.ackeecz.versionupdatehandler.model.DialogSettings
import io.github.ackeecz.versionupdatehandler.model.VersionStatus
import io.github.ackeecz.versionupdatehandler.model.VersionsConfiguration
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk21.coroutines.onClick
import java.util.Random

/**
 * Activity with samples
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollView {
            verticalLayout {
                padding = dip(16)
                button {
                    text = "Not mandatory update"
                    onClick {
                        checkWithVersion(13)
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }

                button {
                    text = "Mandatory update"
                    onClick {
                        checkWithVersion(8)
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }

                button {
                    text = "Customized dialog"
                    onClick {
                        getDefaultStatusResolver().checkVersionStatusAndOpenDialog(8, supportFragmentManager, DialogSettings(
                            title = "My custom title",
                            messageRes = R.string.update_dialog_message,
                            positiveButton = "Yaay",
                            negativeButton = "Never"
                        ))
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }

                button {
                    text = "Custom UI"
                    onClick {
                        when (getDefaultStatusResolver().checkVersionStatus(if (Random().nextInt() % 2 == 0) 8 else 12)) {
                            VersionStatus.UPDATE_AVAILABLE -> toast("Update is available")
                            VersionStatus.UPDATE_REQUIRED -> toast("Mandatory update is available")
                            VersionStatus.UP_TO_DATE -> toast("Up to date")
                        }
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }
            }
        }
    }

    private suspend fun checkWithVersion(version: Int) {
        // replace fetcher with some real one
        getDefaultStatusResolver().checkVersionStatusAndOpenDialog(version, supportFragmentManager)
    }

    private fun getDefaultStatusResolver(minimalVersion: Long = 10, currentVersion: Long = 15): VersionStatusResolver {
        return VersionStatusResolver(object : VersionFetcher {
            override suspend fun fetch(): VersionsConfiguration {
                return BasicVersionsConfiguration(minimalVersion, currentVersion)
            }
        })
    }
}
