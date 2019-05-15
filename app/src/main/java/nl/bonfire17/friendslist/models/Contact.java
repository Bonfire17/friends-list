package nl.bonfire17.friendslist.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private int id;
    private String firstname, lastname, email, phone;

    public Contact(int id, String firstname, String lastname, String email, String phone){
        this.id = id;

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getFirstname(){
        return this.firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public String getLastname(){
        return this.lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String toString(){
        return this.id + " " + this.firstname + " " + this.lastname + " " + this.email + " " + this.phone;
    }
}
