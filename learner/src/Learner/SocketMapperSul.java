package Learner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Socket interface with the external SSH mapper (test harness).
 */
public class SocketMapperSul {

    private PrintWriter sockout;
    private BufferedReader sockin;

    public SocketMapperSul(Socket sock) {
        try {
            // Create socket out (no buffering) and in
            sockout = new PrintWriter(sock.getOutputStream(), true);
            sockin = new BufferedReader(
                new InputStreamReader(sock.getInputStream())
            );
        } catch (IOException e) {
            throw new MapperException(
                "Failed to create communication streams with mapper",
                e
            );
        }
    }

    /**
     * Sends a string (delimited by \n)  to the socket and retrieves the response
     * @param input the input to send to the socket
     * @return The response sent by the other side
     */
    public String sendAndRecv(String input) {
        try {
            // Send input to SUL
            sockout.println(input);

            return sockin.readLine();
        } catch (IOException e) {
            throw new MapperException("Input could not be sent", e);
        }
    }

    /**
     * Sends a string (delimited by \n)  to the socket without expecting a response
     * @param input the input to send to the socket
     */
    public void send(String input) {
        try {
            // Send input to SUL
            sockout.println(input);
        } catch (Exception e) {
            throw new MapperException("Input could not be sent", e);
        }
    }

    public void reset() {
        // Perform a reset on the SUL: empty input list on wrapper and send reset signal
        sockout.println("reset");

        // Check if reset succeeded. Note: this is also needed because not receiving after reset will immediately continue
        // to sending Input, allowing the possibility for the client to receive "reset INPUT" in one string. Reading in between
        // will force a break since reading is blocking.
        try {
            String line = sockin.readLine();
            if (!line.toString().equals("resetok")) {
                throw new MapperException(
                    String.format(
                        "Reset did not succeed. On sending reset expected %s but got %s",
                        "resetok",
                        line
                    )
                );
            }
        } catch (IOException e) {
            throw new MapperException("Reset could not be sent", e);
        }
    }
}
