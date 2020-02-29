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
}