/*     */ package com.formdev.flatlaf;
/*     */ 
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.KeyEventPostProcessor;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.lang.ref.WeakReference;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
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
/*     */ class MnemonicHandler
/*     */   implements KeyEventPostProcessor, ChangeListener
/*     */ {
/*     */   private static boolean showMnemonics;
/*     */   private static WeakReference<Window> lastShowMnemonicWindow;
/*     */   private static WindowListener windowListener;
/*     */   private static int altPressedEventCount;
/*     */   private static boolean selectMenuOnAltReleased;
/*     */   
/*     */   static boolean isShowMnemonics() {
/*  58 */     return (showMnemonics || !UIManager.getBoolean("Component.hideMnemonics"));
/*     */   }
/*     */   
/*     */   void install() {
/*  62 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
/*  63 */     MenuSelectionManager.defaultManager().addChangeListener(this);
/*     */   }
/*     */   
/*     */   void uninstall() {
/*  67 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(this);
/*  68 */     MenuSelectionManager.defaultManager().removeChangeListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcessKeyEvent(KeyEvent e) {
/*  73 */     int keyCode = e.getKeyCode();
/*  74 */     if (SystemInfo.isMacOS) {
/*     */       
/*  76 */       if (keyCode == 17 || keyCode == 18) {
/*  77 */         showMnemonics((shouldShowMnemonics(e) && e.isControlDown() && e.isAltDown()), e.getComponent());
/*     */       }
/*     */     } else {
/*  80 */       if (SystemInfo.isWindows) {
/*  81 */         return processKeyEventOnWindows(e);
/*     */       }
/*  83 */       if (keyCode == 18) {
/*  84 */         showMnemonics(shouldShowMnemonics(e), e.getComponent());
/*     */       }
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   private boolean shouldShowMnemonics(KeyEvent e) {
/*  91 */     return (e.getID() == 401 || (
/*  92 */       MenuSelectionManager.defaultManager().getSelectedPath()).length > 0);
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
/*     */   private boolean processKeyEventOnWindows(KeyEvent e) {
/* 106 */     if (e.getKeyCode() != 18) {
/* 107 */       selectMenuOnAltReleased = false;
/* 108 */       return false;
/*     */     } 
/*     */     
/* 111 */     if (e.getID() == 401) {
/* 112 */       altPressedEventCount++;
/*     */       
/* 114 */       if (altPressedEventCount == 1 && !e.isConsumed()) {
/* 115 */         MenuSelectionManager menuSelectionManager = MenuSelectionManager.defaultManager();
/* 116 */         selectMenuOnAltReleased = ((menuSelectionManager.getSelectedPath()).length == 0);
/*     */ 
/*     */         
/* 119 */         if (!selectMenuOnAltReleased) {
/* 120 */           menuSelectionManager.clearSelectedPath();
/*     */         }
/*     */       } 
/*     */       
/* 124 */       showMnemonics(shouldShowMnemonics(e), e.getComponent());
/*     */ 
/*     */       
/* 127 */       e.consume();
/* 128 */       return true;
/*     */     } 
/* 130 */     if (e.getID() == 402) {
/* 131 */       altPressedEventCount = 0;
/*     */       
/* 133 */       boolean mnemonicsShown = false;
/* 134 */       if (selectMenuOnAltReleased && !e.isConsumed()) {
/* 135 */         MenuSelectionManager menuSelectionManager = MenuSelectionManager.defaultManager();
/* 136 */         if ((menuSelectionManager.getSelectedPath()).length == 0) {
/*     */           
/* 138 */           Component c = e.getComponent();
/* 139 */           JRootPane rootPane = SwingUtilities.getRootPane(c);
/* 140 */           Window window = (rootPane != null) ? SwingUtilities.getWindowAncestor(rootPane) : null;
/* 141 */           JMenuBar menuBar = (rootPane != null) ? rootPane.getJMenuBar() : null;
/* 142 */           if (menuBar == null && window instanceof JFrame)
/* 143 */             menuBar = ((JFrame)window).getJMenuBar(); 
/* 144 */           JMenu firstMenu = (menuBar != null) ? menuBar.getMenu(0) : null;
/*     */ 
/*     */           
/* 147 */           if (firstMenu != null) {
/* 148 */             menuSelectionManager.setSelectedPath(new MenuElement[] { menuBar, firstMenu });
/* 149 */             showMnemonics(true, c);
/* 150 */             mnemonicsShown = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 154 */       selectMenuOnAltReleased = false;
/*     */ 
/*     */       
/* 157 */       if (!mnemonicsShown)
/* 158 */         showMnemonics(shouldShowMnemonics(e), e.getComponent()); 
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stateChanged(ChangeEvent e) {
/* 165 */     MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
/* 166 */     if (selectedPath.length == 0 && altPressedEventCount == 0)
/*     */     {
/* 168 */       showMnemonics(false, null);
/*     */     }
/*     */   }
/*     */   
/*     */   static void showMnemonics(boolean show, Component c) {
/* 173 */     if (show == showMnemonics) {
/*     */       return;
/*     */     }
/* 176 */     showMnemonics = show;
/*     */ 
/*     */     
/* 179 */     if (!UIManager.getBoolean("Component.hideMnemonics")) {
/*     */       return;
/*     */     }
/* 182 */     if (show) {
/*     */       
/* 184 */       JRootPane rootPane = SwingUtilities.getRootPane(c);
/* 185 */       if (rootPane == null) {
/*     */         return;
/*     */       }
/*     */       
/* 189 */       Window window = SwingUtilities.getWindowAncestor(rootPane);
/* 190 */       if (window == null) {
/*     */         return;
/*     */       }
/*     */       
/* 194 */       repaintMnemonics(window);
/*     */ 
/*     */       
/* 197 */       windowListener = new WindowAdapter()
/*     */         {
/*     */           public void windowDeactivated(WindowEvent e) {
/* 200 */             MnemonicHandler.altPressedEventCount = 0;
/* 201 */             MnemonicHandler.selectMenuOnAltReleased = false;
/*     */ 
/*     */ 
/*     */             
/* 205 */             EventQueue.invokeLater(() -> MnemonicHandler.showMnemonics(false, null));
/*     */           }
/*     */         };
/*     */ 
/*     */       
/* 210 */       window.addWindowListener(windowListener);
/*     */       
/* 212 */       lastShowMnemonicWindow = new WeakReference<>(window);
/* 213 */     } else if (lastShowMnemonicWindow != null) {
/* 214 */       Window window = lastShowMnemonicWindow.get();
/* 215 */       if (window != null) {
/* 216 */         repaintMnemonics(window);
/*     */         
/* 218 */         if (windowListener != null) {
/* 219 */           window.removeWindowListener(windowListener);
/* 220 */           windowListener = null;
/*     */         } 
/*     */       } 
/*     */       
/* 224 */       lastShowMnemonicWindow = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void repaintMnemonics(Container container) {
/* 229 */     for (Component c : container.getComponents()) {
/* 230 */       if (c.isVisible()) {
/*     */ 
/*     */         
/* 233 */         if (hasMnemonic(c)) {
/* 234 */           c.repaint();
/*     */         }
/* 236 */         if (c instanceof Container)
/* 237 */           repaintMnemonics((Container)c); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static boolean hasMnemonic(Component c) {
/* 242 */     if (c instanceof JLabel && ((JLabel)c).getDisplayedMnemonicIndex() >= 0) {
/* 243 */       return true;
/*     */     }
/* 245 */     if (c instanceof AbstractButton && ((AbstractButton)c).getDisplayedMnemonicIndex() >= 0) {
/* 246 */       return true;
/*     */     }
/* 248 */     if (c instanceof JTabbedPane) {
/* 249 */       JTabbedPane tabPane = (JTabbedPane)c;
/* 250 */       int tabCount = tabPane.getTabCount();
/* 251 */       for (int i = 0; i < tabCount; i++) {
/* 252 */         if (tabPane.getDisplayedMnemonicIndexAt(i) >= 0) {
/* 253 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 257 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\MnemonicHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */