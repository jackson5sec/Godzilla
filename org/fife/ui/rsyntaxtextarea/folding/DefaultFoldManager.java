/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rtextarea.RDocument;
/*     */ import org.fife.ui.rtextarea.RTextArea;
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
/*     */ public class DefaultFoldManager
/*     */   implements FoldManager
/*     */ {
/*     */   private RSyntaxTextArea textArea;
/*     */   private Parser rstaParser;
/*     */   private FoldParser foldParser;
/*     */   private List<Fold> folds;
/*     */   private boolean codeFoldingEnabled;
/*     */   private PropertyChangeSupport support;
/*     */   private Listener l;
/*     */   
/*     */   public DefaultFoldManager(RSyntaxTextArea textArea) {
/*  67 */     this.textArea = textArea;
/*  68 */     this.support = new PropertyChangeSupport(this);
/*  69 */     this.l = new Listener();
/*  70 */     textArea.getDocument().addDocumentListener(this.l);
/*  71 */     textArea.addPropertyChangeListener("RSTA.syntaxStyle", this.l);
/*  72 */     textArea.addPropertyChangeListener("document", this.l);
/*  73 */     this.folds = new ArrayList<>();
/*  74 */     updateFoldParser();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l) {
/*  80 */     this.support.addPropertyChangeListener(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  86 */     this.folds.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ensureOffsetNotInClosedFold(int offs) {
/*  93 */     boolean foldsOpened = false;
/*  94 */     Fold fold = getDeepestFoldContaining(offs);
/*     */     
/*  96 */     while (fold != null) {
/*  97 */       if (fold.isCollapsed()) {
/*  98 */         fold.setCollapsed(false);
/*  99 */         foldsOpened = true;
/*     */       } 
/* 101 */       fold = fold.getParent();
/*     */     } 
/*     */     
/* 104 */     if (foldsOpened) {
/* 105 */       RSyntaxUtilities.possiblyRepaintGutter((RTextArea)this.textArea);
/*     */     }
/*     */     
/* 108 */     return foldsOpened;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold getDeepestFoldContaining(int offs) {
/* 115 */     Fold deepestFold = null;
/* 116 */     if (offs > -1) {
/* 117 */       for (int i = 0; i < this.folds.size(); i++) {
/* 118 */         Fold fold = getFold(i);
/* 119 */         if (fold.containsOffset(offs)) {
/* 120 */           deepestFold = fold.getDeepestFoldContaining(offs);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 125 */     return deepestFold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold getDeepestOpenFoldContaining(int offs) {
/* 132 */     Fold deepestFold = null;
/*     */     
/* 134 */     if (offs > -1) {
/* 135 */       for (int i = 0; i < this.folds.size(); i++) {
/* 136 */         Fold fold = getFold(i);
/* 137 */         if (fold.containsOffset(offs)) {
/* 138 */           if (fold.isCollapsed()) {
/* 139 */             return null;
/*     */           }
/* 141 */           deepestFold = fold.getDeepestOpenFoldContaining(offs);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 147 */     return deepestFold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold getFold(int index) {
/* 154 */     return this.folds.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFoldCount() {
/* 160 */     return this.folds.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold getFoldForLine(int line) {
/* 166 */     return getFoldForLineImpl(null, this.folds, line);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Fold getFoldForLineImpl(Fold parent, List<Fold> folds, int line) {
/* 172 */     int low = 0;
/* 173 */     int high = folds.size() - 1;
/*     */     
/* 175 */     while (low <= high) {
/* 176 */       int mid = low + high >> 1;
/* 177 */       Fold midFold = folds.get(mid);
/* 178 */       int startLine = midFold.getStartLine();
/* 179 */       if (line == startLine) {
/* 180 */         return midFold;
/*     */       }
/* 182 */       if (line < startLine) {
/* 183 */         high = mid - 1;
/*     */         continue;
/*     */       } 
/* 186 */       int endLine = midFold.getEndLine();
/* 187 */       if (line >= endLine) {
/* 188 */         low = mid + 1;
/*     */         continue;
/*     */       } 
/* 191 */       List<Fold> children = midFold.getChildren();
/* 192 */       return (children != null) ? getFoldForLineImpl(midFold, children, line) : null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHiddenLineCount() {
/* 203 */     int count = 0;
/* 204 */     for (Fold fold : this.folds) {
/* 205 */       count += fold.getCollapsedLineCount();
/*     */     }
/* 207 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHiddenLineCountAbove(int line) {
/* 213 */     return getHiddenLineCountAbove(line, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHiddenLineCountAbove(int line, boolean physical) {
/* 220 */     int count = 0;
/*     */     
/* 222 */     for (Fold fold : this.folds) {
/* 223 */       int comp = physical ? (line + count) : line;
/* 224 */       if (fold.getStartLine() >= comp) {
/*     */         break;
/*     */       }
/* 227 */       count += getHiddenLineCountAboveImpl(fold, comp, physical);
/*     */     } 
/*     */     
/* 230 */     return count;
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
/*     */   private int getHiddenLineCountAboveImpl(Fold fold, int line, boolean physical) {
/* 255 */     int count = 0;
/*     */     
/* 257 */     if (fold.getEndLine() < line || (fold
/* 258 */       .isCollapsed() && fold.getStartLine() < line)) {
/* 259 */       count = fold.getCollapsedLineCount();
/*     */     } else {
/*     */       
/* 262 */       int childCount = fold.getChildCount();
/* 263 */       for (int i = 0; i < childCount; i++) {
/* 264 */         Fold child = fold.getChild(i);
/* 265 */         int comp = physical ? (line + count) : line;
/* 266 */         if (child.getStartLine() >= comp) {
/*     */           break;
/*     */         }
/* 269 */         count += getHiddenLineCountAboveImpl(child, comp, physical);
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastVisibleLine() {
/* 281 */     int lastLine = this.textArea.getLineCount() - 1;
/*     */     
/* 283 */     if (isCodeFoldingSupportedAndEnabled()) {
/* 284 */       int foldCount = getFoldCount();
/* 285 */       if (foldCount > 0) {
/* 286 */         Fold lastFold = getFold(foldCount - 1);
/* 287 */         if (lastFold.containsLine(lastLine)) {
/* 288 */           if (lastFold.isCollapsed()) {
/* 289 */             lastLine = lastFold.getStartLine();
/*     */           } else {
/*     */             
/* 292 */             while (lastFold.getHasChildFolds()) {
/* 293 */               lastFold = lastFold.getLastChild();
/* 294 */               if (lastFold.containsLine(lastLine)) {
/* 295 */                 if (lastFold.isCollapsed()) {
/* 296 */                   lastLine = lastFold.getStartLine();
/*     */ 
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 309 */     return lastLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVisibleLineAbove(int line) {
/* 317 */     if (line <= 0 || line >= this.textArea.getLineCount()) {
/* 318 */       return -1;
/*     */     }
/*     */     
/*     */     do {
/* 322 */       line--;
/* 323 */     } while (line >= 0 && isLineHidden(line));
/*     */     
/* 325 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVisibleLineBelow(int line) {
/* 333 */     int lineCount = this.textArea.getLineCount();
/* 334 */     if (line < 0 || line >= lineCount - 1) {
/* 335 */       return -1;
/*     */     }
/*     */     
/*     */     do {
/* 339 */       line++;
/* 340 */     } while (line < lineCount && isLineHidden(line));
/*     */     
/* 342 */     return (line == lineCount) ? -1 : line;
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
/*     */   public boolean isCodeFoldingEnabled() {
/* 375 */     return this.codeFoldingEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCodeFoldingSupportedAndEnabled() {
/* 381 */     return (this.codeFoldingEnabled && this.foldParser != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFoldStartLine(int line) {
/* 387 */     return (getFoldForLine(line) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLineHidden(int line) {
/* 393 */     for (Fold fold : this.folds) {
/* 394 */       if (fold.containsLine(line)) {
/* 395 */         if (fold.isCollapsed()) {
/* 396 */           return true;
/*     */         }
/*     */         
/* 399 */         return isLineHiddenImpl(fold, line);
/*     */       } 
/*     */     } 
/*     */     
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isLineHiddenImpl(Fold parent, int line) {
/* 408 */     for (int i = 0; i < parent.getChildCount(); i++) {
/* 409 */       Fold child = parent.getChild(i);
/* 410 */       if (child.containsLine(line)) {
/* 411 */         if (child.isCollapsed()) {
/* 412 */           return true;
/*     */         }
/*     */         
/* 415 */         return isLineHiddenImpl(child, line);
/*     */       } 
/*     */     } 
/*     */     
/* 419 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void keepFoldState(Fold newFold, List<Fold> oldFolds) {
/* 424 */     int previousLoc = Collections.binarySearch((List)oldFolds, newFold);
/*     */     
/* 426 */     if (previousLoc >= 0) {
/* 427 */       Fold prevFold = oldFolds.get(previousLoc);
/* 428 */       newFold.setCollapsed(prevFold.isCollapsed());
/*     */     }
/*     */     else {
/*     */       
/* 432 */       int insertionPoint = -(previousLoc + 1);
/* 433 */       if (insertionPoint > 0) {
/* 434 */         Fold possibleParentFold = oldFolds.get(insertionPoint - 1);
/* 435 */         if (possibleParentFold.containsOffset(newFold
/* 436 */             .getStartOffset())) {
/* 437 */           List<Fold> children = possibleParentFold.getChildren();
/* 438 */           if (children != null) {
/* 439 */             keepFoldState(newFold, children);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void keepFoldStates(List<Fold> newFolds, List<Fold> oldFolds) {
/* 448 */     for (Fold newFold : newFolds) {
/* 449 */       keepFoldState(newFold, this.folds);
/* 450 */       List<Fold> newChildFolds = newFold.getChildren();
/* 451 */       if (newChildFolds != null) {
/* 452 */         keepFoldStates(newChildFolds, oldFolds);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l) {
/* 460 */     this.support.removePropertyChangeListener(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reparse() {
/* 467 */     if (this.codeFoldingEnabled && this.foldParser != null) {
/*     */ 
/*     */ 
/*     */       
/* 471 */       List<Fold> newFolds = this.foldParser.getFolds(this.textArea);
/* 472 */       if (newFolds == null) {
/* 473 */         newFolds = Collections.emptyList();
/*     */       } else {
/*     */         
/* 476 */         keepFoldStates(newFolds, this.folds);
/*     */       } 
/* 478 */       this.folds = newFolds;
/*     */ 
/*     */       
/* 481 */       this.support.firePropertyChange("FoldsUpdated", (Object)null, this.folds);
/* 482 */       this.textArea.repaint();
/*     */     }
/*     */     else {
/*     */       
/* 486 */       this.folds.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodeFoldingEnabled(boolean enabled) {
/* 494 */     if (enabled != this.codeFoldingEnabled) {
/* 495 */       this.codeFoldingEnabled = enabled;
/* 496 */       if (this.rstaParser != null) {
/* 497 */         this.textArea.removeParser(this.rstaParser);
/*     */       }
/* 499 */       if (enabled) {
/* 500 */         this.rstaParser = (Parser)new AbstractParser()
/*     */           {
/*     */             public ParseResult parse(RSyntaxDocument doc, String style) {
/* 503 */               DefaultFoldManager.this.reparse();
/* 504 */               return (ParseResult)new DefaultParseResult((Parser)this);
/*     */             }
/*     */           };
/* 507 */         this.textArea.addParser(this.rstaParser);
/* 508 */         this.support.firePropertyChange("FoldsUpdated", (Object)null, (Object)null);
/*     */       }
/*     */       else {
/*     */         
/* 512 */         this.folds = Collections.emptyList();
/* 513 */         this.textArea.repaint();
/* 514 */         this.support.firePropertyChange("FoldsUpdated", (Object)null, (Object)null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFolds(List<Fold> folds) {
/* 522 */     this.folds = folds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateFoldParser() {
/* 531 */     this.foldParser = FoldParserManager.get().getFoldParser(this.textArea
/* 532 */         .getSyntaxEditingStyle());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     implements DocumentListener, PropertyChangeListener
/*     */   {
/*     */     private Listener() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 550 */       int startOffs = e.getOffset();
/* 551 */       int endOffs = startOffs + e.getLength();
/* 552 */       Document doc = e.getDocument();
/* 553 */       Element root = doc.getDefaultRootElement();
/* 554 */       int startLine = root.getElementIndex(startOffs);
/* 555 */       int endLine = root.getElementIndex(endOffs);
/* 556 */       if (startLine != endLine) {
/* 557 */         Fold fold = DefaultFoldManager.this.getFoldForLine(startLine);
/* 558 */         if (fold != null && fold.isCollapsed()) {
/* 559 */           fold.toggleCollapsedState();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 567 */       String name = e.getPropertyName();
/*     */       
/* 569 */       if ("RSTA.syntaxStyle".equals(name)) {
/*     */         
/* 571 */         DefaultFoldManager.this.updateFoldParser();
/* 572 */         DefaultFoldManager.this.reparse();
/*     */       
/*     */       }
/* 575 */       else if ("document".equals(name)) {
/*     */         
/* 577 */         RDocument old = (RDocument)e.getOldValue();
/* 578 */         if (old != null) {
/* 579 */           old.removeDocumentListener(this);
/*     */         }
/* 581 */         RDocument newDoc = (RDocument)e.getNewValue();
/* 582 */         if (newDoc != null) {
/* 583 */           newDoc.addDocumentListener(this);
/*     */         }
/* 585 */         DefaultFoldManager.this.reparse();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 595 */       int offs = e.getOffset();
/*     */       try {
/* 597 */         int lastLineModified = DefaultFoldManager.this.textArea.getLineOfOffset(offs);
/*     */         
/* 599 */         Fold fold = DefaultFoldManager.this.getFoldForLine(lastLineModified);
/*     */         
/* 601 */         if (fold != null && fold.isCollapsed()) {
/* 602 */           fold.toggleCollapsedState();
/*     */         }
/* 604 */       } catch (BadLocationException ble) {
/* 605 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\DefaultFoldManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */