/*     */ package org.fife.rsta.ac.js.ecma.api.ecma3;
/*     */ 
/*     */ import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSDateFunctions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JSDate
/*     */   implements JSDateFunctions
/*     */ {
/*     */   public JSDate prototype;
/*     */   protected JSFunction constructor;
/*     */   
/*     */   public JSDate() {}
/*     */   
/*     */   public JSDate(JSNumber milliseconds) {}
/*     */   
/*     */   public JSDate(JSString datestring) {}
/*     */   
/*     */   public JSDate(JSNumber year, JSNumber month, JSNumber day, JSNumber hours, JSNumber minutes, JSNumber seconds, JSNumber ms) {}
/*     */   
/*     */   public static JSNumber UTC(JSNumber year, JSNumber month, JSNumber day, JSNumber hour, JSNumber min, JSNumber sec, JSNumber ms) {
/* 114 */     return null;
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
/*     */   public static JSNumber parse(JSString string) {
/* 128 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\JSDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */