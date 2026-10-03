import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private Scanner sc;
    private OutputStreamWriter os;
    private String username;
    private PrintWriter out;

    public ClientHandler(Socket socket)
    {
        try{
            this.socket=socket;
            this.sc=new Scanner(socket.getInputStream());
            this.os=new OutputStreamWriter(socket.getOutputStream());
            this.out=new PrintWriter(os,true);
            this.username=sc.nextLine();
            clientHandlers.add(this);// to add user to the gc
            BroadcastMessage("SERVER:"+username+"has entered the chat");
        } catch (IOException e) {
            CloseGC(socket,sc,os,out);
        }
    }

    @Override
    public void run()
    {
        String messageFromClient;
        while(socket.isConnected())
        {
            try{
                messageFromClient=sc.nextLine();
                BroadcastMessage(messageFromClient);
            }catch (Exception e){
                CloseGC(socket,sc,os,out);
                break;
            }
        }
        removeClient();
    }
    public void BroadcastMessage(String messageToSend)
    {
        for(ClientHandler clientHandler:clientHandlers)
        {
            try {
                if (!clientHandler.username.equals(username)) {
                    clientHandler.out.println(messageToSend );
                }
            }catch(Exception e){
                CloseGC(socket,sc,os,out);

            }
        }
    }
    public void removeClient() {
        clientHandlers.remove(this);
        BroadcastMessage("SERVER:"+username+"has left the chat");
    }
    public void CloseGC(Socket socket , Scanner sc, OutputStreamWriter os , PrintWriter out){

        try{
            if(socket!=null)
                socket.close();
            if(sc!=null)
                sc.close();
            if(os!=null)
                os.close();
            if(out!=null)
                os.close();
            removeClient();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
