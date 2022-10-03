/*     */ package com.jediterm.terminal;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class DefaultTerminalCopyPasteHandler implements TerminalCopyPasteHandler, ClipboardOwner {
/*  12 */   private static final Logger LOG = Logger.getLogger(DefaultTerminalCopyPasteHandler.class);
/*     */ 
/*     */   
/*     */   public void setContents(@NotNull String text, boolean useSystemSelectionClipboardIfAvailable) {
/*  16 */     if (text == null) $$$reportNull$$$0(0);  if (useSystemSelectionClipboardIfAvailable) {
/*  17 */       Clipboard systemSelectionClipboard = getSystemSelectionClipboard();
/*  18 */       if (systemSelectionClipboard != null) {
/*  19 */         setClipboardContents(new StringSelection(text), systemSelectionClipboard);
/*     */         return;
/*     */       } 
/*     */     } 
/*  23 */     setSystemClipboardContents(text);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getContents(boolean useSystemSelectionClipboardIfAvailable) {
/*  29 */     if (useSystemSelectionClipboardIfAvailable) {
/*  30 */       Clipboard systemSelectionClipboard = getSystemSelectionClipboard();
/*  31 */       if (systemSelectionClipboard != null) {
/*  32 */         return getClipboardContents(systemSelectionClipboard);
/*     */       }
/*     */     } 
/*  35 */     return getSystemClipboardContents();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setSystemClipboardContents(@NotNull String text) {
/*  40 */     if (text == null) $$$reportNull$$$0(1);  setClipboardContents(new StringSelection(text), getSystemClipboard());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getSystemClipboardContents() {
/*  45 */     return getClipboardContents(getSystemClipboard());
/*     */   }
/*     */   
/*     */   private void setClipboardContents(@NotNull Transferable contents, @Nullable Clipboard clipboard) {
/*  49 */     if (contents == null) $$$reportNull$$$0(2);  if (clipboard != null) {
/*     */       try {
/*  51 */         clipboard.setContents(contents, this);
/*     */       }
/*  53 */       catch (IllegalStateException e) {
/*  54 */         logException("Cannot set contents", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getClipboardContents(@Nullable Clipboard clipboard) {
/*  61 */     if (clipboard != null) {
/*     */       try {
/*  63 */         if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
/*  64 */           return (String)clipboard.getData(DataFlavor.stringFlavor);
/*     */         }
/*     */       }
/*  67 */       catch (Exception e) {
/*  68 */         logException("Cannot get clipboard contents", e);
/*     */       } 
/*     */     }
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Clipboard getSystemClipboard() {
/*     */     try {
/*  77 */       return Toolkit.getDefaultToolkit().getSystemClipboard();
/*     */     }
/*  79 */     catch (IllegalStateException e) {
/*  80 */       logException("Cannot get system clipboard", e);
/*  81 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Clipboard getSystemSelectionClipboard() {
/*     */     try {
/*  88 */       return Toolkit.getDefaultToolkit().getSystemSelection();
/*     */     }
/*  90 */     catch (IllegalStateException e) {
/*  91 */       logException("Cannot get system selection clipboard", e);
/*  92 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void logException(@NotNull String message, @NotNull Exception e) {
/*  97 */     if (message == null) $$$reportNull$$$0(3);  if (e == null) $$$reportNull$$$0(4);  if (UIUtil.isWindows && e instanceof IllegalStateException) {
/*  98 */       LOG.debug(message, e);
/*     */     } else {
/*     */       
/* 101 */       LOG.warn(message, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void lostOwnership(Clipboard clipboard, Transferable contents) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\DefaultTerminalCopyPasteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */