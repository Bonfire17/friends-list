package nl.bonfire17.friendslist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.friendslist.R;

import java.util.ArrayList;

import nl.bonfire17.friendslist.models.Contact;


public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context ctx, ArrayList<Contact> contactList){
        super(ctx, 0, contactList);
    }

    @Override
    public long getItemId(int i){
        return this.getItem(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Contact contact = getItem(i);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_view, parent, false);
        }

        TextView name, email, phone;
        name = (TextView) convertView.findViewById(R.id.name);
        email = (TextView) convertView.findViewById(R.id.email);
        phone = (TextView) convertView.findViewById(R.id.phone);

        name.setText(contact.getFirstname() + " " + contact.getLastname());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());
        return convertView;
    }
}
