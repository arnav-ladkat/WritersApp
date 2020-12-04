package com.example.test1;

public class formhelper {

    String name,date,time,address,amount,language,accepted_by,book_status;

    public formhelper() {

    }



    public formhelper(String name, String language, String date, String time, String address, String amount, String accepted_by, String book_status) {
        this.name = name;
        this.language = language;
        this.date = date;
        this.time = time;
        this.address = address;
        this.amount = amount;
        this.accepted_by = accepted_by;
        this.book_status=book_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {return language;}

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }

    public String getBook_status() {
        return book_status;
    }

    public void setBook_status(String book_status) {
        this.book_status = book_status;
    }
}
