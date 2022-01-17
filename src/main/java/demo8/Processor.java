package demo8;

import java.util.Scanner;

public class Processor {

    public void produce() throws InterruptedException {
        //Syncronized necesita tener el bloqueo del objeto coo parametro para poder correr su codigo
        // Aca bloqueo el objeto Processor, esto NO puede correr hasta que no puedo bloquear el objeto
        synchronized (this) {
            System.out.println("Producer thread running...");
            wait(); // Aca DESBLOQUEO el objeto Processor,
            // y se pausa el codigo, SOLO puedo seguir hasta que se llame a notify() en otro thread
            System.out.println("Resume");
        }
    }

    public void consume() throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        Thread.sleep(2000);

        // Aca bloqueo el objeto Processor, esto NO puede correr hasta que no puedo bloquear el objeto
        synchronized (this) {
            System.out.println("Waiting for returned key...."); // form Scanner
            scanner.nextLine();
            System.out.println("Return key press");
            notify(); // Notifies that thread can wake up from wait(),pero
            // Es la ultima linea del syncronized asi suelta el control del objeto
            Thread.sleep(6000L);
            System.out.println("After sleeping, now releasing");
        } // Cuando termina el bloque, aca suelta el control del objeto,
        // entonces ahi puede seguir el otro thread a pesar de que este haya llamado a notify()
    }
}
