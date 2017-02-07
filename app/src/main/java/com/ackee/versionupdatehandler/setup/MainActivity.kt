package com.ackee.versionupdatehandler.setup

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ackee.versionupdatehandler.R
import com.ackee.versioupdatehandler.VersionStatusResolver
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration
import com.ackee.versioupdatehandler.model.DialogSettings
import com.ackee.versioupdatehandler.model.VersionStatus
import org.jetbrains.anko.*
import rx.Single
import java.util.*

/**
 * Activity with samples

 * @author David Bilik [david.bilik@ackee.cz]
 * @since 07/02/2017
 **/
class MainActivity : AppCompatActivity() {
    companion object {
        val TAG: String = MainActivity::class.java.name
    }


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
                        VersionStatusResolver({
                            Single.just(BasicVersionsConfiguration(10, 15))
                        }).checkVersionStatusAndOpenDefault(8, supportFragmentManager, DialogSettings.Builder()
                                .title("My custom title")
                                .messageRes(R.string.update_dialog_message)
                                .positiveButton("Yaay")
                                .negativeButton("Never")
                                .build()
                        )
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }

                button {
                    text = "Custom UI"
                    onClick {
                        VersionStatusResolver({
                            Single.just(BasicVersionsConfiguration(10, 15))
                        }).checkVersionStatus(if (Random().nextInt() % 2 == 0) 8 else 12)
                                .subscribe {
                                    when (it) {
                                        VersionStatus.UPDATE_AVAILABLE -> toast("Update is available")
                                        VersionStatus.UPDATE_REQUIRED -> toast("Mandatory update is available")
                                    }
                                }
                    }
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }
            }
        }
    }

    private fun checkWithVersion(version: Int) {
        // replace fetcher with some real one
        VersionStatusResolver({
            Single.just(BasicVersionsConfiguration(10, 15))
        }).checkVersionStatusAndOpenDefault(version, supportFragmentManager)
    }
}