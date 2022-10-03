/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.TextAction;
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
/*     */ public abstract class RecordableTextAction
/*     */   extends TextAction
/*     */ {
/*     */   private boolean isRecordable;
/*     */   
/*     */   public RecordableTextAction(String text) {
/*  46 */     this(text, (Icon)null, (String)null, (Integer)null, (KeyStroke)null);
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
/*     */   
/*     */   public RecordableTextAction(String text, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  61 */     super(text);
/*  62 */     putValue("SmallIcon", icon);
/*  63 */     putValue("ShortDescription", desc);
/*  64 */     putValue("AcceleratorKey", accelerator);
/*  65 */     putValue("MnemonicKey", mnemonic);
/*  66 */     setRecordable(true);
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
/*     */   
/*     */   public final void actionPerformed(ActionEvent e) {
/*  81 */     JTextComponent textComponent = getTextComponent(e);
/*  82 */     if (textComponent instanceof RTextArea) {
/*  83 */       RTextArea textArea = (RTextArea)textComponent;
/*     */       
/*  85 */       if (RTextArea.isRecordingMacro() && isRecordable()) {
/*  86 */         int mod = e.getModifiers();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  91 */         String macroID = getMacroID();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  96 */         if (!"default-typed".equals(macroID) || ((mod & 0x8) == 0 && (mod & 0x2) == 0 && (mod & 0x4) == 0)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 101 */           String command = e.getActionCommand();
/* 102 */           RTextArea.addToCurrentMacro(macroID, command);
/*     */         } 
/*     */       } 
/*     */       
/* 106 */       actionPerformedImpl(e, textArea);
/*     */     } 
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
/*     */   public abstract void actionPerformedImpl(ActionEvent paramActionEvent, RTextArea paramRTextArea);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyStroke getAccelerator() {
/* 130 */     return (KeyStroke)getValue("AcceleratorKey");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 140 */     return (String)getValue("ShortDescription");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/* 150 */     return (Icon)getValue("SmallIcon");
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
/*     */   
/*     */   public abstract String getMacroID();
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
/*     */   public int getMnemonic() {
/* 177 */     Integer i = (Integer)getValue("MnemonicKey");
/* 178 */     return (i != null) ? i.intValue() : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 189 */     return (String)getValue("Name");
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
/*     */   public boolean isRecordable() {
/* 201 */     return this.isRecordable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccelerator(KeyStroke accelerator) {
/* 212 */     putValue("AcceleratorKey", accelerator);
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
/*     */   public void setMnemonic(char mnemonic) {
/* 224 */     setMnemonic(Integer.valueOf(mnemonic));
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
/*     */   public void setMnemonic(Integer mnemonic) {
/* 236 */     putValue("MnemonicKey", mnemonic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 247 */     putValue("Name", name);
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
/*     */   public void setProperties(ResourceBundle msg, String keyRoot) {
/* 261 */     setName(msg.getString(keyRoot + ".Name"));
/* 262 */     setMnemonic(msg.getString(keyRoot + ".Mnemonic").charAt(0));
/* 263 */     setShortDescription(msg.getString(keyRoot + ".Desc"));
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
/*     */   public void setRecordable(boolean recordable) {
/* 276 */     this.isRecordable = recordable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShortDescription(String shortDesc) {
/* 286 */     putValue("ShortDescription", shortDesc);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RecordableTextAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */