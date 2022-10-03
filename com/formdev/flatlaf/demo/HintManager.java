/*     */ package com.formdev.flatlaf.demo;
/*     */ 
/*     */ import com.formdev.flatlaf.ui.FlatDropShadowBorder;
/*     */ import com.formdev.flatlaf.ui.FlatPopupMenuBorder;
/*     */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HintManager
/*     */ {
/*  35 */   private static final List<HintPanel> hintPanels = new ArrayList<>();
/*     */ 
/*     */   
/*     */   static void showHint(Hint hint) {
/*  39 */     if (DemoPrefs.getState().getBoolean(hint.prefsKey, false)) {
/*  40 */       if (hint.nextHint != null) {
/*  41 */         showHint(hint.nextHint);
/*     */       }
/*     */       return;
/*     */     } 
/*  45 */     HintPanel hintPanel = new HintPanel(hint);
/*  46 */     hintPanel.showHint();
/*     */     
/*  48 */     hintPanels.add(hintPanel);
/*     */   }
/*     */   
/*     */   static void hideAllHints() {
/*  52 */     HintPanel[] hintPanels2 = hintPanels.<HintPanel>toArray(new HintPanel[hintPanels.size()]);
/*  53 */     for (HintPanel hintPanel : hintPanels2) {
/*  54 */       hintPanel.hideHint();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Hint
/*     */   {
/*     */     private final String message;
/*     */     private final Component owner;
/*     */     private final int position;
/*     */     private final String prefsKey;
/*     */     private final Hint nextHint;
/*     */     
/*     */     Hint(String message, Component owner, int position, String prefsKey, Hint nextHint) {
/*  68 */       this.message = message;
/*  69 */       this.owner = owner;
/*  70 */       this.position = position;
/*  71 */       this.prefsKey = prefsKey;
/*  72 */       this.nextHint = nextHint;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HintPanel
/*     */     extends JPanel
/*     */   {
/*     */     private final HintManager.Hint hint;
/*     */     private JPanel popup;
/*     */     private JLabel hintLabel;
/*     */     private JButton gotItButton;
/*     */     
/*     */     private HintPanel(HintManager.Hint hint) {
/*  86 */       this.hint = hint;
/*     */       
/*  88 */       initComponents();
/*     */       
/*  90 */       this.hintLabel.setText("<html>" + hint.message + "</html>");
/*     */ 
/*     */ 
/*     */       
/*  94 */       addMouseListener(new MouseAdapter() {  }
/*     */         );
/*     */     }
/*     */     
/*     */     public void updateUI() {
/*  99 */       super.updateUI();
/*     */       
/* 101 */       setBackground(UIManager.getColor("HintPanel.backgroundColor"));
/* 102 */       setBorder((Border)new FlatPopupMenuBorder());
/*     */     }
/*     */     
/*     */     void showHint() {
/* 106 */       JRootPane rootPane = SwingUtilities.getRootPane(this.hint.owner);
/* 107 */       if (rootPane == null) {
/*     */         return;
/*     */       }
/* 110 */       JLayeredPane layeredPane = rootPane.getLayeredPane();
/*     */ 
/*     */       
/* 113 */       this.popup = new JPanel(new BorderLayout())
/*     */         {
/*     */           public void updateUI() {
/* 116 */             super.updateUI();
/*     */             
/* 118 */             setBorder((Border)new FlatDropShadowBorder(
/* 119 */                   UIManager.getColor("Popup.dropShadowColor"), 
/* 120 */                   UIManager.getInsets("Popup.dropShadowInsets"), 
/* 121 */                   FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5F)));
/*     */ 
/*     */ 
/*     */             
/* 125 */             EventQueue.invokeLater(() -> {
/*     */                   validate();
/*     */                   setSize(getPreferredSize());
/*     */                 });
/*     */           }
/*     */         };
/* 131 */       this.popup.setOpaque(false);
/* 132 */       this.popup.add(this);
/*     */ 
/*     */       
/* 135 */       Point pt = SwingUtilities.convertPoint(this.hint.owner, 0, 0, layeredPane);
/* 136 */       int x = pt.x;
/* 137 */       int y = pt.y;
/* 138 */       Dimension size = this.popup.getPreferredSize();
/* 139 */       int gap = UIScale.scale(6);
/*     */       
/* 141 */       switch (this.hint.position) {
/*     */         case 2:
/* 143 */           x -= size.width + gap;
/*     */           break;
/*     */         
/*     */         case 1:
/* 147 */           y -= size.height + gap;
/*     */           break;
/*     */         
/*     */         case 4:
/* 151 */           x += this.hint.owner.getWidth() + gap;
/*     */           break;
/*     */         
/*     */         case 3:
/* 155 */           y += this.hint.owner.getHeight() + gap;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 160 */       this.popup.setBounds(x, y, size.width, size.height);
/* 161 */       layeredPane.add(this.popup, JLayeredPane.POPUP_LAYER);
/*     */     }
/*     */     
/*     */     void hideHint() {
/* 165 */       if (this.popup != null) {
/* 166 */         Container parent = this.popup.getParent();
/* 167 */         if (parent != null) {
/* 168 */           parent.remove(this.popup);
/* 169 */           parent.repaint(this.popup.getX(), this.popup.getY(), this.popup.getWidth(), this.popup.getHeight());
/*     */         } 
/*     */       } 
/*     */       
/* 173 */       HintManager.hintPanels.remove(this);
/*     */     }
/*     */ 
/*     */     
/*     */     private void gotIt() {
/* 178 */       hideHint();
/*     */ 
/*     */       
/* 181 */       DemoPrefs.getState().putBoolean(this.hint.prefsKey, true);
/*     */ 
/*     */       
/* 184 */       if (this.hint.nextHint != null) {
/* 185 */         HintManager.showHint(this.hint.nextHint);
/*     */       }
/*     */     }
/*     */     
/*     */     private void initComponents() {
/* 190 */       this.hintLabel = new JLabel();
/* 191 */       this.gotItButton = new JButton();
/*     */ 
/*     */       
/* 194 */       setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[::200,fill]", "[]para[]"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       this.hintLabel.setText("hint");
/* 204 */       add(this.hintLabel, "cell 0 0");
/*     */ 
/*     */       
/* 207 */       this.gotItButton.setText("Got it!");
/* 208 */       this.gotItButton.setFocusable(false);
/* 209 */       this.gotItButton.addActionListener(e -> gotIt());
/* 210 */       add(this.gotItButton, "cell 0 1,alignx right,growx 0");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\HintManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */