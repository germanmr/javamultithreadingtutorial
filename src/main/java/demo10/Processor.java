package demo10;


import java.util.LinkedList;
import java.util.Random;

public class Processor {

    private LinkedList<Integer> list = new LinkedList();
    private final int LIMIT = 10;

    private Object lock = new Object();

    public void produce() throws InterruptedException {

        int value = 0;

        while (true) { // No para nunca

            synchronized (lock) {
                while (list.size() == LIMIT) { // Si la lista esta "llena" esperamos
                    lock.wait();
                }
                // Voy agregando de a uno al final bloqueando intermitentemente hasta que se llena la lista
                // Cuando la lista esta llena esperamos, es decir libero completamente el bloqueo para que el consumidor tome,
                // hasta que el consumidor me de lugar para volver a agregar elementos
                list.add(value++);
                lock.notify();
            }
        }
    }

    public void consume() throws InterruptedException {
        Random random = new Random();

        while (true) {  // No para nunca
            synchronized (lock) { // Usamos el lock sobre el mismo objeto
                while (list.size() == 0) { // Si la lista esta vacia esperamos
                    lock.wait();
                }
                // Si la lista tiene algun elemento empezamos a tomar
                System.out.print("List size is:" + list.size());
                int value = list.removeFirst();
                System.out.println("; value is : " + value);
                lock.notify(); // Como sacamos un elemento notificamos al producer que empiece a agregar
            }
            // Le damos algo de tiempo al producer para que agregue elementos,
            // es decir tenemos una demora de procesamiento al azar para tomar elementos
            // Eso seria que tan rapido podemos consumir
            Thread.sleep(random.nextInt(1000));
        }
    }
}
