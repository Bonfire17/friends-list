package nl.bonfire17.friendslist.models;

import java.io.Serializable;
import java.util.ArrayList;

import nl.bonfire17.friendslist.models.Contact;

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

    public int getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public boolean getIsAdmin(){
        return this.isAdmin;
    }

    public ArrayList<Contact> getContacts(){
        return this.contacts;
    }

    public Contact getContact(int id){
        for (Contact contact: contacts) {
            if(contact.getId() == id){
                return contact;
            }
        }
        return null;
    }
}
