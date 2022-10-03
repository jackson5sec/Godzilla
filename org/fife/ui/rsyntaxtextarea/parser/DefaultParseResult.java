/*     */ package org.fife.ui.rsyntaxtextarea.parser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultParseResult
/*     */   implements ParseResult
/*     */ {
/*     */   private Parser parser;
/*     */   private int firstLineParsed;
/*     */   private int lastLineParsed;
/*     */   private List<ParserNotice> notices;
/*     */   private long parseTime;
/*     */   private Exception error;
/*     */   
/*     */   public DefaultParseResult(Parser parser) {
/*  34 */     this.parser = parser;
/*  35 */     this.notices = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNotice(ParserNotice notice) {
/*  46 */     this.notices.add(notice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearNotices() {
/*  56 */     this.notices.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getError() {
/*  62 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstLineParsed() {
/*  68 */     return this.firstLineParsed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastLineParsed() {
/*  74 */     return this.lastLineParsed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ParserNotice> getNotices() {
/*  80 */     return this.notices;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser getParser() {
/*  86 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getParseTime() {
/*  92 */     return this.parseTime;
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
/*     */   public void setError(Exception e) {
/* 104 */     this.error = e;
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
/*     */   public void setParsedLines(int first, int last) {
/* 117 */     this.firstLineParsed = first;
/* 118 */     this.lastLineParsed = last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParseTime(long time) {
/* 129 */     this.parseTime = time;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\DefaultParseResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */