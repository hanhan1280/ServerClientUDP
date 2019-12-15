/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Network.Client;
import Network.Server;
import java.io.IOException;

/**
 *
 * @author hanan
 */
public class TankyGame {
    
    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        server.start(8888);
        final Client client = new Client();
        client.start();
    }
    
}
