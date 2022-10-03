/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.tools.web.BadHttpRequest;
/*     */ import javassist.tools.web.Webserver;
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
/*     */ public class AppletServer
/*     */   extends Webserver
/*     */ {
/*     */   private StubGenerator stubGen;
/*     */   private Map<String, ExportedObject> exportedNames;
/*     */   private List<ExportedObject> exportedObjects;
/*  53 */   private static final byte[] okHeader = "HTTP/1.0 200 OK\r\n\r\n"
/*  54 */     .getBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletServer(String port) throws IOException, NotFoundException, CannotCompileException {
/*  64 */     this(Integer.parseInt(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletServer(int port) throws IOException, NotFoundException, CannotCompileException {
/*  75 */     this(ClassPool.getDefault(), new StubGenerator(), port);
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
/*     */   public AppletServer(int port, ClassPool src) throws IOException, NotFoundException, CannotCompileException {
/*  87 */     this(new ClassPool(src), new StubGenerator(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AppletServer(ClassPool loader, StubGenerator gen, int port) throws IOException, NotFoundException, CannotCompileException {
/*  93 */     super(port);
/*  94 */     this.exportedNames = new Hashtable<>();
/*  95 */     this.exportedObjects = new Vector<>();
/*  96 */     this.stubGen = gen;
/*  97 */     addTranslator(loader, gen);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 105 */     super.run();
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
/*     */   public synchronized int exportObject(String name, Object obj) throws CannotCompileException {
/* 123 */     Class<?> clazz = obj.getClass();
/* 124 */     ExportedObject eo = new ExportedObject();
/* 125 */     eo.object = obj;
/* 126 */     eo.methods = clazz.getMethods();
/* 127 */     this.exportedObjects.add(eo);
/* 128 */     eo.identifier = this.exportedObjects.size() - 1;
/* 129 */     if (name != null) {
/* 130 */       this.exportedNames.put(name, eo);
/*     */     }
/*     */     try {
/* 133 */       this.stubGen.makeProxyClass(clazz);
/*     */     }
/* 135 */     catch (NotFoundException e) {
/* 136 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 139 */     return eo.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
/* 149 */     if (cmd.startsWith("POST /rmi ")) {
/* 150 */       processRMI(in, out);
/* 151 */     } else if (cmd.startsWith("POST /lookup ")) {
/* 152 */       lookupName(cmd, in, out);
/*     */     } else {
/* 154 */       super.doReply(in, out, cmd);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processRMI(InputStream ins, OutputStream outs) throws IOException {
/* 160 */     ObjectInputStream in = new ObjectInputStream(ins);
/*     */     
/* 162 */     int objectId = in.readInt();
/* 163 */     int methodId = in.readInt();
/* 164 */     Exception err = null;
/* 165 */     Object rvalue = null;
/*     */     try {
/* 167 */       ExportedObject eo = this.exportedObjects.get(objectId);
/* 168 */       Object[] args = readParameters(in);
/* 169 */       rvalue = convertRvalue(eo.methods[methodId].invoke(eo.object, args));
/*     */     
/*     */     }
/* 172 */     catch (Exception e) {
/* 173 */       err = e;
/* 174 */       logging2(e.toString());
/*     */     } 
/*     */     
/* 177 */     outs.write(okHeader);
/* 178 */     ObjectOutputStream out = new ObjectOutputStream(outs);
/* 179 */     if (err != null) {
/* 180 */       out.writeBoolean(false);
/* 181 */       out.writeUTF(err.toString());
/*     */     } else {
/*     */       
/*     */       try {
/* 185 */         out.writeBoolean(true);
/* 186 */         out.writeObject(rvalue);
/*     */       }
/* 188 */       catch (NotSerializableException e) {
/* 189 */         logging2(e.toString());
/*     */       }
/* 191 */       catch (InvalidClassException e) {
/* 192 */         logging2(e.toString());
/*     */       } 
/*     */     } 
/* 195 */     out.flush();
/* 196 */     out.close();
/* 197 */     in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] readParameters(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 203 */     int n = in.readInt();
/* 204 */     Object[] args = new Object[n];
/* 205 */     for (int i = 0; i < n; i++) {
/* 206 */       Object a = in.readObject();
/* 207 */       if (a instanceof RemoteRef) {
/* 208 */         RemoteRef ref = (RemoteRef)a;
/* 209 */         ExportedObject eo = this.exportedObjects.get(ref.oid);
/* 210 */         a = eo.object;
/*     */       } 
/*     */       
/* 213 */       args[i] = a;
/*     */     } 
/*     */     
/* 216 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object convertRvalue(Object rvalue) throws CannotCompileException {
/* 222 */     if (rvalue == null) {
/* 223 */       return null;
/*     */     }
/* 225 */     String classname = rvalue.getClass().getName();
/* 226 */     if (this.stubGen.isProxyClass(classname))
/* 227 */       return new RemoteRef(exportObject((String)null, rvalue), classname); 
/* 228 */     return rvalue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void lookupName(String cmd, InputStream ins, OutputStream outs) throws IOException {
/* 234 */     ObjectInputStream in = new ObjectInputStream(ins);
/* 235 */     String name = DataInputStream.readUTF(in);
/* 236 */     ExportedObject found = this.exportedNames.get(name);
/* 237 */     outs.write(okHeader);
/* 238 */     ObjectOutputStream out = new ObjectOutputStream(outs);
/* 239 */     if (found == null) {
/* 240 */       logging2(name + "not found.");
/* 241 */       out.writeInt(-1);
/* 242 */       out.writeUTF("error");
/*     */     } else {
/*     */       
/* 245 */       logging2(name);
/* 246 */       out.writeInt(found.identifier);
/* 247 */       out.writeUTF(found.object.getClass().getName());
/*     */     } 
/*     */     
/* 250 */     out.flush();
/* 251 */     out.close();
/* 252 */     in.close();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\AppletServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */