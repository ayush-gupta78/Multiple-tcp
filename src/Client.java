import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner sc;
    private OutputStreamWriter os;
    private PrintWriter out;
    private String username;

    public Client(Socket socket,String username) throws IOException {
        this.socket=socket;
        this.sc=new Scanner (socket.getInputStream());
        this.os=new OutputStreamWriter(socket.getOutputStream());
        this.out=new PrintWriter(os,true);
        this.username=username;
    }

    public void SendMessage() {
        try {
            os=new OutputStreamWriter(socket.getOutputStream());
            out=new PrintWriter(os,true);
            out.println(username);
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                out.println(username + ":" + messageToSend);
            }
          }catch(Exception e) {
                closeClient(socket,sc,os,out);
            }
        }
       public void ListenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGC;

                while(socket.isConnected()) {
                    try{
                        messageFromGC=sc.nextLine();
                        IO.println(messageFromGC);
                    }catch (Exception e) {
                        closeClient(socket,sc,os,out);
                    }
                }

            }
        }).start();

       }

       public void closeClient(Socket socket, Scanner sc, OutputStreamWriter os , PrintWriter out)
       {
           try{
               if(socket!=null)
                   socket.close();
               if(sc!=null)
                   sc.close();
               if(os!=null)
                   os.close();
               if(out!=null)
                   os.close();
           }catch (IOException e){
               e.printStackTrace();
           }
       }
       public static void main(String[]args) throws IOException {
           Scanner s=new Scanner (System.in);
           IO.println("Enter your username for group chat:");
           String username=s.nextLine();
           String ip="localhost";
           int port=9999;
           Socket socket1=new Socket(ip,port);
           Client client=new Client(socket1,username);
           client.ListenForMessage();
           client.SendMessage();
       }

}


