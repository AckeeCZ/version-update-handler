package com.ackee.versioupdatehandler.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Visual settings of dialog
 */
class DialogSettings : Parcelable {

    companion object {
        val TAG = DialogSettings::class.java.name
        val CREATOR: Parcelable.Creator<DialogSettings> = object : Parcelable.Creator<DialogSettings> {
            override fun createFromParcel(parcelableValue: Parcel): DialogSettings? {
                return DialogSettings(parcelableValue)
            }

            override fun newArray(size: Int): Array<DialogSettings?> {
                return arrayOfNulls(size)
            }
        }
    }

    var packageName: String?
    var title: String?
    var titleRes: Int
    var message: String?
    var messageRes: Int
    var positiveButton: String?
    var positiveButtonRes: Int
    var negativeButton: String?
    var negativeButtonRes: Int

    private constructor(builder: Builder) {
        packageName = builder.packageName
        title = builder.title
        titleRes = builder.titleRes
        message = builder.message
        messageRes = builder.messageRes
        negativeButton = builder.negativeButton
        positiveButton = builder.positiveButton
        positiveButtonRes = builder.positiveButtonRes
        negativeButtonRes = builder.negativeButtonRes
    }

    protected constructor(parcelableValue: Parcel) {
        packageName = parcelableValue.readString()
        title = parcelableValue.readString()
        message = parcelableValue.readString()
        positiveButton = parcelableValue.readString()
        negativeButton = parcelableValue.readString()
        messageRes = parcelableValue.readInt()
        titleRes = parcelableValue.readInt()
        positiveButtonRes = parcelableValue.readInt()
        negativeButtonRes = parcelableValue.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(packageName)
        dest.writeString(title)
        dest.writeString(message)
        dest.writeString(positiveButton)
        dest.writeString(negativeButton)
        dest.writeInt(messageRes)
        dest.writeInt(titleRes)
        dest.writeInt(positiveButtonRes)
        dest.writeInt(negativeButtonRes)
    }

    class Builder {
        internal var messageRes: Int
        internal var message: String?
        internal var titleRes: Int
        internal var title: String?
        internal var packageName: String?
        internal var positiveButton: String?
        internal var positiveButtonRes: Int
        internal var negativeButton: String?
        internal var negativeButtonRes: Int

        init {
            messageRes = -1
            message = null
            titleRes = -1
            title = null
            packageName = null
            positiveButton = null
            positiveButtonRes = -1
            negativeButton = null
            negativeButtonRes = -1
        }

        fun messageRes(value: Int): Builder {
            messageRes = value
            return this
        }

        fun message(value: String?): Builder {
            message = value
            return this
        }

        fun titleRes(value: Int): Builder {
            titleRes = value
            return this
        }

        fun title(value: String?): Builder {
            title = value
            return this
        }

        fun packageName(value: String?): Builder {
            packageName = value
            return this
        }

        fun positiveButtonRes(value: Int): Builder {
            positiveButtonRes = value
            return this
        }

        fun positiveButton(value: String?): Builder {
            positiveButton = value
            return this
        }

        fun negativeButtonRes(value: Int): Builder {
            negativeButtonRes = value
            return this
        }

        fun negativeButton(value: String?): Builder {
            negativeButton = value
            return this
        }

        fun build(): DialogSettings {
            return DialogSettings(this)
        }
    }
}