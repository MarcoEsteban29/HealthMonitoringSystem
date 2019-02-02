package com.example.healthmonitoringsystem;

public class  User {
    public String Name, Email, Cp, Age, Password,Uri,Verified;

    public User(String name, String age, String cp, String email, String password,String uri, String verified) {
        this.Name = name;
        this.Age = age;
        this.Cp = cp;
        this.Email = email;
        this.Password = password;
        this.Uri = uri;
        this.Verified = verified;

    }
}