package edu.rice.comp504.model.user;

public class UserFact implements IUserFac {

    @Override
    public AUser make(String userType) {
        if (userType.equals("user")) {
            AUser newUser = new User();
            return newUser;
        }
        AUser nullUser = new NullUser();
        return nullUser;
    }
}
