/*    */ package com.jediterm.terminal.debug;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Reader;
/*    */ import java.util.List;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class ControlSequenceVisualizer {
/* 13 */   private static final Logger LOG = Logger.getLogger(ControlSequenceVisualizer.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   private File myTempFile = null; public ControlSequenceVisualizer() {
/*    */     try {
/* 20 */       this.myTempFile = File.createTempFile("jeditermData", ".txt");
/* 21 */       this.myTempFile.deleteOnExit();
/*    */     }
/* 23 */     catch (IOException e) {
/* 24 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getVisualizedString(List<char[]> chunks) {
/*    */     try {
/* 30 */       writeChunksToFile(chunks);
/*    */       
/* 32 */       return readOutput("teseq " + this.myTempFile.getAbsolutePath());
/*    */     }
/* 34 */     catch (IOException e) {
/* 35 */       return "Control sequence visualizer teseq is not installed.\nSee http://www.gnu.org/software/teseq/\nNow printing characters as is:\n\n" + 
/*    */         
/* 37 */         joinChunks(chunks);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String joinChunks(List<char[]> chunks) {
/* 42 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 44 */     for (char[] ch : chunks) {
/* 45 */       sb.append(ch);
/*    */     }
/*    */     
/* 48 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private void writeChunksToFile(List<char[]> chunks) throws IOException {
/* 52 */     OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(this.myTempFile, false));
/*    */     try {
/* 54 */       for (char[] data : chunks) {
/* 55 */         stream.write(data, 0, data.length);
/*    */       }
/*    */     } finally {
/*    */       
/* 59 */       stream.close();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String readOutput(String command) throws IOException {
/* 65 */     Process process = Runtime.getRuntime().exec(command);
/*    */     
/* 67 */     Reader inStreamReader = new InputStreamReader(process.getInputStream());
/* 68 */     BufferedReader in = new BufferedReader(inStreamReader);
/*    */     
/* 70 */     StringBuilder sb = new StringBuilder();
/* 71 */     int i = 0;
/* 72 */     String lastNum = null; String line;
/* 73 */     while ((line = in.readLine()) != null) {
/* 74 */       if (!line.startsWith("&") && !line.startsWith("\"")) {
/* 75 */         lastNum = String.format("%3d ", new Object[] { Integer.valueOf(i++) });
/* 76 */         sb.append(lastNum);
/*    */       }
/* 78 */       else if (lastNum != null) {
/* 79 */         sb.append(CharBuffer.allocate(lastNum.length()).toString().replace(false, ' '));
/*    */       } 
/*    */       
/* 82 */       sb.append(line);
/* 83 */       sb.append("\n");
/*    */     } 
/* 85 */     in.close();
/* 86 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\debug\ControlSequenceVisualizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */