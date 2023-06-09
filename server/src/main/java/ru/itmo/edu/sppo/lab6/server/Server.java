package ru.itmo.edu.sppo.lab6.server;

import lombok.extern.slf4j.Slf4j;
import ru.itmo.edu.sppo.lab6.document.ReadProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

@Slf4j
public class Server {
    private static final String PORT_PROPERTIES = "server.port";
    private ServerSocketChannel serverSocketChannel;
    private final int port;

    public Server() {
        port = Integer.parseInt(
                new ReadProperties().read(PORT_PROPERTIES)
        );
    }

    public void start() {
        try {
            log.debug("Запускается сервер");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            log.debug("Сервер успешно запущен и готов принимать запросы");

            new AcceptConnection(serverSocketChannel).acceptConnection();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.getStackTrace();
            stop();
        }
    }

    public void stop() {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            log.error("Не удалось закрыть соединение с сервером");
        }
    }
}

