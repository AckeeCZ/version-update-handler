package com.ackee.versioupdatehandler.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Visual settings of dialog
 */
@Parcelize
class DialogSettings(
    val packageName: String?,
    val title: String?,
    val titleRes: Int,
    val message: String?,
    val messageRes: Int,
    val positiveButton: String?,
    val positiveButtonRes: Int,
    val negativeButton: String?,
    val negativeButtonRes: Int
) : Parcelable