package com.example.reiseplaner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Journey {
    private String category;
    private String destination;

    //private Date date;
    private LocalDateTime date;
    private String thingsNotToForget;
    private String notes;
    private String stringDate;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    //public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");



    public Journey(String category, String destination, String thingsNotToForget, String notes, String time) {
        setValues(category, destination, thingsNotToForget, notes, time);
    }

    private void setValues(String category, String destination, String thingsNotToForget, String notes, String time) {
        this.category = category;
        this.destination = destination;
        this.thingsNotToForget = thingsNotToForget;
        this.notes = notes;

        this.date = LocalDateTime.parse(time,DATE_FORMAT);
        this.stringDate = time;
    }

    public String getCategory() {
        return category;
    }


    public String getDestination() {
        return destination;
    }



    public LocalDateTime getDate() {
        return date;
    }


    public String getThingsNotToForget() {
        return thingsNotToForget;
    }

    public String getNotes() {
        return notes;
    }


    public String getDateAsString()
    {
        return stringDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(String date) {
        this.date = LocalDateTime.parse(date,DATE_FORMAT);
    }

    public void setThingsNotToForget(String thingsNotToForget) {
        this.thingsNotToForget = thingsNotToForget;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    @Override
    public String toString() {
        return "Journey{" +
                "category='" + category + '\'' +
                ", destination='" + destination + '\'' +
                ", date=" + DATE_FORMAT.format(date) +
                ", thingsNotToForget='" + thingsNotToForget + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
