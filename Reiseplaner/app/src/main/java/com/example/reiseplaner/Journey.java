package com.example.reiseplaner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Journey {
    private String category;
    private String destination;
    private Date date;
    private String thingsNotToForget;
    private String notes;

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");


    public Journey(String category, String destination, String thingsNotToForget, String notes, String time) {
        setValues(category, destination, thingsNotToForget, notes, time);
    }

    private void setValues(String category, String destination, String thingsNotToForget, String notes, String time) {
        this.category = category;
        this.destination = destination;
        this.thingsNotToForget = thingsNotToForget;
        this.notes = notes;
        try {
            this.date = DATE_FORMAT.parse(time);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return category;
    }


    public String getDestination() {
        return destination;
    }


    public Date getDate() {
        return date;
    }


    public String getThingsNotToForget() {
        return thingsNotToForget;
    }

    public String getNotes() {
        return notes;
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
