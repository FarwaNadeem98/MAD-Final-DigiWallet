package com.studios.digiwallet.Models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Transaction {
    public String title, purpose, details, senderName, senderNumber, recName, recNumber;
    public float amount, fee;
    public Timestamp timestamp;
    public String direction;

    public Transaction(String title, String purpose, String details, String senderName, String senderNumber, String recName, String recNumber, float amount, float fee, Timestamp timestamp, String direction) {
        this.title = title;
        this.purpose = purpose;
        this.details = details;
        this.senderName = senderName;
        this.senderNumber = senderNumber;
        this.recName = recName;
        this.recNumber = recNumber;
        this.amount = amount;
        this.fee = fee;
        this.timestamp = timestamp;
        this.direction = direction;
    }
}
