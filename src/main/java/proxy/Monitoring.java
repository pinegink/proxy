package proxy;

public class Monitoring implements Runnable {
    Client client;
    Integer clients;
    public  Monitoring (Client client, Integer clients){
        this.client = client;
        this.clients = clients; // Не уверен не ломает ли логику, мб не стоит в отдельный класс выносить
        // так переменная clients меняется вне этого класа - фиговый подход
    }
    @Override
    public void run() {
//        while (client!=null) {
//            System.out.println("clients:" + clients);
//            if (clients == 0) {
//                client.closeConnection();
//                client = null;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
    }
}
