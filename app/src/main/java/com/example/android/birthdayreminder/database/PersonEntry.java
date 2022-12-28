package com.example.android.birthdayreminder.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "personTable")
public class PersonEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String lastName;

    private String firstName;

    private int year;
    private int month;
    private int day;

    private int relation;

    @Ignore
    public PersonEntry(String lastName, String firstName, int year, int month, int day, int relation){
        this.lastName = lastName;
        this.firstName = firstName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.relation = relation;
    }

    public PersonEntry(int id, String lastName, String firstName, int year, int month, int day, int relation){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.relation = relation;
    }

    public int getId() { return id;}
    public void setId(int id) {this.id = id;}

    public String getLastName() {return lastName;}

    public String getFirstName() {return firstName;}

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public int getRelation() {return relation;}

}
