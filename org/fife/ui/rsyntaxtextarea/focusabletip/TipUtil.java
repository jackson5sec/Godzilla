/*     */ package org.fife.ui.rsyntaxtextarea.focusabletip;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.SystemColor;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.basic.BasicEditorPaneUI;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.HtmlUtil;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ 
/*     */ 
/*     */ public final class TipUtil
/*     */ {
/*     */   public static Rectangle getScreenBoundsForPoint(int x, int y) {
/*  56 */     GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  57 */     GraphicsDevice[] devices = env.getScreenDevices();
/*  58 */     for (GraphicsDevice device : devices) {
/*  59 */       GraphicsConfiguration[] configs = device.getConfigurations();
/*  60 */       for (GraphicsConfiguration config : configs) {
/*  61 */         Rectangle gcBounds = config.getBounds();
/*  62 */         if (gcBounds.contains(x, y)) {
/*  63 */           return gcBounds;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     return env.getMaximumWindowBounds();
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
/*     */   public static Color getToolTipBackground() {
/*  80 */     return getToolTipBackground(null);
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
/*     */   public static Color getToolTipBackground(RTextArea textArea) {
/* 107 */     if (textArea != null && !Color.WHITE.equals(textArea.getBackground())) {
/* 108 */       return textArea.getBackground();
/*     */     }
/*     */     
/* 111 */     Color c = UIManager.getColor("ToolTip.background");
/*     */ 
/*     */     
/* 114 */     boolean isNimbus = isNimbusLookAndFeel();
/* 115 */     if (c == null || isNimbus) {
/* 116 */       c = UIManager.getColor("info");
/* 117 */       if (c == null || (isNimbus && isDerivedColor(c))) {
/* 118 */         c = SystemColor.info;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (c instanceof javax.swing.plaf.ColorUIResource) {
/* 125 */       c = new Color(c.getRGB());
/*     */     }
/*     */     
/* 128 */     return c;
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
/*     */   public static Border getToolTipBorder() {
/* 141 */     return getToolTipBorder(null);
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
/*     */   public static Border getToolTipBorder(RTextArea textArea) {
/* 168 */     if (textArea != null && !Color.WHITE.equals(textArea.getBackground())) {
/* 169 */       Color color = textArea.getBackground();
/* 170 */       if (color != null) {
/* 171 */         return BorderFactory.createLineBorder(color.brighter());
/*     */       }
/*     */     } 
/*     */     
/* 175 */     Border border = UIManager.getBorder("ToolTip.border");
/*     */     
/* 177 */     if (border == null || isNimbusLookAndFeel()) {
/* 178 */       border = UIManager.getBorder("nimbusBorder");
/* 179 */       if (border == null) {
/* 180 */         border = BorderFactory.createLineBorder(SystemColor.controlDkShadow);
/*     */       }
/*     */     } 
/*     */     
/* 184 */     return border;
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
/*     */   private static boolean isDerivedColor(Color c) {
/* 198 */     return (c != null && c.getClass().getName().endsWith(".DerivedColor"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNimbusLookAndFeel() {
/* 208 */     return UIManager.getLookAndFeel().getName().equals("Nimbus");
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
/*     */   public static void tweakTipEditorPane(JEditorPane textArea) {
/* 222 */     boolean isNimbus = isNimbusLookAndFeel();
/* 223 */     if (isNimbus) {
/* 224 */       Color selBG = textArea.getSelectionColor();
/* 225 */       Color selFG = textArea.getSelectedTextColor();
/* 226 */       textArea.setUI(new BasicEditorPaneUI());
/* 227 */       textArea.setSelectedTextColor(selFG);
/* 228 */       textArea.setSelectionColor(selBG);
/*     */     } 
/*     */     
/* 231 */     textArea.setEditable(false);
/* 232 */     textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/*     */ 
/*     */     
/* 235 */     textArea.getCaret().setSelectionVisible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     Color fg = UIManager.getColor("Label.foreground");
/* 241 */     if (fg == null || (isNimbus && isDerivedColor(fg))) {
/* 242 */       fg = SystemColor.textText;
/*     */     }
/* 244 */     textArea.setForeground(fg);
/*     */ 
/*     */     
/* 247 */     textArea.setBackground(getToolTipBackground());
/*     */ 
/*     */ 
/*     */     
/* 251 */     Font font = UIManager.getFont("Label.font");
/* 252 */     if (font == null) {
/* 253 */       font = new Font("SansSerif", 0, 12);
/*     */     }
/* 255 */     HTMLDocument doc = (HTMLDocument)textArea.getDocument();
/* 256 */     setFont(doc, font, fg);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     Color linkFG = RSyntaxUtilities.getHyperlinkForeground();
/* 262 */     doc.getStyleSheet().addRule("a { color: " + 
/* 263 */         HtmlUtil.getHexString(linkFG) + "; }");
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
/*     */   public static void setFont(HTMLDocument doc, Font font, Color fg) {
/* 278 */     doc.getStyleSheet().addRule("body { font-family: " + font
/* 279 */         .getFamily() + "; font-size: " + font
/* 280 */         .getSize() + "pt; color: " + 
/* 281 */         HtmlUtil.getHexString(fg) + "; }");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\focusabletip\TipUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */