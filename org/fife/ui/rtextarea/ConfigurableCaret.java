/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.NavigationFilter;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
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
/*     */ public class ConfigurableCaret
/*     */   extends DefaultCaret
/*     */ {
/*  66 */   private static transient Action selectWord = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private static transient Action selectLine = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private transient MouseEvent selectedWordEvent = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Segment seg;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CaretStyle style;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChangeableHighlightPainter selectionPainter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean alwaysVisible;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean pasteOnMiddleMouseClick;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableCaret() {
/* 107 */     this(CaretStyle.THICK_VERTICAL_LINE_STYLE);
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
/*     */   public ConfigurableCaret(CaretStyle style) {
/* 119 */     this.seg = new Segment();
/* 120 */     setStyle(style);
/* 121 */     this.selectionPainter = new ChangeableHighlightPainter();
/* 122 */     this.pasteOnMiddleMouseClick = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustCaret(MouseEvent e) {
/* 130 */     if ((e.getModifiers() & 0x1) != 0 && getDot() != -1) {
/* 131 */       moveCaret(e);
/*     */     } else {
/*     */       
/* 134 */       positionCaret(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustFocus(boolean inWindow) {
/* 145 */     RTextArea textArea = getTextArea();
/* 146 */     if (textArea != null && textArea.isEnabled() && textArea
/* 147 */       .isRequestFocusEnabled()) {
/* 148 */       if (inWindow) {
/* 149 */         textArea.requestFocusInWindow();
/*     */       } else {
/*     */         
/* 152 */         textArea.requestFocus();
/*     */       } 
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
/*     */   protected synchronized void damage(Rectangle r) {
/* 166 */     if (r != null) {
/* 167 */       validateWidth(r);
/* 168 */       this.x = r.x - 1;
/* 169 */       this.y = r.y;
/* 170 */       this.width = r.width + 4;
/* 171 */       this.height = r.height;
/* 172 */       repaint();
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
/*     */   public void deinstall(JTextComponent c) {
/* 188 */     if (!(c instanceof RTextArea)) {
/* 189 */       throw new IllegalArgumentException("c must be instance of RTextArea");
/*     */     }
/*     */     
/* 192 */     super.deinstall(c);
/* 193 */     c.setNavigationFilter((NavigationFilter)null);
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
/*     */   public boolean getPasteOnMiddleMouseClick() {
/* 205 */     return this.pasteOnMiddleMouseClick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RTextArea getTextArea() {
/* 215 */     return (RTextArea)getComponent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRoundedSelectionEdges() {
/* 226 */     return ((ChangeableHighlightPainter)getSelectionPainter())
/* 227 */       .getRoundedEdges();
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
/*     */   protected Highlighter.HighlightPainter getSelectionPainter() {
/* 239 */     return this.selectionPainter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CaretStyle getStyle() {
/* 250 */     return this.style;
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
/*     */   public void install(JTextComponent c) {
/* 262 */     if (!(c instanceof RTextArea)) {
/* 263 */       throw new IllegalArgumentException("c must be instance of RTextArea");
/*     */     }
/*     */     
/* 266 */     super.install(c);
/* 267 */     c.setNavigationFilter(new FoldAwareNavigationFilter());
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
/*     */   public boolean isAlwaysVisible() {
/* 281 */     return this.alwaysVisible;
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
/*     */   public void mouseClicked(MouseEvent e) {
/* 295 */     if (!e.isConsumed()) {
/*     */       
/* 297 */       RTextArea textArea = getTextArea();
/* 298 */       int nclicks = e.getClickCount();
/*     */       
/* 300 */       if (SwingUtilities.isLeftMouseButton(e)) {
/* 301 */         if (nclicks > 2) {
/* 302 */           Action a; ActionMap map; nclicks %= 2;
/* 303 */           switch (nclicks) {
/*     */             case 0:
/* 305 */               selectWord(e);
/* 306 */               this.selectedWordEvent = null;
/*     */               break;
/*     */             case 1:
/* 309 */               a = null;
/* 310 */               map = textArea.getActionMap();
/* 311 */               if (map != null) {
/* 312 */                 a = map.get("select-line");
/*     */               }
/* 314 */               if (a == null) {
/* 315 */                 if (selectLine == null) {
/* 316 */                   selectLine = new RTextAreaEditorKit.SelectLineAction();
/*     */                 }
/* 318 */                 a = selectLine;
/*     */               } 
/* 320 */               a.actionPerformed(new ActionEvent(textArea, 1001, null, e
/*     */                     
/* 322 */                     .getWhen(), e.getModifiers()));
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/* 327 */       } else if (SwingUtilities.isMiddleMouseButton(e) && 
/* 328 */         getPasteOnMiddleMouseClick() && 
/* 329 */         nclicks == 1 && textArea.isEditable() && textArea.isEnabled()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 334 */         JTextComponent c = (JTextComponent)e.getSource();
/* 335 */         if (c != null) {
/*     */           try {
/* 337 */             Toolkit tk = c.getToolkit();
/* 338 */             Clipboard buffer = tk.getSystemSelection();
/*     */ 
/*     */             
/* 341 */             if (buffer != null) {
/* 342 */               adjustCaret(e);
/* 343 */               TransferHandler th = c.getTransferHandler();
/* 344 */               if (th != null) {
/* 345 */                 Transferable trans = buffer.getContents(null);
/* 346 */                 if (trans != null) {
/* 347 */                   th.importData(c, trans);
/*     */                 }
/*     */               } 
/* 350 */               adjustFocus(true);
/*     */             
/*     */             }
/*     */             else {
/*     */               
/* 355 */               textArea.paste();
/*     */             } 
/* 357 */           } catch (HeadlessException headlessException) {}
/*     */         }
/*     */       } 
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
/*     */   public void mousePressed(MouseEvent e) {
/* 376 */     super.mousePressed(e);
/* 377 */     if (!e.isConsumed() && SwingUtilities.isRightMouseButton(e)) {
/* 378 */       JTextComponent c = getComponent();
/* 379 */       if (c != null && c.isEnabled() && c.isRequestFocusEnabled()) {
/* 380 */         c.requestFocusInWindow();
/*     */       }
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
/*     */   public void paint(Graphics g) {
/* 395 */     if (isVisible() || this.alwaysVisible) {
/*     */       try {
/*     */         Color textAreaBg;
/*     */         int y;
/* 399 */         RTextArea textArea = getTextArea();
/* 400 */         g.setColor(textArea.getCaretColor());
/* 401 */         TextUI mapper = textArea.getUI();
/* 402 */         Rectangle r = mapper.modelToView(textArea, getDot());
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
/* 413 */         validateWidth(r);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 420 */         if (this.width > 0 && this.height > 0 && 
/* 421 */           !contains(r.x, r.y, r.width, r.height)) {
/* 422 */           Rectangle clip = g.getClipBounds();
/* 423 */           if (clip != null && !clip.contains(this))
/*     */           {
/*     */             
/* 426 */             repaint();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 431 */           damage(r);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 436 */         r.height -= 2;
/*     */         
/* 438 */         switch (this.style) {
/*     */ 
/*     */           
/*     */           case BLOCK_STYLE:
/* 442 */             textAreaBg = textArea.getBackground();
/* 443 */             if (textAreaBg == null) {
/* 444 */               textAreaBg = Color.white;
/*     */             }
/* 446 */             g.setXORMode(textAreaBg);
/*     */             
/* 448 */             g.fillRect(r.x, r.y, r.width, r.height);
/*     */             return;
/*     */ 
/*     */ 
/*     */           
/*     */           case BLOCK_BORDER_STYLE:
/* 454 */             g.drawRect(r.x, r.y, r.width - 1, r.height);
/*     */             return;
/*     */ 
/*     */           
/*     */           case UNDERLINE_STYLE:
/* 459 */             textAreaBg = textArea.getBackground();
/* 460 */             if (textAreaBg == null) {
/* 461 */               textAreaBg = Color.white;
/*     */             }
/* 463 */             g.setXORMode(textAreaBg);
/* 464 */             y = r.y + r.height;
/* 465 */             g.drawLine(r.x, y, r.x + r.width - 1, y);
/*     */             return;
/*     */ 
/*     */ 
/*     */           
/*     */           default:
/* 471 */             g.drawLine(r.x, r.y, r.x, r.y + r.height);
/*     */             return;
/*     */           case THICK_VERTICAL_LINE_STYLE:
/*     */             break;
/*     */         } 
/* 476 */         g.drawLine(r.x, r.y, r.x, r.y + r.height);
/* 477 */         r.x++;
/* 478 */         g.drawLine(r.x, r.y, r.x, r.y + r.height);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 483 */       catch (BadLocationException ble) {
/* 484 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void selectWord(MouseEvent e) {
/* 496 */     if (this.selectedWordEvent != null && this.selectedWordEvent
/* 497 */       .getX() == e.getX() && this.selectedWordEvent
/* 498 */       .getY() == e.getY()) {
/*     */       return;
/*     */     }
/*     */     
/* 502 */     Action a = null;
/* 503 */     RTextArea textArea = getTextArea();
/* 504 */     ActionMap map = textArea.getActionMap();
/* 505 */     if (map != null) {
/* 506 */       a = map.get("select-word");
/*     */     }
/* 508 */     if (a == null) {
/* 509 */       if (selectWord == null) {
/* 510 */         selectWord = new RTextAreaEditorKit.SelectWordAction();
/*     */       }
/* 512 */       a = selectWord;
/*     */     } 
/* 514 */     a.actionPerformed(new ActionEvent(textArea, 1001, null, e
/*     */           
/* 516 */           .getWhen(), e.getModifiers()));
/* 517 */     this.selectedWordEvent = e;
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
/*     */   public void setAlwaysVisible(boolean alwaysVisible) {
/* 531 */     if (alwaysVisible != this.alwaysVisible) {
/* 532 */       this.alwaysVisible = alwaysVisible;
/* 533 */       if (!isVisible())
/*     */       {
/*     */         
/* 536 */         repaint();
/*     */       }
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
/*     */   public void setPasteOnMiddleMouseClick(boolean paste) {
/* 550 */     this.pasteOnMiddleMouseClick = paste;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundedSelectionEdges(boolean rounded) {
/* 561 */     ((ChangeableHighlightPainter)getSelectionPainter())
/* 562 */       .setRoundedEdges(rounded);
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
/*     */   public void setSelectionVisible(boolean visible) {
/* 575 */     super.setSelectionVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(CaretStyle style) {
/* 586 */     if (style == null) {
/* 587 */       style = CaretStyle.THICK_VERTICAL_LINE_STYLE;
/*     */     }
/* 589 */     if (style != this.style) {
/* 590 */       this.style = style;
/* 591 */       repaint();
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
/*     */   private void validateWidth(Rectangle rect) {
/* 623 */     if (rect != null && rect.width <= 1) {
/*     */       
/*     */       try {
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
/* 636 */         RTextArea textArea = getTextArea();
/* 637 */         textArea.getDocument().getText(getDot(), 1, this.seg);
/* 638 */         Font font = textArea.getFont();
/* 639 */         FontMetrics fm = textArea.getFontMetrics(font);
/* 640 */         rect.width = fm.charWidth(this.seg.array[this.seg.offset]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 646 */         if (rect.width == 0) {
/* 647 */           rect.width = fm.charWidth(' ');
/*     */         }
/*     */       }
/* 650 */       catch (BadLocationException ble) {
/*     */         
/* 652 */         ble.printStackTrace();
/* 653 */         rect.width = 8;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class FoldAwareNavigationFilter
/*     */     extends NavigationFilter
/*     */   {
/*     */     private FoldAwareNavigationFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
/* 675 */       RTextArea textArea = ConfigurableCaret.this.getTextArea();
/* 676 */       if (textArea instanceof RSyntaxTextArea) {
/*     */         
/* 678 */         RSyntaxTextArea rsta = (RSyntaxTextArea)ConfigurableCaret.this.getTextArea();
/* 679 */         if (rsta.isCodeFoldingEnabled()) {
/*     */           
/* 681 */           int lastDot = ConfigurableCaret.this.getDot();
/* 682 */           FoldManager fm = rsta.getFoldManager();
/* 683 */           int line = 0;
/*     */           try {
/* 685 */             line = textArea.getLineOfOffset(dot);
/* 686 */           } catch (Exception e) {
/* 687 */             e.printStackTrace();
/*     */           } 
/*     */           
/* 690 */           if (fm.isLineHidden(line)) {
/*     */             
/*     */             try {
/*     */               
/* 694 */               if (dot > lastDot) {
/* 695 */                 int lineCount = textArea.getLineCount();
/* 696 */                 while (++line < lineCount && fm
/* 697 */                   .isLineHidden(line));
/* 698 */                 if (line < lineCount) {
/* 699 */                   dot = textArea.getLineStartOffset(line);
/*     */                 } else {
/*     */                   
/* 702 */                   UIManager.getLookAndFeel()
/* 703 */                     .provideErrorFeedback(textArea);
/*     */                   
/*     */                   return;
/*     */                 } 
/* 707 */               } else if (dot < lastDot) {
/* 708 */                 while (--line >= 0 && fm.isLineHidden(line));
/* 709 */                 if (line >= 0) {
/* 710 */                   dot = textArea.getLineEndOffset(line) - 1;
/*     */                 }
/*     */               } 
/* 713 */             } catch (Exception e) {
/* 714 */               e.printStackTrace();
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 724 */       super.setDot(fb, dot, bias);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
/* 730 */       super.moveDot(fb, dot, bias);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ConfigurableCaret.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */