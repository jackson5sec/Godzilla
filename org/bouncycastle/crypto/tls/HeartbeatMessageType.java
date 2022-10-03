package org.bouncycastle.crypto.tls;

public class HeartbeatMessageType {
  public static final short heartbeat_request = 1;
  
  public static final short heartbeat_response = 2;
  
  public static boolean isValid(short paramShort) {
    return (paramShort >= 1 && paramShort <= 2);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\HeartbeatMessageType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */