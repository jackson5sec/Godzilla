/*     */ package org.fife.ui.rsyntaxtextarea.focusabletip;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SizeGrip
/*     */   extends JPanel
/*     */ {
/*     */   private transient Image osxSizeGrip;
/*     */   
/*     */   SizeGrip() {
/*  50 */     MouseHandler adapter = new MouseHandler();
/*  51 */     addMouseListener(adapter);
/*  52 */     addMouseMotionListener(adapter);
/*  53 */     setPreferredSize(new Dimension(16, 16));
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
/*     */   public void applyComponentOrientation(ComponentOrientation o) {
/*  65 */     possiblyFixCursor(o.isLeftToRight());
/*  66 */     super.applyComponentOrientation(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Image createOSXSizeGrip() {
/*  76 */     ClassLoader cl = getClass().getClassLoader();
/*  77 */     URL url = cl.getResource("org/fife/ui/rsyntaxtextarea/focusabletip/osx_sizegrip.png");
/*  78 */     if (url == null) {
/*     */ 
/*     */       
/*  81 */       File f = new File("../RSyntaxTextArea/src/org/fife/ui/rsyntaxtextarea/focusabletip/osx_sizegrip.png");
/*  82 */       if (f.isFile()) {
/*     */         try {
/*  84 */           url = f.toURI().toURL();
/*  85 */         } catch (MalformedURLException mue) {
/*  86 */           mue.printStackTrace();
/*  87 */           return null;
/*     */         } 
/*     */       } else {
/*     */         
/*  91 */         return null;
/*     */       } 
/*     */     } 
/*  94 */     Image image = null;
/*     */     try {
/*  96 */       image = ImageIO.read(url);
/*  97 */     } catch (IOException ioe) {
/*  98 */       ioe.printStackTrace();
/*     */     } 
/* 100 */     return image;
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
/*     */   protected void paintComponent(Graphics g) {
/* 112 */     super.paintComponent(g);
/*     */     
/* 114 */     Dimension dim = getSize();
/*     */     
/* 116 */     if (this.osxSizeGrip != null) {
/* 117 */       g.drawImage(this.osxSizeGrip, dim.width - 16, dim.height - 16, null);
/*     */       
/*     */       return;
/*     */     } 
/* 121 */     Color c1 = UIManager.getColor("Label.disabledShadow");
/* 122 */     Color c2 = UIManager.getColor("Label.disabledForeground");
/* 123 */     ComponentOrientation orientation = getComponentOrientation();
/*     */     
/* 125 */     if (orientation.isLeftToRight()) {
/* 126 */       int width = dim.width -= 3;
/* 127 */       int height = dim.height -= 3;
/* 128 */       g.setColor(c1);
/* 129 */       g.fillRect(width - 9, height - 1, 3, 3);
/* 130 */       g.fillRect(width - 5, height - 1, 3, 3);
/* 131 */       g.fillRect(width - 1, height - 1, 3, 3);
/* 132 */       g.fillRect(width - 5, height - 5, 3, 3);
/* 133 */       g.fillRect(width - 1, height - 5, 3, 3);
/* 134 */       g.fillRect(width - 1, height - 9, 3, 3);
/* 135 */       g.setColor(c2);
/* 136 */       g.fillRect(width - 9, height - 1, 2, 2);
/* 137 */       g.fillRect(width - 5, height - 1, 2, 2);
/* 138 */       g.fillRect(width - 1, height - 1, 2, 2);
/* 139 */       g.fillRect(width - 5, height - 5, 2, 2);
/* 140 */       g.fillRect(width - 1, height - 5, 2, 2);
/* 141 */       g.fillRect(width - 1, height - 9, 2, 2);
/*     */     } else {
/*     */       
/* 144 */       int height = dim.height -= 3;
/* 145 */       g.setColor(c1);
/* 146 */       g.fillRect(10, height - 1, 3, 3);
/* 147 */       g.fillRect(6, height - 1, 3, 3);
/* 148 */       g.fillRect(2, height - 1, 3, 3);
/* 149 */       g.fillRect(6, height - 5, 3, 3);
/* 150 */       g.fillRect(2, height - 5, 3, 3);
/* 151 */       g.fillRect(2, height - 9, 3, 3);
/* 152 */       g.setColor(c2);
/* 153 */       g.fillRect(10, height - 1, 2, 2);
/* 154 */       g.fillRect(6, height - 1, 2, 2);
/* 155 */       g.fillRect(2, height - 1, 2, 2);
/* 156 */       g.fillRect(6, height - 5, 2, 2);
/* 157 */       g.fillRect(2, height - 5, 2, 2);
/* 158 */       g.fillRect(2, height - 9, 2, 2);
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
/*     */   protected void possiblyFixCursor(boolean ltr) {
/* 171 */     int cursor = 7;
/* 172 */     if (ltr) {
/* 173 */       cursor = 6;
/*     */     }
/* 175 */     if (cursor != getCursor().getType()) {
/* 176 */       setCursor(Cursor.getPredefinedCursor(cursor));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 183 */     super.updateUI();
/*     */ 
/*     */     
/* 186 */     if (System.getProperty("os.name").contains("OS X")) {
/* 187 */       if (this.osxSizeGrip == null) {
/* 188 */         this.osxSizeGrip = createOSXSizeGrip();
/*     */       }
/*     */     } else {
/*     */       
/* 192 */       this.osxSizeGrip = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MouseHandler
/*     */     extends MouseInputAdapter
/*     */   {
/*     */     private Point origPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MouseHandler() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 215 */       Point newPos = e.getPoint();
/* 216 */       SwingUtilities.convertPointToScreen(newPos, SizeGrip.this);
/* 217 */       int xDelta = newPos.x - this.origPos.x;
/* 218 */       int yDelta = newPos.y - this.origPos.y;
/* 219 */       Window wind = SwingUtilities.getWindowAncestor(SizeGrip.this);
/* 220 */       if (wind != null) {
/* 221 */         if (SizeGrip.this.getComponentOrientation().isLeftToRight()) {
/* 222 */           int w = wind.getWidth();
/* 223 */           if (newPos.x >= wind.getX()) {
/* 224 */             w += xDelta;
/*     */           }
/* 226 */           int h = wind.getHeight();
/* 227 */           if (newPos.y >= wind.getY()) {
/* 228 */             h += yDelta;
/*     */           }
/* 230 */           wind.setSize(w, h);
/*     */         } else {
/*     */           
/* 233 */           int newW = Math.max(1, wind.getWidth() - xDelta);
/* 234 */           int newH = Math.max(1, wind.getHeight() + yDelta);
/* 235 */           wind.setBounds(newPos.x, wind.getY(), newW, newH);
/*     */         } 
/*     */         
/* 238 */         wind.invalidate();
/* 239 */         wind.validate();
/*     */       } 
/* 241 */       this.origPos.setLocation(newPos);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 246 */       this.origPos = e.getPoint();
/* 247 */       SwingUtilities.convertPointToScreen(this.origPos, SizeGrip.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 252 */       this.origPos = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\focusabletip\SizeGrip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */