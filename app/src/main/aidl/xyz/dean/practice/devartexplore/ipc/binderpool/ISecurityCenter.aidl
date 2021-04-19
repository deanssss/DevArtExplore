// ISecurityCenter.aidl
package xyz.dean.practice.devartexplore.ipc.binderpool;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}