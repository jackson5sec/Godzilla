/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JsHinter
/*     */ {
/*     */   private JavaScriptParser parser;
/*     */   private DefaultParseResult result;
/*  50 */   private static final Map<String, MarkStrategy> MARK_STRATEGIES = new HashMap<>(); static {
/*  51 */     MARK_STRATEGIES.put("E015", MarkStrategy.MARK_CUR_TOKEN);
/*  52 */     MARK_STRATEGIES.put("E019", MarkStrategy.MARK_CUR_TOKEN);
/*  53 */     MARK_STRATEGIES.put("E030", MarkStrategy.MARK_CUR_TOKEN);
/*  54 */     MARK_STRATEGIES.put("E041", MarkStrategy.STOP_PARSING);
/*  55 */     MARK_STRATEGIES.put("E042", MarkStrategy.STOP_PARSING);
/*  56 */     MARK_STRATEGIES.put("E043", MarkStrategy.STOP_PARSING);
/*  57 */     MARK_STRATEGIES.put("W004", MarkStrategy.MARK_PREV_NON_WS_TOKEN);
/*  58 */     MARK_STRATEGIES.put("W015", MarkStrategy.MARK_CUR_TOKEN);
/*  59 */     MARK_STRATEGIES.put("W032", MarkStrategy.MARK_PREV_TOKEN);
/*  60 */     MARK_STRATEGIES.put("W033", MarkStrategy.MARK_PREV_TOKEN);
/*  61 */     MARK_STRATEGIES.put("W060", MarkStrategy.MARK_CUR_TOKEN);
/*  62 */     MARK_STRATEGIES.put("W098", MarkStrategy.MARK_PREV_TOKEN);
/*  63 */     MARK_STRATEGIES.put("W116", MarkStrategy.MARK_PREV_TOKEN);
/*  64 */     MARK_STRATEGIES.put("W117", MarkStrategy.MARK_CUR_TOKEN);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private JsHinter(JavaScriptParser parser, RSyntaxDocument doc, DefaultParseResult result) {
/*  70 */     this.parser = parser;
/*     */     
/*  72 */     this.result = result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void parse(JavaScriptParser parser, RSyntaxTextArea textArea, DefaultParseResult result) throws IOException {
/*  79 */     String stdout = null;
/*  80 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*     */     
/*  82 */     List<String> command = new ArrayList<>();
/*  83 */     if (File.separatorChar == '\\') {
/*  84 */       command.add("cmd.exe");
/*  85 */       command.add("/c");
/*     */     } else {
/*     */       
/*  88 */       command.add("/bin/sh");
/*  89 */       command.add("-c");
/*     */     } 
/*  91 */     command.add("jshint");
/*  92 */     File jshintrc = parser.getJsHintRCFile(textArea);
/*  93 */     if (jshintrc != null) {
/*  94 */       command.add("--config");
/*  95 */       command.add(jshintrc.getAbsolutePath());
/*     */     } 
/*  97 */     command.add("--verbose");
/*  98 */     command.add("-");
/*     */     
/* 100 */     ProcessBuilder pb = new ProcessBuilder(command);
/* 101 */     pb.redirectErrorStream();
/*     */     
/* 103 */     Process p = pb.start();
/* 104 */     PrintWriter w = new PrintWriter(p.getOutputStream());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     InputStream outStream = p.getInputStream();
/* 110 */     InputStream errStream = p.getErrorStream();
/* 111 */     StreamReaderThread stdoutThread = new StreamReaderThread(outStream);
/* 112 */     StreamReaderThread stderrThread = new StreamReaderThread(errStream);
/* 113 */     stdoutThread.start();
/*     */ 
/*     */     
/*     */     try {
/* 117 */       String text = doc.getText(0, doc.getLength());
/* 118 */       w.print(text);
/* 119 */       w.flush();
/* 120 */       w.close();
/*     */       
/* 122 */       p.waitFor();
/* 123 */       p = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 129 */       stdoutThread.join();
/* 130 */       stderrThread.join();
/* 131 */       stdout = stdoutThread.getStreamOutput();
/*     */     }
/* 133 */     catch (InterruptedException ie) {
/*     */       
/* 135 */       stdoutThread.interrupt();
/*     */     }
/* 137 */     catch (BadLocationException ble) {
/* 138 */       ble.printStackTrace();
/*     */     } finally {
/* 140 */       if (outStream != null) {
/* 141 */         outStream.close();
/*     */       }
/* 143 */       w.close();
/* 144 */       if (p != null) {
/* 145 */         p.destroy();
/*     */       }
/*     */     } 
/*     */     
/* 149 */     JsHinter hinter = new JsHinter(parser, doc, result);
/* 150 */     hinter.parseOutput(doc, stdout);
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
/*     */   private void parseOutput(RSyntaxDocument doc, String output) {
/* 163 */     String[] lines = output.split("\r?\n");
/*     */     
/* 165 */     for (String line : lines) {
/*     */       
/* 167 */       String origLine = line;
/*     */       
/* 169 */       if (line.startsWith("stdin: line ")) {
/* 170 */         line = line.substring("stdin: line ".length());
/* 171 */         int end = 0;
/* 172 */         while (Character.isDigit(line.charAt(end))) {
/* 173 */           end++;
/*     */         }
/* 175 */         int lineNum = Integer.parseInt(line.substring(0, end)) - 1;
/* 176 */         if (lineNum == -1) {
/*     */ 
/*     */ 
/*     */           
/* 180 */           DefaultParserNotice dpn = new DefaultParserNotice((Parser)this.parser, origLine, 0);
/*     */           
/* 182 */           this.result.addNotice((ParserNotice)dpn);
/*     */         } else {
/*     */           
/* 185 */           line = line.substring(end);
/* 186 */           if (line.startsWith(", col ")) {
/* 187 */             line = line.substring(", col ".length());
/* 188 */             end = 0;
/* 189 */             while (Character.isDigit(line.charAt(end))) {
/* 190 */               end++;
/*     */             }
/*     */             
/* 193 */             line = line.substring(end);
/* 194 */             if (line.startsWith(", ")) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 199 */               String msg = line.substring(", ".length());
/* 200 */               String errorCode = null;
/*     */ 
/*     */               
/* 203 */               ParserNotice.Level noticeType = ParserNotice.Level.ERROR;
/* 204 */               if (msg.charAt(msg.length() - 1) == ')') {
/* 205 */                 int openParen = msg.lastIndexOf('(');
/* 206 */                 errorCode = msg.substring(openParen + 1, msg
/* 207 */                     .length() - 1);
/* 208 */                 if (msg.charAt(openParen + 1) == 'W') {
/* 209 */                   noticeType = ParserNotice.Level.WARNING;
/*     */                 }
/* 211 */                 msg = msg.substring(0, openParen - 1);
/*     */               } 
/*     */ 
/*     */               
/* 215 */               MarkStrategy markStrategy = getMarkStrategy(errorCode);
/* 216 */               switch (markStrategy) {
/*     */               
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 249 */               DefaultParserNotice dpn = new DefaultParserNotice((Parser)this.parser, msg, lineNum);
/*     */ 
/*     */ 
/*     */               
/* 253 */               dpn.setLevel(noticeType);
/* 254 */               this.result.addNotice((ParserNotice)dpn);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final MarkStrategy getMarkStrategy(String msgCode) {
/* 316 */     MarkStrategy strategy = MARK_STRATEGIES.get(msgCode);
/* 317 */     return (strategy != null) ? strategy : MarkStrategy.MARK_LINE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class StreamReaderThread
/*     */     extends Thread
/*     */   {
/*     */     private BufferedReader r;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private StringBuilder buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StreamReaderThread(InputStream in) {
/* 342 */       this.r = new BufferedReader(new InputStreamReader(in));
/* 343 */       this.buffer = new StringBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getStreamOutput() {
/* 352 */       return this.buffer.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/*     */         String line;
/* 363 */         while ((line = this.r.readLine()) != null) {
/* 364 */           this.buffer.append(line).append('\n');
/*     */         }
/*     */       }
/* 367 */       catch (IOException ioe) {
/* 368 */         this.buffer.append("IOException occurred: " + ioe.getMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum MarkStrategy
/*     */   {
/* 380 */     MARK_LINE, MARK_CUR_TOKEN, MARK_PREV_TOKEN, MARK_PREV_NON_WS_TOKEN,
/* 381 */     IGNORE, STOP_PARSING;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JsHinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */