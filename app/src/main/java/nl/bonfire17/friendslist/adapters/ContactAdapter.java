package nl.bonfire17.friendslist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.friendslist.R;

import java.util.ArrayList;

import nl.bonfire17.friendslist.models.Contact;


public class ContactAdapter extends BaseAdapter {

    Context ctx;
    ArrayList<Contact> contactList;

    private static String TAG = "TAG";

    public ContactAdapter(Context ctx, ArrayList<Contact> contactList){
        this.contactList = contactList;
        this.ctx = ctx;
    }

    public void clearAdapter(){
        contactList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contactList.size();
    }
    @Override
    public Object getItem(int i) {
        return this.contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return contactList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View row;
        row = inflater.inflate(R.layout.contact_view, viewGroup, false);
        TextView name, email, phone;
        name = (TextView) row.findViewById(R.id.name);
        email = (TextView) row.findViewById(R.id.email);
        phone = (TextView) row.findViewById(R.id.phone);
        Contact ct = contactList.get(i);
        name.setText(ct.getFirstname() + " " + ct.getLastname());
        email.setText(ct.getEmail());
        phone.setText(ct.getPhone());
        return row;
    }
}
