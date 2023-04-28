import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Server extends Thread{

    private ServerSocketChannel serverSocketChannel=null;
    private Selector selector = null;
    private final HashMap <String, String> addressToIdMap=new HashMap<>();
    private final HashMap <String, StringBuilder> idToLogsMap = new HashMap<>();
    private final StringBuilder generalLog = new StringBuilder();

    public Server(String host, int port) {
        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void startServer(){
        start();
    }

    public void stopServer(){
        interrupt();
    }

    public String getServerLog(){
        return generalLog.toString();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keysIterator = keys.iterator();
                while(keysIterator.hasNext()){
                    SelectionKey key = keysIterator.next();
                    keysIterator.remove();

                    if(key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        serviceRequest(socketChannel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void serviceRequest(SocketChannel socketChannel){
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            String currentTime = LocalDateTime.now().format(dateTimeFormatter)+"."+(LocalDateTime.now().getNano()+"").substring(0,3);;

            if(charBuffer.toString().matches("login\\s.+")){
                String id = charBuffer.toString().split("\\s")[1];
                addressToIdMap.put(socketChannel.getRemoteAddress().toString(),id);
                idToLogsMap.put(id, new StringBuilder("=== "+id+" log start ===\nlogged in"));
                generalLog.append("\n").append(id).append(" logged in at ").append(currentTime);
                sendResponse(socketChannel, "logged in");
            } else if (charBuffer.toString().matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{4}-\\d{2}-\\d{2}") || charBuffer.toString().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}\\s\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")){
                String result = Time.passed(charBuffer.toString().split("\\s")[0], charBuffer.toString().split("\\s")[1]);
                sendResponse(socketChannel, result);
                StringBuilder stringBuilder = idToLogsMap.get(addressToIdMap.get(socketChannel.getRemoteAddress().toString()));
                stringBuilder.append("\nRequest: ").append(charBuffer).append("\nResult:\n").append(result);
                idToLogsMap.put(socketChannel.getRemoteAddress().toString(), stringBuilder);
                generalLog.append("\n").append(addressToIdMap.get(socketChannel.getRemoteAddress().toString())).append(" request at ").append(currentTime).append(": \"").append(charBuffer).append("\"");
            } else if(charBuffer.toString().equals("bye")){
                StringBuilder stringBuilder = idToLogsMap.get(socketChannel.getRemoteAddress().toString());
                stringBuilder.append("\nlogged out\n=== ").append(addressToIdMap.get(socketChannel.getRemoteAddress().toString())).append(" log end ===");
                generalLog.append("\n").append(addressToIdMap.get(socketChannel.getRemoteAddress().toString())).append(" logged out at ").append(currentTime);
                sendResponse(socketChannel,"logged out");
            }else if(charBuffer.toString().equals("bye and log transfer")){
                StringBuilder stringBuilder = idToLogsMap.get(socketChannel.getRemoteAddress().toString());
                stringBuilder.append("\nlogged out\n=== ").append(addressToIdMap.get(socketChannel.getRemoteAddress().toString())).append(" log end ===\n");
                generalLog.append("\n").append(addressToIdMap.get(socketChannel.getRemoteAddress().toString())).append(" logged out at ").append(currentTime);
                sendResponse(socketChannel, idToLogsMap.get(socketChannel.getRemoteAddress().toString()).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(SocketChannel socketChannel, String resp){
        try{
            ByteBuffer byteBuffer = ByteBuffer.wrap(resp.getBytes(StandardCharsets.UTF_8));
            socketChannel.write(byteBuffer);
        }catch (Exception e){e.printStackTrace();}
    }

}
