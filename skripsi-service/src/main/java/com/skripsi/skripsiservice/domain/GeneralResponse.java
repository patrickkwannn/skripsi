package com.skripsi.skripsiservice.domain;

import java.util.Date;

public class GeneralResponse {

    private String response;
    private String message;
    private String path;
    private Date date;

    public GeneralResponse() {

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeneralResponse(Builder builder){
        this.response = builder.response;
        this.message = builder.message;
        this.path = builder.path;
        this.date = builder.date;
    }

    public static class Builder{
        private String response;
        private String message;
        private String path;
        private Date date;

        public Builder withResponse(String response){
            this.response = response;
            return this;
        }

        public Builder withMessage(String message){
            this.message = message;
            return this;
        }

        public Builder withPath(String path){
            this.path = path;
            return this;
        }

        public Builder withDate(Date date){
            this.date = date;
            return this;
        }

        public GeneralResponse build() {return new GeneralResponse(this);}
    }
}
