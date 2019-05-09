package nl.bonfire17.friendslist.models;

public class Contact {

    int id;
    String firstname, lastname, email, phone;

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

    public String getFirstname(){
        return this.firstname;
    }

    public String getLastname(){
        return this.lastname;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public String toString(){
        return this.id + " " + this.firstname + " " + this.lastname + " " + this.email + " " + this.phone;
    }
}
