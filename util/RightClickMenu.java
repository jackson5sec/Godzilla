/*    */ package util;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JPopupMenu;
/*    */ 
/*    */ public class RightClickMenu extends MouseAdapter {
/*    */   private final JPopupMenu popupMenu;
/*    */   
/*    */   public RightClickMenu(JPopupMenu popupMenu) {
/* 12 */     this.popupMenu = popupMenu;
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(MouseEvent mouseEvent) {
/* 17 */     super.mouseClicked(mouseEvent);
/* 18 */     if (mouseEvent.getButton() == 3) {
/* 19 */       Component c = mouseEvent.getComponent();
/* 20 */       if (this.popupMenu != null && c != null)
/* 21 */         this.popupMenu.show(c, mouseEvent.getX(), mouseEvent.getY()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\RightClickMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */