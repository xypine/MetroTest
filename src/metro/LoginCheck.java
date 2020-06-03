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
        String enc = encrypt("Hello!", "21");
        System.out.println(enc);
        System.out.println(decrypt(enc, "21"));
    }
    
    protected static String salt(String user, String pass){
        return (user.toLowerCase() + pass).hashCode() + "";
    }
    
    protected static String encrypt(String inp, String key){
        byte[] encodedBytes = Base64.getEncoder().encode(inp.getBytes());
        byte[] two = new byte[encodedBytes.length];
        int ind = 0;
        for(byte i : encodedBytes){
            two[ind] = i;
            ind++;
        }
        return new String(two);
    }
    
    protected static String decrypt(String inp, String key){
        byte[] oregano = inp.getBytes();
        byte[] two = new byte[oregano.length];
        int ind = 0;
        for(byte i : oregano){
            two[ind] = i;
            ind++;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(new String(two));
        return new String(decodedBytes);
    }
}
