/*     */ package javassist;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
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
/*     */ public class ByteArrayClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   protected String classname;
/*     */   protected byte[] classfile;
/*     */   
/*     */   public ByteArrayClassPath(String name, byte[] classfile) {
/*  65 */     this.classname = name;
/*  66 */     this.classfile = classfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     return "byte[]:" + this.classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openClassfile(String classname) {
/*  79 */     if (this.classname.equals(classname))
/*  80 */       return new ByteArrayInputStream(this.classfile); 
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL find(String classname) {
/*  89 */     if (this.classname.equals(classname)) {
/*  90 */       String cname = classname.replace('.', '/') + ".class";
/*     */       try {
/*  92 */         return new URL(null, "file:/ByteArrayClassPath/" + cname, new BytecodeURLStreamHandler());
/*     */       }
/*  94 */       catch (MalformedURLException malformedURLException) {}
/*     */     } 
/*     */     
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   private class BytecodeURLStreamHandler extends URLStreamHandler {
/*     */     protected URLConnection openConnection(URL u) {
/* 102 */       return new ByteArrayClassPath.BytecodeURLConnection(u);
/*     */     }
/*     */     
/*     */     private BytecodeURLStreamHandler() {} }
/*     */   
/*     */   private class BytecodeURLConnection extends URLConnection { protected BytecodeURLConnection(URL url) {
/* 108 */       super(url);
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() throws IOException {}
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 115 */       return new ByteArrayInputStream(ByteArrayClassPath.this.classfile);
/*     */     }
/*     */     
/*     */     public int getContentLength() {
/* 119 */       return ByteArrayClassPath.this.classfile.length;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\ByteArrayClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */