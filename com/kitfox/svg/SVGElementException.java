/*    */ package com.kitfox.svg;
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
/*    */ public class SVGElementException
/*    */   extends SVGException
/*    */ {
/*    */   public static final long serialVersionUID = 0L;
/*    */   private final SVGElement element;
/*    */   
/*    */   public SVGElementException(SVGElement element) {
/* 55 */     this(element, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SVGElementException(SVGElement element, String msg) {
/* 66 */     this(element, msg, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SVGElementException(SVGElement element, String msg, Throwable cause) {
/* 71 */     super(msg, cause);
/* 72 */     this.element = element;
/*    */   }
/*    */ 
/*    */   
/*    */   public SVGElementException(SVGElement element, Throwable cause) {
/* 77 */     this(element, null, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SVGElement getElement() {
/* 82 */     return this.element;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGElementException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */