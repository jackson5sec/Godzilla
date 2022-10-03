/*    */ package com.jediterm.terminal.model.hyperlinks;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinkResult
/*    */ {
/*    */   private final LinkResultItem myItem;
/*    */   private List<LinkResultItem> myItemList;
/*    */   
/*    */   public LinkResult(@NotNull LinkResultItem item) {
/* 16 */     this.myItem = item;
/* 17 */     this.myItemList = null;
/*    */   }
/*    */   
/*    */   public LinkResult(@NotNull List<LinkResultItem> itemList) {
/* 21 */     this.myItemList = itemList;
/* 22 */     this.myItem = null;
/*    */   }
/*    */   
/*    */   public List<LinkResultItem> getItems() {
/* 26 */     if (this.myItemList == null) {
/* 27 */       this.myItemList = Lists.newArrayList((Object[])new LinkResultItem[] { this.myItem });
/*    */     }
/* 29 */     return this.myItemList;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\hyperlinks\LinkResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */