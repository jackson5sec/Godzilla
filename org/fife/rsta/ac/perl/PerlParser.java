/*     */ package org.fife.rsta.ac.perl;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultEditorKit;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.rsta.ac.IOUtil;
/*     */ import org.fife.rsta.ac.OutputCollector;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerlParser
/*     */   extends AbstractParser
/*     */ {
/*  67 */   private DefaultParseResult result = new DefaultParseResult((Parser)this);
/*     */   
/*     */   private boolean taintModeEnabled;
/*     */   
/*     */   private boolean warningsEnabled;
/*     */   
/*     */   private String perl5LibOverride;
/*     */   private String[] perlEnvironment;
/*     */   private static final int MAX_COMPILE_MILLIS = 10000;
/*     */   
/*     */   private void createPerlEnvironment() {
/*  78 */     this.perlEnvironment = null;
/*     */ 
/*     */     
/*  81 */     String perl5Lib = getPerl5LibOverride();
/*  82 */     if (perl5Lib != null) {
/*  83 */       String[] toAdd = { "PERL5LIB", perl5Lib };
/*  84 */       this.perlEnvironment = IOUtil.getEnvironmentSafely(toAdd);
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
/*     */   public String getPerl5LibOverride() {
/*  98 */     return this.perl5LibOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getWarningsEnabled() {
/* 109 */     return this.warningsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTaintModeEnabled() {
/* 120 */     return this.taintModeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseResult parse(RSyntaxDocument doc, String style) {
/* 130 */     this.result.clearNotices();
/*     */     
/* 132 */     int lineCount = doc.getDefaultRootElement().getElementCount();
/* 133 */     this.result.setParsedLines(0, lineCount - 1);
/*     */     
/* 135 */     long start = System.currentTimeMillis();
/*     */ 
/*     */     
/*     */     try {
/* 139 */       File dir = PerlLanguageSupport.getPerlInstallLocation();
/* 140 */       if (dir == null) {
/* 141 */         return (ParseResult)this.result;
/*     */       }
/* 143 */       String exe = (File.separatorChar == '\\') ? "bin/perl.exe" : "bin/perl";
/* 144 */       File perl = new File(dir, exe);
/* 145 */       if (!perl.isFile()) {
/* 146 */         return (ParseResult)this.result;
/*     */       }
/*     */       
/* 149 */       File tempFile = File.createTempFile("perlParser", ".tmp");
/* 150 */       BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
/*     */       
/*     */       try {
/* 153 */         (new DefaultEditorKit()).write(out, (Document)doc, 0, doc.getLength());
/* 154 */       } catch (BadLocationException ble) {
/* 155 */         ble.printStackTrace();
/* 156 */         throw new IOException(ble.getMessage());
/*     */       } 
/* 158 */       out.close();
/*     */       
/* 160 */       String opts = "-c";
/* 161 */       if (getWarningsEnabled()) {
/* 162 */         opts = opts + "w";
/*     */       }
/* 164 */       if (isTaintModeEnabled()) {
/* 165 */         opts = opts + "t";
/*     */       }
/*     */       
/* 168 */       String[] envp = this.perlEnvironment;
/* 169 */       String[] cmd = { perl.getAbsolutePath(), opts, tempFile.getAbsolutePath() };
/* 170 */       Process p = Runtime.getRuntime().exec(cmd, envp);
/* 171 */       Element root = doc.getDefaultRootElement();
/* 172 */       OutputCollector stdout = new OutputCollector(p.getInputStream(), false);
/*     */       
/* 174 */       Thread t = new Thread((Runnable)stdout);
/* 175 */       t.start();
/*     */       
/* 177 */       PerlOutputCollector stderr = new PerlOutputCollector(p.getErrorStream(), this, this.result, root);
/* 178 */       Thread t2 = new Thread((Runnable)stderr);
/* 179 */       t2.start();
/*     */       
/*     */       try {
/* 182 */         t2.join(10000L);
/* 183 */         t.join(10000L);
/* 184 */         if (t.isAlive()) {
/* 185 */           t.interrupt();
/*     */         }
/*     */         else {
/*     */           
/* 189 */           p.waitFor();
/*     */         }
/*     */       
/* 192 */       } catch (InterruptedException ie) {
/* 193 */         ie.printStackTrace();
/*     */       } 
/*     */       
/* 196 */       long time = System.currentTimeMillis() - start;
/* 197 */       this.result.setParseTime(time);
/*     */     
/*     */     }
/* 200 */     catch (IOException ioe) {
/* 201 */       this.result.setError(ioe);
/* 202 */       ioe.printStackTrace();
/*     */     } 
/*     */     
/* 205 */     return (ParseResult)this.result;
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
/*     */   public void setPerl5LibOverride(String override) {
/* 219 */     this.perl5LibOverride = override;
/* 220 */     createPerlEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaintModeEnabled(boolean enabled) {
/* 231 */     this.taintModeEnabled = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarningsEnabled(boolean enabled) {
/* 242 */     this.warningsEnabled = enabled;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */