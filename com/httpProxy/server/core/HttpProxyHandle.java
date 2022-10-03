package com.httpProxy.server.core;

import com.httpProxy.server.request.HttpRequest;
import java.net.Socket;

public interface HttpProxyHandle {
  void handler(Socket paramSocket, HttpRequest paramHttpRequest) throws Exception;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\core\HttpProxyHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */