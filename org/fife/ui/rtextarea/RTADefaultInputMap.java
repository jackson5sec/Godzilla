/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.KeyStroke;
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
/*     */ public class RTADefaultInputMap
/*     */   extends InputMap
/*     */ {
/*     */   public RTADefaultInputMap() {
/*  52 */     int defaultModifier = RTextArea.getDefaultModifier();
/*     */     
/*  54 */     int alt = 512;
/*  55 */     int shift = 64;
/*  56 */     boolean isOSX = RTextArea.isOSX();
/*  57 */     int moveByWordMod = isOSX ? alt : defaultModifier;
/*     */     
/*  59 */     put(KeyStroke.getKeyStroke(36, 0), isOSX ? "caret-begin" : "caret-begin-line");
/*  60 */     put(KeyStroke.getKeyStroke(36, shift), isOSX ? "selection-begin" : "selection-begin-line");
/*  61 */     put(KeyStroke.getKeyStroke(36, defaultModifier), "caret-begin");
/*  62 */     put(KeyStroke.getKeyStroke(36, defaultModifier | shift), "selection-begin");
/*  63 */     put(KeyStroke.getKeyStroke(35, 0), isOSX ? "caret-end" : "caret-end-line");
/*  64 */     put(KeyStroke.getKeyStroke(35, shift), isOSX ? "selection-end" : "selection-end-line");
/*  65 */     put(KeyStroke.getKeyStroke(35, defaultModifier), "caret-end");
/*  66 */     put(KeyStroke.getKeyStroke(35, defaultModifier | shift), "selection-end");
/*     */     
/*  68 */     put(KeyStroke.getKeyStroke(37, 0), "caret-backward");
/*  69 */     put(KeyStroke.getKeyStroke(37, shift), "selection-backward");
/*  70 */     put(KeyStroke.getKeyStroke(37, moveByWordMod), "caret-previous-word");
/*  71 */     put(KeyStroke.getKeyStroke(37, moveByWordMod | shift), "selection-previous-word");
/*  72 */     put(KeyStroke.getKeyStroke(40, 0), "caret-down");
/*  73 */     put(KeyStroke.getKeyStroke(40, shift), "selection-down");
/*  74 */     put(KeyStroke.getKeyStroke(40, defaultModifier), "RTA.ScrollDownAction");
/*  75 */     put(KeyStroke.getKeyStroke(40, alt), "RTA.LineDownAction");
/*  76 */     put(KeyStroke.getKeyStroke(39, 0), "caret-forward");
/*  77 */     put(KeyStroke.getKeyStroke(39, shift), "selection-forward");
/*  78 */     put(KeyStroke.getKeyStroke(39, moveByWordMod), "caret-next-word");
/*  79 */     put(KeyStroke.getKeyStroke(39, moveByWordMod | shift), "selection-next-word");
/*  80 */     put(KeyStroke.getKeyStroke(38, 0), "caret-up");
/*  81 */     put(KeyStroke.getKeyStroke(38, shift), "selection-up");
/*  82 */     put(KeyStroke.getKeyStroke(38, defaultModifier), "RTA.ScrollUpAction");
/*  83 */     put(KeyStroke.getKeyStroke(38, alt), "RTA.LineUpAction");
/*     */     
/*  85 */     put(KeyStroke.getKeyStroke(33, 0), "page-up");
/*  86 */     put(KeyStroke.getKeyStroke(33, shift), "RTA.SelectionPageUpAction");
/*  87 */     put(KeyStroke.getKeyStroke(33, defaultModifier | shift), "RTA.SelectionPageLeftAction");
/*  88 */     put(KeyStroke.getKeyStroke(34, 0), "page-down");
/*  89 */     put(KeyStroke.getKeyStroke(34, shift), "RTA.SelectionPageDownAction");
/*  90 */     put(KeyStroke.getKeyStroke(34, defaultModifier | shift), "RTA.SelectionPageRightAction");
/*     */     
/*  92 */     put(KeyStroke.getKeyStroke(65489, 0), "cut-to-clipboard");
/*  93 */     put(KeyStroke.getKeyStroke(65485, 0), "copy-to-clipboard");
/*  94 */     put(KeyStroke.getKeyStroke(65487, 0), "paste-from-clipboard");
/*     */     
/*  96 */     put(KeyStroke.getKeyStroke(88, defaultModifier), "cut-to-clipboard");
/*  97 */     put(KeyStroke.getKeyStroke(67, defaultModifier), "copy-to-clipboard");
/*  98 */     put(KeyStroke.getKeyStroke(86, defaultModifier), "paste-from-clipboard");
/*  99 */     put(KeyStroke.getKeyStroke(86, defaultModifier | shift), "RTA.PasteHistoryAction");
/* 100 */     put(KeyStroke.getKeyStroke(127, 0), "delete-next");
/* 101 */     put(KeyStroke.getKeyStroke(127, shift), "cut-to-clipboard");
/* 102 */     put(KeyStroke.getKeyStroke(127, defaultModifier), "RTA.DeleteRestOfLineAction");
/* 103 */     put(KeyStroke.getKeyStroke(155, 0), "RTA.ToggleTextModeAction");
/* 104 */     put(KeyStroke.getKeyStroke(155, shift), "paste-from-clipboard");
/* 105 */     put(KeyStroke.getKeyStroke(155, defaultModifier), "copy-to-clipboard");
/* 106 */     put(KeyStroke.getKeyStroke(65, defaultModifier), "select-all");
/*     */     
/* 108 */     put(KeyStroke.getKeyStroke(68, defaultModifier), "RTA.DeleteLineAction");
/* 109 */     put(KeyStroke.getKeyStroke(74, defaultModifier), "RTA.JoinLinesAction");
/*     */     
/* 111 */     put(KeyStroke.getKeyStroke(8, shift), "delete-previous");
/* 112 */     put(KeyStroke.getKeyStroke(8, defaultModifier), "RTA.DeletePrevWordAction");
/* 113 */     put(KeyStroke.getKeyStroke(9, 0), "insert-tab");
/* 114 */     put(KeyStroke.getKeyStroke(10, 0), "insert-break");
/* 115 */     put(KeyStroke.getKeyStroke(10, shift), "insert-break");
/* 116 */     put(KeyStroke.getKeyStroke(10, defaultModifier), "RTA.DumbCompleteWordAction");
/*     */     
/* 118 */     put(KeyStroke.getKeyStroke(90, defaultModifier), "RTA.UndoAction");
/* 119 */     put(KeyStroke.getKeyStroke(89, defaultModifier), "RTA.RedoAction");
/*     */     
/* 121 */     put(KeyStroke.getKeyStroke(113, 0), "RTA.NextBookmarkAction");
/* 122 */     put(KeyStroke.getKeyStroke(113, shift), "RTA.PrevBookmarkAction");
/* 123 */     put(KeyStroke.getKeyStroke(113, defaultModifier), "RTA.ToggleBookmarkAction");
/*     */     
/* 125 */     put(KeyStroke.getKeyStroke(75, defaultModifier | shift), "RTA.PrevOccurrenceAction");
/* 126 */     put(KeyStroke.getKeyStroke(75, defaultModifier), "RTA.NextOccurrenceAction");
/*     */     
/* 128 */     if (isOSX) {
/* 129 */       put(KeyStroke.getKeyStroke(37, defaultModifier), "caret-begin-line");
/* 130 */       put(KeyStroke.getKeyStroke(39, defaultModifier), "caret-end-line");
/* 131 */       put(KeyStroke.getKeyStroke(37, defaultModifier | shift), "selection-begin-line");
/* 132 */       put(KeyStroke.getKeyStroke(39, defaultModifier | shift), "selection-end-line");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTADefaultInputMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */