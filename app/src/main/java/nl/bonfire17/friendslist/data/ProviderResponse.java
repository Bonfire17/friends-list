package nl.bonfire17.friendslist.data;

import java.util.ArrayList;

import nl.bonfire17.friendslist.models.User;

/*
    This interface is used to make listeners for the request method of the DataProvider class
 */
public interface ProviderResponse{
    public void error();

    interface LoginResponse extends ProviderResponse{
        public void response(boolean login, int id);
    }

    interface UserResponse extends ProviderResponse{
        public void response(User user);
    }

    interface UsersResponse extends ProviderResponse{
        public void response(ArrayList<User> users);
    }

    interface SuccessResponse extends ProviderResponse{
        public void response(boolean success);
    }
}