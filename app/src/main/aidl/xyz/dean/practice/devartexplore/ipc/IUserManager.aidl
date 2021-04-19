// IUserManager.aidl
package xyz.dean.practice.devartexplore.ipc;

// Declare any non-default types here with import statements
import xyz.dean.practice.devartexplore.ipc.User;
import xyz.dean.practice.devartexplore.ipc.NewUserListener;

interface IUserManager {
    List<User> getUserList();
    void addUser(in User user);
    void registerNewUserListener(NewUserListener listener);
    void unregisterNewUserListener(NewUserListener listener);
}