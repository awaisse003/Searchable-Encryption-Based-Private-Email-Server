package com.mail.key;

public class logisticmap {

    public static void main(String[] args) {

        long i = Long.parseLong("9876543210123456");
        System.out.println("Long: " + i);

        //====================LOgistic Mapp Genration=========================//   
        int n = 30;
        double r = 3.81; // same secret
        double[] arrayOfLogisticMaps = new double[n];
        arrayOfLogisticMaps = LogisticMap(r, n);
        //=============================================//
    }

    public static double[] LogisticMap(double r, int n) {
        double[] tempArray = new double[n];

        double Xn = 0.5; //first time x0
        double xn_Plus1 = 0;
        //...............//working//....................//
        //xnPlus1= r * xn( 1 - xn )
        // suppose x0= .5
        // r = 3.5
        int counter = 0;
        for (int i = 0; i < n; i++) {
            xn_Plus1 = (r * Xn) * (1 - Xn);
            Xn = xn_Plus1;
            tempArray[counter] = (Xn*1000) % 255;
           // System.out.println(Xn);
            counter++;
        }

        return tempArray;
    }
       public static double[] LogisticMapWithRB(double r, int n,int rB) {
        double[] tempArray = new double[n];

        double Xn = 0.5; //first time x0
        double xn_Plus1 = 0;
        //...............//working//....................//
        //xnPlus1= r * xn( 1 - xn )
        // suppose x0= .5
        // r = 3.5
        int counter = 0;
        for (int i = 0; i < n; i++) {
            xn_Plus1 = (r * Xn) * (1 - Xn);
            Xn = xn_Plus1;
            tempArray[counter] = (Xn*1000 + rB) % 255;
          //  System.out.println(Xn);
            counter++;
        }

        return tempArray;
    }

}
