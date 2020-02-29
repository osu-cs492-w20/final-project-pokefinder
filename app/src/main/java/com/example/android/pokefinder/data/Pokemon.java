package com.example.android.pokefinder.data;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "pokemon")
public class Pokemon implements Serializable {
    @PrimaryKey
    @NonNull
    public int id;

    @NonNull
    public String name;

    public int weight;
    public int height;

    @Override
    public boolean equals(Object other) {
        if(this == other)
            return true;
        if(other == null)
            return false;
        if(getClass() != other.getClass())
            return false;

        Pokemon test = (Pokemon)other;
        return this.id == test.id;
    }
}