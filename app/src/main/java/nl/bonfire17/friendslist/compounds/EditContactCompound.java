package nl.bonfire17.friendslist.compounds;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.android.friendslist.R;

public class EditContactCompound extends ConstraintLayout {

    private EditText firstname, lastname, email, phonenumber;

    public EditContactCompound(Context ctx){
        super(ctx, null);
        loadViews();
    }

    public EditContactCompound(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.edit_contact_panel, this);

        loadViews();
    }

    private void loadViews(){
        firstname = (EditText)findViewById(R.id.firstname);
        lastname = (EditText)findViewById(R.id.lastname);
        email = (EditText)findViewById(R.id.email);
        phonenumber = (EditText)findViewById(R.id.phonenumber);
    }

    public String getFirstname(){
        return this.firstname.getText().toString();
    }

    public void setFirstname(String firstname){
        this.firstname.setText(firstname);
    }

    public String getLastname(){
        return this.lastname.getText().toString();
    }

    public void setLastname(String lastname){
        this.lastname.setText(lastname);
    }

    public String getEmail(){
        return this.email.getText().toString();
    }

    public void setEmail(String email){
        this.email.setText(email);
    }

    public String getPhonenumber(){
        return this.phonenumber.getText().toString();
    }

    public void setPhonenumber(String phonenumber){
        this.phonenumber.setText(phonenumber);
    }
}
