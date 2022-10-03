/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompletionCellRenderer
/*     */   extends DefaultListCellRenderer
/*     */ {
/*     */   private static Color altBG;
/*     */   private Font font;
/*     */   private boolean showTypes;
/*     */   private String typeColor;
/*     */   private boolean selected;
/*     */   private Color realBG;
/*     */   private String paramColor;
/*     */   private Icon emptyIcon;
/*     */   private Rectangle paintTextR;
/*     */   private DefaultListCellRenderer delegate;
/*     */   private static final String SUBSTANCE_RENDERER_CLASS_NAME = "org.pushingpixels.substance.api.renderer.SubstanceDefaultListCellRenderer";
/*     */   private static final String PREFIX = "<html><nobr>";
/*     */   
/*     */   public CompletionCellRenderer() {
/* 116 */     init();
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
/*     */   public CompletionCellRenderer(DefaultListCellRenderer delegate) {
/* 131 */     setDelegateRenderer(delegate);
/* 132 */     init();
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
/*     */   protected Icon createEmptyIcon() {
/* 144 */     return new EmptyIcon(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createParamColor() {
/* 155 */     return Util.isLightForeground(getForeground()) ? 
/* 156 */       Util.getHexString(Util.getHyperlinkForeground()) : "#aa0077";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createTypeColor() {
/* 167 */     return "#808080";
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
/*     */   public void delegateToSubstanceRenderer() throws Exception {
/* 181 */     Class<?> clazz = Class.forName("org.pushingpixels.substance.api.renderer.SubstanceDefaultListCellRenderer");
/*     */     
/* 183 */     DefaultListCellRenderer delegate = (DefaultListCellRenderer)clazz.newInstance();
/* 184 */     setDelegateRenderer(delegate);
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
/*     */   public static Color getAlternateBackground() {
/* 196 */     return altBG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultListCellRenderer getDelegateRenderer() {
/* 207 */     return this.delegate;
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
/*     */   public Font getDisplayFont() {
/* 219 */     return this.font;
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
/*     */   protected Icon getEmptyIcon() {
/* 231 */     if (this.emptyIcon == null) {
/* 232 */       this.emptyIcon = createEmptyIcon();
/*     */     }
/* 234 */     return this.emptyIcon;
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
/*     */   protected Icon getIcon(String resource) {
/* 246 */     URL url = getClass().getResource(resource);
/* 247 */     if (url == null) {
/* 248 */       File file = new File(resource);
/*     */       try {
/* 250 */         url = file.toURI().toURL();
/* 251 */       } catch (MalformedURLException mue) {
/* 252 */         mue.printStackTrace();
/*     */       } 
/*     */     } 
/* 255 */     return (url != null) ? new ImageIcon(url) : null;
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
/*     */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean hasFocus) {
/* 272 */     super.getListCellRendererComponent(list, value, index, selected, hasFocus);
/* 273 */     if (this.font != null) {
/* 274 */       setFont(this.font);
/*     */     }
/* 276 */     this.selected = selected;
/* 277 */     this.realBG = (altBG != null && (index & 0x1) == 1) ? altBG : list.getBackground();
/*     */     
/* 279 */     Completion c = (Completion)value;
/* 280 */     setIcon(c.getIcon());
/*     */     
/* 282 */     if (c instanceof FunctionCompletion) {
/* 283 */       FunctionCompletion fc = (FunctionCompletion)value;
/* 284 */       prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
/*     */     }
/* 286 */     else if (c instanceof VariableCompletion) {
/* 287 */       VariableCompletion vc = (VariableCompletion)value;
/* 288 */       prepareForVariableCompletion(list, vc, index, selected, hasFocus);
/*     */     }
/* 290 */     else if (c instanceof TemplateCompletion) {
/* 291 */       TemplateCompletion tc = (TemplateCompletion)value;
/* 292 */       prepareForTemplateCompletion(list, tc, index, selected, hasFocus);
/*     */     }
/* 294 */     else if (c instanceof MarkupTagCompletion) {
/* 295 */       MarkupTagCompletion mtc = (MarkupTagCompletion)value;
/* 296 */       prepareForMarkupTagCompletion(list, mtc, index, selected, hasFocus);
/*     */     } else {
/*     */       
/* 299 */       prepareForOtherCompletion(list, c, index, selected, hasFocus);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 304 */     if (this.delegate != null) {
/* 305 */       this.delegate.getListCellRendererComponent(list, getText(), index, selected, hasFocus);
/*     */       
/* 307 */       this.delegate.setFont(getFont());
/* 308 */       this.delegate.setIcon(getIcon());
/* 309 */       return this.delegate;
/*     */     } 
/*     */     
/* 312 */     if (!selected && (index & 0x1) == 1 && altBG != null) {
/* 313 */       setBackground(altBG);
/*     */     }
/*     */     
/* 316 */     return this;
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
/*     */   public boolean getShowTypes() {
/* 329 */     return this.showTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 335 */     setShowTypes(true);
/* 336 */     this.typeColor = createTypeColor();
/* 337 */     this.paramColor = createParamColor();
/* 338 */     this.paintTextR = new Rectangle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 347 */     g.setColor(this.realBG);
/* 348 */     int iconW = 0;
/* 349 */     if (getIcon() != null) {
/* 350 */       iconW = getIcon().getIconWidth();
/*     */     }
/* 352 */     if (this.selected && iconW > 0) {
/* 353 */       g.fillRect(0, 0, iconW, getHeight());
/* 354 */       g.setColor(getBackground());
/* 355 */       g.fillRect(iconW, 0, getWidth() - iconW, getHeight());
/*     */     } else {
/*     */       
/* 358 */       g.setColor(getBackground());
/* 359 */       g.fillRect(0, 0, getWidth(), getHeight());
/*     */     } 
/* 361 */     if (getIcon() != null) {
/* 362 */       Icon icon = getIcon();
/* 363 */       icon.paintIcon(this, g, 0, (getHeight() - icon.getIconHeight()) / 2);
/*     */     } 
/*     */     
/* 366 */     String text = getText();
/* 367 */     if (text != null) {
/* 368 */       this.paintTextR.setBounds(iconW, 0, getWidth() - iconW, getHeight());
/* 369 */       this.paintTextR.x += 3;
/* 370 */       int space = this.paintTextR.height - g.getFontMetrics().getHeight();
/* 371 */       View v = (View)getClientProperty("html");
/* 372 */       if (v != null) {
/*     */ 
/*     */         
/* 375 */         this.paintTextR.y += space / 2;
/* 376 */         this.paintTextR.height -= space;
/* 377 */         v.paint(g, this.paintTextR);
/*     */       } else {
/*     */         
/* 380 */         int textX = this.paintTextR.x;
/* 381 */         int textY = this.paintTextR.y;
/*     */         
/* 383 */         g.drawString(text, textX, textY);
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
/*     */   
/*     */   protected void prepareForFunctionCompletion(JList list, FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
/* 402 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 403 */     sb.append(fc.getName());
/*     */     
/* 405 */     char paramListStart = fc.getProvider().getParameterListStart();
/* 406 */     if (paramListStart != '\000') {
/* 407 */       sb.append(paramListStart);
/*     */     }
/*     */     
/* 410 */     int paramCount = fc.getParamCount();
/* 411 */     for (int i = 0; i < paramCount; i++) {
/* 412 */       ParameterizedCompletion.Parameter param = fc.getParam(i);
/* 413 */       String type = param.getType();
/* 414 */       String name = param.getName();
/* 415 */       if (type != null) {
/* 416 */         if (!selected) {
/* 417 */           sb.append("<font color='").append(this.paramColor).append("'>");
/*     */         }
/* 419 */         sb.append(type);
/* 420 */         if (!selected) {
/* 421 */           sb.append("</font>");
/*     */         }
/* 423 */         if (name != null) {
/* 424 */           sb.append(' ');
/*     */         }
/*     */       } 
/* 427 */       if (name != null) {
/* 428 */         sb.append(name);
/*     */       }
/* 430 */       if (i < paramCount - 1) {
/* 431 */         sb.append(fc.getProvider().getParameterListSeparator());
/*     */       }
/*     */     } 
/*     */     
/* 435 */     char paramListEnd = fc.getProvider().getParameterListEnd();
/* 436 */     if (paramListEnd != '\000') {
/* 437 */       sb.append(paramListEnd);
/*     */     }
/*     */     
/* 440 */     if (getShowTypes() && fc.getType() != null) {
/* 441 */       sb.append(" : ");
/* 442 */       if (!selected) {
/* 443 */         sb.append("<font color='").append(this.typeColor).append("'>");
/*     */       }
/* 445 */       sb.append(fc.getType());
/* 446 */       if (!selected) {
/* 447 */         sb.append("</font>");
/*     */       }
/*     */     } 
/*     */     
/* 451 */     setText(sb.toString());
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
/*     */   protected void prepareForMarkupTagCompletion(JList list, MarkupTagCompletion mc, int index, boolean selected, boolean hasFocus) {
/* 468 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 469 */     sb.append(mc.getName());
/*     */     
/* 471 */     setText(sb.toString());
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
/*     */   protected void prepareForOtherCompletion(JList list, Completion c, int index, boolean selected, boolean hasFocus) {
/* 489 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 490 */     sb.append(c.getInputText());
/*     */     
/* 492 */     if (c instanceof BasicCompletion) {
/* 493 */       String definition = ((BasicCompletion)c).getShortDescription();
/* 494 */       if (definition != null) {
/* 495 */         sb.append(" - ");
/* 496 */         if (!selected) {
/* 497 */           sb.append("<font color='").append(this.typeColor).append("'>");
/*     */         }
/* 499 */         sb.append(definition);
/* 500 */         if (!selected) {
/* 501 */           sb.append("</font>");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 506 */     setText(sb.toString());
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
/*     */   protected void prepareForTemplateCompletion(JList list, TemplateCompletion tc, int index, boolean selected, boolean hasFocus) {
/* 523 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 524 */     sb.append(tc.getInputText());
/*     */     
/* 526 */     String definition = tc.getShortDescription();
/* 527 */     if (definition != null) {
/* 528 */       sb.append(" - ");
/* 529 */       if (!selected) {
/* 530 */         sb.append("<font color='").append(this.typeColor).append("'>");
/*     */       }
/* 532 */       sb.append(definition);
/* 533 */       if (!selected) {
/* 534 */         sb.append("</font>");
/*     */       }
/*     */     } 
/*     */     
/* 538 */     setText(sb.toString());
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
/*     */   protected void prepareForVariableCompletion(JList list, VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
/* 555 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 556 */     sb.append(vc.getName());
/*     */     
/* 558 */     if (getShowTypes() && vc.getType() != null) {
/* 559 */       sb.append(" : ");
/* 560 */       if (!selected) {
/* 561 */         sb.append("<font color='").append(this.typeColor).append("'>");
/*     */       }
/* 563 */       sb.append(vc.getType());
/* 564 */       if (!selected) {
/* 565 */         sb.append("</font>");
/*     */       }
/*     */     } 
/*     */     
/* 569 */     setText(sb.toString());
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
/*     */   public static void setAlternateBackground(Color altBG) {
/* 583 */     CompletionCellRenderer.altBG = altBG;
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
/*     */   public void setDelegateRenderer(DefaultListCellRenderer delegate) {
/* 599 */     this.delegate = delegate;
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
/*     */   public void setDisplayFont(Font font) {
/* 611 */     this.font = font;
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
/*     */   protected void setIconWithDefault(Completion completion) {
/* 623 */     setIconWithDefault(completion, getEmptyIcon());
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
/*     */   protected void setIconWithDefault(Completion completion, Icon defaultIcon) {
/* 637 */     Icon icon = completion.getIcon();
/* 638 */     setIcon((icon != null) ? icon : ((defaultIcon != null) ? defaultIcon : this.emptyIcon));
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
/*     */   public void setParamColor(Color color) {
/* 650 */     if (color != null) {
/* 651 */       this.paramColor = Util.getHexString(color);
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
/*     */   public void setShowTypes(boolean show) {
/* 664 */     this.showTypes = show;
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
/*     */   public void setTypeColor(Color color) {
/* 679 */     if (color != null) {
/* 680 */       this.typeColor = Util.getHexString(color);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 690 */     super.updateUI();
/* 691 */     if (this.delegate != null) {
/* 692 */       SwingUtilities.updateComponentTreeUI(this.delegate);
/*     */     }
/* 694 */     this.paramColor = createParamColor();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\CompletionCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */