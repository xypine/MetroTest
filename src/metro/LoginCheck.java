/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Elias Eskelinen <elias.eskelinen@protonmail.com>
 */
public class LoginCheck {
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> data = new HashMap<>();
    
    static{
        users.put("Admin".toLowerCase(), salt("Admin".toLowerCase(), "Hunter2"));
        readChanges("admin", "Hunter2");
        //users.put("elias", "7613055"); //1802619435
        
        //String en = "劭撛抝熎撛玌犍";    //encrypt("Secrets", "7613055");
        //data.put("1802619435", en);
        //data.put(key, value);
        //writeData("Admin", "Hunter2", "Hello World!", false);
        //data.put("Hunter2".hashCode() + "", "Hello World!");
    }
    
    protected static Set<String> getUsers(String user, String password){
        if(check(user, password)){
            return users.keySet();
        }
        return null;
    }
    
    protected static void addUser(String user, String password, String newUser, String newPassword){
        if(check(user, password)){
            users.put(newUser.toLowerCase(), salt(newUser.toLowerCase(), newPassword));
            writeChanges(user, password);
            writeData(newUser, newPassword, "Welcome to your notes, " + newUser + "!", false);
            writeChanges(user, password);
        }
    }
    
    protected static void removeUser(String user, String password, String removedUser){
        if(check(user, password)){
            users.remove(removedUser);
        }
    }
    
    protected static void writeData(String user, String password, String nData){
        writeData(user, password, nData, true);
    }
    
    protected static void writeData(String user, String password, String nData, boolean save){
        if(check(user, password)){
            String en = encrypt(nData, salt(user, password));
            data.put(user.hashCode() + "", en);
            if(save){
                try {
                    writeFile(users, "users.ser");
                    writeFile(data, "userdata.ser");
                    System.out.println("Wrote userdata succesfully!");
                } catch (IOException ex) {
                    System.out.println("Couldn't write userdata!");
                    ex.printStackTrace();
                }
            }
        }
    }
    
    protected static void writeChanges(String user, String password){
        if (check(user, password)) {
            try {
                writeFile(users, "users.ser");
                writeFile(data, "userdata.ser");
                System.out.println("Wrote userdata succesfully!");
            } catch (IOException ex) {
                System.out.println("Couldn't write userdata!");
                ex.printStackTrace();
            }
        }
    }
    
    protected static void readChanges(String user, String password){
        if (check(user, password)) {
            try {
                users = (HashMap<String, String>) readFile("users.ser");
                data = (HashMap<String, String>) readFile("userdata.ser");
                System.out.println("Read userdata succesfully!");
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Couldn't load data files: ");
                ex.printStackTrace();
            }
        }
    }
    
    protected static String readData(String user, String password){
        return readData(user, password, true);
    }
    
    protected static String readData(String user, String password, boolean loadFromFile){
        if(loadFromFile){
            try {
                users = (HashMap<String, String>) readFile("users.ser");
                data = (HashMap<String, String>) readFile("userdata.ser");
                System.out.println("Read userdata succesfully!");
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Couldn't load data files: ");
                ex.printStackTrace();
            }
        }
        if(check(user, password)){
            String oregano = data.get(user.hashCode() + "");
            if(oregano == null){
                return "[Data index empty]";
            }
            String de = decrypt(oregano, salt(user, password));
            return de;
            //return data.get(salt(user, password));
        }
        return null;
    }
    
    protected static void writeFile(Object o, String name) throws FileNotFoundException, IOException{
        FileOutputStream fileOut =
        new FileOutputStream(name);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(o);
        out.close();
        fileOut.close();
    }
    
    protected static Object readFile(String name) throws FileNotFoundException, IOException, ClassNotFoundException{
        Object e = null;
        FileInputStream fileIn = new FileInputStream(name);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        e = in.readObject();
        in.close();
        fileIn.close();
        return e;
    }
    
    static boolean verbose = true;
    protected static void report(String r){
        if(verbose){
            System.out.println(r);
        }
    }
    
    protected static boolean check(String user, String password){
        String user_l = user.toLowerCase();
        try {
            if (users.get(user_l) != null) {
                if(users.get(user_l).equals(salt(user_l, password))){
                    return true;
                }
                else{
                    report("Password Mismatch");
                }
            }
            else{
                report("User Mismatch / No Password Set");
                return false;
            }
        } catch (Exception e) {
            report("Error: " + e);
        }
        report("No Conditions Met");
        return false;
    }
    
    public static void main(String[] args) {
        System.out.println(check("Admin", "Hunter2"));      //true
        System.out.println(check("admin", "Hunter2"));      //true
        System.out.println(check("Admin", "Hunter1"));      //false
        System.out.println(check("Admin1", "Hunter2"));     //false
        System.out.println(check("Admin", "hunter2"));     //false
        String key = salt("Admin", "Hunter2");
        String key2 = salt("admin", "Hunter2");
        System.out.println("Key: " + key);
        System.out.println("Alt-Key: " + key2);
        String enc = encrypt("THE QUICK BROWN FOX JUMPED OVER THE LAZY DOG'S BACK 1234567890!", key);
        System.out.println(enc);
        //System.out.print(enc + " --> ");
        System.out.println(decrypt(enc, key));
        
    }
    
    protected static String salt(String user, String password){
        return Math.abs((user.toLowerCase() + password).hashCode()) + "";
    }
    
    protected static String encrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) * (Integer.valueOf(key)%500) );
        }
        return out;
    }
    
    protected static String decrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) / (Integer.valueOf(key)%500) );
        }
        return out;
    }
}
