package com.example.reiseplaner.Journey;


import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Journey {
    private String category;
    private String destination;
    private LocalDateTime date;
    private String thingsNotToForget;
    private String notes;
    private List<Uri> uris;
    private String temperature;
    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Journey(String category, String destination, String thingsNotToForget, String notes, String time, List<Uri> uris, String temperature) {
        setValues(category, destination, thingsNotToForget, notes, time, uris, temperature);
    }

    private void setValues(String category, String destination, String thingsNotToForget, String notes, String time, List<Uri> uris, String temperature) {

        this.category = category;
        this.destination = destination;
        this.thingsNotToForget = thingsNotToForget;
        this.notes = notes;
        this.date = LocalDateTime.parse(time,DATE_FORMAT);
        this.uris = uris;
        this.temperature = temperature;

    }

    public String getCategory() {
        return category;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
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

    public void addUri(Uri uri)
    {
        this.uris.add(uri);
    }

    public void setUris(List<Uri> u)
    {
        this.uris = u;
    }

    public List<Uri> getUris()
    {
        return this.uris;
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
