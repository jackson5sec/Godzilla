/*    */ package com.jediterm.terminal.model.hyperlinks;
/*    */ 
/*    */ import com.jediterm.terminal.ui.TerminalAction;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinkInfo
/*    */ {
/*    */   private final Runnable myNavigateCallback;
/*    */   private final PopupMenuGroupProvider myPopupMenuGroupProvider;
/*    */   private final HoverConsumer myHoverConsumer;
/*    */   
/*    */   public LinkInfo(@NotNull Runnable navigateCallback) {
/* 21 */     this(navigateCallback, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private LinkInfo(@NotNull Runnable navigateCallback, @Nullable PopupMenuGroupProvider popupMenuGroupProvider, @Nullable HoverConsumer hoverConsumer) {
/* 27 */     this.myNavigateCallback = navigateCallback;
/* 28 */     this.myPopupMenuGroupProvider = popupMenuGroupProvider;
/* 29 */     this.myHoverConsumer = hoverConsumer;
/*    */   }
/*    */   
/*    */   public void navigate() {
/* 33 */     this.myNavigateCallback.run();
/*    */   }
/*    */   @Nullable
/*    */   public PopupMenuGroupProvider getPopupMenuGroupProvider() {
/* 37 */     return this.myPopupMenuGroupProvider;
/*    */   }
/*    */   @Nullable
/*    */   public HoverConsumer getHoverConsumer() {
/* 41 */     return this.myHoverConsumer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Builder
/*    */   {
/*    */     private Runnable myNavigateCallback;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private LinkInfo.PopupMenuGroupProvider myPopupMenuGroupProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private LinkInfo.HoverConsumer myHoverConsumer;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     @NotNull
/*    */     public Builder setNavigateCallback(@NotNull Runnable navigateCallback) {
/* 67 */       if (navigateCallback == null) $$$reportNull$$$0(0);  this.myNavigateCallback = navigateCallback;
/* 68 */       if (this == null) $$$reportNull$$$0(1);  return this;
/*    */     }
/*    */     @NotNull
/*    */     public Builder setPopupMenuGroupProvider(@Nullable LinkInfo.PopupMenuGroupProvider popupMenuGroupProvider) {
/* 72 */       this.myPopupMenuGroupProvider = popupMenuGroupProvider;
/* 73 */       if (this == null) $$$reportNull$$$0(2);  return this;
/*    */     }
/*    */     @NotNull
/*    */     public Builder setHoverConsumer(@Nullable LinkInfo.HoverConsumer hoverConsumer) {
/* 77 */       this.myHoverConsumer = hoverConsumer;
/* 78 */       if (this == null) $$$reportNull$$$0(3);  return this;
/*    */     }
/*    */     @NotNull
/*    */     public LinkInfo build() {
/* 82 */       return new LinkInfo(this.myNavigateCallback, this.myPopupMenuGroupProvider, this.myHoverConsumer);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface HoverConsumer {
/*    */     void onMouseEntered(@NotNull JComponent param1JComponent, @NotNull Rectangle param1Rectangle);
/*    */     
/*    */     void onMouseExited();
/*    */   }
/*    */   
/*    */   public static interface PopupMenuGroupProvider {
/*    */     @NotNull
/*    */     List<TerminalAction> getPopupMenuGroup(@NotNull MouseEvent param1MouseEvent);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\hyperlinks\LinkInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */