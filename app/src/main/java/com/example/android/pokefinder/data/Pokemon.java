package com.example.android.pokefinder.data;

import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(tableName = "pokemon")
public class Pokemon implements Serializable {
    @PrimaryKey
    public int id;

    public String name;

    public int weight;
    public int height;

    public String evolves_from = null;
    public int evolves_from_id;

    public String evolves_to = null;
    public int evolves_to_id;

    public ArrayList<String> types;

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