package nl.bonfire17.friendslist;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.android.friendslist.R;

public class EditUserCompound extends ConstraintLayout {

    private EditText email, password;
    private CheckBox admin, changePassword;

    public EditUserCompound(Context ctx){
        super(ctx, null);
        loadViews();
    }

    public EditUserCompound(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.edit_user_panel, this);

        loadViews();
    }

    private void loadViews(){
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        admin = (CheckBox)findViewById(R.id.admin);
        changePassword = (CheckBox)findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new CheckListener());
    }

    public void setPasswordVisibility(int i){
        password.setVisibility(i);
    }

    public void setChangePasswordVisibility(int i){
        changePassword.setVisibility(i);
    }

    public String getEmail(){
        return this.email.getText().toString();
    }

    public void setEmail(String email){
        this.email.setText(email);
    }

    public String getPassword(){
        return this.password.getText().toString();
    }

    public void setPassword(String password){
        this.password.setText(password);
    }

    public boolean getAdmin(){
        return this.admin.isChecked();
    }

    public void setAdmin(boolean admin){
        this.admin.setChecked(admin);
    }

    public boolean getChangePassword(){
        return this.changePassword.isChecked();
    }

    public void setChangePassword(boolean changePassword){
        this.changePassword.setChecked(changePassword);
    }

    class CheckListener implements CheckBox.OnClickListener{

        @Override
        public void onClick(View view) {
            CheckBox check = (CheckBox)view;
            if(check.isChecked()){
                setPasswordVisibility(View.VISIBLE);
            }else{
                setPasswordVisibility(View.GONE);
                setPassword("");
            }
        }
    }

}
