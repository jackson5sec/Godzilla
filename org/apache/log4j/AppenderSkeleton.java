/*     */ package org.apache.log4j;
/*     */ 
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OnlyOnceErrorHandler;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.Filter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.OptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AppenderSkeleton
/*     */   implements Appender, OptionHandler
/*     */ {
/*     */   protected Layout layout;
/*     */   protected String name;
/*     */   protected Priority threshold;
/*  53 */   protected ErrorHandler errorHandler = (ErrorHandler)new OnlyOnceErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter headFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter tailFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderSkeleton() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AppenderSkeleton(boolean isActive) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter newFilter) {
/* 102 */     if (this.headFilter == null) {
/* 103 */       this.headFilter = this.tailFilter = newFilter;
/*     */     } else {
/* 105 */       this.tailFilter.setNext(newFilter);
/* 106 */       this.tailFilter = newFilter;
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
/*     */   protected abstract void append(LoggingEvent paramLoggingEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearFilters() {
/* 128 */     this.headFilter = this.tailFilter = null;
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
/*     */   public void finalize() {
/* 140 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 143 */     LogLog.debug("Finalizing appender named [" + this.name + "].");
/* 144 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 155 */     return this.errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 166 */     return this.headFilter;
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
/*     */   public final Filter getFirstFilter() {
/* 178 */     return this.headFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layout getLayout() {
/* 186 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 197 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Priority getThreshold() {
/* 207 */     return this.threshold;
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
/*     */   public boolean isAsSevereAsThreshold(Priority priority) {
/* 219 */     return (this.threshold == null || priority.isGreaterOrEqual(this.threshold));
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
/*     */   public synchronized void doAppend(LoggingEvent event) {
/* 231 */     if (this.closed) {
/* 232 */       LogLog.error("Attempted to append to closed appender named [" + this.name + "].");
/*     */       
/*     */       return;
/*     */     } 
/* 236 */     if (!isAsSevereAsThreshold(event.getLevel())) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     Filter f = this.headFilter;
/*     */ 
/*     */     
/* 243 */     while (f != null) {
/* 244 */       switch (f.decide(event)) { case -1: return;
/*     */         case 1: break;
/*     */         case 0:
/* 247 */           f = f.getNext(); }
/*     */ 
/*     */     
/*     */     } 
/* 251 */     append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setErrorHandler(ErrorHandler eh) {
/* 261 */     if (eh == null) {
/*     */ 
/*     */       
/* 264 */       LogLog.warn("You have tried to set a null error-handler.");
/*     */     } else {
/* 266 */       this.errorHandler = eh;
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
/*     */   public void setLayout(Layout layout) {
/* 278 */     this.layout = layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 287 */     this.name = name;
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
/*     */   public void setThreshold(Priority threshold) {
/* 302 */     this.threshold = threshold;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\AppenderSkeleton.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */