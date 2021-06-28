package io.github.ackeecz.versionupdatehandler.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Visual settings of dialog
 */
@Parcelize
class DialogSettings(
    val packageName: String? = null,
    val title: String? = null,
    val titleRes: Int = -1,
    val message: String? = null,
    val messageRes: Int = -1,
    val positiveButton: String? = null,
    val positiveButtonRes: Int = -1,
    val negativeButton: String? = null,
    val negativeButtonRes: Int = -1
) : Parcelable
