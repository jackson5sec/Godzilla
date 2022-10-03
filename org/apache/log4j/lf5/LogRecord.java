/*     */ package org.apache.log4j.lf5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
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
/*     */ public abstract class LogRecord
/*     */   implements Serializable
/*     */ {
/*  41 */   protected static long _seqCount = 0L;
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
/*  65 */   protected long _millis = System.currentTimeMillis();
/*  66 */   protected String _category = "Debug";
/*  67 */   protected String _message = "";
/*  68 */   protected LogLevel _level = LogLevel.INFO;
/*  69 */   protected long _sequenceNumber = getNextId();
/*  70 */   protected String _thread = Thread.currentThread().toString();
/*  71 */   protected String _ndc = "";
/*  72 */   protected String _location = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _thrownStackTrace;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Throwable _thrown;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getLevel() {
/*  87 */     return this._level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(LogLevel level) {
/*  98 */     this._level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isSevereLevel();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThrown() {
/* 111 */     Throwable thrown = getThrown();
/* 112 */     if (thrown == null) {
/* 113 */       return false;
/*     */     }
/* 115 */     String thrownString = thrown.toString();
/* 116 */     return (thrownString != null && thrownString.trim().length() != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFatal() {
/* 123 */     return (isSevereLevel() || hasThrown());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCategory() {
/* 134 */     return this._category;
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
/*     */   public void setCategory(String category) {
/* 156 */     this._category = category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 166 */     return this._message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String message) {
/* 176 */     this._message = message;
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
/*     */   public long getSequenceNumber() {
/* 188 */     return this._sequenceNumber;
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
/*     */   public void setSequenceNumber(long number) {
/* 200 */     this._sequenceNumber = number;
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
/*     */   public long getMillis() {
/* 212 */     return this._millis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMillis(long millis) {
/* 223 */     this._millis = millis;
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
/*     */   public String getThreadDescription() {
/* 236 */     return this._thread;
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
/*     */   public void setThreadDescription(String threadDescription) {
/* 249 */     this._thread = threadDescription;
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
/*     */   public String getThrownStackTrace() {
/* 270 */     return this._thrownStackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThrownStackTrace(String trace) {
/* 280 */     this._thrownStackTrace = trace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 291 */     return this._thrown;
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
/*     */   public void setThrown(Throwable thrown) {
/* 304 */     if (thrown == null) {
/*     */       return;
/*     */     }
/* 307 */     this._thrown = thrown;
/* 308 */     StringWriter sw = new StringWriter();
/* 309 */     PrintWriter out = new PrintWriter(sw);
/* 310 */     thrown.printStackTrace(out);
/* 311 */     out.flush();
/* 312 */     this._thrownStackTrace = sw.toString();
/*     */     try {
/* 314 */       out.close();
/* 315 */       sw.close();
/* 316 */     } catch (IOException e) {}
/*     */ 
/*     */     
/* 319 */     out = null;
/* 320 */     sw = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 327 */     StringBuffer buf = new StringBuffer();
/* 328 */     buf.append("LogRecord: [" + this._level + ", " + this._message + "]");
/* 329 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNDC() {
/* 338 */     return this._ndc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNDC(String ndc) {
/* 347 */     this._ndc = ndc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 356 */     return this._location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(String location) {
/* 365 */     this._location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void resetSequenceNumber() {
/* 373 */     _seqCount = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static synchronized long getNextId() {
/* 381 */     _seqCount++;
/* 382 */     return _seqCount;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\LogRecord.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */