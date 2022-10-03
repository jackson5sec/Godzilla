/*     */ package javassist.tools.web;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
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
/*     */ public class Viewer
/*     */   extends ClassLoader
/*     */ {
/*     */   private String server;
/*     */   private int port;
/*     */   
/*     */   public static void main(String[] args) throws Throwable {
/*  61 */     if (args.length >= 3) {
/*  62 */       Viewer cl = new Viewer(args[0], Integer.parseInt(args[1]));
/*  63 */       String[] args2 = new String[args.length - 3];
/*  64 */       System.arraycopy(args, 3, args2, 0, args.length - 3);
/*  65 */       cl.run(args[2], args2);
/*     */     } else {
/*     */       
/*  68 */       System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Viewer(String host, int p) {
/*  79 */     this.server = host;
/*  80 */     this.port = p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServer() {
/*  86 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  91 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String classname, String[] args) throws Throwable {
/* 102 */     Class<?> c = loadClass(classname);
/*     */     try {
/* 104 */       c.getDeclaredMethod("main", new Class[] { String[].class
/* 105 */           }).invoke(null, new Object[] { args });
/*     */     }
/* 107 */     catch (InvocationTargetException e) {
/* 108 */       throw e.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/* 119 */     Class<?> c = findLoadedClass(name);
/* 120 */     if (c == null) {
/* 121 */       c = findClass(name);
/*     */     }
/* 123 */     if (c == null) {
/* 124 */       throw new ClassNotFoundException(name);
/*     */     }
/* 126 */     if (resolve) {
/* 127 */       resolveClass(c);
/*     */     }
/* 129 */     return c;
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
/*     */   protected Class<?> findClass(String name) throws ClassNotFoundException {
/* 144 */     Class<?> c = null;
/* 145 */     if (name.startsWith("java.") || name.startsWith("javax.") || name
/* 146 */       .equals("javassist.tools.web.Viewer")) {
/* 147 */       c = findSystemClass(name);
/*     */     }
/* 149 */     if (c == null) {
/*     */       try {
/* 151 */         byte[] b = fetchClass(name);
/* 152 */         if (b != null) {
/* 153 */           c = defineClass(name, b, 0, b.length);
/*     */         }
/* 155 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 158 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] fetchClass(String classname) throws Exception {
/*     */     byte[] b;
/* 169 */     URL url = new URL("http", this.server, this.port, "/" + classname.replace('.', '/') + ".class");
/* 170 */     URLConnection con = url.openConnection();
/* 171 */     con.connect();
/* 172 */     int size = con.getContentLength();
/* 173 */     InputStream s = con.getInputStream();
/* 174 */     if (size <= 0) {
/* 175 */       b = readStream(s);
/*     */     } else {
/* 177 */       b = new byte[size];
/* 178 */       int len = 0;
/*     */       do {
/* 180 */         int n = s.read(b, len, size - len);
/* 181 */         if (n < 0) {
/* 182 */           s.close();
/* 183 */           throw new IOException("the stream was closed: " + classname);
/*     */         } 
/*     */         
/* 186 */         len += n;
/* 187 */       } while (len < size);
/*     */     } 
/*     */     
/* 190 */     s.close();
/* 191 */     return b;
/*     */   }
/*     */   
/*     */   private byte[] readStream(InputStream fin) throws IOException {
/* 195 */     byte[] buf = new byte[4096];
/* 196 */     int size = 0;
/* 197 */     int len = 0;
/*     */     do {
/* 199 */       size += len;
/* 200 */       if (buf.length - size <= 0) {
/* 201 */         byte[] newbuf = new byte[buf.length * 2];
/* 202 */         System.arraycopy(buf, 0, newbuf, 0, size);
/* 203 */         buf = newbuf;
/*     */       } 
/*     */       
/* 206 */       len = fin.read(buf, size, buf.length - size);
/* 207 */     } while (len >= 0);
/*     */     
/* 209 */     byte[] result = new byte[size];
/* 210 */     System.arraycopy(buf, 0, result, 0, size);
/* 211 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\web\Viewer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */