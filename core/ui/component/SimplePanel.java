/*    */ package core.ui.component;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.GridBagLayout;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimplePanel
/*    */   extends JPanel
/*    */ {
/* 14 */   private int currentComponentIndex = 0;
/* 15 */   private int setup = 80;
/*    */   
/*    */   public SimplePanel() {
/* 18 */     super(new GridBagLayout());
/*    */   }
/*    */ 
/*    */   
/*    */   public SimplePanel(LayoutManager paramLayoutManager) {
/* 23 */     super(paramLayoutManager);
/*    */   }
/*    */   
/*    */   public synchronized void addComponent(int setup, Component... component) {
/* 27 */     for (int i = 0; i < component.length; i++) {
/* 28 */       GBC gbc = (new GBC(1, this.currentComponentIndex)).setInsets(5, i * -setup, 0, 0);
/* 29 */       add(component[i], gbc);
/*    */     } 
/* 31 */     this.currentComponentIndex++;
/*    */   }
/*    */   
/*    */   public synchronized void addLComponent(int setup, Component... component) {
/* 35 */     for (int i = 0; i < component.length; i++) {
/* 36 */       GBC gbc = (new GBC(0, this.currentComponentIndex)).setInsets(0, i * setup, 0, 320);
/* 37 */       add(component[i], gbc);
/*    */     } 
/* 39 */     this.currentComponentIndex++;
/*    */   }
/*    */   
/*    */   public synchronized void addLRComponent(Component left, Component right) {
/* 43 */     GBC gbcLeft = (new GBC(0, this.currentComponentIndex)).setInsets(5, -40, 0, 0);
/* 44 */     GBC gbcRight = (new GBC(1, this.currentComponentIndex, 3, 1)).setInsets(5, 20, 0, 0);
/* 45 */     add(left, gbcLeft);
/* 46 */     add(right, gbcRight);
/*    */   }
/*    */   
/*    */   public void addX(Component... component) {
/* 50 */     addComponent(this.setup, component);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSetup() {
/* 56 */     return this.setup;
/*    */   }
/*    */   
/*    */   public void setSetup(int setup) {
/* 60 */     this.setup = setup;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\SimplePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */