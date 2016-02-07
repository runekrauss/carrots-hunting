package server;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the server of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Server implements Runnable {

	/** Port of the server */
	protected int port = 55555;

	/** Socket of the server */
	protected ServerSocket serverSocket;

	/** Status of the server */
	protected boolean isStopped;

	/** Thread of the server */
	protected Thread thread;

	/** Allowed threads at same time */
	protected ExecutorService threadPool = Executors.newFixedThreadPool(50);

	/**
	 * Constructor of the server which initializes the attributes.
	 * 
	 * @param port
	 *            Port of the server
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Here, Program code is executed in parallel. Creates a new thread to
	 * communicate with a client. Furthermore, protects critical sections.
	 */
	public void run() {
		synchronized (this) {
			thread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException ioe) {
				if (isStopped()) {
					System.out.println("Server was stopped...");
					break;
				}
				throw new RuntimeException("An error was occured while accepting client connection.", ioe);
			}
			threadPool.execute(new Handler(clientSocket));
		}
		threadPool.shutdown();
	}

	/**
	 * Checks the status of the server.
	 * 
	 * @return Status of the server
	 */
	private synchronized boolean isStopped() {
		return isStopped;
	}

	/**
	 * Opens a server socket.
	 */
	private void openServerSocket() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("The port " + port + " could not be opened.", e);
		}
	}

	/**
	 * Stops the server.
	 */
	public synchronized void stop() {
		isStopped = true;
		try {
			serverSocket.close();
		} catch (IOException ioe) {
			throw new RuntimeException("An error was occured while closing server.", ioe);
		}
	}
}