/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OutputCollector
/*     */   implements Runnable
/*     */ {
/*     */   private InputStream in;
/*     */   private StringBuilder sb;
/*     */   
/*     */   public OutputCollector(InputStream in) {
/*  38 */     this(in, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputCollector(InputStream in, StringBuilder sb) {
/*  49 */     this.in = in;
/*  50 */     this.sb = sb;
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
/*     */   public OutputCollector(InputStream in, boolean collect) {
/*  64 */     this.in = in;
/*  65 */     if (collect) {
/*  66 */       this.sb = new StringBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuilder getOutput() {
/*  77 */     return this.sb;
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
/*     */   protected void handleLineRead(String line) {
/*  89 */     if (this.sb != null) {
/*  90 */       this.sb.append(line).append('\n');
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
/*     */   public void run() {
/* 102 */     try (BufferedReader r = new BufferedReader(new InputStreamReader(this.in))) {
/* 103 */       String line; while ((line = r.readLine()) != null) {
/* 104 */         handleLineRead(line);
/*     */       
/*     */       }
/*     */     }
/* 108 */     catch (IOException ioe) {
/* 109 */       ioe.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\OutputCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */