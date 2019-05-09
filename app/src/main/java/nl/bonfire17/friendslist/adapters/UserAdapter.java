package nl.bonfire17.friendslist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.friendslist.R;

import java.util.ArrayList;

import nl.bonfire17.friendslist.models.User;

public class UserAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<User> users;

    public UserAdapter(Context ctx, ArrayList<User> users){
        this.ctx = ctx;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return users.get(i).getId();
    }

    @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View row = inflater.inflate(R.layout.user_view, viewGroup, false);
            TextView email = (TextView) row.findViewById(R.id.email);
            TextView isAdmin = (TextView) row.findViewById(R.id.isAdmin);
            TextView numberOfContacts = (TextView) row.findViewById(R.id.numberOfContacts);
            User user = users.get(i);
            email.setText(user.getEmail());
            isAdmin.setText(ctx.getResources().getString(R.string.administrator) + ": " +
                    (user.getIsAdmin() ? ctx.getResources().getString(R.string.yes): ctx.getResources().getString(R.string.no)));
            numberOfContacts.setText(Integer.toString(user.getContactsCount()));
        return row;
    }
}
