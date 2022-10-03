/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.WeakReference;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatComboBoxUI
/*     */   extends BasicComboBoxUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected int editorColumns;
/*     */   protected String buttonStyle;
/*     */   protected String arrowType;
/*     */   protected boolean isIntelliJTheme;
/*     */   protected Color borderColor;
/*     */   protected Color disabledBorderColor;
/*     */   protected Color editableBackground;
/*     */   protected Color disabledBackground;
/*     */   protected Color disabledForeground;
/*     */   protected Color buttonBackground;
/*     */   protected Color buttonEditableBackground;
/*     */   protected Color buttonArrowColor;
/*     */   protected Color buttonDisabledArrowColor;
/*     */   protected Color buttonHoverArrowColor;
/*     */   protected Color buttonPressedArrowColor;
/*     */   private MouseListener hoverListener;
/*     */   protected boolean hover;
/*     */   protected boolean pressed;
/*     */   private WeakReference<Component> lastRendererComponent;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 135 */     return new FlatComboBoxUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 140 */     super.installListeners();
/*     */     
/* 142 */     this.hoverListener = new MouseAdapter()
/*     */       {
/*     */         public void mouseEntered(MouseEvent e) {
/* 145 */           FlatComboBoxUI.this.hover = true;
/* 146 */           repaintArrowButton();
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseExited(MouseEvent e) {
/* 151 */           FlatComboBoxUI.this.hover = false;
/* 152 */           repaintArrowButton();
/*     */         }
/*     */ 
/*     */         
/*     */         public void mousePressed(MouseEvent e) {
/* 157 */           FlatComboBoxUI.this.pressed = true;
/* 158 */           repaintArrowButton();
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseReleased(MouseEvent e) {
/* 163 */           FlatComboBoxUI.this.pressed = false;
/* 164 */           repaintArrowButton();
/*     */         }
/*     */         
/*     */         private void repaintArrowButton() {
/* 168 */           if (FlatComboBoxUI.this.arrowButton != null && !FlatComboBoxUI.this.comboBox.isEditable())
/* 169 */             FlatComboBoxUI.this.arrowButton.repaint(); 
/*     */         }
/*     */       };
/* 172 */     this.comboBox.addMouseListener(this.hoverListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 177 */     super.uninstallListeners();
/*     */     
/* 179 */     this.comboBox.removeMouseListener(this.hoverListener);
/* 180 */     this.hoverListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 185 */     super.installDefaults();
/*     */     
/* 187 */     LookAndFeel.installProperty(this.comboBox, "opaque", Boolean.valueOf(false));
/*     */     
/* 189 */     this.minimumWidth = UIManager.getInt("ComboBox.minimumWidth");
/* 190 */     this.editorColumns = UIManager.getInt("ComboBox.editorColumns");
/* 191 */     this.buttonStyle = UIManager.getString("ComboBox.buttonStyle");
/* 192 */     this.arrowType = UIManager.getString("Component.arrowType");
/* 193 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/* 194 */     this.borderColor = UIManager.getColor("Component.borderColor");
/* 195 */     this.disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
/*     */     
/* 197 */     this.editableBackground = UIManager.getColor("ComboBox.editableBackground");
/* 198 */     this.disabledBackground = UIManager.getColor("ComboBox.disabledBackground");
/* 199 */     this.disabledForeground = UIManager.getColor("ComboBox.disabledForeground");
/*     */     
/* 201 */     this.buttonBackground = UIManager.getColor("ComboBox.buttonBackground");
/* 202 */     this.buttonEditableBackground = UIManager.getColor("ComboBox.buttonEditableBackground");
/* 203 */     this.buttonArrowColor = UIManager.getColor("ComboBox.buttonArrowColor");
/* 204 */     this.buttonDisabledArrowColor = UIManager.getColor("ComboBox.buttonDisabledArrowColor");
/* 205 */     this.buttonHoverArrowColor = UIManager.getColor("ComboBox.buttonHoverArrowColor");
/* 206 */     this.buttonPressedArrowColor = UIManager.getColor("ComboBox.buttonPressedArrowColor");
/*     */ 
/*     */     
/* 209 */     int maximumRowCount = UIManager.getInt("ComboBox.maximumRowCount");
/* 210 */     if (maximumRowCount > 0 && maximumRowCount != 8 && this.comboBox.getMaximumRowCount() == 8) {
/* 211 */       this.comboBox.setMaximumRowCount(maximumRowCount);
/*     */     }
/*     */     
/* 214 */     this.padding = UIScale.scale(this.padding);
/*     */     
/* 216 */     MigLayoutVisualPadding.install(this.comboBox);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 221 */     super.uninstallDefaults();
/*     */     
/* 223 */     this.borderColor = null;
/* 224 */     this.disabledBorderColor = null;
/*     */     
/* 226 */     this.editableBackground = null;
/* 227 */     this.disabledBackground = null;
/* 228 */     this.disabledForeground = null;
/*     */     
/* 230 */     this.buttonBackground = null;
/* 231 */     this.buttonEditableBackground = null;
/* 232 */     this.buttonArrowColor = null;
/* 233 */     this.buttonDisabledArrowColor = null;
/* 234 */     this.buttonHoverArrowColor = null;
/* 235 */     this.buttonPressedArrowColor = null;
/*     */     
/* 237 */     MigLayoutVisualPadding.uninstall(this.comboBox);
/*     */   }
/*     */ 
/*     */   
/*     */   protected LayoutManager createLayoutManager() {
/* 242 */     return new BasicComboBoxUI.ComboBoxLayoutManager()
/*     */       {
/*     */         public void layoutContainer(Container parent) {
/* 245 */           super.layoutContainer(parent);
/*     */           
/* 247 */           if (FlatComboBoxUI.this.editor != null && FlatComboBoxUI.this.padding != null)
/*     */           {
/* 249 */             FlatComboBoxUI.this.editor.setBounds(FlatUIUtils.subtractInsets(FlatComboBoxUI.this.editor.getBounds(), FlatComboBoxUI.this.padding));
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FocusListener createFocusListener() {
/* 258 */     return new BasicComboBoxUI.FocusHandler()
/*     */       {
/*     */         public void focusGained(FocusEvent e) {
/* 261 */           super.focusGained(e);
/* 262 */           if (FlatComboBoxUI.this.comboBox != null && FlatComboBoxUI.this.comboBox.isEditable()) {
/* 263 */             FlatComboBoxUI.this.comboBox.repaint();
/*     */           }
/*     */         }
/*     */         
/*     */         public void focusLost(FocusEvent e) {
/* 268 */           super.focusLost(e);
/* 269 */           if (FlatComboBoxUI.this.comboBox != null && FlatComboBoxUI.this.comboBox.isEditable()) {
/* 270 */             FlatComboBoxUI.this.comboBox.repaint();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 277 */     return new BasicComboBoxUI.PropertyChangeHandler()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent e) {
/* 280 */           super.propertyChange(e);
/*     */           
/* 282 */           Object source = e.getSource();
/* 283 */           String propertyName = e.getPropertyName();
/*     */           
/* 285 */           if (FlatComboBoxUI.this.editor != null && ((source == FlatComboBoxUI.this
/* 286 */             .comboBox && propertyName == "foreground") || (source == FlatComboBoxUI.this
/* 287 */             .editor && propertyName == "enabled"))) {
/*     */ 
/*     */             
/* 290 */             FlatComboBoxUI.this.updateEditorColors();
/* 291 */           } else if (FlatComboBoxUI.this.editor != null && source == FlatComboBoxUI.this.comboBox && propertyName == "componentOrientation") {
/* 292 */             ComponentOrientation o = (ComponentOrientation)e.getNewValue();
/* 293 */             FlatComboBoxUI.this.editor.applyComponentOrientation(o);
/* 294 */           } else if (FlatComboBoxUI.this.editor != null && "JTextField.placeholderText".equals(propertyName)) {
/* 295 */             FlatComboBoxUI.this.editor.repaint();
/* 296 */           } else if ("JComponent.roundRect".equals(propertyName)) {
/* 297 */             FlatComboBoxUI.this.comboBox.repaint();
/* 298 */           } else if ("JComponent.minimumWidth".equals(propertyName)) {
/* 299 */             FlatComboBoxUI.this.comboBox.revalidate();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected ComboPopup createPopup() {
/* 306 */     return new FlatComboPopup(this.comboBox);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ComboBoxEditor createEditor() {
/* 311 */     ComboBoxEditor comboBoxEditor = super.createEditor();
/*     */     
/* 313 */     Component editor = comboBoxEditor.getEditorComponent();
/* 314 */     if (editor instanceof JTextField) {
/* 315 */       JTextField textField = (JTextField)editor;
/* 316 */       textField.setColumns(this.editorColumns);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 323 */       textField.setBorder(BorderFactory.createEmptyBorder());
/*     */     } 
/*     */     
/* 326 */     return comboBoxEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureEditor() {
/* 331 */     super.configureEditor();
/*     */ 
/*     */     
/* 334 */     if (this.editor instanceof JTextField && ((JTextField)this.editor).getBorder() instanceof FlatTextBorder) {
/* 335 */       ((JTextField)this.editor).setBorder(BorderFactory.createEmptyBorder());
/*     */     }
/*     */     
/* 338 */     if (this.editor instanceof JComponent) {
/* 339 */       ((JComponent)this.editor).setOpaque(false);
/*     */     }
/* 341 */     this.editor.applyComponentOrientation(this.comboBox.getComponentOrientation());
/*     */     
/* 343 */     updateEditorColors();
/*     */ 
/*     */     
/* 346 */     if (SystemInfo.isMacOS && this.editor instanceof JTextComponent) {
/*     */ 
/*     */       
/* 349 */       InputMap inputMap = ((JTextComponent)this.editor).getInputMap();
/* 350 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("UP"));
/* 351 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("KP_UP"));
/* 352 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("DOWN"));
/* 353 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("KP_DOWN"));
/* 354 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("HOME"));
/* 355 */       new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("END"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateEditorColors() {
/* 363 */     boolean isTextComponent = this.editor instanceof JTextComponent;
/* 364 */     this.editor.setForeground(FlatUIUtils.nonUIResource(getForeground((isTextComponent || this.editor.isEnabled()))));
/*     */     
/* 366 */     if (isTextComponent) {
/* 367 */       ((JTextComponent)this.editor).setDisabledTextColor(FlatUIUtils.nonUIResource(getForeground(false)));
/*     */     }
/*     */   }
/*     */   
/*     */   protected JButton createArrowButton() {
/* 372 */     return new FlatComboBoxButton();
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 377 */     float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
/* 378 */     float arc = FlatUIUtils.getBorderArc(c);
/*     */ 
/*     */     
/* 381 */     if (c.isOpaque() && (focusWidth > 0.0F || arc > 0.0F)) {
/* 382 */       FlatUIUtils.paintParentBackground(g, c);
/*     */     }
/* 384 */     Graphics2D g2 = (Graphics2D)g;
/* 385 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g2);
/*     */     
/* 387 */     int width = c.getWidth();
/* 388 */     int height = c.getHeight();
/* 389 */     int arrowX = this.arrowButton.getX();
/* 390 */     int arrowWidth = this.arrowButton.getWidth();
/* 391 */     boolean paintButton = ((this.comboBox.isEditable() || "button".equals(this.buttonStyle)) && !"none".equals(this.buttonStyle));
/* 392 */     boolean enabled = this.comboBox.isEnabled();
/* 393 */     boolean isLeftToRight = this.comboBox.getComponentOrientation().isLeftToRight();
/*     */ 
/*     */     
/* 396 */     g2.setColor(getBackground(enabled));
/* 397 */     FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
/*     */ 
/*     */     
/* 400 */     if (enabled) {
/* 401 */       g2.setColor(paintButton ? this.buttonEditableBackground : this.buttonBackground);
/* 402 */       Shape oldClip = g2.getClip();
/* 403 */       if (isLeftToRight) {
/* 404 */         g2.clipRect(arrowX, 0, width - arrowX, height);
/*     */       } else {
/* 406 */         g2.clipRect(0, 0, arrowX + arrowWidth, height);
/* 407 */       }  FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
/* 408 */       g2.setClip(oldClip);
/*     */     } 
/*     */ 
/*     */     
/* 412 */     if (paintButton) {
/* 413 */       g2.setColor(enabled ? this.borderColor : this.disabledBorderColor);
/* 414 */       float lw = UIScale.scale(1.0F);
/* 415 */       float lx = isLeftToRight ? arrowX : ((arrowX + arrowWidth) - lw);
/* 416 */       g2.fill(new Rectangle2D.Float(lx, focusWidth, lw, (height - 1) - focusWidth * 2.0F));
/*     */     } 
/*     */ 
/*     */     
/* 420 */     FlatUIUtils.resetRenderingHints(g2, oldRenderingHints);
/*     */     
/* 422 */     paint(g, c);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/* 428 */     ListCellRenderer<Object> renderer = (ListCellRenderer)this.comboBox.getRenderer();
/* 429 */     uninstallCellPaddingBorder(renderer);
/* 430 */     if (renderer == null)
/* 431 */       renderer = new DefaultListCellRenderer(); 
/* 432 */     Component c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/* 433 */     c.setFont(this.comboBox.getFont());
/* 434 */     c.applyComponentOrientation(this.comboBox.getComponentOrientation());
/* 435 */     uninstallCellPaddingBorder(c);
/*     */     
/* 437 */     boolean enabled = this.comboBox.isEnabled();
/* 438 */     c.setBackground(getBackground(enabled));
/* 439 */     c.setForeground(getForeground(enabled));
/*     */     
/* 441 */     boolean shouldValidate = c instanceof javax.swing.JPanel;
/* 442 */     if (this.padding != null) {
/* 443 */       bounds = FlatUIUtils.subtractInsets(bounds, this.padding);
/*     */     }
/*     */ 
/*     */     
/* 447 */     Insets rendererInsets = getRendererComponentInsets(c);
/* 448 */     if (rendererInsets != null) {
/* 449 */       bounds = FlatUIUtils.addInsets(bounds, rendererInsets);
/*     */     }
/* 451 */     this.currentValuePane.paintComponent(g, c, this.comboBox, bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {}
/*     */ 
/*     */   
/*     */   protected Color getBackground(boolean enabled) {
/* 460 */     return enabled ? ((this.editableBackground != null && this.comboBox
/* 461 */       .isEditable()) ? this.editableBackground : this.comboBox.getBackground()) : (this.isIntelliJTheme ? 
/* 462 */       FlatUIUtils.getParentBackground(this.comboBox) : this.disabledBackground);
/*     */   }
/*     */   
/*     */   protected Color getForeground(boolean enabled) {
/* 466 */     return enabled ? this.comboBox.getForeground() : this.disabledForeground;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 471 */     Dimension minimumSize = super.getMinimumSize(c);
/* 472 */     minimumSize.width = Math.max(minimumSize.width, UIScale.scale(FlatUIUtils.minimumWidth(c, this.minimumWidth)));
/* 473 */     return minimumSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Dimension getDefaultSize() {
/* 479 */     ListCellRenderer<Object> renderer = (ListCellRenderer)this.comboBox.getRenderer();
/* 480 */     uninstallCellPaddingBorder(renderer);
/*     */     
/* 482 */     Dimension size = super.getDefaultSize();
/*     */     
/* 484 */     uninstallCellPaddingBorder(renderer);
/* 485 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Dimension getDisplaySize() {
/* 491 */     ListCellRenderer<Object> renderer = (ListCellRenderer)this.comboBox.getRenderer();
/* 492 */     uninstallCellPaddingBorder(renderer);
/*     */     
/* 494 */     Dimension displaySize = super.getDisplaySize();
/*     */ 
/*     */     
/* 497 */     if (displaySize.width == 100 + this.padding.left + this.padding.right && this.comboBox
/* 498 */       .isEditable() && this.comboBox
/* 499 */       .getItemCount() == 0 && this.comboBox
/* 500 */       .getPrototypeDisplayValue() == null) {
/*     */       
/* 502 */       int width = (getDefaultSize()).width;
/* 503 */       width = Math.max(width, (this.editor.getPreferredSize()).width);
/* 504 */       width += this.padding.left + this.padding.right;
/* 505 */       displaySize = new Dimension(width, displaySize.height);
/*     */     } 
/*     */     
/* 508 */     uninstallCellPaddingBorder(renderer);
/* 509 */     return displaySize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getSizeForComponent(Component comp) {
/* 514 */     Dimension size = super.getSizeForComponent(comp);
/*     */ 
/*     */ 
/*     */     
/* 518 */     Insets rendererInsets = getRendererComponentInsets(comp);
/* 519 */     if (rendererInsets != null) {
/* 520 */       size = new Dimension(size.width, size.height - rendererInsets.top - rendererInsets.bottom);
/*     */     }
/* 522 */     return size;
/*     */   }
/*     */   
/*     */   private Insets getRendererComponentInsets(Component rendererComponent) {
/* 526 */     if (rendererComponent instanceof JComponent) {
/* 527 */       Border rendererBorder = ((JComponent)rendererComponent).getBorder();
/* 528 */       if (rendererBorder != null) {
/* 529 */         return rendererBorder.getBorderInsets(rendererComponent);
/*     */       }
/*     */     } 
/* 532 */     return null;
/*     */   }
/*     */   
/*     */   private void uninstallCellPaddingBorder(Object o) {
/* 536 */     CellPaddingBorder.uninstall(o);
/* 537 */     if (this.lastRendererComponent != null) {
/* 538 */       CellPaddingBorder.uninstall(this.lastRendererComponent);
/* 539 */       this.lastRendererComponent = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatComboBoxButton
/*     */     extends FlatArrowButton
/*     */   {
/*     */     protected FlatComboBoxButton() {
/* 549 */       this(5, FlatComboBoxUI.this.arrowType, FlatComboBoxUI.this.buttonArrowColor, FlatComboBoxUI.this.buttonDisabledArrowColor, FlatComboBoxUI.this.buttonHoverArrowColor, null, FlatComboBoxUI.this.buttonPressedArrowColor, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected FlatComboBoxButton(int direction, String type, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground, Color pressedForeground, Color pressedBackground) {
/* 556 */       super(direction, type, foreground, disabledForeground, hoverForeground, hoverBackground, pressedForeground, pressedBackground);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isHover() {
/* 562 */       return (super.isHover() || (!FlatComboBoxUI.this.comboBox.isEditable() && FlatComboBoxUI.this.hover));
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isPressed() {
/* 567 */       return (super.isPressed() || (!FlatComboBoxUI.this.comboBox.isEditable() && FlatComboBoxUI.this.pressed));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatComboPopup
/*     */     extends BasicComboPopup
/*     */   {
/*     */     private FlatComboBoxUI.CellPaddingBorder paddingBorder;
/*     */ 
/*     */     
/*     */     protected FlatComboPopup(JComboBox combo) {
/* 580 */       super(combo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 586 */       ComponentOrientation o = this.comboBox.getComponentOrientation();
/* 587 */       this.list.setComponentOrientation(o);
/* 588 */       this.scroller.setComponentOrientation(o);
/* 589 */       setComponentOrientation(o);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
/* 595 */       int displayWidth = (FlatComboBoxUI.this.getDisplaySize()).width;
/*     */ 
/*     */       
/* 598 */       for (Border border : new Border[] { this.scroller.getViewportBorder(), this.scroller.getBorder() }) {
/* 599 */         if (border != null) {
/* 600 */           Insets borderInsets = border.getBorderInsets(null);
/* 601 */           displayWidth += borderInsets.left + borderInsets.right;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 606 */       JScrollBar verticalScrollBar = this.scroller.getVerticalScrollBar();
/* 607 */       if (verticalScrollBar != null) {
/* 608 */         displayWidth += (verticalScrollBar.getPreferredSize()).width;
/*     */       }
/*     */       
/* 611 */       if (displayWidth > pw) {
/*     */         
/* 613 */         GraphicsConfiguration gc = this.comboBox.getGraphicsConfiguration();
/* 614 */         if (gc != null) {
/* 615 */           Rectangle screenBounds = gc.getBounds();
/* 616 */           Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
/* 617 */           displayWidth = Math.min(displayWidth, screenBounds.width - screenInsets.left - screenInsets.right);
/*     */         } else {
/* 619 */           Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/* 620 */           displayWidth = Math.min(displayWidth, screenSize.width);
/*     */         } 
/*     */         
/* 623 */         int diff = displayWidth - pw;
/* 624 */         pw = displayWidth;
/*     */         
/* 626 */         if (!this.comboBox.getComponentOrientation().isLeftToRight()) {
/* 627 */           px -= diff;
/*     */         }
/*     */       } 
/* 630 */       return super.computePopupBounds(px, py, pw, ph);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void configurePopup() {
/* 635 */       super.configurePopup();
/*     */       
/* 637 */       Border border = UIManager.getBorder("PopupMenu.border");
/* 638 */       if (border != null) {
/* 639 */         setBorder(border);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void configureList() {
/* 644 */       super.configureList();
/*     */       
/* 646 */       this.list.setCellRenderer(new PopupListCellRenderer());
/*     */     }
/*     */ 
/*     */     
/*     */     protected PropertyChangeListener createPropertyChangeListener() {
/* 651 */       return new BasicComboPopup.PropertyChangeHandler()
/*     */         {
/*     */           public void propertyChange(PropertyChangeEvent e) {
/* 654 */             super.propertyChange(e);
/*     */             
/* 656 */             if (e.getPropertyName() == "renderer") {
/* 657 */               FlatComboBoxUI.FlatComboPopup.this.list.setCellRenderer(new FlatComboBoxUI.FlatComboPopup.PopupListCellRenderer());
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     private class PopupListCellRenderer
/*     */       implements ListCellRenderer
/*     */     {
/*     */       private PopupListCellRenderer() {}
/*     */ 
/*     */       
/*     */       public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 671 */         ListCellRenderer<Object> renderer = FlatComboBoxUI.FlatComboPopup.this.comboBox.getRenderer();
/* 672 */         FlatComboBoxUI.CellPaddingBorder.uninstall(renderer);
/* 673 */         FlatComboBoxUI.CellPaddingBorder.uninstall(FlatComboBoxUI.this.lastRendererComponent);
/*     */         
/* 675 */         if (renderer == null)
/* 676 */           renderer = new DefaultListCellRenderer(); 
/* 677 */         Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 678 */         c.applyComponentOrientation(FlatComboBoxUI.FlatComboPopup.this.comboBox.getComponentOrientation());
/*     */         
/* 680 */         if (c instanceof JComponent) {
/* 681 */           if (FlatComboBoxUI.FlatComboPopup.this.paddingBorder == null)
/* 682 */             FlatComboBoxUI.FlatComboPopup.this.paddingBorder = new FlatComboBoxUI.CellPaddingBorder(FlatComboBoxUI.this.padding); 
/* 683 */           FlatComboBoxUI.FlatComboPopup.this.paddingBorder.install((JComponent)c);
/*     */         } 
/*     */         
/* 686 */         FlatComboBoxUI.this.lastRendererComponent = (c != renderer) ? new WeakReference<>(c) : null;
/*     */         
/* 688 */         return c;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CellPaddingBorder
/*     */     extends AbstractBorder
/*     */   {
/*     */     private final Insets padding;
/*     */ 
/*     */ 
/*     */     
/*     */     private Border rendererBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CellPaddingBorder(Insets padding) {
/* 710 */       this.padding = padding;
/*     */     }
/*     */     
/*     */     void install(JComponent rendererComponent) {
/* 714 */       Border oldBorder = rendererComponent.getBorder();
/* 715 */       if (!(oldBorder instanceof CellPaddingBorder)) {
/* 716 */         this.rendererBorder = oldBorder;
/* 717 */         rendererComponent.setBorder(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     static void uninstall(Object o) {
/* 722 */       if (o instanceof WeakReference) {
/* 723 */         o = ((WeakReference)o).get();
/*     */       }
/* 725 */       if (!(o instanceof JComponent)) {
/*     */         return;
/*     */       }
/* 728 */       JComponent rendererComponent = (JComponent)o;
/* 729 */       Border border = rendererComponent.getBorder();
/* 730 */       if (border instanceof CellPaddingBorder) {
/* 731 */         CellPaddingBorder paddingBorder = (CellPaddingBorder)border;
/* 732 */         rendererComponent.setBorder(paddingBorder.rendererBorder);
/* 733 */         paddingBorder.rendererBorder = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 739 */       if (this.rendererBorder != null) {
/* 740 */         Insets insideInsets = this.rendererBorder.getBorderInsets(c);
/* 741 */         insets.top = Math.max(this.padding.top, insideInsets.top);
/* 742 */         insets.left = Math.max(this.padding.left, insideInsets.left);
/* 743 */         insets.bottom = Math.max(this.padding.bottom, insideInsets.bottom);
/* 744 */         insets.right = Math.max(this.padding.right, insideInsets.right);
/*     */       } else {
/* 746 */         insets.top = this.padding.top;
/* 747 */         insets.left = this.padding.left;
/* 748 */         insets.bottom = this.padding.bottom;
/* 749 */         insets.right = this.padding.right;
/*     */       } 
/* 751 */       return insets;
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 756 */       if (this.rendererBorder != null) {
/* 757 */         this.rendererBorder.paintBorder(c, g, x, y, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EditorDelegateAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final KeyStroke keyStroke;
/*     */ 
/*     */ 
/*     */     
/*     */     EditorDelegateAction(InputMap inputMap, KeyStroke keyStroke) {
/* 772 */       this.keyStroke = keyStroke;
/*     */ 
/*     */       
/* 775 */       inputMap.put(keyStroke, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 780 */       ActionListener action = FlatComboBoxUI.this.comboBox.getActionForKeyStroke(this.keyStroke);
/* 781 */       if (action != null)
/* 782 */         action.actionPerformed(new ActionEvent(FlatComboBoxUI.this.comboBox, e.getID(), e
/* 783 */               .getActionCommand(), e.getWhen(), e.getModifiers())); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatComboBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */