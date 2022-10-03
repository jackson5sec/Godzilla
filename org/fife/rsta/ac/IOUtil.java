/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtil
/*     */ {
/*     */   private static Map<String, String> DEFAULT_ENV;
/*     */   
/*     */   private static Map<String, String> getDefaultEnvMap() {
/*  48 */     if (DEFAULT_ENV != null) {
/*  49 */       return DEFAULT_ENV;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  54 */       DEFAULT_ENV = System.getenv();
/*  55 */     } catch (SecurityException e) {
/*  56 */       DEFAULT_ENV = Collections.emptyMap();
/*     */     } 
/*     */     
/*  59 */     return DEFAULT_ENV;
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
/*     */   public static String getEnvSafely(String var) {
/*  74 */     String value = null;
/*     */     try {
/*  76 */       value = System.getenv(var);
/*  77 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/*  80 */     return value;
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
/*     */   public static String[] getEnvironmentSafely(String[] toAdd) {
/*  98 */     Map<String, String> env = getDefaultEnvMap();
/*     */ 
/*     */     
/* 101 */     if (toAdd != null) {
/* 102 */       Map<String, String> temp = new HashMap<>(env);
/* 103 */       for (int j = 0; j < toAdd.length; j += 2) {
/* 104 */         temp.put(toAdd[j], toAdd[j + 1]);
/*     */       }
/* 106 */       env = temp;
/*     */     } 
/*     */ 
/*     */     
/* 110 */     int count = env.size();
/* 111 */     String[] vars = new String[count];
/* 112 */     int i = 0;
/* 113 */     for (Map.Entry<String, String> entry : env.entrySet()) {
/* 114 */       vars[i++] = (String)entry.getKey() + "=" + (String)entry.getValue();
/*     */     }
/*     */     
/* 117 */     return vars;
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
/*     */   public static int waitForProcess(Process p, StringBuilder stdout, StringBuilder stderr) throws IOException {
/* 137 */     InputStream in = p.getInputStream();
/* 138 */     InputStream err = p.getErrorStream();
/* 139 */     Thread t1 = new Thread(new OutputCollector(in, stdout));
/* 140 */     Thread t2 = new Thread(new OutputCollector(err, stderr));
/* 141 */     t1.start();
/* 142 */     t2.start();
/* 143 */     int rc = -1;
/*     */     
/*     */     try {
/* 146 */       rc = p.waitFor();
/* 147 */       t1.join();
/* 148 */       t2.join();
/* 149 */     } catch (InterruptedException ie) {
/* 150 */       p.destroy();
/*     */     } finally {
/* 152 */       in.close();
/* 153 */       err.close();
/*     */     } 
/*     */     
/* 156 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 167 */     for (String arg : args) {
/* 168 */       String value = getEnvSafely(arg);
/* 169 */       System.out.println(arg + "=" + value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\IOUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */