package edu.rice.comp504.model.user;

/**
 * A factory that makes users.
 */
public interface IUserFac {

    /**
     * Make a user.
     *
     * @param userType the type of user need to make
     * @return AUser class
     */
    AUser make(String userType);
}
