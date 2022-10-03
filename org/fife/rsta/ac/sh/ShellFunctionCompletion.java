/*     */ package org.fife.rsta.ac.sh;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ShellFunctionCompletion
/*     */   extends FunctionCompletion
/*     */ {
/*     */   public ShellFunctionCompletion(CompletionProvider provider, String name, String returnType) {
/*  40 */     super(provider, name, returnType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  50 */     String summary = null;
/*  51 */     if (ShellCompletionProvider.getUseLocalManPages()) {
/*  52 */       summary = getSummaryFromManPage();
/*     */     }
/*     */     
/*  55 */     if (summary == null) {
/*  56 */       summary = super.getSummary();
/*     */     }
/*     */     
/*  59 */     return summary;
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
/*     */   private String getSummaryFromManPage() {
/*     */     Process p;
/*  73 */     String[] cmd = { "/usr/bin/man", getName() };
/*     */     try {
/*  75 */       p = Runtime.getRuntime().exec(cmd);
/*  76 */     } catch (IOException ioe) {
/*  77 */       ioe.printStackTrace();
/*  78 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  82 */     OutputCollector stdout = new OutputCollector(p.getInputStream());
/*  83 */     Thread t = new Thread((Runnable)stdout);
/*  84 */     t.start();
/*  85 */     int rc = 0;
/*     */     try {
/*  87 */       rc = p.waitFor();
/*  88 */       t.join();
/*     */     }
/*  90 */     catch (InterruptedException ie) {
/*  91 */       ie.printStackTrace();
/*     */     } 
/*     */     
/*  94 */     CharSequence output = null;
/*  95 */     if (rc == 0) {
/*  96 */       output = stdout.getOutput();
/*  97 */       if (output != null && output.length() > 0) {
/*  98 */         output = manToHtml(output);
/*     */       }
/*     */     } 
/*     */     
/* 102 */     return (output == null) ? null : output.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final StringBuffer manToHtml(CharSequence text) {
/* 109 */     Pattern p = Pattern.compile("(?:_\\010.)+|(?:(.)\\010\\1)+");
/* 110 */     Matcher m = p.matcher(text);
/* 111 */     StringBuffer sb = new StringBuffer("<html><pre>");
/* 112 */     while (m.find()) {
/* 113 */       System.out.println("... found '" + m.group() + "'");
/* 114 */       String group = m.group();
/* 115 */       if (group.startsWith("_")) {
/* 116 */         sb.append("<u>");
/* 117 */         String str = group.replaceAll("_\\010", "");
/* 118 */         str = quoteReplacement(str);
/* 119 */         m.appendReplacement(sb, str);
/* 120 */         System.out.println("--- '" + str);
/* 121 */         sb.append("</u>");
/*     */         continue;
/*     */       } 
/* 124 */       String replacement = group.replaceAll(".\\010.", "");
/* 125 */       replacement = quoteReplacement(replacement);
/* 126 */       m.appendReplacement(sb, replacement);
/* 127 */       System.out.println("--- '" + replacement);
/*     */     } 
/*     */     
/* 130 */     m.appendTail(sb);
/*     */     
/* 132 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String quoteReplacement(String text) {
/* 137 */     if (text.indexOf('$') > -1 || text.indexOf('\\') > -1) {
/* 138 */       StringBuilder sb = new StringBuilder();
/* 139 */       for (int i = 0; i < text.length(); i++) {
/* 140 */         char ch = text.charAt(i);
/* 141 */         if (ch == '$' || ch == '\\') {
/* 142 */           sb.append('\\');
/*     */         }
/* 144 */         sb.append(ch);
/*     */       } 
/* 146 */       text = sb.toString();
/*     */     } 
/* 148 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\sh\ShellFunctionCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */