package il.co.ilrd.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatClient {
	private final SocketChannel socket;
	private final String userName;
	private Consumer<String> action;
	private BufferedReader buffer;
	
	public ChatClient(String ip, int port, String userName, Consumer<String> action) throws IOException {
		
		this.action = action;
		socket = SocketChannel.open(new InetSocketAddress(ip, port));
		this.userName = userName;
		buffer = new BufferedReader(new InputStreamReader(socket.socket().getInputStream()));
		start();
	}

	private void start() throws IOException{
		new Thread(new ListenThread()).start();
	}
	
	public void sendMessage(String userInput) throws IOException {
		String finalMessage = "<" + userName + "> :" + userInput;
		socket.write(ByteBuffer.wrap(finalMessage.getBytes())); 										
	}
	
	private class ListenThread implements Runnable{
		 
		@Override
		public void run() {
			while(socket.isOpen()) {
				try {
					action.accept(buffer.readLine());
				}
				catch (IOException e) {
					System.out.println("chat closed");
					return;
				}
			}
		}
	}
	
	public void close() {		
		try {
			socket.close();
			buffer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
				
		boolean flag = true;
		String temp;

		System.out.println("your name is:");
		ChatClient client = new ChatClient("10.1.0.28", 40002, new Scanner(System.in).nextLine(), x->System.out.println(x));
		
		while (flag) {
			temp = new Scanner(System.in).nextLine();
			switch(temp) {
				
				case "exit": {
					flag = false;
					client.close();
					return;
				}
					
				default: {
					client.sendMessage(temp + "\n");	
					break;
				}
			}
		}
	}
}
