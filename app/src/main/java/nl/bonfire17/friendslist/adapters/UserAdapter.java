package nl.bonfire17.friendslist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.friendslist.R;

import java.util.ArrayList;

import nl.bonfire17.friendslist.ContactIllustrator;
import nl.bonfire17.friendslist.models.User;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context ctx, ArrayList<User> users){
        super(ctx, 0, users);
    }

    @Override
    public long getItemId(int i) {
        return this.getItem(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        User user = getItem(i);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_view, parent, false);
        }

        TextView email, isAdmin;
        ContactIllustrator contactIllustrator;
        email = (TextView) convertView.findViewById(R.id.email);
        isAdmin = (TextView) convertView.findViewById(R.id.isAdmin);
        contactIllustrator = (ContactIllustrator) convertView.findViewById(R.id.contactIllustrator);

        email.setText(user.getEmail());
        isAdmin.setText(getContext().getResources().getString(R.string.administrator) + ": " +
                (user.getIsAdmin() ? getContext().getResources().getString(R.string.yes): getContext().getResources().getString(R.string.no)));
        contactIllustrator.setContactNumber(user.getContactsCount());
        return convertView;
    }
}
