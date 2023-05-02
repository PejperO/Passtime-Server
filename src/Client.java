import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel socketChannel=null;
    private final StringBuilder log = new StringBuilder();

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void connect(){
        try{
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String send (String req){
        try{
            if(socketChannel.isOpen()){
                socketChannel.write(ByteBuffer.wrap(req.getBytes(StandardCharsets.UTF_8)));
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                while(byteBuffer.position()==0){
                    socketChannel.read(byteBuffer);
                    Thread.sleep(100);
                }


                byteBuffer.flip();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                String resp = charBuffer.toString();

                if(resp.equals("logged in"))
                    log.append("\nlogged in");
                else if (req.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{4}-\\d{2}-\\d{2}") || req.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}\\s\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")){
                    log.append("\nRequest: ").append(req).append("\nResult:\n").append(resp);
                } else if (req.equals("bye") || req.equals("bye and log transfer")){
                    log.append("\nlogged out\n=== ").append(id).append("log end ===");
                }

                return resp;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
