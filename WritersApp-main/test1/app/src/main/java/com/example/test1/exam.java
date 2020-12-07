package com.example.test1;

public class exam {
    private String name;
    private String language;
    private String date;
    private String time;
    private String address;
    private String amount;
    private String book_status;
    public exam()
    {

    }
    public exam(String name, String address, String language, String date, String time, String amount,String book_status) {
        this.name = name;
        this.address = address;
        this.language = language;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.book_status = book_status;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLanguage() {
        return language;
    }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBook_status(){return book_status;}


}
