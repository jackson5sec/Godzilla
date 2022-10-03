/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.MDCKeySetExtractor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertiesPatternConverter
/*     */   extends LoggingEventPatternConverter
/*     */ {
/*     */   private final String option;
/*     */   
/*     */   private PropertiesPatternConverter(String[] options) {
/*  50 */     super((options != null && options.length > 0) ? ("Property{" + options[0] + "}") : "Properties", "property");
/*     */ 
/*     */ 
/*     */     
/*  54 */     if (options != null && options.length > 0) {
/*  55 */       this.option = options[0];
/*     */     } else {
/*  57 */       this.option = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertiesPatternConverter newInstance(String[] options) {
/*  68 */     return new PropertiesPatternConverter(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/*  77 */     if (this.option == null) {
/*  78 */       toAppendTo.append("{");
/*     */       
/*     */       try {
/*  81 */         Set keySet = MDCKeySetExtractor.INSTANCE.getPropertyKeySet(event);
/*  82 */         if (keySet != null) {
/*  83 */           for (Iterator i = keySet.iterator(); i.hasNext(); ) {
/*  84 */             Object item = i.next();
/*  85 */             Object val = event.getMDC(item.toString());
/*  86 */             toAppendTo.append("{").append(item).append(",").append(val).append("}");
/*     */           }
/*     */         
/*     */         }
/*  90 */       } catch (Exception ex) {
/*  91 */         LogLog.error("Unexpected exception while extracting MDC keys", ex);
/*     */       } 
/*     */       
/*  94 */       toAppendTo.append("}");
/*     */     } else {
/*     */       
/*  97 */       Object val = event.getMDC(this.option);
/*     */       
/*  99 */       if (val != null)
/* 100 */         toAppendTo.append(val); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\PropertiesPatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */