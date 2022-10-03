/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.SystemColor;
/*     */ import java.net.URL;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.basic.BasicEditorPaneUI;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TipUtil
/*     */ {
/*     */   public static Color getToolTipBackground() {
/*  45 */     Color c = UIManager.getColor("ToolTip.background");
/*     */ 
/*     */     
/*  48 */     boolean isNimbus = isNimbusLookAndFeel();
/*  49 */     if (c == null || isNimbus) {
/*  50 */       c = UIManager.getColor("info");
/*  51 */       if (c == null || (isNimbus && isDerivedColor(c))) {
/*  52 */         c = SystemColor.info;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  58 */     if (c instanceof javax.swing.plaf.ColorUIResource) {
/*  59 */       c = new Color(c.getRGB());
/*     */     }
/*     */     
/*  62 */     return c;
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
/*     */   public static Border getToolTipBorder() {
/*  74 */     Border border = UIManager.getBorder("ToolTip.border");
/*     */     
/*  76 */     if (border == null || isNimbusLookAndFeel()) {
/*  77 */       border = UIManager.getBorder("nimbusBorder");
/*  78 */       if (border == null) {
/*  79 */         border = BorderFactory.createLineBorder(SystemColor.controlDkShadow);
/*     */       }
/*     */     } 
/*     */     
/*  83 */     return border;
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
/*     */   static Color getToolTipHyperlinkForeground() {
/* 101 */     Color fg = UIManager.getColor("ToolTip.foreground");
/* 102 */     if (fg == null || isNimbusLookAndFeel()) {
/* 103 */       fg = (new JToolTip()).getForeground();
/*     */     }
/*     */     
/* 106 */     return Util.isLightForeground(fg) ? Util.LIGHT_HYPERLINK_FG : Color.blue;
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
/* 120 */     return (c != null && (c.getClass().getName().endsWith(".DerivedColor") || c
/* 121 */       .getClass().getName().endsWith(".DerivedColor$UIResource")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNimbusLookAndFeel() {
/* 131 */     return UIManager.getLookAndFeel().getName().equals("Nimbus");
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
/* 145 */     boolean isNimbus = isNimbusLookAndFeel();
/* 146 */     if (isNimbus) {
/* 147 */       Color selBG = textArea.getSelectionColor();
/* 148 */       Color selFG = textArea.getSelectedTextColor();
/* 149 */       textArea.setUI(new BasicEditorPaneUI());
/* 150 */       textArea.setSelectedTextColor(selFG);
/* 151 */       textArea.setSelectionColor(selBG);
/*     */     } 
/*     */     
/* 154 */     textArea.setEditable(false);
/* 155 */     textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/*     */ 
/*     */     
/* 158 */     textArea.getCaret().setSelectionVisible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     Color fg = UIManager.getColor("ToolTip.foreground");
/* 164 */     if (fg == null) {
/* 165 */       fg = UIManager.getColor("Label.foreground");
/*     */     }
/* 167 */     if (fg == null || (isNimbus && isDerivedColor(fg))) {
/* 168 */       fg = SystemColor.textText;
/*     */     }
/* 170 */     textArea.setForeground(fg);
/*     */ 
/*     */     
/* 173 */     textArea.setBackground(getToolTipBackground());
/*     */ 
/*     */ 
/*     */     
/* 177 */     Font font = UIManager.getFont("Label.font");
/* 178 */     if (font == null) {
/* 179 */       font = new Font("SansSerif", 0, 12);
/*     */     }
/* 181 */     HTMLDocument doc = (HTMLDocument)textArea.getDocument();
/* 182 */     doc.getStyleSheet().addRule("body { font-family: " + font
/* 183 */         .getFamily() + "; font-size: " + font
/* 184 */         .getSize() + "pt" + "; color: " + 
/* 185 */         Util.getHexString(fg) + "; }");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     Color linkFG = getToolTipHyperlinkForeground();
/* 191 */     doc.getStyleSheet().addRule("a { color: " + 
/* 192 */         Util.getHexString(linkFG) + "; }");
/*     */     
/* 194 */     URL url = TipUtil.class.getResource("bullet_black.png");
/* 195 */     if (url != null)
/* 196 */       doc.getStyleSheet().addRule("ul { list-style-image: '" + url
/* 197 */           .toString() + "'; }"); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\TipUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */