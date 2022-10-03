/*     */ package org.fife.rsta.ac.xml.tree;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.plaf.basic.BasicLabelUI;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class XmlTreeCellRenderer
/*     */   extends DefaultTreeCellRenderer
/*     */ {
/*     */   private Icon elemIcon;
/*     */   private String elem;
/*     */   private String attr;
/*     */   private boolean selected;
/*  42 */   private static final XmlTreeCellUI UI = new XmlTreeCellUI();
/*  43 */   private static final Color ATTR_COLOR = new Color(8421504);
/*     */ 
/*     */   
/*     */   public XmlTreeCellRenderer() {
/*  47 */     URL url = getClass().getResource("tag.png");
/*  48 */     if (url != null) {
/*  49 */       this.elemIcon = new ImageIcon(url);
/*     */     }
/*  51 */     setUI(UI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
/*  59 */     super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focused);
/*     */     
/*  61 */     this.selected = sel;
/*  62 */     if (value instanceof XmlTreeNode) {
/*     */       
/*  64 */       XmlTreeNode node = (XmlTreeNode)value;
/*  65 */       this.elem = node.getElement();
/*  66 */       this.attr = node.getMainAttr();
/*     */     } else {
/*     */       
/*  69 */       this.elem = this.attr = null;
/*     */     } 
/*  71 */     setIcon(this.elemIcon);
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/*  80 */     super.updateUI();
/*  81 */     setUI(UI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class XmlTreeCellUI
/*     */     extends BasicLabelUI
/*     */   {
/*     */     private XmlTreeCellUI() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void installDefaults(JLabel label) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
/* 105 */       XmlTreeCellRenderer r = (XmlTreeCellRenderer)l;
/* 106 */       Graphics2D g2d = (Graphics2D)g;
/* 107 */       Map<?, ?> hints = RSyntaxUtilities.getDesktopAntiAliasHints();
/* 108 */       if (hints != null) {
/* 109 */         g2d.addRenderingHints(hints);
/*     */       }
/* 111 */       g2d.setColor(l.getForeground());
/* 112 */       g2d.drawString(r.elem, textX, textY);
/* 113 */       if (r.attr != null) {
/* 114 */         textX += g2d.getFontMetrics().stringWidth(r.elem + " ");
/* 115 */         if (!r.selected) {
/* 116 */           g2d.setColor(XmlTreeCellRenderer.ATTR_COLOR);
/*     */         }
/* 118 */         g2d.drawString(r.attr, textX, textY);
/*     */       } 
/* 120 */       g2d.dispose();
/*     */     }
/*     */     
/*     */     protected void uninstallDefaults(JLabel label) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\tree\XmlTreeCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */