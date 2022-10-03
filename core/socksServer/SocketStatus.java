package core.socksServer;

public interface SocketStatus {
  String getErrorMessage();
  
  boolean isActive();
  
  boolean start();
  
  boolean stop();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\SocketStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */