/*    */ package org.fife.rsta.ac.perl;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.swing.text.Element;
/*    */ import org.fife.rsta.ac.OutputCollector;
/*    */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*    */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
/*    */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*    */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PerlOutputCollector
/*    */   extends OutputCollector
/*    */ {
/*    */   private PerlParser parser;
/*    */   private DefaultParseResult result;
/*    */   private Element root;
/* 36 */   private static final Pattern ERROR_PATTERN = Pattern.compile(" at .+ line (\\d+)\\.$");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PerlOutputCollector(InputStream in, PerlParser parser, DefaultParseResult result, Element root) {
/* 46 */     super(in);
/* 47 */     this.parser = parser;
/* 48 */     this.result = result;
/* 49 */     this.root = root;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleLineRead(String line) {
/* 59 */     Matcher m = ERROR_PATTERN.matcher(line);
/*    */     
/* 61 */     if (m.find()) {
/*    */       
/* 63 */       line = line.substring(0, line.length() - m.group().length());
/*    */       
/* 65 */       int lineNumber = Integer.parseInt(m.group(1)) - 1;
/* 66 */       Element elem = this.root.getElement(lineNumber);
/* 67 */       int start = elem.getStartOffset();
/* 68 */       int end = elem.getEndOffset();
/*    */       
/* 70 */       DefaultParserNotice pn = new DefaultParserNotice((Parser)this.parser, line, lineNumber, start, end - start);
/*    */ 
/*    */       
/* 73 */       this.result.addNotice((ParserNotice)pn);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlOutputCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */