/*     */ package org.fife.ui.rtextarea;
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.InputMapUIResource;
/*     */ import javax.swing.plaf.basic.BasicBorders;
/*     */ import javax.swing.plaf.basic.BasicTextAreaUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Keymap;
/*     */ import javax.swing.text.PlainView;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.WrappedPlainView;
/*     */ 
/*     */ public class RTextAreaUI extends BasicTextAreaUI {
/*     */   private static final String SHARED_ACTION_MAP_NAME = "RTextAreaUI.actionMap";
/*  37 */   private static final EditorKit DEFAULT_KIT = new RTextAreaEditorKit(); private static final String SHARED_INPUT_MAP_NAME = "RTextAreaUI.inputMap"; protected RTextArea textArea;
/*  38 */   private static final TransferHandler DEFAULT_TRANSFER_HANDLER = new RTATextTransferHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String RTEXTAREA_KEYMAP_NAME = "RTextAreaKeymap";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ComponentUI createUI(JComponent textArea) {
/*  51 */     return new RTextAreaUI(textArea);
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
/*     */   public RTextAreaUI(JComponent textArea) {
/*  63 */     if (!(textArea instanceof RTextArea)) {
/*  64 */       throw new IllegalArgumentException("RTextAreaUI is for instances of RTextArea only!");
/*     */     }
/*     */     
/*  67 */     this.textArea = (RTextArea)textArea;
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
/*     */   private void correctNimbusDefaultProblems(JTextComponent editor) {
/*  91 */     Color c = editor.getCaretColor();
/*  92 */     if (c == null) {
/*  93 */       editor.setCaretColor(RTextArea.getDefaultCaretColor());
/*     */     }
/*     */     
/*  96 */     c = editor.getSelectionColor();
/*  97 */     if (c == null) {
/*  98 */       c = UIManager.getColor("nimbusSelectionBackground");
/*  99 */       if (c == null) {
/* 100 */         c = UIManager.getColor("textHighlight");
/* 101 */         if (c == null) {
/* 102 */           c = new ColorUIResource(Color.BLUE);
/*     */         }
/*     */       } 
/* 105 */       editor.setSelectionColor(c);
/*     */     } 
/*     */     
/* 108 */     c = editor.getSelectedTextColor();
/* 109 */     if (c == null) {
/* 110 */       c = UIManager.getColor("nimbusSelectedText");
/* 111 */       if (c == null) {
/* 112 */         c = UIManager.getColor("textHighlightText");
/* 113 */         if (c == null) {
/* 114 */           c = new ColorUIResource(Color.WHITE);
/*     */         }
/*     */       } 
/* 117 */       editor.setSelectedTextColor(c);
/*     */     } 
/*     */     
/* 120 */     c = editor.getDisabledTextColor();
/* 121 */     if (c == null) {
/* 122 */       c = UIManager.getColor("nimbusDisabledText");
/* 123 */       if (c == null) {
/* 124 */         c = UIManager.getColor("textInactiveText");
/* 125 */         if (c == null) {
/* 126 */           c = new ColorUIResource(Color.DARK_GRAY);
/*     */         }
/*     */       } 
/* 129 */       editor.setDisabledTextColor(c);
/*     */     } 
/*     */     
/* 132 */     Border border = editor.getBorder();
/* 133 */     if (border == null) {
/* 134 */       editor.setBorder(new BasicBorders.MarginBorder());
/*     */     }
/*     */     
/* 137 */     Insets margin = editor.getMargin();
/* 138 */     if (margin == null) {
/* 139 */       editor.setMargin(new InsetsUIResource(2, 2, 2, 2));
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
/*     */   public View create(Element elem) {
/* 154 */     if (this.textArea.getLineWrap()) {
/* 155 */       return new WrappedPlainView(elem, this.textArea.getWrapStyleWord());
/*     */     }
/*     */     
/* 158 */     return new PlainView(elem);
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
/*     */   protected Caret createCaret() {
/* 171 */     Caret caret = new ConfigurableCaret();
/* 172 */     caret.setBlinkRate(500);
/* 173 */     return caret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Highlighter createHighlighter() {
/* 179 */     return new RTextAreaHighlighter();
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
/*     */   protected Keymap createKeymap() {
/* 196 */     Keymap map = JTextComponent.getKeymap("RTextAreaKeymap");
/* 197 */     if (map == null) {
/* 198 */       Keymap parent = JTextComponent.getKeymap("default");
/* 199 */       map = JTextComponent.addKeymap("RTextAreaKeymap", parent);
/* 200 */       map.setDefaultAction(new RTextAreaEditorKit.DefaultKeyTypedAction());
/*     */     } 
/*     */     
/* 203 */     return map;
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
/*     */   protected ActionMap createRTextAreaActionMap() {
/* 223 */     ActionMap map = new ActionMapUIResource();
/* 224 */     Action[] actions = this.textArea.getActions();
/* 225 */     int n = actions.length;
/* 226 */     for (Action a : actions) {
/* 227 */       map.put(a.getValue("Name"), a);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 232 */     map.put(TransferHandler.getCutAction().getValue("Name"), 
/* 233 */         TransferHandler.getCutAction());
/* 234 */     map.put(TransferHandler.getCopyAction().getValue("Name"), 
/* 235 */         TransferHandler.getCopyAction());
/* 236 */     map.put(TransferHandler.getPasteAction().getValue("Name"), 
/* 237 */         TransferHandler.getPasteAction());
/*     */     
/* 239 */     return map;
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
/*     */   protected String getActionMapName() {
/* 252 */     return "RTextAreaUI.actionMap";
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
/*     */   public EditorKit getEditorKit(JTextComponent tc) {
/* 265 */     return DEFAULT_KIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RTextArea getRTextArea() {
/* 275 */     return this.textArea;
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
/*     */   private ActionMap getRTextAreaActionMap() {
/* 295 */     ActionMap map = (ActionMap)UIManager.get(getActionMapName());
/* 296 */     if (map == null) {
/* 297 */       map = createRTextAreaActionMap();
/* 298 */       UIManager.put(getActionMapName(), map);
/*     */     } 
/*     */     
/* 301 */     ActionMap componentMap = new ActionMapUIResource();
/* 302 */     componentMap.put("requestFocus", new FocusAction());
/*     */     
/* 304 */     if (map != null) {
/* 305 */       componentMap.setParent(map);
/*     */     }
/* 307 */     return componentMap;
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
/*     */   protected InputMap getRTextAreaInputMap() {
/* 322 */     InputMap map = new InputMapUIResource();
/* 323 */     InputMap shared = (InputMap)UIManager.get("RTextAreaUI.inputMap");
/* 324 */     if (shared == null) {
/* 325 */       shared = new RTADefaultInputMap();
/* 326 */       UIManager.put("RTextAreaUI.inputMap", shared);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 331 */     map.setParent(shared);
/* 332 */     return map;
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
/*     */   protected Rectangle getVisibleEditorRect() {
/* 348 */     Rectangle alloc = this.textArea.getBounds();
/* 349 */     if (alloc.width > 0 && alloc.height > 0) {
/* 350 */       alloc.x = alloc.y = 0;
/* 351 */       Insets insets = this.textArea.getInsets();
/* 352 */       alloc.x += insets.left;
/* 353 */       alloc.y += insets.top;
/* 354 */       alloc.width -= insets.left + insets.right;
/* 355 */       alloc.height -= insets.top + insets.bottom;
/* 356 */       return alloc;
/*     */     } 
/* 358 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 365 */     super.installDefaults();
/*     */     
/* 367 */     JTextComponent editor = getComponent();
/* 368 */     editor.setFont(RTextAreaBase.getDefaultFont());
/*     */ 
/*     */ 
/*     */     
/* 372 */     correctNimbusDefaultProblems(editor);
/*     */     
/* 374 */     editor.setTransferHandler(DEFAULT_TRANSFER_HANDLER);
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
/*     */   protected void installKeyboardActions() {
/* 392 */     RTextArea textArea = getRTextArea();
/*     */ 
/*     */ 
/*     */     
/* 396 */     textArea.setKeymap(createKeymap());
/*     */ 
/*     */ 
/*     */     
/* 400 */     InputMap map = getRTextAreaInputMap();
/* 401 */     SwingUtilities.replaceUIInputMap(textArea, 0, map);
/*     */ 
/*     */     
/* 404 */     ActionMap am = getRTextAreaActionMap();
/* 405 */     if (am != null) {
/* 406 */       SwingUtilities.replaceUIActionMap(textArea, am);
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
/*     */   public void installUI(JComponent c) {
/* 418 */     if (!(c instanceof RTextArea)) {
/* 419 */       throw new Error("RTextAreaUI needs an instance of RTextArea!");
/*     */     }
/* 421 */     super.installUI(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {
/* 429 */     Color bg = this.textArea.getBackground();
/* 430 */     if (bg != null) {
/* 431 */       g.setColor(bg);
/*     */       
/* 433 */       Rectangle r = g.getClipBounds();
/* 434 */       g.fillRect(r.x, r.y, r.width, r.height);
/*     */     } 
/*     */     
/* 437 */     paintEditorAugmentations(g);
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
/*     */   protected void paintCurrentLineHighlight(Graphics g, Rectangle visibleRect) {
/* 450 */     if (this.textArea.getHighlightCurrentLine()) {
/*     */       
/* 452 */       Caret caret = this.textArea.getCaret();
/* 453 */       if (caret.getDot() == caret.getMark()) {
/*     */         
/* 455 */         Color highlight = this.textArea.getCurrentLineHighlightColor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 463 */         int height = this.textArea.getLineHeight();
/*     */         
/* 465 */         if (this.textArea.getFadeCurrentLineHighlight()) {
/* 466 */           Graphics2D g2d = (Graphics2D)g;
/* 467 */           Color bg = this.textArea.getBackground();
/* 468 */           GradientPaint paint = new GradientPaint(visibleRect.x, 0.0F, highlight, (visibleRect.x + visibleRect.width), 0.0F, (bg == null) ? Color.WHITE : bg);
/*     */ 
/*     */ 
/*     */           
/* 472 */           g2d.setPaint(paint);
/* 473 */           g2d.fillRect(visibleRect.x, this.textArea.currentCaretY, visibleRect.width, height);
/*     */         }
/*     */         else {
/*     */           
/* 477 */           g.setColor(highlight);
/* 478 */           g.fillRect(visibleRect.x, this.textArea.currentCaretY, visibleRect.width, height);
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
/*     */   protected void paintEditorAugmentations(Graphics g) {
/* 496 */     Rectangle visibleRect = this.textArea.getVisibleRect();
/* 497 */     paintLineHighlights(g);
/* 498 */     paintCurrentLineHighlight(g, visibleRect);
/* 499 */     paintMarginLine(g, visibleRect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintLineHighlights(Graphics g) {
/* 509 */     LineHighlightManager lhm = this.textArea.getLineHighlightManager();
/* 510 */     if (lhm != null) {
/* 511 */       lhm.paintLineHighlights(g);
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
/*     */   protected void paintMarginLine(Graphics g, Rectangle visibleRect) {
/* 523 */     if (this.textArea.isMarginLineEnabled()) {
/* 524 */       g.setColor(this.textArea.getMarginLineColor());
/* 525 */       Insets insets = this.textArea.getInsets();
/* 526 */       int marginLineX = this.textArea.getMarginLinePixelLocation() + ((insets == null) ? 0 : insets.left);
/*     */       
/* 528 */       g.drawLine(marginLineX, visibleRect.y, marginLineX, visibleRect.y + visibleRect.height);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 538 */     if (!this.textArea.isOpaque()) {
/* 539 */       paintEditorAugmentations(g);
/*     */     }
/* 541 */     super.paintSafely(g);
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
/*     */   public int yForLine(int line) throws BadLocationException {
/* 567 */     int startOffs = this.textArea.getLineStartOffset(line);
/* 568 */     return yForLineContaining(startOffs);
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
/*     */   public int yForLineContaining(int offs) throws BadLocationException {
/* 595 */     Rectangle r = modelToView(this.textArea, offs);
/* 596 */     return (r != null) ? r.y : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class FocusAction
/*     */     extends AbstractAction
/*     */   {
/*     */     public void actionPerformed(ActionEvent e) {
/* 607 */       RTextAreaUI.this.textArea.requestFocus();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled() {
/* 612 */       return RTextAreaUI.this.textArea.isEditable();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextAreaUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */