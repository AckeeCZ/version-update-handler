package com.ackee.versionupdatehandler.setup

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ackee.versioupdatehandler.VersionStatusResolver
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration
import org.jetbrains.anko.*
import rx.Single

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
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }

                button {
                    text = "Custom UI"
                }.lparams(width = matchParent) {
                    bottomMargin = dip(16)
                }
            }
        }
    }

    private fun checkWithVersion(version: Int) {
        VersionStatusResolver({
            Single.just(BasicVersionsConfiguration(10, 15))
        }).checkVersionStatusAndOpenDefault(version, supportFragmentManager)
    }
}