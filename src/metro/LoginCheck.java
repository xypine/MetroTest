/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro;

import java.util.HashMap;
import java.util.Base64;
/**
 *
 * @author Elias Eskelinen <elias.eskelinen@protonmail.com>
 */
public class LoginCheck {
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> data = new HashMap<>();
    
    static{
        users.put("Admin".toLowerCase(), salt("Admin", "Hunter2"));
        users.put("elias", "7613055"); //1802619435
        
        String en = "劭撛抝熎撛玌犍";    //encrypt("Secrets", "7613055");
        data.put("1802619435", en);
        //data.put(key, value);
        writeData("Admin", "Hunter2", "Hello World!");
        //data.put("Hunter2".hashCode() + "", "Hello World!");
    }
    
    protected static void writeData(String user, String password, String nData){
        if(check(user, password)){
            String en = encrypt(nData, salt(user, password));
            data.put(password.hashCode() + "", en);
        }
    }
    
    protected static String readData(String user, String password){
        if(check(user, password)){
            String oregano = data.get(password.hashCode() + "");
            String de = decrypt(oregano, salt(user, password));
            return de;
            //return data.get(salt(user, password));
        }
        return null;
    }
    
    protected static boolean check(String user, String password){
        String user_l = user.toLowerCase();
        try {
            if (users.get(user_l) != null) {
                if(users.get(user_l).equals(salt(user_l, password))){
                    return true;
                }
            }
            else{
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    public static void main(String[] args) {
        System.out.println(check("Admin", "Hunter2"));      //true
        System.out.println(check("admin", "Hunter2"));      //true
        System.out.println(check("Admin", "Hunter1"));      //false
        System.out.println(check("Admin1", "Hunter2"));     //false
        System.out.println(check("Admin", "hunter2"));     //false
        String key = salt("Admin", "Hunter2");
        System.out.println("Key: " + key);
        String enc = encrypt("THE QUICK BROWN FOX JUMPED OVER THE LAZY DOG'S BACK 1234567890!", key);
        System.out.println(enc);
        //System.out.print(enc + " --> ");
        System.out.println(decrypt(enc, key));
        
    }
    
    protected static String salt(String user, String password){
        return (user.toLowerCase() + password).hashCode() + "";
    }
    
    protected static String encrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) * (Integer.valueOf(key)%600) );
        }
        return out;
    }
    
    protected static String decrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) / (Integer.valueOf(key)%600) );
        }
        return out;
    }
}
