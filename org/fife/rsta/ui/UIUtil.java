/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.Spring;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
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
/*     */ public final class UIUtil
/*     */ {
/*     */   private static boolean desktopCreationAttempted;
/*     */   private static Object desktop;
/*  42 */   private static final Object LOCK_DESKTOP_CREATION = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final Border EMPTY_5_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean browse(String uri) {
/*  68 */     if (uri == null) {
/*  69 */       return false;
/*     */     }
/*     */     try {
/*  72 */       return browse(new URI(uri));
/*  73 */     } catch (URISyntaxException e) {
/*  74 */       return false;
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
/*     */   public static boolean browse(URI uri) {
/*  90 */     boolean success = false;
/*     */     
/*  92 */     if (uri != null) {
/*  93 */       Object desktop = getDesktop();
/*  94 */       if (desktop != null) {
/*     */         try {
/*  96 */           Method m = desktop.getClass().getDeclaredMethod("browse", new Class[] { URI.class });
/*     */           
/*  98 */           m.invoke(desktop, new Object[] { uri });
/*  99 */           success = true;
/* 100 */         } catch (RuntimeException re) {
/* 101 */           throw re;
/* 102 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 108 */     return success;
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
/*     */   public static void fixComboOrientation(JComboBox<?> combo) {
/* 120 */     ListCellRenderer<?> r = combo.getRenderer();
/* 121 */     if (r instanceof Component) {
/*     */       
/* 123 */       ComponentOrientation o = ComponentOrientation.getOrientation(Locale.getDefault());
/* 124 */       ((Component)r).setComponentOrientation(o);
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
/*     */   private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
/* 141 */     SpringLayout layout = (SpringLayout)parent.getLayout();
/* 142 */     Component c = parent.getComponent(row * cols + col);
/* 143 */     return layout.getConstraints(c);
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
/*     */   private static Object getDesktop() {
/* 156 */     synchronized (LOCK_DESKTOP_CREATION) {
/*     */       
/* 158 */       if (!desktopCreationAttempted) {
/*     */         
/* 160 */         desktopCreationAttempted = true;
/*     */         
/*     */         try {
/* 163 */           Class<?> desktopClazz = Class.forName("java.awt.Desktop");
/*     */           
/* 165 */           Method m = desktopClazz.getDeclaredMethod("isDesktopSupported", new Class[0]);
/*     */           
/* 167 */           boolean supported = ((Boolean)m.invoke(null, new Object[0])).booleanValue();
/* 168 */           if (supported) {
/* 169 */             m = desktopClazz.getDeclaredMethod("getDesktop", new Class[0]);
/* 170 */             desktop = m.invoke(null, new Object[0]);
/*     */           }
/*     */         
/* 173 */         } catch (RuntimeException re) {
/* 174 */           throw re;
/* 175 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     return desktop;
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
/*     */   public static Border getEmpty5Border() {
/* 195 */     return EMPTY_5_BORDER;
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
/*     */   public static Color getErrorTextForeground() {
/* 207 */     Color defaultFG = UIManager.getColor("TextField.foreground");
/* 208 */     if (defaultFG.getRed() >= 160 && defaultFG.getGreen() >= 160 && defaultFG
/* 209 */       .getBlue() >= 160) {
/* 210 */       return new Color(255, 160, 160);
/*     */     }
/* 212 */     return Color.red;
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
/*     */   public static int getMnemonic(ResourceBundle msg, String key) {
/* 224 */     int mnemonic = 0;
/*     */     try {
/* 226 */       Object value = msg.getObject(key);
/* 227 */       if (value instanceof String) {
/* 228 */         mnemonic = ((String)value).charAt(0);
/*     */       }
/* 230 */     } catch (MissingResourceException missingResourceException) {}
/*     */ 
/*     */     
/* 233 */     return mnemonic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JTextComponent getTextComponent(JComboBox<?> combo) {
/* 244 */     return (JTextComponent)combo.getEditor().getEditorComponent();
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
/*     */   public static void makeSpringCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
/*     */     SpringLayout layout;
/*     */     try {
/* 273 */       layout = (SpringLayout)parent.getLayout();
/* 274 */     } catch (ClassCastException cce) {
/* 275 */       System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 281 */     Spring x = Spring.constant(initialX);
/* 282 */     for (int c = 0; c < cols; c++) {
/* 283 */       Spring width = Spring.constant(0); int i;
/* 284 */       for (i = 0; i < rows; i++) {
/* 285 */         width = Spring.max(width, 
/* 286 */             getConstraintsForCell(i, c, parent, cols)
/* 287 */             .getWidth());
/*     */       }
/* 289 */       for (i = 0; i < rows; i++) {
/*     */         
/* 291 */         SpringLayout.Constraints constraints = getConstraintsForCell(i, c, parent, cols);
/* 292 */         constraints.setX(x);
/* 293 */         constraints.setWidth(width);
/*     */       } 
/* 295 */       x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
/*     */     } 
/*     */ 
/*     */     
/* 299 */     Spring y = Spring.constant(initialY);
/* 300 */     for (int r = 0; r < rows; r++) {
/* 301 */       Spring height = Spring.constant(0); int i;
/* 302 */       for (i = 0; i < cols; i++) {
/* 303 */         height = Spring.max(height, 
/* 304 */             getConstraintsForCell(r, i, parent, cols).getHeight());
/*     */       }
/* 306 */       for (i = 0; i < cols; i++) {
/*     */         
/* 308 */         SpringLayout.Constraints constraints = getConstraintsForCell(r, i, parent, cols);
/* 309 */         constraints.setY(y);
/* 310 */         constraints.setHeight(height);
/*     */       } 
/* 312 */       y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
/*     */     } 
/*     */ 
/*     */     
/* 316 */     SpringLayout.Constraints pCons = layout.getConstraints(parent);
/* 317 */     pCons.setConstraint("South", y);
/* 318 */     pCons.setConstraint("East", x);
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
/*     */   public static JButton newButton(ResourceBundle bundle, String key) {
/* 333 */     JButton b = new JButton(bundle.getString(key));
/* 334 */     b.setMnemonic(getMnemonic(bundle, key + ".Mnemonic"));
/* 335 */     return b;
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
/*     */   public static JLabel newLabel(ResourceBundle msg, String key, Component labelFor) {
/* 351 */     JLabel label = new JLabel(msg.getString(key));
/* 352 */     String mnemonicKey = key + ".Mnemonic";
/* 353 */     label.setDisplayedMnemonic(getMnemonic(msg, mnemonicKey));
/* 354 */     if (labelFor != null) {
/* 355 */       label.setLabelFor(labelFor);
/*     */     }
/* 357 */     return label;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\UIUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */