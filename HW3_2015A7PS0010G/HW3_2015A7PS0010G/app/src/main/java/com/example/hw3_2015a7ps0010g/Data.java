package com.example.hw3_2015a7ps0010g;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data implements Parcelable {
    public Date time;
    public double acceleration;
    public static final Parcelable.Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel parcel) {
            try {
                return new Data(parcel);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Data[] newArray(int i) {
            return new Data[0];
        }
    };
    public Data(Date time, double acceleration) {
        this.time = time;
        this.acceleration = acceleration;
    }

    public Data(Parcel parcel) throws ParseException {
        this.time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(parcel.readString());
        this.acceleration = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time.toString());
        parcel.writeDouble(acceleration);
    }
}
