/*     */ package core.ui.component;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.ui.component.listener.RTabbedPaneRemoveListener;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTabbedPane;
/*     */ import util.RightClickMenu;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RTabbedPane
/*     */   extends JTabbedPane
/*     */ {
/*  24 */   private JPopupMenu rightClickMenu = new JPopupMenu();
/*  25 */   private ArrayList<Component> components = new ArrayList<>();
/*     */   
/*     */   private RTabbedPaneRemoveListener removeListener;
/*     */ 
/*     */   
/*     */   public RTabbedPane() {
/*  31 */     init();
/*     */   }
/*     */   
/*     */   public RTabbedPane(int tabPlacement, int tabLayoutPolicy) {
/*  35 */     super(tabPlacement, tabLayoutPolicy);
/*  36 */     init();
/*     */   }
/*     */   
/*     */   public RTabbedPane(int tabPlacement) {
/*  40 */     super(tabPlacement);
/*  41 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/*  45 */     JMenuItem close = new JMenuItem("关闭当前");
/*  46 */     close.setActionCommand("closeCurrent");
/*  47 */     JMenuItem closeOther = new JMenuItem("关闭其它");
/*  48 */     closeOther.setActionCommand("closeOther");
/*  49 */     JMenuItem closeLeft = new JMenuItem("关闭左边所有");
/*  50 */     closeLeft.setActionCommand("closeLeft");
/*  51 */     JMenuItem closeRight = new JMenuItem("关闭右边所有");
/*  52 */     closeRight.setActionCommand("closeRight");
/*  53 */     JMenuItem copyNewWindow = new JMenuItem("复制到新窗口");
/*  54 */     copyNewWindow.setActionCommand("copyNewWindow");
/*     */     
/*  56 */     this.rightClickMenu.add(close);
/*  57 */     this.rightClickMenu.add(closeOther);
/*  58 */     this.rightClickMenu.add(closeLeft);
/*  59 */     this.rightClickMenu.add(closeRight);
/*  60 */     this.rightClickMenu.add(copyNewWindow);
/*     */     
/*  62 */     automaticBindClick.bindMenuItemClick(this.rightClickMenu, null, this);
/*     */     
/*  64 */     addMouseListener((MouseListener)new RightClickMenu(this.rightClickMenu));
/*  65 */     EasyI18N.installObject(this);
/*     */   }
/*     */   private void closeCurrentMenuItemClick(ActionEvent e) {
/*  68 */     int selected = getSelectedIndex();
/*  69 */     if (selected != -1)
/*  70 */       remove(selected); 
/*     */   }
/*     */   
/*     */   private void closeOtherMenuItemClick(ActionEvent e) {
/*  74 */     int selected = getSelectedIndex();
/*  75 */     if (selected != -1) {
/*  76 */       int max = getTabCount();
/*  77 */       for (int i = max - 1; i >= 0; i--) {
/*  78 */         if (i != selected)
/*  79 */           remove(i); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeLeftMenuItemClick(ActionEvent e) {
/*  85 */     int selected = getSelectedIndex();
/*  86 */     if (selected != -1) {
/*  87 */       int max = getTabCount();
/*  88 */       for (int i = 0; i < selected; i++)
/*  89 */         remove(0); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeRightMenuItemClick(ActionEvent e) {
/*  94 */     int selected = getSelectedIndex();
/*  95 */     if (selected != -1) {
/*  96 */       int max = getTabCount();
/*  97 */       for (int i = max - 1; i > selected; i--)
/*  98 */         remove(i); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copyNewWindowMenuItemClick(ActionEvent e) {
/* 103 */     int selected = getSelectedIndex();
/* 104 */     if (selected != -1) {
/* 105 */       JFrame frame = new JFrame();
/* 106 */       frame.setTitle(getTitleAt(selected));
/* 107 */       frame.add(getComponent(selected));
/* 108 */       frame.setLocationRelativeTo(UiFunction.getParentFrame(this));
/* 109 */       functions.setWindowSize(frame, 1300, 600);
/* 110 */       frame.setVisible(true);
/* 111 */       frame.setDefaultCloseOperation(2);
/* 112 */       this.components.add(frame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void remove(int index) {
/* 118 */     int before = getTabCount();
/* 119 */     super.remove(index);
/* 120 */     int current = getTabCount();
/* 121 */     notifyRemoveListener(current, before - current);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeAll() {
/* 126 */     int before = getTabCount();
/* 127 */     super.removeAll();
/* 128 */     int current = getTabCount();
/* 129 */     notifyRemoveListener(current, before - current);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeTabAt(int index) {
/* 134 */     int before = getTabCount();
/* 135 */     super.removeTabAt(index);
/* 136 */     int current = getTabCount();
/* 137 */     notifyRemoveListener(current, before - current);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void remove(Component component) {
/* 142 */     int before = getTabCount();
/* 143 */     super.remove(component);
/* 144 */     int current = getTabCount();
/* 145 */     notifyRemoveListener(current, before - current);
/*     */   }
/*     */   
/*     */   protected void notifyRemoveListener(int currentSize, int removeSize) {
/* 149 */     if (this.removeListener != null) {
/* 150 */       this.removeListener.actionPerformed(currentSize, removeSize);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean removeStoreComponent(Component component) {
/* 155 */     return this.components.remove(component);
/*     */   }
/*     */   
/*     */   public void setRemoveListener(RTabbedPaneRemoveListener removeListener) {
/* 159 */     this.removeListener = removeListener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disable() {
/* 165 */     super.disable();
/*     */     
/* 167 */     Iterator<Component> iterator = this.components.iterator();
/*     */     
/* 169 */     while (iterator.hasNext()) {
/* 170 */       Component component = iterator.next();
/* 171 */       if (component != null) {
/* 172 */         component.setVisible(false);
/* 173 */         component.setEnabled(false);
/* 174 */         component.disable();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\RTabbedPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */