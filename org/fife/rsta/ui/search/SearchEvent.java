/*    */ package org.fife.rsta.ui.search;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import org.fife.ui.rtextarea.SearchContext;
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
/*    */ public class SearchEvent
/*    */   extends EventObject
/*    */ {
/*    */   private SearchContext context;
/*    */   private Type type;
/*    */   
/*    */   public SearchEvent(Object source, Type type, SearchContext context) {
/* 29 */     super(source);
/* 30 */     this.type = type;
/* 31 */     this.context = context;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type getType() {
/* 36 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public SearchContext getSearchContext() {
/* 41 */     return this.context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public enum Type
/*    */   {
/* 53 */     MARK_ALL,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     FIND,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     REPLACE,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     REPLACE_ALL;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\SearchEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */