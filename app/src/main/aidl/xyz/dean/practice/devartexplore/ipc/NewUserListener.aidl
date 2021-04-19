// NewUserListener.aidl
package xyz.dean.practice.devartexplore.ipc;

// Declare any non-default types here with import statements
import xyz.dean.practice.devartexplore.ipc.User;

interface NewUserListener {
    void onNewUserAdded(in User user);
}