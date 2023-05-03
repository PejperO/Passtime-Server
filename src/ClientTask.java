import java.util.List;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> {

    private ClientTask(Client client, List<String> reqs, boolean showSendRes) {
        super(()->{
            client.connect();
            client.send("login " + client.getId());
            for (String req : reqs) {
                String resp = client.send(req);
                if (showSendRes)
                    System.out.println(resp);
            }
            return client.send("bye and log transfer");
        });
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes){
        return new ClientTask(c, reqs, showSendRes);
    }
}
