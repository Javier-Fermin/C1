/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.util.Scanner;

/**
 *
 * @author Imanol
 */
public class WaitToStopThread extends Thread{
    

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Espera a que el usuario presione Enter
    }
}
