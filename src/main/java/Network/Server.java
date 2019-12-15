/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hanan
 */
public class Server {
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;//client input
    private PrintWriter out;//client output
    
    public void start(final int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = this.serverSocket.accept();
            this.out = new PrintWriter(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final String receiver = this.in.readLine();
            this.out.println("Server received " + receiver);
    }
    
    public void stop() throws IOException{
        serverSocket.close();
        clientSocket.close();
        this.in.close();
        this.out.close();
    }
}
