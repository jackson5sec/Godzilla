/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.Objects;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTableHeaderUI;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTableHeaderUI
/*     */   extends BasicTableHeaderUI
/*     */ {
/*     */   protected Color separatorColor;
/*     */   protected Color bottomSeparatorColor;
/*     */   protected int height;
/*     */   protected int sortIconPosition;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  73 */     return new FlatTableHeaderUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  78 */     super.installDefaults();
/*     */     
/*  80 */     this.separatorColor = UIManager.getColor("TableHeader.separatorColor");
/*  81 */     this.bottomSeparatorColor = UIManager.getColor("TableHeader.bottomSeparatorColor");
/*  82 */     this.height = UIManager.getInt("TableHeader.height");
/*  83 */     switch (Objects.toString(UIManager.getString("TableHeader.sortIconPosition"), "right"))
/*     */     { default:
/*  85 */         this.sortIconPosition = 4; return;
/*  86 */       case "left": this.sortIconPosition = 2; return;
/*  87 */       case "top": this.sortIconPosition = 1; return;
/*  88 */       case "bottom": break; }  this.sortIconPosition = 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  94 */     super.uninstallDefaults();
/*     */     
/*  96 */     this.separatorColor = null;
/*  97 */     this.bottomSeparatorColor = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 102 */     if (this.header.getColumnModel().getColumnCount() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     TableCellRenderer defaultRenderer = this.header.getDefaultRenderer();
/* 107 */     boolean paintBorders = isSystemDefaultRenderer(defaultRenderer);
/* 108 */     if (!paintBorders) {
/*     */       
/* 110 */       Component rendererComponent = defaultRenderer.getTableCellRendererComponent(this.header
/* 111 */           .getTable(), "", false, false, -1, 0);
/* 112 */       paintBorders = isSystemDefaultRenderer(rendererComponent);
/*     */     } 
/*     */     
/* 115 */     if (paintBorders) {
/* 116 */       paintColumnBorders(g, c);
/*     */     }
/*     */     
/* 119 */     FlatTableCellHeaderRenderer sortIconRenderer = null;
/* 120 */     if (this.sortIconPosition != 4) {
/* 121 */       sortIconRenderer = new FlatTableCellHeaderRenderer(this.header.getDefaultRenderer());
/* 122 */       this.header.setDefaultRenderer(sortIconRenderer);
/*     */     } 
/*     */ 
/*     */     
/* 126 */     super.paint(g, c);
/*     */ 
/*     */     
/* 129 */     if (sortIconRenderer != null) {
/* 130 */       sortIconRenderer.reset();
/* 131 */       this.header.setDefaultRenderer(sortIconRenderer.delegate);
/*     */     } 
/*     */     
/* 134 */     if (paintBorders)
/* 135 */       paintDraggedColumnBorders(g, c); 
/*     */   }
/*     */   
/*     */   private boolean isSystemDefaultRenderer(Object headerRenderer) {
/* 139 */     String rendererClassName = headerRenderer.getClass().getName();
/* 140 */     return (rendererClassName.equals("sun.swing.table.DefaultTableCellHeaderRenderer") || rendererClassName
/* 141 */       .equals("sun.swing.FilePane$AlignableTableHeaderRenderer"));
/*     */   }
/*     */   
/*     */   protected void paintColumnBorders(Graphics g, JComponent c) {
/* 145 */     int width = c.getWidth();
/* 146 */     int height = c.getHeight();
/* 147 */     float lineWidth = UIScale.scale(1.0F);
/* 148 */     float topLineIndent = lineWidth;
/* 149 */     float bottomLineIndent = lineWidth * 3.0F;
/* 150 */     TableColumnModel columnModel = this.header.getColumnModel();
/* 151 */     int columnCount = columnModel.getColumnCount();
/* 152 */     int sepCount = columnCount;
/* 153 */     if (hideLastVerticalLine()) {
/* 154 */       sepCount--;
/*     */     }
/* 156 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/* 158 */       FlatUIUtils.setRenderingHints(g2);
/*     */ 
/*     */       
/* 161 */       g2.setColor(this.bottomSeparatorColor);
/* 162 */       g2.fill(new Rectangle2D.Float(0.0F, height - lineWidth, width, lineWidth));
/*     */ 
/*     */       
/* 165 */       g2.setColor(this.separatorColor);
/*     */       
/* 167 */       float y = topLineIndent;
/* 168 */       float h = height - bottomLineIndent;
/*     */       
/* 170 */       if (this.header.getComponentOrientation().isLeftToRight()) {
/* 171 */         int x = 0;
/* 172 */         for (int i = 0; i < sepCount; i++) {
/* 173 */           x += columnModel.getColumn(i).getWidth();
/* 174 */           g2.fill(new Rectangle2D.Float(x - lineWidth, y, lineWidth, h));
/*     */         } 
/*     */ 
/*     */         
/* 178 */         if (!hideTrailingVerticalLine())
/* 179 */           g2.fill(new Rectangle2D.Float(this.header.getWidth() - lineWidth, y, lineWidth, h)); 
/*     */       } else {
/* 181 */         Rectangle cellRect = this.header.getHeaderRect(0);
/* 182 */         int x = cellRect.x + cellRect.width;
/* 183 */         for (int i = 0; i < sepCount; i++) {
/* 184 */           x -= columnModel.getColumn(i).getWidth();
/* 185 */           g2.fill(new Rectangle2D.Float(x - ((i < sepCount - 1) ? lineWidth : 0.0F), y, lineWidth, h));
/*     */         } 
/*     */ 
/*     */         
/* 189 */         if (!hideTrailingVerticalLine())
/* 190 */           g2.fill(new Rectangle2D.Float(0.0F, y, lineWidth, h)); 
/*     */       } 
/*     */     } finally {
/* 193 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void paintDraggedColumnBorders(Graphics g, JComponent c) {
/* 198 */     TableColumn draggedColumn = this.header.getDraggedColumn();
/* 199 */     if (draggedColumn == null) {
/*     */       return;
/*     */     }
/*     */     
/* 203 */     TableColumnModel columnModel = this.header.getColumnModel();
/* 204 */     int columnCount = columnModel.getColumnCount();
/* 205 */     int draggedColumnIndex = -1;
/* 206 */     for (int i = 0; i < columnCount; i++) {
/* 207 */       if (columnModel.getColumn(i) == draggedColumn) {
/* 208 */         draggedColumnIndex = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 213 */     if (draggedColumnIndex < 0) {
/*     */       return;
/*     */     }
/* 216 */     float lineWidth = UIScale.scale(1.0F);
/* 217 */     float topLineIndent = lineWidth;
/* 218 */     float bottomLineIndent = lineWidth * 3.0F;
/* 219 */     Rectangle r = this.header.getHeaderRect(draggedColumnIndex);
/* 220 */     r.x += this.header.getDraggedDistance();
/*     */     
/* 222 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/* 224 */       FlatUIUtils.setRenderingHints(g2);
/*     */ 
/*     */       
/* 227 */       g2.setColor(this.bottomSeparatorColor);
/* 228 */       g2.fill(new Rectangle2D.Float(r.x, (r.y + r.height) - lineWidth, r.width, lineWidth));
/*     */ 
/*     */       
/* 231 */       g2.setColor(this.separatorColor);
/* 232 */       g2.fill(new Rectangle2D.Float(r.x, topLineIndent, lineWidth, r.height - bottomLineIndent));
/* 233 */       g2.fill(new Rectangle2D.Float((r.x + r.width) - lineWidth, r.y + topLineIndent, lineWidth, r.height - bottomLineIndent));
/*     */     } finally {
/* 235 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 241 */     Dimension size = super.getPreferredSize(c);
/* 242 */     if (size.height > 0)
/* 243 */       size.height = Math.max(size.height, UIScale.scale(this.height)); 
/* 244 */     return size;
/*     */   }
/*     */   
/*     */   protected boolean hideLastVerticalLine() {
/* 248 */     Container viewport = this.header.getParent();
/* 249 */     Container viewportParent = (viewport != null) ? viewport.getParent() : null;
/* 250 */     if (!(viewportParent instanceof JScrollPane)) {
/* 251 */       return false;
/*     */     }
/* 253 */     Rectangle cellRect = this.header.getHeaderRect(this.header.getColumnModel().getColumnCount() - 1);
/*     */ 
/*     */     
/* 256 */     JScrollPane scrollPane = (JScrollPane)viewportParent;
/* 257 */     return scrollPane.getComponentOrientation().isLeftToRight() ? (
/* 258 */       (cellRect.x + cellRect.width >= viewport.getWidth())) : ((cellRect.x <= 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hideTrailingVerticalLine() {
/* 263 */     Container viewport = this.header.getParent();
/* 264 */     Container viewportParent = (viewport != null) ? viewport.getParent() : null;
/* 265 */     if (!(viewportParent instanceof JScrollPane)) {
/* 266 */       return false;
/*     */     }
/* 268 */     JScrollPane scrollPane = (JScrollPane)viewportParent;
/* 269 */     return (viewport == scrollPane.getColumnHeader() && scrollPane
/* 270 */       .getCorner("UPPER_TRAILING_CORNER") == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class FlatTableCellHeaderRenderer
/*     */     implements TableCellRenderer, Border, UIResource
/*     */   {
/*     */     private final TableCellRenderer delegate;
/*     */ 
/*     */     
/*     */     private JLabel l;
/*     */ 
/*     */     
/* 285 */     private int oldHorizontalTextPosition = -1;
/*     */     private Border origBorder;
/*     */     private Icon sortIcon;
/*     */     
/*     */     FlatTableCellHeaderRenderer(TableCellRenderer delegate) {
/* 290 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 297 */       Component c = this.delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/* 298 */       if (!(c instanceof JLabel)) {
/* 299 */         return c;
/*     */       }
/* 301 */       this.l = (JLabel)c;
/*     */       
/* 303 */       if (FlatTableHeaderUI.this.sortIconPosition == 2) {
/* 304 */         if (this.oldHorizontalTextPosition < 0)
/* 305 */           this.oldHorizontalTextPosition = this.l.getHorizontalTextPosition(); 
/* 306 */         this.l.setHorizontalTextPosition(4);
/*     */       } else {
/*     */         
/* 309 */         this.sortIcon = this.l.getIcon();
/* 310 */         this.origBorder = this.l.getBorder();
/* 311 */         this.l.setIcon((Icon)null);
/* 312 */         this.l.setBorder(this);
/*     */       } 
/*     */       
/* 315 */       return this.l;
/*     */     }
/*     */     
/*     */     void reset() {
/* 319 */       if (this.l != null && FlatTableHeaderUI.this.sortIconPosition == 2 && this.oldHorizontalTextPosition >= 0) {
/* 320 */         this.l.setHorizontalTextPosition(this.oldHorizontalTextPosition);
/*     */       }
/*     */     }
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 325 */       if (this.origBorder != null) {
/* 326 */         this.origBorder.paintBorder(c, g, x, y, width, height);
/*     */       }
/* 328 */       if (this.sortIcon != null) {
/* 329 */         int xi = x + (width - this.sortIcon.getIconWidth()) / 2;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 334 */         int yi = (FlatTableHeaderUI.this.sortIconPosition == 1) ? (y + UIScale.scale(1)) : (y + height - this.sortIcon.getIconHeight() - 1 - (int)(1.0F * UIScale.getUserScaleFactor()));
/* 335 */         this.sortIcon.paintIcon(c, g, xi, yi);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 341 */       return (this.origBorder != null) ? this.origBorder.getBorderInsets(c) : new Insets(0, 0, 0, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBorderOpaque() {
/* 346 */       return (this.origBorder != null) ? this.origBorder.isBorderOpaque() : false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTableHeaderUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */