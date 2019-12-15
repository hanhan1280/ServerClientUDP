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
import java.net.Socket;

/**
 *
 * @author hanan
 */
public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public void start() throws IOException{
        this.socket = new Socket("127.0.0.1",8888);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
        this.out.println("We received " + this.in.readLine());
    }
    
    void stop() throws IOException{
        this.in.close();
        this.out.close();
        this.socket.close();
    }
}
