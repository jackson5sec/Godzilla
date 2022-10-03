/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionDialog
/*     */   extends JDialog
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   final boolean verbose;
/*     */   private JButton bn_close;
/*     */   private JPanel jPanel1;
/*     */   private JPanel jPanel2;
/*     */   private JTextPane textpane_text;
/*     */   
/*     */   public VersionDialog(Frame parent, boolean modal, boolean verbose) {
/*  58 */     super(parent, modal);
/*  59 */     initComponents();
/*     */     
/*  61 */     this.verbose = verbose;
/*     */     
/*  63 */     this.textpane_text.setContentType("text/html");
/*     */     
/*  65 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */     try {
/*  68 */       URL url = getClass().getResource("/res/help/about/about.html");
/*  69 */       if (verbose)
/*     */       {
/*  71 */         System.err.println("" + getClass() + " trying to load about html " + url);
/*     */       }
/*     */       
/*  74 */       BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*     */       
/*     */       while (true) {
/*  77 */         String line = reader.readLine();
/*  78 */         if (line == null)
/*  79 */           break;  sb.append(line);
/*     */       } 
/*     */       
/*  82 */       this.textpane_text.setText(sb.toString());
/*     */     }
/*  84 */     catch (Exception e) {
/*     */       
/*  86 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
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
/*     */   private void initComponents() {
/*  99 */     this.jPanel1 = new JPanel();
/* 100 */     this.textpane_text = new JTextPane();
/* 101 */     this.jPanel2 = new JPanel();
/* 102 */     this.bn_close = new JButton();
/*     */     
/* 104 */     setDefaultCloseOperation(2);
/* 105 */     setTitle("About SVG Salamander");
/* 106 */     this.jPanel1.setLayout(new BorderLayout());
/*     */     
/* 108 */     this.textpane_text.setEditable(false);
/* 109 */     this.textpane_text.setPreferredSize(new Dimension(400, 300));
/* 110 */     this.jPanel1.add(this.textpane_text, "Center");
/*     */     
/* 112 */     getContentPane().add(this.jPanel1, "Center");
/*     */     
/* 114 */     this.bn_close.setText("Close");
/* 115 */     this.bn_close.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 119 */             VersionDialog.this.bn_closeActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 123 */     this.jPanel2.add(this.bn_close);
/*     */     
/* 125 */     getContentPane().add(this.jPanel2, "South");
/*     */     
/* 127 */     pack();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void bn_closeActionPerformed(ActionEvent evt) {
/* 133 */     setVisible(false);
/* 134 */     dispose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 142 */     EventQueue.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 146 */             (new VersionDialog(new JFrame(), true, true)).setVisible(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\VersionDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */