package nl.bonfire17.friendslist.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private int id;
    private String email;
    private boolean isAdmin;

    private ArrayList<Contact> contacts;

    public User(int id, String email, boolean isAdmin, ArrayList<Contact> contacts){
        this.id = id;
        this.email = email;
        this.isAdmin = isAdmin;
        this.contacts = contacts;
    }

    public int getContactsCount(){
        return this.contacts.size();
    }

    public Contact getContact(int id){
        for (Contact contact: contacts) {
            if(contact.getId() == id){
                return contact;
            }
        }
        return null;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public boolean getIsAdmin(){
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public ArrayList<Contact> getContacts(){
        return this.contacts;
    }

    public void setContacts(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }

    public String toString(){
        return this.id + " " + this.email + " " + this.isAdmin;
    }
}
