package it.polito.dp2.NFFG.tests;

import it.polito.dp2.NFFG.lab3.NFFGClientException;

public class Main {
    private static int THREAD_NUM = 1;

    public static void main(String[] args) throws NFFGClientException {
        System.setProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file", "http://localhost:8081/NffgService/rest");

        System.out.println("> START CONCURRENT TESTS ON " + THREAD_NUM + " THREADS!");
        for (int i = 1; i <= THREAD_NUM; i++) {
            ConcurrentClient l = new ConcurrentClient(i);
            l.start();
        }

        System.out.println("> MAIN TERMINATED!");
        return;
    }
}
