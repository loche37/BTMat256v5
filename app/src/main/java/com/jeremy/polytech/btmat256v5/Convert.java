package com.jeremy.polytech.btmat256v5;

import java.util.ArrayList;

/**
 * Created by Jeremy on 20/03/2016.
 */
public class Convert {


    /**
     * toByteTab(String s) convertie la chaine s de 256 '0' et '1' en un tableau de byte[] de 32 éléments
     * @param s
     * @return
     */
    public static byte[] toByteTab(String s){
        ArrayList<Byte> tab=new ArrayList<>();
        for(int i=0;i<s.length();i+=8){
            tab.add((byte)Integer.parseInt(s.substring(i,i+8),2));
        }
        Byte[] a= tab.toArray(new Byte[]{});

        byte[] result=new byte[a.length];
        for(int i=0;i<a.length;i++){
            result[i]=a[i];
        }
        return result;
    }


}
