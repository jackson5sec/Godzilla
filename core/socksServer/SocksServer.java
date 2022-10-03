/*     */ package core.socksServer;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ public class SocksServer {
/*     */   private ServerSocket serverSocket;
/*     */   
/*     */   public void start(InetSocketAddress inetSocketAddress) throws IOException {
/*  21 */     this.serverSocket = new ServerSocket();
/*  22 */     this.serverSocket.bind(inetSocketAddress, 1000);
/*     */   }
/*     */   
/*     */   public Socket accept() throws Exception {
/*  26 */     return this.serverSocket.accept();
/*     */   }
/*     */ 
/*     */   
/*     */   public static SocksRelayInfo handleSocks(Socket client, int packetMaxSize, int capacity) {
/*     */     try {
/*  32 */       byte[] handshakePacker = new byte[404];
/*     */       
/*  34 */       InputStream inputStream = client.getInputStream();
/*     */       
/*  36 */       client.setSoTimeout(3000);
/*  37 */       int readNum = inputStream.read(handshakePacker);
/*     */       
/*  39 */       SocksRelayInfo socksRelayInfo = null;
/*     */ 
/*     */       
/*  42 */       try { switch (handshakePacker[0])
/*     */         { case 4:
/*  44 */             socksRelayInfo = socks4(client, handshakePacker, packetMaxSize, capacity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  59 */             client.setSoTimeout(0);
/*  60 */             return socksRelayInfo;case 5: socksRelayInfo = socks5(client, handshakePacker, packetMaxSize, capacity); client.setSoTimeout(0); return socksRelayInfo; }  Log.log("未知的Socks协议标志 读取长度:%d 协议标志:%d", new Object[] { Integer.valueOf(readNum), Byte.valueOf(handshakePacker[0]) }); client.close(); } catch (Exception e) { e.printStackTrace(); socksRelayInfo = null; }  client.setSoTimeout(0); return socksRelayInfo;
/*  61 */     } catch (Exception e) {
/*  62 */       return null;
/*     */     } 
/*     */   }
/*     */   public void close() throws IOException {
/*  66 */     if (this.serverSocket != null && !this.serverSocket.isClosed())
/*  67 */       this.serverSocket.close(); 
/*     */   }
/*     */   
/*     */   private static SocksRelayInfo socks5(Socket client, byte[] handshakePacker, int packetMaxSize, int capacity) throws Exception {
/*  71 */     ByteBuffer buff = ByteBuffer.wrap(handshakePacker);
/*  72 */     OutputStream outputStream = client.getOutputStream();
/*  73 */     InputStream inputStream = client.getInputStream();
/*  74 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
/*  75 */     byte version = buff.get();
/*  76 */     if (version == 5) {
/*  77 */       byte[] shortByteArr = new byte[2];
/*  78 */       shortByteArr[1] = buff.get();
/*  79 */       short authMethodNum = ByteBuffer.wrap(shortByteArr).getShort();
/*  80 */       Log.log(String.format(EasyI18N.getI18nString("address:%s port:%s 认证方法数量: %d"), new Object[] { client.getInetAddress().getHostAddress(), Integer.valueOf(client.getPort()), Short.valueOf(authMethodNum) }), new Object[0]);
/*  81 */       byte[] authMethod = new byte[authMethodNum];
/*  82 */       for (int i = 0; i < authMethodNum; i++) {
/*  83 */         authMethod[i] = buff.get();
/*     */       }
/*  85 */       if (Arrays.binarySearch(authMethod, (byte)0) > -1) {
/*  86 */         byte[] hostNameLenByteArr; short hostNameLen; outputStream.write(new byte[] { version, 0 });
/*  87 */         outputStream.flush();
/*  88 */         inputStream.read(handshakePacker);
/*  89 */         buff = ByteBuffer.wrap(handshakePacker);
/*  90 */         SocksRelayInfo socks = new SocksRelayInfo(packetMaxSize, capacity);
/*  91 */         socks.setVersion(buff.get());
/*  92 */         socks.setCommand(buff.get());
/*  93 */         buff.get();
/*  94 */         byte addressType = buff.get();
/*  95 */         byte[] addressByteArr = null;
/*  96 */         byte[] hostName = null;
/*  97 */         switch (addressType) {
/*     */           case 1:
/*  99 */             addressByteArr = new byte[4];
/* 100 */             buff.get(addressByteArr);
/*     */             break;
/*     */           case 3:
/* 103 */             hostNameLenByteArr = new byte[2];
/* 104 */             hostNameLenByteArr[1] = buff.get();
/* 105 */             hostNameLen = ByteBuffer.wrap(hostNameLenByteArr).getShort();
/* 106 */             hostName = new byte[hostNameLen];
/* 107 */             buff.get(hostName);
/*     */             break;
/*     */           case 4:
/* 110 */             addressByteArr = new byte[16];
/* 111 */             buff.get(addressByteArr);
/*     */             break;
/*     */           default:
/* 114 */             Log.log(String.format(EasyI18N.getI18nString("address:%s port:%s 不支持的地址类型: %d"), new Object[] { client.getInetAddress().getHostAddress(), Integer.valueOf(client.getPort()), Byte.valueOf(addressType) }), new Object[0]); break;
/*     */         } 
/* 116 */         if (addressByteArr != null) {
/* 117 */           socks.setDestHost(InetAddress.getByAddress(addressByteArr).getHostAddress());
/* 118 */         } else if (hostName != null) {
/* 119 */           socks.setDestHost(new String(hostName));
/*     */         } 
/* 121 */         byte[] portByteArr = new byte[2];
/* 122 */         buff.get(portByteArr);
/* 123 */         socks.setDestPort(ByteBuffer.wrap(portByteArr).getShort());
/* 124 */         if (socks.getCommand() == 1) {
/*     */           
/* 126 */           byteArrayOutputStream.write(version);
/* 127 */           byteArrayOutputStream.write(0);
/* 128 */           byteArrayOutputStream.write(1);
/* 129 */           byteArrayOutputStream.write(1);
/* 130 */           byteArrayOutputStream.write(client.getLocalAddress().getAddress());
/* 131 */           byteArrayOutputStream.write(portByteArr);
/* 132 */           socks.connectSuccessMessage = byteArrayOutputStream.toByteArray();
/* 133 */           socks.setClient(client);
/* 134 */           return socks;
/*     */         } 
/* 136 */         Log.log(String.format(EasyI18N.getI18nString("address:%s port:%s 不支持的命令 command : %d "), new Object[] { client.getInetAddress().getHostAddress(), Integer.valueOf(client.getPort()), Byte.valueOf(socks.getCommand()) }), new Object[0]);
/*     */       } else {
/*     */         
/* 139 */         Log.log(String.format(EasyI18N.getI18nString("address:%s port:%s 不支持的认证方法:%s"), new Object[] { client.getInetAddress().getHostAddress(), Integer.valueOf(client.getPort()), Arrays.toString(authMethod) }), new Object[0]);
/*     */       } 
/*     */     } else {
/* 142 */       Log.log(String.format(EasyI18N.getI18nString("address:%s port:%s 不是socks5"), new Object[] { client.getInetAddress().getHostAddress(), Integer.valueOf(client.getPort()) }), new Object[0]);
/*     */     } 
/* 144 */     return null;
/*     */   }
/*     */   private static SocksRelayInfo socks4(Socket client, byte[] handshakePacker, int packetMaxSize, int capacity) throws Exception {
/* 147 */     InputStream inputStream = client.getInputStream();
/* 148 */     OutputStream outputStream = client.getOutputStream();
/* 149 */     SocksRelayInfo socks4 = new SocksRelayInfo(packetMaxSize, capacity);
/* 150 */     ByteBuffer buff = ByteBuffer.wrap(handshakePacker);
/* 151 */     socks4.setVersion(buff.get());
/* 152 */     socks4.setCommand(buff.get());
/* 153 */     socks4.setDestPort(buff.getShort());
/* 154 */     byte[] host = new byte[4];
/* 155 */     buff.get(host);
/* 156 */     socks4.setDestHost(InetAddress.getByAddress(host).getHostAddress());
/* 157 */     socks4.setUSERID(functions.readCString(buff));
/* 158 */     if (socks4.getDestHost().startsWith("0.0.0.") && buff.position() + 2 < handshakePacker.length) {
/* 159 */       socks4.setDestHost(functions.readCString(buff));
/*     */     }
/* 161 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
/* 162 */     byteArrayOutputStream.write(0);
/* 163 */     byteArrayOutputStream.write(90);
/* 164 */     byteArrayOutputStream.write(ByteBuffer.allocate(2).putShort(socks4.getDestPort()).array());
/* 165 */     byteArrayOutputStream.write(host);
/* 166 */     socks4.connectSuccessMessage = byteArrayOutputStream.toByteArray();
/*     */     
/* 168 */     socks4.setClient(client);
/*     */     
/* 170 */     return socks4;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\SocksServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */