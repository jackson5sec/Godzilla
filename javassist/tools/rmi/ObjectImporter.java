/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectImporter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  84 */   private final byte[] endofline = new byte[] { 13, 10 };
/*     */   
/*     */   private String servername;
/*     */   private String orgServername;
/*  88 */   protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes(); private int port; private int orgPort;
/*  89 */   protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
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
/*     */   public ObjectImporter(Applet applet) {
/* 101 */     URL codebase = applet.getCodeBase();
/* 102 */     this.orgServername = this.servername = codebase.getHost();
/* 103 */     this.orgPort = this.port = codebase.getPort();
/*     */   }
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
/*     */   public ObjectImporter(String servername, int port) {
/* 120 */     this.orgServername = this.servername = servername;
/* 121 */     this.orgPort = this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(String name) {
/*     */     try {
/* 133 */       return lookupObject(name);
/*     */     }
/* 135 */     catch (ObjectNotFoundException e) {
/* 136 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpProxy(String host, int port) {
/* 145 */     String proxyHeader = "POST http://" + this.orgServername + ":" + this.orgPort;
/* 146 */     String cmd = proxyHeader + "/lookup HTTP/1.0";
/* 147 */     this.lookupCommand = cmd.getBytes();
/* 148 */     cmd = proxyHeader + "/rmi HTTP/1.0";
/* 149 */     this.rmiCommand = cmd.getBytes();
/* 150 */     this.servername = host;
/* 151 */     this.port = port;
/*     */   }
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
/*     */   public Object lookupObject(String name) throws ObjectNotFoundException {
/*     */     try {
/* 165 */       Socket sock = new Socket(this.servername, this.port);
/* 166 */       OutputStream out = sock.getOutputStream();
/* 167 */       out.write(this.lookupCommand);
/* 168 */       out.write(this.endofline);
/* 169 */       out.write(this.endofline);
/*     */       
/* 171 */       ObjectOutputStream dout = new ObjectOutputStream(out);
/* 172 */       dout.writeUTF(name);
/* 173 */       dout.flush();
/*     */       
/* 175 */       InputStream in = new BufferedInputStream(sock.getInputStream());
/* 176 */       skipHeader(in);
/* 177 */       ObjectInputStream din = new ObjectInputStream(in);
/* 178 */       int n = din.readInt();
/* 179 */       String classname = din.readUTF();
/* 180 */       din.close();
/* 181 */       dout.close();
/* 182 */       sock.close();
/*     */       
/* 184 */       if (n >= 0) {
/* 185 */         return createProxy(n, classname);
/*     */       }
/* 187 */     } catch (Exception e) {
/* 188 */       e.printStackTrace();
/* 189 */       throw new ObjectNotFoundException(name, e);
/*     */     } 
/*     */     
/* 192 */     throw new ObjectNotFoundException(name);
/*     */   }
/*     */   
/* 195 */   private static final Class<?>[] proxyConstructorParamTypes = new Class[] { ObjectImporter.class, int.class };
/*     */ 
/*     */   
/*     */   private Object createProxy(int oid, String classname) throws Exception {
/* 199 */     Class<?> c = Class.forName(classname);
/* 200 */     Constructor<?> cons = c.getConstructor(proxyConstructorParamTypes);
/* 201 */     return cons.newInstance(new Object[] { this, Integer.valueOf(oid) });
/*     */   }
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
/*     */   public Object call(int objectid, int methodid, Object[] args) throws RemoteException {
/*     */     boolean result;
/*     */     Object rvalue;
/*     */     String errmsg;
/*     */     try {
/* 234 */       Socket sock = new Socket(this.servername, this.port);
/*     */       
/* 236 */       OutputStream out = new BufferedOutputStream(sock.getOutputStream());
/* 237 */       out.write(this.rmiCommand);
/* 238 */       out.write(this.endofline);
/* 239 */       out.write(this.endofline);
/*     */       
/* 241 */       ObjectOutputStream dout = new ObjectOutputStream(out);
/* 242 */       dout.writeInt(objectid);
/* 243 */       dout.writeInt(methodid);
/* 244 */       writeParameters(dout, args);
/* 245 */       dout.flush();
/*     */       
/* 247 */       InputStream ins = new BufferedInputStream(sock.getInputStream());
/* 248 */       skipHeader(ins);
/* 249 */       ObjectInputStream din = new ObjectInputStream(ins);
/* 250 */       result = din.readBoolean();
/* 251 */       rvalue = null;
/* 252 */       errmsg = null;
/* 253 */       if (result) {
/* 254 */         rvalue = din.readObject();
/*     */       } else {
/* 256 */         errmsg = din.readUTF();
/*     */       } 
/* 258 */       din.close();
/* 259 */       dout.close();
/* 260 */       sock.close();
/*     */       
/* 262 */       if (rvalue instanceof RemoteRef) {
/* 263 */         RemoteRef ref = (RemoteRef)rvalue;
/* 264 */         rvalue = createProxy(ref.oid, ref.classname);
/*     */       }
/*     */     
/* 267 */     } catch (ClassNotFoundException e) {
/* 268 */       throw new RemoteException(e);
/*     */     }
/* 270 */     catch (IOException e) {
/* 271 */       throw new RemoteException(e);
/*     */     }
/* 273 */     catch (Exception e) {
/* 274 */       throw new RemoteException(e);
/*     */     } 
/*     */     
/* 277 */     if (result)
/* 278 */       return rvalue; 
/* 279 */     throw new RemoteException(errmsg);
/*     */   }
/*     */ 
/*     */   
/*     */   private void skipHeader(InputStream in) throws IOException {
/*     */     int len;
/*     */     do {
/* 286 */       len = 0; int c;
/* 287 */       while ((c = in.read()) >= 0 && c != 13) {
/* 288 */         len++;
/*     */       }
/* 290 */       in.read();
/* 291 */     } while (len > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeParameters(ObjectOutputStream dout, Object[] params) throws IOException {
/* 297 */     int n = params.length;
/* 298 */     dout.writeInt(n);
/* 299 */     for (int i = 0; i < n; i++) {
/* 300 */       if (params[i] instanceof Proxy) {
/* 301 */         Proxy p = (Proxy)params[i];
/* 302 */         dout.writeObject(new RemoteRef(p._getObjectId()));
/*     */       } else {
/*     */         
/* 305 */         dout.writeObject(params[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\ObjectImporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */