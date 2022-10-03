/*     */ package org.fife.ui.rsyntaxtextarea.focusabletip;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.net.URL;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FocusableTip
/*     */ {
/*     */   private JTextArea textArea;
/*     */   private TipWindow tipWindow;
/*     */   private URL imageBase;
/*     */   private TextAreaListener textAreaListener;
/*     */   private HyperlinkListener hyperlinkListener;
/*     */   private String lastText;
/*     */   private Dimension maxSize;
/*     */   private Rectangle tipVisibleBounds;
/*     */   private static final int X_MARGIN = 18;
/*     */   private static final int Y_MARGIN = 12;
/*  72 */   private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip");
/*     */ 
/*     */ 
/*     */   
/*     */   public FocusableTip(JTextArea textArea, HyperlinkListener listener) {
/*  77 */     setTextArea(textArea);
/*  78 */     this.hyperlinkListener = listener;
/*  79 */     this.textAreaListener = new TextAreaListener();
/*  80 */     this.tipVisibleBounds = new Rectangle();
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
/*     */   private void computeTipVisibleBounds() {
/*  92 */     Rectangle r = this.tipWindow.getBounds();
/*  93 */     Point p = r.getLocation();
/*  94 */     SwingUtilities.convertPointFromScreen(p, this.textArea);
/*  95 */     r.setLocation(p);
/*  96 */     this.tipVisibleBounds.setBounds(r.x, r.y - 15, r.width, r.height + 30);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createAndShowTipWindow(MouseEvent e, String text) {
/* 102 */     Window owner = SwingUtilities.getWindowAncestor(this.textArea);
/* 103 */     this.tipWindow = new TipWindow(owner, this, text);
/* 104 */     this.tipWindow.setHyperlinkListener(this.hyperlinkListener);
/*     */ 
/*     */     
/* 107 */     PopupWindowDecorator decorator = PopupWindowDecorator.get();
/* 108 */     if (decorator != null) {
/* 109 */       decorator.decorate(this.tipWindow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     SwingUtilities.invokeLater(() -> {
/*     */           if (this.tipWindow == null) {
/*     */             return;
/*     */           }
/*     */           this.tipWindow.fixSize();
/*     */           ComponentOrientation o = this.textArea.getComponentOrientation();
/*     */           Point p = e.getPoint();
/*     */           SwingUtilities.convertPointToScreen(p, this.textArea);
/*     */           Rectangle sb = TipUtil.getScreenBoundsForPoint(p.x, p.y);
/*     */           int y = p.y + 12;
/*     */           if (y + this.tipWindow.getHeight() >= sb.y + sb.height) {
/*     */             y = p.y - 12 - this.tipWindow.getHeight();
/*     */             if (y < sb.y) {
/*     */               y = sb.y + 12;
/*     */             }
/*     */           } 
/*     */           int x = p.x - 18;
/*     */           if (!o.isLeftToRight()) {
/*     */             x = p.x - this.tipWindow.getWidth() + 18;
/*     */           }
/*     */           if (x < sb.x) {
/*     */             x = sb.x;
/*     */           } else if (x + this.tipWindow.getWidth() > sb.x + sb.width) {
/*     */             x = sb.x + sb.width - this.tipWindow.getWidth();
/*     */           } 
/*     */           this.tipWindow.setLocation(x, y);
/*     */           this.tipWindow.setVisible(true);
/*     */           computeTipVisibleBounds();
/*     */           this.textAreaListener.install(this.textArea);
/*     */           this.lastText = text;
/*     */         });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getImageBase() {
/* 189 */     return this.imageBase;
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
/*     */   public Dimension getMaxSize() {
/* 201 */     return this.maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getString(String key) {
/* 212 */     return MSG.getString(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void possiblyDisposeOfTipWindow() {
/* 220 */     if (this.tipWindow != null) {
/* 221 */       this.tipWindow.dispose();
/* 222 */       this.tipWindow = null;
/* 223 */       this.textAreaListener.uninstall();
/* 224 */       this.tipVisibleBounds.setBounds(-1, -1, 0, 0);
/* 225 */       this.lastText = null;
/* 226 */       this.textArea.requestFocus();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void removeListeners() {
/* 233 */     this.textAreaListener.uninstall();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImageBase(URL url) {
/* 244 */     this.imageBase = url;
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
/*     */   public void setMaxSize(Dimension maxSize) {
/* 256 */     this.maxSize = maxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setTextArea(JTextArea textArea) {
/* 261 */     this.textArea = textArea;
/*     */     
/* 263 */     ToolTipManager.sharedInstance().registerComponent(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toolTipRequested(MouseEvent e, String text) {
/* 269 */     if (text == null || text.length() == 0) {
/* 270 */       possiblyDisposeOfTipWindow();
/* 271 */       this.lastText = text;
/*     */       
/*     */       return;
/*     */     } 
/* 275 */     if (this.lastText == null || text.length() != this.lastText.length() || 
/* 276 */       !text.equals(this.lastText)) {
/* 277 */       possiblyDisposeOfTipWindow();
/* 278 */       createAndShowTipWindow(e, text);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class TextAreaListener
/*     */     extends MouseInputAdapter
/*     */     implements CaretListener, ComponentListener, FocusListener, KeyListener
/*     */   {
/*     */     private TextAreaListener() {}
/*     */ 
/*     */     
/*     */     public void caretUpdate(CaretEvent e) {
/* 292 */       Object source = e.getSource();
/* 293 */       if (source == FocusableTip.this.textArea) {
/* 294 */         FocusableTip.this.possiblyDisposeOfTipWindow();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentHidden(ComponentEvent e) {
/* 300 */       handleComponentEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentMoved(ComponentEvent e) {
/* 305 */       handleComponentEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentResized(ComponentEvent e) {
/* 310 */       handleComponentEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentShown(ComponentEvent e) {
/* 315 */       handleComponentEvent(e);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 328 */       Component c = e.getOppositeComponent();
/*     */ 
/*     */       
/* 331 */       boolean tipClicked = (c instanceof TipWindow || (c != null && SwingUtilities.getWindowAncestor(c) instanceof TipWindow));
/* 332 */       if (!tipClicked) {
/* 333 */         FocusableTip.this.possiblyDisposeOfTipWindow();
/*     */       }
/*     */     }
/*     */     
/*     */     private void handleComponentEvent(ComponentEvent e) {
/* 338 */       FocusableTip.this.possiblyDisposeOfTipWindow();
/*     */     }
/*     */     
/*     */     public void install(JTextArea textArea) {
/* 342 */       textArea.addCaretListener(this);
/* 343 */       textArea.addComponentListener(this);
/* 344 */       textArea.addFocusListener(this);
/* 345 */       textArea.addKeyListener(this);
/* 346 */       textArea.addMouseListener(this);
/* 347 */       textArea.addMouseMotionListener(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void keyPressed(KeyEvent e) {
/* 352 */       if (e.getKeyCode() == 27) {
/* 353 */         FocusableTip.this.possiblyDisposeOfTipWindow();
/*     */       }
/* 355 */       else if (e.getKeyCode() == 113 && 
/* 356 */         FocusableTip.this.tipWindow != null && !FocusableTip.this.tipWindow.getFocusableWindowState()) {
/* 357 */         FocusableTip.this.tipWindow.actionPerformed((ActionEvent)null);
/* 358 */         e.consume();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void keyReleased(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 378 */       if (FocusableTip.this.tipVisibleBounds == null || 
/* 379 */         !FocusableTip.this.tipVisibleBounds.contains(e.getPoint())) {
/* 380 */         FocusableTip.this.possiblyDisposeOfTipWindow();
/*     */       }
/*     */     }
/*     */     
/*     */     public void uninstall() {
/* 385 */       FocusableTip.this.textArea.removeCaretListener(this);
/* 386 */       FocusableTip.this.textArea.removeComponentListener(this);
/* 387 */       FocusableTip.this.textArea.removeFocusListener(this);
/* 388 */       FocusableTip.this.textArea.removeKeyListener(this);
/* 389 */       FocusableTip.this.textArea.removeMouseListener(this);
/* 390 */       FocusableTip.this.textArea.removeMouseMotionListener(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\focusabletip\FocusableTip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */