/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import javassist.bytecode.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ClassPoolTail
/*     */ {
/* 198 */   protected ClassPathList pathList = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 203 */     StringBuffer buf = new StringBuffer();
/* 204 */     buf.append("[class path: ");
/* 205 */     ClassPathList list = this.pathList;
/* 206 */     while (list != null) {
/* 207 */       buf.append(list.path.toString());
/* 208 */       buf.append(File.pathSeparatorChar);
/* 209 */       list = list.next;
/*     */     } 
/*     */     
/* 212 */     buf.append(']');
/* 213 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public synchronized ClassPath insertClassPath(ClassPath cp) {
/* 217 */     this.pathList = new ClassPathList(cp, this.pathList);
/* 218 */     return cp;
/*     */   }
/*     */   
/*     */   public synchronized ClassPath appendClassPath(ClassPath cp) {
/* 222 */     ClassPathList tail = new ClassPathList(cp, null);
/* 223 */     ClassPathList list = this.pathList;
/* 224 */     if (list == null) {
/* 225 */       this.pathList = tail;
/*     */     } else {
/* 227 */       while (list.next != null) {
/* 228 */         list = list.next;
/*     */       }
/* 230 */       list.next = tail;
/*     */     } 
/*     */     
/* 233 */     return cp;
/*     */   }
/*     */   
/*     */   public synchronized void removeClassPath(ClassPath cp) {
/* 237 */     ClassPathList list = this.pathList;
/* 238 */     if (list != null)
/* 239 */       if (list.path == cp) {
/* 240 */         this.pathList = list.next;
/*     */       } else {
/* 242 */         while (list.next != null) {
/* 243 */           if (list.next.path == cp) {
/* 244 */             list.next = list.next.next; continue;
/*     */           } 
/* 246 */           list = list.next;
/*     */         } 
/*     */       }  
/*     */   }
/*     */   public ClassPath appendSystemPath() {
/* 251 */     if (ClassFile.MAJOR_VERSION < 53)
/* 252 */       return appendClassPath(new ClassClassPath()); 
/* 253 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 254 */     return appendClassPath(new LoaderClassPath(cl));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPath insertClassPath(String pathname) throws NotFoundException {
/* 260 */     return insertClassPath(makePathObject(pathname));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPath appendClassPath(String pathname) throws NotFoundException {
/* 266 */     return appendClassPath(makePathObject(pathname));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ClassPath makePathObject(String pathname) throws NotFoundException {
/* 272 */     String lower = pathname.toLowerCase();
/* 273 */     if (lower.endsWith(".jar") || lower.endsWith(".zip")) {
/* 274 */       return new JarClassPath(pathname);
/*     */     }
/* 276 */     int len = pathname.length();
/* 277 */     if (len > 2 && pathname.charAt(len - 1) == '*' && (pathname
/* 278 */       .charAt(len - 2) == '/' || pathname
/* 279 */       .charAt(len - 2) == File.separatorChar)) {
/* 280 */       String dir = pathname.substring(0, len - 2);
/* 281 */       return new JarDirClassPath(dir);
/*     */     } 
/*     */     
/* 284 */     return new DirClassPath(pathname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
/* 293 */     InputStream fin = openClassfile(classname);
/* 294 */     if (fin == null) {
/* 295 */       throw new NotFoundException(classname);
/*     */     }
/*     */     try {
/* 298 */       copyStream(fin, out);
/*     */     } finally {
/*     */       
/* 301 */       fin.close();
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream openClassfile(String classname) throws NotFoundException {
/* 335 */     ClassPathList list = this.pathList;
/* 336 */     InputStream ins = null;
/* 337 */     NotFoundException error = null;
/* 338 */     while (list != null) {
/*     */       try {
/* 340 */         ins = list.path.openClassfile(classname);
/*     */       }
/* 342 */       catch (NotFoundException e) {
/* 343 */         if (error == null) {
/* 344 */           error = e;
/*     */         }
/*     */       } 
/* 347 */       if (ins == null) {
/* 348 */         list = list.next; continue;
/*     */       } 
/* 350 */       return ins;
/*     */     } 
/*     */     
/* 353 */     if (error != null)
/* 354 */       throw error; 
/* 355 */     return null;
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
/*     */   public URL find(String classname) {
/* 367 */     ClassPathList list = this.pathList;
/* 368 */     URL url = null;
/* 369 */     while (list != null) {
/* 370 */       url = list.path.find(classname);
/* 371 */       if (url == null) {
/* 372 */         list = list.next; continue;
/*     */       } 
/* 374 */       return url;
/*     */     } 
/*     */     
/* 377 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readStream(InputStream fin) throws IOException {
/* 386 */     byte[][] bufs = new byte[8][];
/* 387 */     int bufsize = 4096;
/*     */     
/* 389 */     for (int i = 0; i < 8; i++) {
/* 390 */       bufs[i] = new byte[bufsize];
/* 391 */       int size = 0;
/* 392 */       int len = 0;
/*     */       while (true) {
/* 394 */         len = fin.read(bufs[i], size, bufsize - size);
/* 395 */         if (len >= 0) {
/* 396 */           size += len;
/*     */         } else {
/* 398 */           byte[] result = new byte[bufsize - 4096 + size];
/* 399 */           int s = 0;
/* 400 */           for (int j = 0; j < i; j++) {
/* 401 */             System.arraycopy(bufs[j], 0, result, s, s + 4096);
/* 402 */             s = s + s + 4096;
/*     */           } 
/*     */           
/* 405 */           System.arraycopy(bufs[i], 0, result, s, size);
/* 406 */           return result;
/*     */         } 
/* 408 */         if (size >= bufsize) {
/* 409 */           bufsize *= 2; break;
/*     */         } 
/*     */       } 
/* 412 */     }  throw new IOException("too much data");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream fin, OutputStream fout) throws IOException {
/* 423 */     int bufsize = 4096;
/* 424 */     byte[] buf = null;
/* 425 */     for (int i = 0; i < 64; i++) {
/* 426 */       if (i < 8) {
/* 427 */         bufsize *= 2;
/* 428 */         buf = new byte[bufsize];
/*     */       } 
/* 430 */       int size = 0;
/* 431 */       int len = 0;
/*     */       while (true) {
/* 433 */         len = fin.read(buf, size, bufsize - size);
/* 434 */         if (len >= 0) {
/* 435 */           size += len;
/*     */         } else {
/* 437 */           fout.write(buf, 0, size);
/*     */           return;
/*     */         } 
/* 440 */         if (size >= bufsize) {
/* 441 */           fout.write(buf); break;
/*     */         } 
/*     */       } 
/* 444 */     }  throw new IOException("too much data");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\ClassPoolTail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */