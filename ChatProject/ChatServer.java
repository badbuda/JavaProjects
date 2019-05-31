package il.co.ilrd.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChatServer {
	private final ByteBuffer buff = ByteBuffer.allocate(1024);
	private final Selector selector;
	private final int port;
	private List<SocketChannel> clients;
	boolean isContinue = true;
	private ServerSocketChannel serversc;
	
	public ChatServer(int port) throws IOException {
		this.port = port;
		clients = new ArrayList<SocketChannel>();
		selector = Selector.open();
		start();
	}

	public void start() throws IOException {
		serversc = ServerSocketChannel.open();
		InetSocketAddress host = new InetSocketAddress("10.1.0.28", port);
		serversc.configureBlocking(false);
		serversc.bind(host);
		int ops = serversc.validOps();
		serversc.register(selector, ops , null);
		
		while (isContinue) {
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> keysIter = keys.iterator();
			while (keysIter.hasNext()) {
				SelectionKey selectionKey = keysIter.next();
				if (selectionKey.isAcceptable()) {
					handelAccepet(selectionKey, serversc);
				}
				
				else if (selectionKey.isWritable() || selectionKey.isReadable()) {
					handelRead(selectionKey);
					buff.clear();
				}
			}
			
			keysIter.remove();
		}
	}
	
	private void handelAccepet (SelectionKey selectionKey, ServerSocketChannel serversc) throws IOException {
		SocketChannel channel = serversc.accept();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		clients.add(channel);
	}
	
	private void handelRead (SelectionKey selectionKey) throws IOException {
		
		SocketChannel channel = (SocketChannel) selectionKey.channel();
		if (-1 == channel.read(buff)) {
			selectionKey.cancel();
			return;
		}

		buff.flip();
		for (int i = 0; i < clients.size(); ++i) {
			if (!clients.get(i).equals(channel)) {	
				clients.get(i).write(buff);
			}
		
			buff.rewind();
		}
	}
	
	public void close() throws IOException {
		isContinue = false;
		serversc.close();
		selector.close();
	}
	
	public static void main(String[] args) throws IOException {
			        
	       new ChatServer(40002);
		}
	}
