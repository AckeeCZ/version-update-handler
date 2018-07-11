package com.ackee.versioupdatehandler.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Visual settings of dialog
 */
public class DialogSettings implements Parcelable {
    public static final String TAG = DialogSettings.class.getName();
    String packageName;
    String title;
    int titleRes;
    String message;
    int messageRes;
    String positiveButton;
    int positiveButtonRes;
    String negativeButton;
    int negativeButtonRes;


    private DialogSettings(Builder builder) {
        packageName = builder.packageName;
        title = builder.title;
        titleRes = builder.titleRes;
        message = builder.message;
        messageRes = builder.messageRes;
        negativeButton = builder.negativeButton;
        positiveButton = builder.positiveButton;
        positiveButtonRes = builder.positiveButtonRes;
        negativeButtonRes = builder.negativeButtonRes;
    }


    protected DialogSettings(Parcel in) {
        packageName = in.readString();
        title = in.readString();
        message = in.readString();
        positiveButton = in.readString();
        negativeButton = in.readString();
        messageRes = in.readInt();
        titleRes = in.readInt();
        positiveButtonRes = in.readInt();
        negativeButtonRes = in.readInt();
    }


    public String getPackageName() {
        return packageName;
    }

    public String getTitle() {
        return title;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageRes() {
        return messageRes;
    }

    public String getPositiveButton() {
        return positiveButton;
    }

    public int getPositiveButtonRes() {
        return positiveButtonRes;
    }

    public String getNegativeButton() {
        return negativeButton;
    }

    public int getNegativeButtonRes() {
        return negativeButtonRes;
    }

    public static final Creator<DialogSettings> CREATOR = new Creator<DialogSettings>() {
        @Override
        public DialogSettings createFromParcel(Parcel in) {
            return new DialogSettings(in);
        }

        @Override
        public DialogSettings[] newArray(int size) {
            return new DialogSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(positiveButton);
        dest.writeString(negativeButton);
        dest.writeInt(messageRes);
        dest.writeInt(titleRes);
        dest.writeInt(positiveButtonRes);
        dest.writeInt(negativeButtonRes);
    }


    public static final class Builder {
        private int messageRes;
        private String message;
        private int titleRes;
        private String title;
        private String packageName;
        private String positiveButton;
        private int positiveButtonRes;
        private String negativeButton;
        private int negativeButtonRes;

        public Builder() {
            messageRes = -1;
            message = null;
            titleRes = -1;
            title = null;
            packageName = null;
            positiveButton = null;
            positiveButtonRes = -1;
            negativeButton = null;
            negativeButtonRes = -1;
        }

        public Builder messageRes(int val) {
            messageRes = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder titleRes(int val) {
            titleRes = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder packageName(String val) {
            packageName = val;
            return this;
        }

        public Builder positiveButtonRes(int val) {
            positiveButtonRes = val;
            return this;
        }

        public Builder positiveButton(String val) {
            positiveButton = val;
            return this;
        }

        public Builder negativeButtonRes(int val) {
            negativeButtonRes = val;
            return this;
        }

        public Builder negativeButton(String val) {
            negativeButton = val;
            return this;
        }

        public DialogSettings build() {
            return new DialogSettings(this);
        }
    }
}
