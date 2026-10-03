import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void StartServer()
    {
        try{
            while(!serverSocket.isClosed()){
                Socket socket=serverSocket.accept();
                IO.println("A new Client has connected");
                ClientHandler clientHandler =new ClientHandler(socket);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket()
    {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args)throws IOException
    {
        ServerSocket serversocket=new ServerSocket(9999);
        Server server=new Server(serversocket);
        server.StartServer();
    }

}
