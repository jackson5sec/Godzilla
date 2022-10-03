/*     */ package org.fife.ui.rsyntaxtextarea.focusabletip;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.html.HTMLDocument;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TipWindow
/*     */   extends JWindow
/*     */   implements ActionListener
/*     */ {
/*     */   private FocusableTip ft;
/*     */   private JEditorPane textArea;
/*     */   private String text;
/*     */   private transient TipListener tipListener;
/*     */   private transient HyperlinkListener userHyperlinkListener;
/*     */   private static TipWindow visibleInstance;
/*     */   
/*     */   TipWindow(Window owner, FocusableTip ft, String msg) {
/*  74 */     super(owner);
/*  75 */     this.ft = ft;
/*     */     
/*  77 */     if (msg != null && msg.length() >= 6 && 
/*  78 */       !msg.substring(0, 6).toLowerCase().equals("<html>")) {
/*  79 */       msg = "<html>" + RSyntaxUtilities.escapeForHtml(msg, "<br>", false);
/*     */     }
/*  81 */     this.text = msg;
/*  82 */     this.tipListener = new TipListener();
/*     */     
/*  84 */     JPanel cp = new JPanel(new BorderLayout());
/*  85 */     cp.setBorder(TipUtil.getToolTipBorder());
/*  86 */     cp.setBackground(TipUtil.getToolTipBackground());
/*  87 */     this.textArea = new JEditorPane("text/html", this.text);
/*  88 */     TipUtil.tweakTipEditorPane(this.textArea);
/*  89 */     if (ft.getImageBase() != null) {
/*  90 */       ((HTMLDocument)this.textArea.getDocument()).setBase(ft.getImageBase());
/*     */     }
/*  92 */     this.textArea.addMouseListener(this.tipListener);
/*  93 */     this.textArea.addHyperlinkListener(e -> {
/*     */           if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
/*     */             this.ft.possiblyDisposeOfTipWindow();
/*     */           }
/*     */         });
/*  98 */     cp.add(this.textArea);
/*     */     
/* 100 */     setFocusableWindowState(false);
/* 101 */     setContentPane(cp);
/* 102 */     setBottomPanel();
/* 103 */     pack();
/*     */ 
/*     */ 
/*     */     
/* 107 */     KeyAdapter ka = new KeyAdapter()
/*     */       {
/*     */         public void keyPressed(KeyEvent e) {
/* 110 */           if (e.getKeyCode() == 27) {
/* 111 */             TipWindow.this.ft.possiblyDisposeOfTipWindow();
/*     */           }
/*     */         }
/*     */       };
/* 115 */     addKeyListener(ka);
/* 116 */     this.textArea.addKeyListener(ka);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     synchronized (TipWindow.class) {
/* 122 */       if (visibleInstance != null) {
/* 123 */         visibleInstance.dispose();
/*     */       }
/* 125 */       visibleInstance = this;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 134 */     if (!getFocusableWindowState()) {
/* 135 */       setFocusableWindowState(true);
/* 136 */       setBottomPanel();
/* 137 */       this.textArea.removeMouseListener(this.tipListener);
/* 138 */       pack();
/* 139 */       addWindowFocusListener(new WindowAdapter()
/*     */           {
/*     */             public void windowLostFocus(WindowEvent e) {
/* 142 */               TipWindow.this.ft.possiblyDisposeOfTipWindow();
/*     */             }
/*     */           });
/* 145 */       this.ft.removeListeners();
/* 146 */       if (e == null) {
/* 147 */         requestFocus();
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
/*     */   public void dispose() {
/* 160 */     Container cp = getContentPane();
/* 161 */     for (int i = 0; i < cp.getComponentCount(); i++)
/*     */     {
/* 163 */       cp.getComponent(i).removeMouseListener(this.tipListener);
/*     */     }
/* 165 */     this.ft.removeListeners();
/* 166 */     super.dispose();
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
/*     */   void fixSize() {
/* 178 */     Dimension d = this.textArea.getPreferredSize();
/* 179 */     Rectangle r = null;
/*     */ 
/*     */     
/*     */     try {
/* 183 */       r = this.textArea.modelToView(this.textArea.getDocument().getLength() - 1);
/*     */ 
/*     */       
/* 186 */       d = this.textArea.getPreferredSize();
/* 187 */       d.width += 25;
/*     */       
/* 189 */       int maxWindowW = (this.ft.getMaxSize() != null) ? (this.ft.getMaxSize()).width : 600;
/*     */       
/* 191 */       int maxWindowH = (this.ft.getMaxSize() != null) ? (this.ft.getMaxSize()).height : 400;
/* 192 */       d.width = Math.min(d.width, maxWindowW);
/* 193 */       d.height = Math.min(d.height, maxWindowH);
/*     */ 
/*     */       
/* 196 */       this.textArea.setPreferredSize(d);
/* 197 */       this.textArea.setSize(d);
/*     */ 
/*     */ 
/*     */       
/* 201 */       r = this.textArea.modelToView(this.textArea.getDocument().getLength() - 1);
/* 202 */       if (r.y + r.height > d.height) {
/* 203 */         d.height = r.y + r.height + 5;
/* 204 */         if (this.ft.getMaxSize() != null) {
/* 205 */           d.height = Math.min(d.height, maxWindowH);
/*     */         }
/* 207 */         this.textArea.setPreferredSize(d);
/*     */       }
/*     */     
/* 210 */     } catch (BadLocationException ble) {
/* 211 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 214 */     pack();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 220 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setBottomPanel() {
/* 226 */     final JPanel panel = new JPanel(new BorderLayout());
/* 227 */     panel.add(new JSeparator(), "North");
/*     */     
/* 229 */     boolean focusable = getFocusableWindowState();
/* 230 */     if (focusable) {
/* 231 */       SizeGrip sg = new SizeGrip();
/* 232 */       sg.applyComponentOrientation(sg.getComponentOrientation());
/* 233 */       panel.add(sg, "After");
/* 234 */       MouseInputAdapter adapter = new MouseInputAdapter() {
/*     */           private Point lastPoint;
/*     */           
/*     */           public void mouseDragged(MouseEvent e) {
/* 238 */             Point p = e.getPoint();
/* 239 */             SwingUtilities.convertPointToScreen(p, panel);
/* 240 */             if (this.lastPoint == null) {
/* 241 */               this.lastPoint = p;
/*     */             } else {
/*     */               
/* 244 */               int dx = p.x - this.lastPoint.x;
/* 245 */               int dy = p.y - this.lastPoint.y;
/* 246 */               TipWindow.this.setLocation(TipWindow.this.getX() + dx, TipWindow.this.getY() + dy);
/* 247 */               this.lastPoint = p;
/*     */             } 
/*     */           }
/*     */           
/*     */           public void mousePressed(MouseEvent e) {
/* 252 */             this.lastPoint = e.getPoint();
/* 253 */             SwingUtilities.convertPointToScreen(this.lastPoint, panel);
/*     */           }
/*     */         };
/* 256 */       panel.addMouseListener(adapter);
/* 257 */       panel.addMouseMotionListener(adapter);
/*     */     }
/*     */     else {
/*     */       
/* 261 */       panel.setOpaque(false);
/* 262 */       JLabel label = new JLabel(FocusableTip.getString("FocusHotkey"));
/* 263 */       Color fg = UIManager.getColor("Label.disabledForeground");
/* 264 */       Font font = this.textArea.getFont();
/* 265 */       font = font.deriveFont(font.getSize2D() - 1.0F);
/* 266 */       label.setFont(font);
/* 267 */       if (fg == null) {
/* 268 */         fg = Color.GRAY;
/*     */       }
/* 270 */       label.setOpaque(true);
/* 271 */       Color bg = TipUtil.getToolTipBackground();
/* 272 */       label.setBackground(bg);
/* 273 */       label.setForeground(fg);
/* 274 */       label.setHorizontalAlignment(11);
/* 275 */       label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
/* 276 */       panel.add(label);
/* 277 */       panel.addMouseListener(this.tipListener);
/*     */     } 
/*     */ 
/*     */     
/* 281 */     Container cp = getContentPane();
/* 282 */     if (cp.getComponentCount() == 2) {
/* 283 */       Component comp = cp.getComponent(0);
/* 284 */       cp.remove(0);
/* 285 */       JScrollPane sp = new JScrollPane(comp);
/* 286 */       Border emptyBorder = BorderFactory.createEmptyBorder();
/* 287 */       sp.setBorder(emptyBorder);
/* 288 */       sp.setViewportBorder(emptyBorder);
/* 289 */       sp.setBackground(this.textArea.getBackground());
/* 290 */       sp.getViewport().setBackground(this.textArea.getBackground());
/* 291 */       cp.add(sp);
/*     */       
/* 293 */       cp.getComponent(0).removeMouseListener(this.tipListener);
/* 294 */       cp.remove(0);
/*     */     } 
/*     */     
/* 297 */     cp.add(panel, "South");
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
/*     */   public void setHyperlinkListener(HyperlinkListener listener) {
/* 310 */     if (this.userHyperlinkListener != null) {
/* 311 */       this.textArea.removeHyperlinkListener(this.userHyperlinkListener);
/*     */     }
/* 313 */     this.userHyperlinkListener = listener;
/* 314 */     if (this.userHyperlinkListener != null) {
/* 315 */       this.textArea.addHyperlinkListener(this.userHyperlinkListener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class TipListener
/*     */     extends MouseAdapter
/*     */   {
/*     */     private TipListener() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 330 */       TipWindow.this.actionPerformed((ActionEvent)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 337 */       Component source = (Component)e.getSource();
/* 338 */       Point p = e.getPoint();
/* 339 */       SwingUtilities.convertPointToScreen(p, source);
/* 340 */       if (!TipWindow.this.getBounds().contains(p))
/* 341 */         TipWindow.this.ft.possiblyDisposeOfTipWindow(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\focusabletip\TipWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */