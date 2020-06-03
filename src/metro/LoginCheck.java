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
    
    static{
        users.put("Admin".toLowerCase(), salt("Admin", "Hunter2"));
    }
    
    protected static boolean check(String user, String pass){
        String user_l = user.toLowerCase();
        try {
            if (users.get(user_l) != null) {
                if(users.get(user_l).equals(salt(user_l, pass))){
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
        String enc = encrypt("Hello World! Hello You!", key);
        //System.out.print(enc + " --> ");
        System.out.println(decrypt(enc, key));
    }
    
    protected static String salt(String user, String pass){
        return (user.toLowerCase() + pass).hashCode() + "";
    }
    
    protected static String encrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) * (Integer.valueOf(key)%200) );
        }
        return out;
    }
    
    protected static String decrypt(String inp, String key){
        String out = "";
        for(char i : inp.toCharArray()){
            out = out + (char) ( (int) (i) / (Integer.valueOf(key)%200) );
        }
        return out;
    }
}
