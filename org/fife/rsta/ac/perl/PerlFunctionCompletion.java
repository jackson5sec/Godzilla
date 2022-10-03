/*     */ package org.fife.rsta.ac.perl;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.swing.UIManager;
/*     */ import org.fife.rsta.ac.OutputCollector;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerlFunctionCompletion
/*     */   extends FunctionCompletion
/*     */ {
/*     */   public PerlFunctionCompletion(CompletionProvider provider, String name, String returnType) {
/*  41 */     super(provider, name, returnType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  51 */     String summary = null;
/*  52 */     File installLoc = PerlLanguageSupport.getPerlInstallLocation();
/*  53 */     if (installLoc != null && PerlLanguageSupport.getUseSystemPerldoc()) {
/*  54 */       summary = getSummaryFromPerldoc(installLoc);
/*     */     }
/*     */     
/*  57 */     if (summary == null) {
/*  58 */       summary = super.getSummary();
/*     */     }
/*     */     
/*  61 */     return summary;
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
/*     */   private String getSummaryFromPerldoc(File installLoc) {
/*     */     Process p;
/*  76 */     String fileName = "bin/perldoc";
/*  77 */     if (File.separatorChar == '\\') {
/*  78 */       fileName = fileName + ".bat";
/*     */     }
/*  80 */     File perldoc = new File(installLoc, fileName);
/*  81 */     if (!perldoc.isFile()) {
/*  82 */       return null;
/*     */     }
/*     */     
/*  85 */     String[] cmd = { perldoc.getAbsolutePath(), "-f", getName() };
/*     */     try {
/*  87 */       p = Runtime.getRuntime().exec(cmd);
/*  88 */     } catch (IOException ioe) {
/*  89 */       ioe.printStackTrace();
/*  90 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     OutputCollector oc = new OutputCollector(p.getInputStream());
/*  95 */     Thread t = new Thread((Runnable)oc);
/*  96 */     t.start();
/*  97 */     int rc = 0;
/*     */     try {
/*  99 */       rc = p.waitFor();
/* 100 */       t.join();
/*     */     }
/* 102 */     catch (InterruptedException ie) {
/* 103 */       ie.printStackTrace();
/*     */     } 
/*     */     
/* 106 */     CharSequence output = null;
/* 107 */     if (rc == 0) {
/* 108 */       output = oc.getOutput();
/* 109 */       if (output != null && output.length() > 0) {
/* 110 */         output = perldocToHtml(output);
/*     */       }
/*     */     } 
/*     */     
/* 114 */     return (output == null) ? null : output.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder perldocToHtml(CharSequence text) {
/*     */     StringBuilder sb;
/* 123 */     Font font = UIManager.getFont("Label.font");
/*     */     
/* 125 */     if (font != null) {
/*     */       
/* 127 */       sb = (new StringBuilder("<html><style>pre { font-family: ")).append(font.getFamily()).append("; }</style><pre>");
/*     */     } else {
/*     */       
/* 130 */       sb = new StringBuilder("<html><pre>");
/*     */     } 
/*     */     
/* 133 */     sb.append(text);
/* 134 */     return sb;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlFunctionCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */