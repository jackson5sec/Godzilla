/*     */ package com.jediterm.terminal.ui;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.KeyStroke;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ public class TerminalAction
/*     */ {
/*     */   private final String myName;
/*     */   private final KeyStroke[] myKeyStrokes;
/*     */   private final Predicate<KeyEvent> myRunnable;
/*  19 */   private Character myMnemonic = null;
/*     */   private Supplier<Boolean> myEnabledSupplier = () -> Boolean.valueOf(true);
/*  21 */   private Integer myMnemonicKey = null;
/*     */   private boolean mySeparatorBefore = false;
/*     */   private boolean myHidden = false;
/*     */   
/*     */   public TerminalAction(@NotNull TerminalActionPresentation presentation, @NotNull Predicate<KeyEvent> runnable) {
/*  26 */     this(presentation.getName(), presentation.getKeyStrokes().<KeyStroke>toArray(new KeyStroke[0]), runnable);
/*     */   }
/*     */   
/*     */   public TerminalAction(@NotNull TerminalActionPresentation presentation) {
/*  30 */     this(presentation, keyEvent -> true);
/*     */   }
/*     */   
/*     */   public TerminalAction(@NotNull String name, @NotNull KeyStroke[] keyStrokes, @NotNull Predicate<KeyEvent> runnable) {
/*  34 */     this.myName = name;
/*  35 */     this.myKeyStrokes = keyStrokes;
/*  36 */     this.myRunnable = runnable;
/*     */   }
/*     */   
/*     */   public boolean matches(KeyEvent e) {
/*  40 */     for (KeyStroke ks : this.myKeyStrokes) {
/*  41 */       if (ks.equals(KeyStroke.getKeyStrokeForEvent(e))) {
/*  42 */         return true;
/*     */       }
/*     */     } 
/*  45 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEnabled(@Nullable KeyEvent e) {
/*  49 */     return ((Boolean)this.myEnabledSupplier.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean actionPerformed(@Nullable KeyEvent e) {
/*  53 */     return this.myRunnable.test(e);
/*     */   }
/*     */   
/*     */   public static boolean processEvent(@NotNull TerminalActionProvider actionProvider, @NotNull KeyEvent e) {
/*  57 */     if (actionProvider == null) $$$reportNull$$$0(6);  if (e == null) $$$reportNull$$$0(7);  for (TerminalAction a : actionProvider.getActions()) {
/*  58 */       if (a.matches(e)) {
/*  59 */         return (a.isEnabled(e) && a.actionPerformed(e));
/*     */       }
/*     */     } 
/*     */     
/*  63 */     if (actionProvider.getNextProvider() != null) {
/*  64 */       return processEvent(actionProvider.getNextProvider(), e);
/*     */     }
/*     */     
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean addToMenu(JPopupMenu menu, TerminalActionProvider actionProvider) {
/*  71 */     boolean added = false;
/*  72 */     if (actionProvider.getNextProvider() != null) {
/*  73 */       added = addToMenu(menu, actionProvider.getNextProvider());
/*     */     }
/*  75 */     boolean addSeparator = added;
/*  76 */     for (TerminalAction a : actionProvider.getActions()) {
/*  77 */       if (a.isHidden()) {
/*     */         continue;
/*     */       }
/*  80 */       if (!addSeparator) {
/*  81 */         addSeparator = a.isSeparated();
/*     */       }
/*  83 */       if (addSeparator) {
/*  84 */         menu.addSeparator();
/*  85 */         addSeparator = false;
/*     */       } 
/*     */       
/*  88 */       menu.add(a.toMenuItem());
/*     */       
/*  90 */       added = true;
/*     */     } 
/*     */     
/*  93 */     return added;
/*     */   }
/*     */   
/*     */   public int getKeyCode() {
/*  97 */     KeyStroke[] arrayOfKeyStroke = this.myKeyStrokes; int i = arrayOfKeyStroke.length; byte b = 0; if (b < i) { KeyStroke ks = arrayOfKeyStroke[b];
/*  98 */       return ks.getKeyCode(); }
/*     */     
/* 100 */     return 0;
/*     */   }
/*     */   
/*     */   public int getModifiers() {
/* 104 */     KeyStroke[] arrayOfKeyStroke = this.myKeyStrokes; int i = arrayOfKeyStroke.length; byte b = 0; if (b < i) { KeyStroke ks = arrayOfKeyStroke[b];
/* 105 */       return ks.getModifiers(); }
/*     */     
/* 107 */     return 0;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 111 */     return this.myName;
/*     */   }
/*     */   
/*     */   public TerminalAction withMnemonic(Character ch) {
/* 115 */     this.myMnemonic = ch;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public TerminalAction withMnemonicKey(Integer key) {
/* 120 */     this.myMnemonicKey = key;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public TerminalAction withEnabledSupplier(@NotNull Supplier<Boolean> enabledSupplier) {
/* 125 */     if (enabledSupplier == null) $$$reportNull$$$0(8);  this.myEnabledSupplier = enabledSupplier;
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public TerminalAction separatorBefore(boolean enabled) {
/* 130 */     this.mySeparatorBefore = enabled;
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public JMenuItem toMenuItem() {
/* 135 */     JMenuItem menuItem = new JMenuItem(this.myName);
/*     */     
/* 137 */     if (this.myMnemonic != null) {
/* 138 */       menuItem.setMnemonic(this.myMnemonic.charValue());
/*     */     }
/* 140 */     if (this.myMnemonicKey != null) {
/* 141 */       menuItem.setMnemonic(this.myMnemonicKey.intValue());
/*     */     }
/*     */     
/* 144 */     if (this.myKeyStrokes.length > 0) {
/* 145 */       menuItem.setAccelerator(this.myKeyStrokes[0]);
/*     */     }
/*     */     
/* 148 */     menuItem.addActionListener(actionEvent -> actionPerformed(null));
/* 149 */     menuItem.setEnabled(isEnabled(null));
/*     */     
/* 151 */     return menuItem;
/*     */   }
/*     */   
/*     */   public boolean isSeparated() {
/* 155 */     return this.mySeparatorBefore;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 159 */     return this.myHidden;
/*     */   }
/*     */   
/*     */   public TerminalAction withHidden(boolean hidden) {
/* 163 */     this.myHidden = hidden;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     return "'" + this.myName + "'";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */