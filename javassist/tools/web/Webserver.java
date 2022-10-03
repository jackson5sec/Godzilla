/*     */ package javassist.tools.web;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Date;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.Translator;
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
/*     */ public class Webserver
/*     */ {
/*     */   private ServerSocket socket;
/*     */   private ClassPool classPool;
/*     */   protected Translator translator;
/*  55 */   private static final byte[] endofline = new byte[] { 13, 10 };
/*     */ 
/*     */   
/*     */   private static final int typeHtml = 1;
/*     */ 
/*     */   
/*     */   private static final int typeClass = 2;
/*     */   
/*     */   private static final int typeGif = 3;
/*     */   
/*     */   private static final int typeJpeg = 4;
/*     */   
/*     */   private static final int typeText = 5;
/*     */   
/*  69 */   public String debugDir = null;
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
/*  85 */   public String htmlfileBase = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/*  92 */     if (args.length == 1) {
/*  93 */       Webserver web = new Webserver(args[0]);
/*  94 */       web.run();
/*     */     } else {
/*     */       
/*  97 */       System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Webserver(String port) throws IOException {
/* 107 */     this(Integer.parseInt(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Webserver(int port) throws IOException {
/* 116 */     this.socket = new ServerSocket(port);
/* 117 */     this.classPool = null;
/* 118 */     this.translator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPool(ClassPool loader) {
/* 126 */     this.classPool = loader;
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
/*     */   public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
/* 140 */     this.classPool = cp;
/* 141 */     this.translator = t;
/* 142 */     t.start(this.classPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() throws IOException {
/* 149 */     this.socket.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg) {
/* 156 */     System.out.println(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg1, String msg2) {
/* 163 */     System.out.print(msg1);
/* 164 */     System.out.print(" ");
/* 165 */     System.out.println(msg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg1, String msg2, String msg3) {
/* 172 */     System.out.print(msg1);
/* 173 */     System.out.print(" ");
/* 174 */     System.out.print(msg2);
/* 175 */     System.out.print(" ");
/* 176 */     System.out.println(msg3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging2(String msg) {
/* 183 */     System.out.print("    ");
/* 184 */     System.out.println(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 191 */     System.err.println("ready to service..."); while (true) {
/*     */       try {
/*     */         while (true) {
/* 194 */           ServiceThread th = new ServiceThread(this, this.socket.accept());
/* 195 */           th.start();
/*     */         }  break;
/* 197 */       } catch (IOException e) {
/* 198 */         logging(e.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   final void process(Socket clnt) throws IOException {
/* 203 */     InputStream in = new BufferedInputStream(clnt.getInputStream());
/* 204 */     String cmd = readLine(in);
/* 205 */     logging(clnt.getInetAddress().getHostName(), (new Date())
/* 206 */         .toString(), cmd);
/* 207 */     while (skipLine(in) > 0);
/*     */ 
/*     */     
/* 210 */     OutputStream out = new BufferedOutputStream(clnt.getOutputStream());
/*     */     try {
/* 212 */       doReply(in, out, cmd);
/*     */     }
/* 214 */     catch (BadHttpRequest e) {
/* 215 */       replyError(out, e);
/*     */     } 
/*     */     
/* 218 */     out.flush();
/* 219 */     in.close();
/* 220 */     out.close();
/* 221 */     clnt.close();
/*     */   }
/*     */   
/*     */   private String readLine(InputStream in) throws IOException {
/* 225 */     StringBuffer buf = new StringBuffer();
/*     */     int c;
/* 227 */     while ((c = in.read()) >= 0 && c != 13) {
/* 228 */       buf.append((char)c);
/*     */     }
/* 230 */     in.read();
/* 231 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private int skipLine(InputStream in) throws IOException {
/* 236 */     int len = 0; int c;
/* 237 */     while ((c = in.read()) >= 0 && c != 13) {
/* 238 */       len++;
/*     */     }
/* 240 */     in.read();
/* 241 */     return len;
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
/*     */   public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
/*     */     int fileType;
/*     */     String filename, urlName;
/* 257 */     if (cmd.startsWith("GET /")) {
/* 258 */       filename = urlName = cmd.substring(5, cmd.indexOf(' ', 5));
/*     */     } else {
/* 260 */       throw new BadHttpRequest();
/*     */     } 
/* 262 */     if (filename.endsWith(".class")) {
/* 263 */       fileType = 2;
/* 264 */     } else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
/* 265 */       fileType = 1;
/* 266 */     } else if (filename.endsWith(".gif")) {
/* 267 */       fileType = 3;
/* 268 */     } else if (filename.endsWith(".jpg")) {
/* 269 */       fileType = 4;
/*     */     } else {
/* 271 */       fileType = 5;
/*     */     } 
/* 273 */     int len = filename.length();
/* 274 */     if (fileType == 2 && 
/* 275 */       letUsersSendClassfile(out, filename, len)) {
/*     */       return;
/*     */     }
/* 278 */     checkFilename(filename, len);
/* 279 */     if (this.htmlfileBase != null) {
/* 280 */       filename = this.htmlfileBase + filename;
/*     */     }
/* 282 */     if (File.separatorChar != '/') {
/* 283 */       filename = filename.replace('/', File.separatorChar);
/*     */     }
/* 285 */     File file = new File(filename);
/* 286 */     if (file.canRead()) {
/* 287 */       sendHeader(out, file.length(), fileType);
/* 288 */       FileInputStream fin = new FileInputStream(file);
/* 289 */       byte[] filebuffer = new byte[4096];
/*     */       while (true) {
/* 291 */         len = fin.read(filebuffer);
/* 292 */         if (len <= 0)
/*     */           break; 
/* 294 */         out.write(filebuffer, 0, len);
/*     */       } 
/*     */       
/* 297 */       fin.close();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 304 */     if (fileType == 2) {
/*     */       
/* 306 */       InputStream fin = getClass().getResourceAsStream("/" + urlName);
/* 307 */       if (fin != null) {
/* 308 */         ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 309 */         byte[] filebuffer = new byte[4096];
/*     */         while (true) {
/* 311 */           len = fin.read(filebuffer);
/* 312 */           if (len <= 0)
/*     */             break; 
/* 314 */           barray.write(filebuffer, 0, len);
/*     */         } 
/*     */         
/* 317 */         byte[] classfile = barray.toByteArray();
/* 318 */         sendHeader(out, classfile.length, 2);
/* 319 */         out.write(classfile);
/* 320 */         fin.close();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 325 */     throw new BadHttpRequest();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkFilename(String filename, int len) throws BadHttpRequest {
/* 331 */     for (int i = 0; i < len; i++) {
/* 332 */       char c = filename.charAt(i);
/* 333 */       if (!Character.isJavaIdentifierPart(c) && c != '.' && c != '/') {
/* 334 */         throw new BadHttpRequest();
/*     */       }
/*     */     } 
/* 337 */     if (filename.indexOf("..") >= 0) {
/* 338 */       throw new BadHttpRequest();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean letUsersSendClassfile(OutputStream out, String filename, int length) throws IOException, BadHttpRequest {
/*     */     byte[] classfile;
/* 345 */     if (this.classPool == null) {
/* 346 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 350 */     String classname = filename.substring(0, length - 6).replace('/', '.');
/*     */     try {
/* 352 */       if (this.translator != null) {
/* 353 */         this.translator.onLoad(this.classPool, classname);
/*     */       }
/* 355 */       CtClass c = this.classPool.get(classname);
/* 356 */       classfile = c.toBytecode();
/* 357 */       if (this.debugDir != null) {
/* 358 */         c.writeFile(this.debugDir);
/*     */       }
/* 360 */     } catch (Exception e) {
/* 361 */       throw new BadHttpRequest(e);
/*     */     } 
/*     */     
/* 364 */     sendHeader(out, classfile.length, 2);
/* 365 */     out.write(classfile);
/* 366 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendHeader(OutputStream out, long dataLength, int filetype) throws IOException {
/* 372 */     out.write("HTTP/1.0 200 OK".getBytes());
/* 373 */     out.write(endofline);
/* 374 */     out.write("Content-Length: ".getBytes());
/* 375 */     out.write(Long.toString(dataLength).getBytes());
/* 376 */     out.write(endofline);
/* 377 */     if (filetype == 2) {
/* 378 */       out.write("Content-Type: application/octet-stream".getBytes());
/* 379 */     } else if (filetype == 1) {
/* 380 */       out.write("Content-Type: text/html".getBytes());
/* 381 */     } else if (filetype == 3) {
/* 382 */       out.write("Content-Type: image/gif".getBytes());
/* 383 */     } else if (filetype == 4) {
/* 384 */       out.write("Content-Type: image/jpg".getBytes());
/* 385 */     } else if (filetype == 5) {
/* 386 */       out.write("Content-Type: text/plain".getBytes());
/*     */     } 
/* 388 */     out.write(endofline);
/* 389 */     out.write(endofline);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void replyError(OutputStream out, BadHttpRequest e) throws IOException {
/* 395 */     logging2("bad request: " + e.toString());
/* 396 */     out.write("HTTP/1.0 400 Bad Request".getBytes());
/* 397 */     out.write(endofline);
/* 398 */     out.write(endofline);
/* 399 */     out.write("<H1>Bad Request</H1>".getBytes());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\web\Webserver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */