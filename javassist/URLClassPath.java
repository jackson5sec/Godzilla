/*     */ package javassist;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
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
/*     */ 
/*     */ 
/*     */ public class URLClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   protected String hostname;
/*     */   protected int port;
/*     */   protected String directory;
/*     */   protected String packageName;
/*     */   
/*     */   public URLClassPath(String host, int port, String directory, String packageName) {
/*  65 */     this.hostname = host;
/*  66 */     this.port = port;
/*  67 */     this.directory = directory;
/*  68 */     this.packageName = packageName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  73 */     return this.hostname + ":" + this.port + this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openClassfile(String classname) {
/*     */     try {
/*  84 */       URLConnection con = openClassfile0(classname);
/*  85 */       if (con != null) {
/*  86 */         return con.getInputStream();
/*     */       }
/*  88 */     } catch (IOException iOException) {}
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   private URLConnection openClassfile0(String classname) throws IOException {
/*  93 */     if (this.packageName == null || classname.startsWith(this.packageName)) {
/*     */       
/*  95 */       String jarname = this.directory + classname.replace('.', '/') + ".class";
/*  96 */       return fetchClass0(this.hostname, this.port, jarname);
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL find(String classname) {
/*     */     try {
/* 109 */       URLConnection con = openClassfile0(classname);
/* 110 */       InputStream is = con.getInputStream();
/* 111 */       if (is != null) {
/* 112 */         is.close();
/* 113 */         return con.getURL();
/*     */       }
/*     */     
/* 116 */     } catch (IOException iOException) {}
/* 117 */     return null;
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
/*     */   public static byte[] fetchClass(String host, int port, String directory, String classname) throws IOException {
/*     */     byte[] b;
/* 135 */     URLConnection con = fetchClass0(host, port, directory + classname
/* 136 */         .replace('.', '/') + ".class");
/* 137 */     int size = con.getContentLength();
/* 138 */     InputStream s = con.getInputStream();
/*     */     try {
/* 140 */       if (size <= 0) {
/* 141 */         b = ClassPoolTail.readStream(s);
/*     */       } else {
/* 143 */         b = new byte[size];
/* 144 */         int len = 0;
/*     */         do {
/* 146 */           int n = s.read(b, len, size - len);
/* 147 */           if (n < 0) {
/* 148 */             throw new IOException("the stream was closed: " + classname);
/*     */           }
/*     */           
/* 151 */           len += n;
/* 152 */         } while (len < size);
/*     */       } 
/*     */     } finally {
/*     */       
/* 156 */       s.close();
/*     */     } 
/*     */     
/* 159 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URLConnection fetchClass0(String host, int port, String filename) throws IOException {
/*     */     URL url;
/*     */     try {
/* 168 */       url = new URL("http", host, port, filename);
/*     */     }
/* 170 */     catch (MalformedURLException e) {
/*     */       
/* 172 */       throw new IOException("invalid URL?");
/*     */     } 
/*     */     
/* 175 */     URLConnection con = url.openConnection();
/* 176 */     con.connect();
/* 177 */     return con;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\URLClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */