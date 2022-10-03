/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Position;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ public class Fold
/*     */   implements Comparable<Fold>
/*     */ {
/*     */   private int type;
/*     */   private RSyntaxTextArea textArea;
/*     */   private Position startOffs;
/*     */   private Position endOffs;
/*     */   private Fold parent;
/*     */   private List<Fold> children;
/*     */   private boolean collapsed;
/*     */   private int childCollapsedLineCount;
/*  47 */   private int lastStartOffs = -1;
/*     */   
/*     */   private int cachedStartLine;
/*  50 */   private int lastEndOffs = -1;
/*     */   
/*     */   private int cachedEndLine;
/*     */ 
/*     */   
/*     */   public Fold(int type, RSyntaxTextArea textArea, int startOffs) throws BadLocationException {
/*  56 */     this.type = type;
/*  57 */     this.textArea = textArea;
/*  58 */     this.startOffs = textArea.getDocument().createPosition(startOffs);
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
/*     */   public Fold createChild(int type, int startOffs) throws BadLocationException {
/*  72 */     Fold child = new Fold(type, this.textArea, startOffs);
/*  73 */     child.parent = this;
/*  74 */     if (this.children == null) {
/*  75 */       this.children = new ArrayList<>();
/*     */     }
/*  77 */     this.children.add(child);
/*  78 */     return child;
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
/*     */   public int compareTo(Fold otherFold) {
/*  90 */     int result = -1;
/*  91 */     if (otherFold != null) {
/*  92 */       result = this.startOffs.getOffset() - otherFold.startOffs.getOffset();
/*     */     }
/*     */     
/*  95 */     return result;
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
/*     */   public boolean containsLine(int line) {
/* 111 */     return (line > getStartLine() && line <= getEndLine());
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
/*     */   public boolean containsOrStartsOnLine(int line) {
/* 124 */     return (line >= getStartLine() && line <= getEndLine());
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
/*     */   public boolean containsOffset(int offs) {
/* 138 */     boolean contained = false;
/* 139 */     if (offs > getStartOffset()) {
/*     */       
/* 141 */       Element root = this.textArea.getDocument().getDefaultRootElement();
/* 142 */       int line = root.getElementIndex(offs);
/* 143 */       contained = (line <= getEndLine());
/*     */     } 
/* 145 */     return contained;
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
/*     */   public boolean equals(Object otherFold) {
/* 158 */     return (otherFold instanceof Fold && compareTo((Fold)otherFold) == 0);
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
/*     */   public Fold getChild(int index) {
/* 170 */     return this.children.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 181 */     return (this.children == null) ? 0 : this.children.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Fold> getChildren() {
/* 192 */     return this.children;
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
/*     */   public int getCollapsedLineCount() {
/* 208 */     return this.collapsed ? getLineCount() : this.childCollapsedLineCount;
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
/*     */   Fold getDeepestFoldContaining(int offs) {
/* 223 */     Fold deepestFold = this;
/* 224 */     for (int i = 0; i < getChildCount(); i++) {
/* 225 */       Fold fold = getChild(i);
/* 226 */       if (fold.containsOffset(offs)) {
/* 227 */         deepestFold = fold.getDeepestFoldContaining(offs);
/*     */         break;
/*     */       } 
/*     */     } 
/* 231 */     return deepestFold;
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
/*     */   Fold getDeepestOpenFoldContaining(int offs) {
/* 247 */     Fold deepestFold = this;
/*     */     
/* 249 */     for (int i = 0; i < getChildCount(); i++) {
/* 250 */       Fold fold = getChild(i);
/* 251 */       if (fold.containsOffset(offs)) {
/* 252 */         if (fold.isCollapsed()) {
/*     */           break;
/*     */         }
/* 255 */         deepestFold = fold.getDeepestOpenFoldContaining(offs);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 260 */     return deepestFold;
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
/*     */   public int getEndLine() {
/* 279 */     int endOffs = getEndOffset();
/* 280 */     if (this.lastEndOffs == endOffs) {
/* 281 */       return this.cachedEndLine;
/*     */     }
/* 283 */     this.lastEndOffs = endOffs;
/* 284 */     Element root = this.textArea.getDocument().getDefaultRootElement();
/* 285 */     return this.cachedEndLine = root.getElementIndex(endOffs);
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
/*     */   public int getEndOffset() {
/* 305 */     return (this.endOffs != null) ? this.endOffs.getOffset() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFoldType() {
/* 316 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasChildFolds() {
/* 327 */     return (getChildCount() > 0);
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
/*     */   public Fold getLastChild() {
/* 340 */     int childCount = getChildCount();
/* 341 */     return (childCount == 0) ? null : getChild(childCount - 1);
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
/*     */   public int getLineCount() {
/* 354 */     return getEndLine() - getStartLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold getParent() {
/* 365 */     return this.parent;
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
/*     */   public int getStartLine() {
/* 382 */     int startOffs = getStartOffset();
/* 383 */     if (this.lastStartOffs == startOffs) {
/* 384 */       return this.cachedStartLine;
/*     */     }
/* 386 */     this.lastStartOffs = startOffs;
/* 387 */     Element root = this.textArea.getDocument().getDefaultRootElement();
/* 388 */     return this.cachedStartLine = root.getElementIndex(startOffs);
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
/*     */   public int getStartOffset() {
/* 406 */     return this.startOffs.getOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 412 */     return getStartLine();
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
/*     */   public boolean isCollapsed() {
/* 424 */     return this.collapsed;
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
/*     */   public boolean isOnSingleLine() {
/* 437 */     return (getStartLine() == getEndLine());
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
/*     */   public boolean removeFromParent() {
/* 450 */     if (this.parent != null) {
/* 451 */       this.parent.removeMostRecentChild();
/* 452 */       this.parent = null;
/* 453 */       return true;
/*     */     } 
/* 455 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeMostRecentChild() {
/* 460 */     this.children.remove(this.children.size() - 1);
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
/*     */   public void setCollapsed(boolean collapsed) {
/* 474 */     if (collapsed != this.collapsed) {
/*     */ 
/*     */       
/* 477 */       int lineCount = getLineCount();
/* 478 */       int linesToCollapse = lineCount - this.childCollapsedLineCount;
/* 479 */       if (!collapsed) {
/* 480 */         linesToCollapse = -linesToCollapse;
/*     */       }
/*     */ 
/*     */       
/* 484 */       this.collapsed = collapsed;
/* 485 */       if (this.parent != null) {
/* 486 */         this.parent.updateChildCollapsedLineCount(linesToCollapse);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 491 */       if (collapsed) {
/* 492 */         int dot = this.textArea.getSelectionStart();
/* 493 */         Element root = this.textArea.getDocument().getDefaultRootElement();
/* 494 */         int dotLine = root.getElementIndex(dot);
/* 495 */         boolean updateCaret = containsLine(dotLine);
/* 496 */         if (!updateCaret) {
/* 497 */           int mark = this.textArea.getSelectionEnd();
/* 498 */           if (mark != dot) {
/* 499 */             int markLine = root.getElementIndex(mark);
/* 500 */             updateCaret = containsLine(markLine);
/*     */           } 
/*     */         } 
/* 503 */         if (updateCaret) {
/* 504 */           dot = root.getElement(getStartLine()).getEndOffset() - 1;
/* 505 */           this.textArea.setCaretPosition(dot);
/*     */         } 
/*     */       } 
/*     */       
/* 509 */       this.textArea.foldToggled(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndOffset(int endOffs) throws BadLocationException {
/* 528 */     this.endOffs = this.textArea.getDocument().createPosition(endOffs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toggleCollapsedState() {
/* 538 */     setCollapsed(!this.collapsed);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateChildCollapsedLineCount(int count) {
/* 543 */     this.childCollapsedLineCount += count;
/*     */ 
/*     */ 
/*     */     
/* 547 */     if (!this.collapsed && this.parent != null) {
/* 548 */       this.parent.updateChildCollapsedLineCount(count);
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
/*     */   public String toString() {
/* 560 */     return "[Fold: startOffs=" + 
/* 561 */       getStartOffset() + ", endOffs=" + 
/* 562 */       getEndOffset() + ", collapsed=" + this.collapsed + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\Fold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */