package com.mail.key;

import com.mail.database.config;
import static com.mail.key.logisticmap.LogisticMap;
import static com.mail.key.logisticmap.LogisticMapWithRB;
import com.mail.se.newtech;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class keyMain {

    public void generateKey(ArrayList<String> info) throws SQLException {
        config c = new config();
        String i = c.getPass(Integer.parseInt(info.get(1)));
        // System.out.println(i);
        ArrayList<Integer> asciis = new ArrayList<Integer>();

        double oldRange = 0 - 10;
        double newRange = 3.6 - 4.0;
        int n = 1000;
        double[][] arrayOfLogisticMaps = new double[i.length()][n];
        for (int j = 0; j < i.length(); j++) {
            int asc = i.charAt(j);
            asciis.add(asc);

            //1. log 
//            2. formulla to limit 3.6 - 4.0
            double log = Math.log10(asc);
            double r = ((log - 0.0) * newRange / oldRange) + 3.7;
            System.out.println(r);
            // System.out.println("r " + r);

            arrayOfLogisticMaps[j] = LogisticMap(r, n);

        }

        newtech nObj = new newtech();
        nObj.createAndSaveRndKeys(arrayOfLogisticMaps, info);
        // System.out.println(arrayOfLogisticMaps[0].length);

    }

    public void generateNewKey(int id, int rB, ArrayList<String> info) throws SQLException {
        config c = new config();
        String i = c.getPass(id);
        //  i = " my name is kahn asf asdf asdf AASDASD  44534@#$@#$0";
        ArrayList<Integer> asciis = new ArrayList<Integer>();

        double oldRange = 0 - 10;
        double newRange = 3.6 - 4.0;
        int n = 1000;
        double[][] arrayOfLogisticMaps = new double[i.length()][n];
        for (int j = 0; j < i.length(); j++) {
            int asc = i.charAt(j);
            asciis.add(asc);
            double log = Math.log10(asc);
            double r = ((log - 0.0) * newRange / oldRange) + 3.7;
            //System.out.println(r);
            // System.out.println("r " + r);

            arrayOfLogisticMaps[j] = LogisticMapWithRB(r, n, rB);

        }

        newtech nObj = new newtech();
        nObj.createAndSaveRndSynKeys(arrayOfLogisticMaps, info);
    }

    public int addSecret(int s) {

        Diffie_Hellman d = new Diffie_Hellman();
        return d.computeA(s);

    }

    public static void main(String args[]) throws SQLException, UnsupportedEncodingException {

    }

}
