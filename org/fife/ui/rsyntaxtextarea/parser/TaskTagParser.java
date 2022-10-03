/*     */ package org.fife.ui.rsyntaxtextarea.parser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskTagParser
/*     */   extends AbstractParser
/*     */ {
/*     */   private DefaultParseResult result;
/*     */   private static final String DEFAULT_TASK_PATTERN = "TODO|FIXME|HACK";
/*     */   private Pattern taskPattern;
/*  35 */   private static final Color COLOR = new Color(48, 150, 252);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskTagParser() {
/*  44 */     this.result = new DefaultParseResult(this);
/*  45 */     setTaskPattern("TODO|FIXME|HACK");
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
/*     */   public String getTaskPattern() {
/*  58 */     return (this.taskPattern == null) ? null : this.taskPattern.pattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseResult parse(RSyntaxDocument doc, String style) {
/*  65 */     Element root = doc.getDefaultRootElement();
/*  66 */     int lineCount = root.getElementCount();
/*     */     
/*  68 */     if (this.taskPattern == null || style == null || "text/plain"
/*  69 */       .equals(style)) {
/*  70 */       this.result.clearNotices();
/*  71 */       this.result.setParsedLines(0, lineCount - 1);
/*  72 */       return this.result;
/*     */     } 
/*     */ 
/*     */     
/*  76 */     this.result.clearNotices();
/*  77 */     this.result.setParsedLines(0, lineCount - 1);
/*     */     
/*  79 */     for (int line = 0; line < lineCount; line++) {
/*     */       
/*  81 */       Token t = doc.getTokenListForLine(line);
/*  82 */       int offs = -1;
/*  83 */       int start = -1;
/*  84 */       String text = null;
/*     */       
/*  86 */       while (t != null && t.isPaintable()) {
/*  87 */         if (t.isComment()) {
/*     */           
/*  89 */           offs = t.getOffset();
/*  90 */           text = t.getLexeme();
/*     */           
/*  92 */           Matcher m = this.taskPattern.matcher(text);
/*  93 */           if (m.find()) {
/*  94 */             start = m.start();
/*  95 */             offs += start;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 100 */         t = t.getNextToken();
/*     */       } 
/*     */       
/* 103 */       if (start > -1 && text != null) {
/* 104 */         text = text.substring(start);
/*     */         
/* 106 */         int len = text.length();
/* 107 */         TaskNotice pn = new TaskNotice(this, text, line + 1, offs, len);
/* 108 */         pn.setLevel(ParserNotice.Level.INFO);
/* 109 */         pn.setShowInEditor(false);
/* 110 */         pn.setColor(COLOR);
/* 111 */         this.result.addNotice(pn);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 116 */     return this.result;
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
/*     */   public void setTaskPattern(String pattern) {
/* 133 */     if (pattern == null || pattern.length() == 0) {
/* 134 */       this.taskPattern = null;
/*     */     } else {
/*     */       
/* 137 */       this.taskPattern = Pattern.compile(pattern);
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
/*     */   public static class TaskNotice
/*     */     extends DefaultParserNotice
/*     */   {
/*     */     public TaskNotice(Parser parser, String message, int line, int offs, int length) {
/* 152 */       super(parser, message, line, offs, length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\TaskTagParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */