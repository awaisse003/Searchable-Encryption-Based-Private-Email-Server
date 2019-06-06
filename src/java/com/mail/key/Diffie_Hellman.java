package com.mail.key;

public class Diffie_Hellman {

    int p = 23;
    int g = 5;

    int A;
    int B;

    public int computeB(int b) {
        B = (int) Math.pow(g, b) % p;
        return B;
    }

    public int computeA(int a) {
        A = (int) Math.pow(g, a) % p;
        return A;
    }

    public int computekeyForB(int A, int b) {
        int S_B = (int) Math.pow(A, b) % p;
        return S_B;
    }

    public int computekeyForA(int B, int a) {
        int S_A = (int) Math.pow(B, a) % p;
        return S_A;
    }

    public static void main(String args[]) {

        Diffie_Hellman d = new Diffie_Hellman();

        d.computeA(10);
        d.computeB(5);

        System.out.println(d.computekeyForA(d.B, 10));
        System.out.println(d.computekeyForB(d.A, 5));

//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter modulo(p)");
//        int p = sc.nextInt();
//        System.out.println("Enter primitive root of " + p);
//        int g = sc.nextInt();
//        System.out.println("Choose 1st secret no(Alice)");
//        int a = sc.nextInt();
//        System.out.println("Choose 2nd secret no(BOB)");
//        int b = sc.nextInt();
//
//        int A = (int) Math.pow(g, a) % p;
//        System.out.println("A : " + A);
//        int B = (int) Math.pow(g, b) % p;
//        System.out.println(" : " + B);
//
//        int S_A = (int) Math.pow(B, a) % p;
//        int S_B = (int) Math.pow(A, b) % p;
//
//        if (S_A == S_B) {
//            System.out.println("ALice and Bob can communicate with each other!!!");
//            System.out.println("They share a secret no = " + S_A);
//        } else {
//            System.out.println("ALice and Bob cannot communicate with each other!!!");
//        }
    }
}
