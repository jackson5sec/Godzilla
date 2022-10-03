/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTreeUI;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTreeUI
/*     */   extends BasicTreeUI
/*     */ {
/*     */   protected Color selectionBackground;
/*     */   protected Color selectionForeground;
/*     */   protected Color selectionInactiveBackground;
/*     */   protected Color selectionInactiveForeground;
/*     */   protected Color selectionBorderColor;
/*     */   protected boolean wideSelection;
/*     */   protected boolean showCellFocusIndicator;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 112 */     return new FlatTreeUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 117 */     super.installDefaults();
/*     */     
/* 119 */     LookAndFeel.installBorder(this.tree, "Tree.border");
/*     */     
/* 121 */     this.selectionBackground = UIManager.getColor("Tree.selectionBackground");
/* 122 */     this.selectionForeground = UIManager.getColor("Tree.selectionForeground");
/* 123 */     this.selectionInactiveBackground = UIManager.getColor("Tree.selectionInactiveBackground");
/* 124 */     this.selectionInactiveForeground = UIManager.getColor("Tree.selectionInactiveForeground");
/* 125 */     this.selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
/* 126 */     this.wideSelection = UIManager.getBoolean("Tree.wideSelection");
/* 127 */     this.showCellFocusIndicator = UIManager.getBoolean("Tree.showCellFocusIndicator");
/*     */ 
/*     */     
/* 130 */     int rowHeight = FlatUIUtils.getUIInt("Tree.rowHeight", 16);
/* 131 */     if (rowHeight > 0)
/* 132 */       LookAndFeel.installProperty(this.tree, "rowHeight", Integer.valueOf(UIScale.scale(rowHeight))); 
/* 133 */     setLeftChildIndent(UIScale.scale(getLeftChildIndent()));
/* 134 */     setRightChildIndent(UIScale.scale(getRightChildIndent()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 139 */     super.uninstallDefaults();
/*     */     
/* 141 */     LookAndFeel.uninstallBorder(this.tree);
/*     */     
/* 143 */     this.selectionBackground = null;
/* 144 */     this.selectionForeground = null;
/* 145 */     this.selectionInactiveBackground = null;
/* 146 */     this.selectionInactiveForeground = null;
/* 147 */     this.selectionBorderColor = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MouseListener createMouseListener() {
/* 152 */     return new BasicTreeUI.MouseHandler()
/*     */       {
/*     */         public void mousePressed(MouseEvent e) {
/* 155 */           super.mousePressed(handleWideMouseEvent(e));
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseReleased(MouseEvent e) {
/* 160 */           super.mouseReleased(handleWideMouseEvent(e));
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseDragged(MouseEvent e) {
/* 165 */           super.mouseDragged(handleWideMouseEvent(e));
/*     */         }
/*     */         
/*     */         private MouseEvent handleWideMouseEvent(MouseEvent e) {
/* 169 */           if (!FlatTreeUI.this.isWideSelection() || !FlatTreeUI.this.tree.isEnabled() || !SwingUtilities.isLeftMouseButton(e) || e.isConsumed()) {
/* 170 */             return e;
/*     */           }
/* 172 */           int x = e.getX();
/* 173 */           int y = e.getY();
/* 174 */           TreePath path = FlatTreeUI.this.getClosestPathForLocation(FlatTreeUI.this.tree, x, y);
/* 175 */           if (path == null || FlatTreeUI.this.isLocationInExpandControl(path, x, y)) {
/* 176 */             return e;
/*     */           }
/* 178 */           Rectangle bounds = FlatTreeUI.this.getPathBounds(FlatTreeUI.this.tree, path);
/* 179 */           if (bounds == null || y < bounds.y || y >= bounds.y + bounds.height) {
/* 180 */             return e;
/*     */           }
/* 182 */           int newX = Math.max(bounds.x, Math.min(x, bounds.x + bounds.width - 1));
/* 183 */           if (newX == x) {
/* 184 */             return e;
/*     */           }
/*     */           
/* 187 */           return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e
/* 188 */               .getModifiers() | e.getModifiersEx(), newX, e.getY(), e
/* 189 */               .getClickCount(), e.isPopupTrigger(), e.getButton());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 196 */     return new BasicTreeUI.PropertyChangeHandler()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent e) {
/* 199 */           super.propertyChange(e);
/*     */           
/* 201 */           if (e.getSource() == FlatTreeUI.this.tree) {
/* 202 */             switch (e.getPropertyName()) {
/*     */               case "JTree.wideSelection":
/*     */               case "JTree.paintSelection":
/* 205 */                 FlatTreeUI.this.tree.repaint();
/*     */                 break;
/*     */               
/*     */               case "dropLocation":
/* 209 */                 if (FlatTreeUI.this.isWideSelection()) {
/* 210 */                   JTree.DropLocation oldValue = (JTree.DropLocation)e.getOldValue();
/* 211 */                   repaintWideDropLocation(oldValue);
/* 212 */                   repaintWideDropLocation(FlatTreeUI.this.tree.getDropLocation());
/*     */                 } 
/*     */                 break;
/*     */             } 
/*     */           }
/*     */         }
/*     */         
/*     */         private void repaintWideDropLocation(JTree.DropLocation loc) {
/* 220 */           if (loc == null || FlatTreeUI.this.isDropLine(loc)) {
/*     */             return;
/*     */           }
/* 223 */           Rectangle r = FlatTreeUI.this.tree.getPathBounds(loc.getPath());
/* 224 */           if (r != null) {
/* 225 */             FlatTreeUI.this.tree.repaint(0, r.y, FlatTreeUI.this.tree.getWidth(), r.height);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
/* 238 */     boolean isEditing = (this.editingComponent != null && this.editingRow == row);
/* 239 */     boolean isSelected = this.tree.isRowSelected(row);
/* 240 */     boolean isDropRow = isDropRow(row);
/* 241 */     boolean needsSelectionPainting = ((isSelected || isDropRow) && isPaintSelection());
/*     */ 
/*     */     
/* 244 */     if (isEditing && !needsSelectionPainting) {
/*     */       return;
/*     */     }
/* 247 */     boolean hasFocus = FlatUIUtils.isPermanentFocusOwner(this.tree);
/* 248 */     boolean cellHasFocus = (hasFocus && row == getLeadSelectionRow());
/*     */ 
/*     */ 
/*     */     
/* 252 */     if (!hasFocus && isSelected && this.tree.getParent() instanceof javax.swing.CellRendererPane) {
/* 253 */       hasFocus = FlatUIUtils.isPermanentFocusOwner(this.tree.getParent().getParent());
/*     */     }
/*     */     
/* 256 */     Component rendererComponent = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, path
/* 257 */         .getLastPathComponent(), isSelected, isExpanded, isLeaf, row, cellHasFocus);
/*     */ 
/*     */     
/* 260 */     Color oldBackgroundSelectionColor = null;
/* 261 */     if (isSelected && !hasFocus && !isDropRow) {
/* 262 */       if (rendererComponent instanceof DefaultTreeCellRenderer) {
/* 263 */         DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)rendererComponent;
/* 264 */         if (renderer.getBackgroundSelectionColor() == this.selectionBackground) {
/* 265 */           oldBackgroundSelectionColor = renderer.getBackgroundSelectionColor();
/* 266 */           renderer.setBackgroundSelectionColor(this.selectionInactiveBackground);
/*     */         }
/*     */       
/* 269 */       } else if (rendererComponent.getBackground() == this.selectionBackground) {
/* 270 */         rendererComponent.setBackground(this.selectionInactiveBackground);
/*     */       } 
/*     */       
/* 273 */       if (rendererComponent.getForeground() == this.selectionForeground) {
/* 274 */         rendererComponent.setForeground(this.selectionInactiveForeground);
/*     */       }
/*     */     } 
/*     */     
/* 278 */     Color oldBorderSelectionColor = null;
/* 279 */     if (isSelected && hasFocus && (!this.showCellFocusIndicator || this.tree
/* 280 */       .getMinSelectionRow() == this.tree.getMaxSelectionRow()) && rendererComponent instanceof DefaultTreeCellRenderer) {
/*     */ 
/*     */       
/* 283 */       DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)rendererComponent;
/* 284 */       if (renderer.getBorderSelectionColor() == this.selectionBorderColor) {
/* 285 */         oldBorderSelectionColor = renderer.getBorderSelectionColor();
/* 286 */         renderer.setBorderSelectionColor(null);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 291 */     if (needsSelectionPainting) {
/*     */       
/* 293 */       Color oldColor = g.getColor();
/* 294 */       g.setColor(isDropRow ? 
/* 295 */           UIManager.getColor("Tree.dropCellBackground") : ((rendererComponent instanceof DefaultTreeCellRenderer) ? ((DefaultTreeCellRenderer)rendererComponent)
/*     */           
/* 297 */           .getBackgroundSelectionColor() : (hasFocus ? this.selectionBackground : this.selectionInactiveBackground)));
/*     */ 
/*     */       
/* 300 */       if (isWideSelection()) {
/*     */         
/* 302 */         g.fillRect(0, bounds.y, this.tree.getWidth(), bounds.height);
/*     */ 
/*     */ 
/*     */         
/* 306 */         if (shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
/* 307 */           paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 312 */         int xOffset = 0;
/* 313 */         int imageOffset = 0;
/*     */         
/* 315 */         if (rendererComponent instanceof JLabel) {
/* 316 */           JLabel label = (JLabel)rendererComponent;
/* 317 */           Icon icon = label.getIcon();
/*     */           
/* 319 */           imageOffset = (icon != null && label.getText() != null) ? (icon.getIconWidth() + Math.max(label.getIconTextGap() - 1, 0)) : 0;
/*     */           
/* 321 */           xOffset = label.getComponentOrientation().isLeftToRight() ? imageOffset : 0;
/*     */         } 
/*     */         
/* 324 */         g.fillRect(bounds.x + xOffset, bounds.y, bounds.width - imageOffset, bounds.height);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 329 */       g.setColor(oldColor);
/*     */     } 
/*     */ 
/*     */     
/* 333 */     if (!isEditing) {
/* 334 */       this.rendererPane.paintComponent(g, rendererComponent, this.tree, bounds.x, bounds.y, bounds.width, bounds.height, true);
/*     */     }
/*     */     
/* 337 */     if (oldBackgroundSelectionColor != null)
/* 338 */       ((DefaultTreeCellRenderer)rendererComponent).setBackgroundSelectionColor(oldBackgroundSelectionColor); 
/* 339 */     if (oldBorderSelectionColor != null) {
/* 340 */       ((DefaultTreeCellRenderer)rendererComponent).setBorderSelectionColor(oldBorderSelectionColor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDropRow(int row) {
/* 348 */     JTree.DropLocation dropLocation = this.tree.getDropLocation();
/* 349 */     return (dropLocation != null && dropLocation
/* 350 */       .getChildIndex() == -1 && this.tree
/* 351 */       .getRowForPath(dropLocation.getPath()) == row);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Rectangle getDropLineRect(JTree.DropLocation loc) {
/* 356 */     Rectangle r = super.getDropLineRect(loc);
/* 357 */     return isWideSelection() ? new Rectangle(0, r.y, this.tree.getWidth(), r.height) : r;
/*     */   }
/*     */   
/*     */   protected boolean isWideSelection() {
/* 361 */     return FlatClientProperties.clientPropertyBoolean(this.tree, "JTree.wideSelection", this.wideSelection);
/*     */   }
/*     */   
/*     */   protected boolean isPaintSelection() {
/* 365 */     return FlatClientProperties.clientPropertyBoolean(this.tree, "JTree.paintSelection", true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTreeUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */