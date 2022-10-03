/*     */ package org.apache.log4j.nt;
/*     */ 
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.TTCCLayout;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NTEventLogAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*  42 */   private int _handle = 0;
/*     */   
/*  44 */   private String source = null;
/*  45 */   private String server = null;
/*     */ 
/*     */   
/*     */   public NTEventLogAppender() {
/*  49 */     this(null, null, null);
/*     */   }
/*     */   
/*     */   public NTEventLogAppender(String source) {
/*  53 */     this(null, source, null);
/*     */   }
/*     */   
/*     */   public NTEventLogAppender(String server, String source) {
/*  57 */     this(server, source, null);
/*     */   }
/*     */   
/*     */   public NTEventLogAppender(Layout layout) {
/*  61 */     this(null, null, layout);
/*     */   }
/*     */   
/*     */   public NTEventLogAppender(String source, Layout layout) {
/*  65 */     this(null, source, layout);
/*     */   }
/*     */   
/*     */   public NTEventLogAppender(String server, String source, Layout layout) {
/*  69 */     if (source == null) {
/*  70 */       source = "Log4j";
/*     */     }
/*  72 */     if (layout == null) {
/*  73 */       this.layout = (Layout)new TTCCLayout();
/*     */     } else {
/*  75 */       this.layout = layout;
/*     */     } 
/*     */     
/*     */     try {
/*  79 */       this._handle = registerEventSource(server, source);
/*  80 */     } catch (Exception e) {
/*  81 */       e.printStackTrace();
/*  82 */       this._handle = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*  93 */     if (this.source != null) {
/*     */       try {
/*  95 */         this._handle = registerEventSource(this.server, this.source);
/*  96 */       } catch (Exception e) {
/*  97 */         LogLog.error("Could not register event source.", e);
/*  98 */         this._handle = 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 106 */     StringBuffer sbuf = new StringBuffer();
/*     */     
/* 108 */     sbuf.append(this.layout.format(event));
/* 109 */     if (this.layout.ignoresThrowable()) {
/* 110 */       String[] s = event.getThrowableStrRep();
/* 111 */       if (s != null) {
/* 112 */         int len = s.length;
/* 113 */         for (int i = 0; i < len; i++) {
/* 114 */           sbuf.append(s[i]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     int nt_category = event.getLevel().toInt();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     reportEvent(this._handle, sbuf.toString(), nt_category);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finalize() {
/* 131 */     deregisterEventSource(this._handle);
/* 132 */     this._handle = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(String source) {
/* 141 */     this.source = source.trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSource() {
/* 146 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private native int registerEventSource(String paramString1, String paramString2);
/*     */ 
/*     */   
/*     */   static {
/*     */     String[] arrayOfString;
/*     */     try {
/* 164 */       arrayOfString = new String[] { System.getProperty("os.arch") };
/* 165 */     } catch (SecurityException e) {
/* 166 */       arrayOfString = new String[] { "amd64", "ia64", "x86" };
/*     */     } 
/* 168 */     boolean loaded = false;
/* 169 */     for (int i = 0; i < arrayOfString.length; i++) {
/*     */       try {
/* 171 */         System.loadLibrary("NTEventLogAppender." + arrayOfString[i]);
/* 172 */         loaded = true;
/*     */         break;
/* 174 */       } catch (UnsatisfiedLinkError e) {
/* 175 */         loaded = false;
/*     */       } 
/*     */     } 
/* 178 */     if (!loaded)
/* 179 */       System.loadLibrary("NTEventLogAppender"); 
/*     */   }
/*     */   
/*     */   private native void reportEvent(int paramInt1, String paramString, int paramInt2);
/*     */   
/*     */   private native void deregisterEventSource(int paramInt);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\nt\NTEventLogAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */