/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MainFrame
/*     */   extends JFrame
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   private JButton bn_quit;
/*     */   private JButton bn_svgViewer;
/*     */   private JButton bn_svgViewer1;
/*     */   private JPanel jPanel1;
/*     */   private JPanel jPanel2;
/*     */   
/*     */   public MainFrame() {
/*  50 */     initComponents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  60 */     this.jPanel1 = new JPanel();
/*  61 */     this.bn_svgViewer = new JButton();
/*  62 */     this.bn_svgViewer1 = new JButton();
/*  63 */     this.jPanel2 = new JPanel();
/*  64 */     this.bn_quit = new JButton();
/*     */     
/*  66 */     setTitle("SVG Salamander - Application Launcher");
/*  67 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           
/*     */           public void windowClosing(WindowEvent evt)
/*     */           {
/*  72 */             MainFrame.this.exitForm(evt);
/*     */           }
/*     */         });
/*     */     
/*  76 */     this.jPanel1.setLayout(new BoxLayout(this.jPanel1, 1));
/*     */     
/*  78 */     this.bn_svgViewer.setText("SVG Viewer (No animation)");
/*  79 */     this.bn_svgViewer.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/*  83 */             MainFrame.this.bn_svgViewerActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/*  87 */     this.jPanel1.add(this.bn_svgViewer);
/*     */     
/*  89 */     this.bn_svgViewer1.setText("SVG Player (Animation)");
/*  90 */     this.bn_svgViewer1.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/*  94 */             MainFrame.this.bn_svgViewer1ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/*  98 */     this.jPanel1.add(this.bn_svgViewer1);
/*     */     
/* 100 */     getContentPane().add(this.jPanel1, "Center");
/*     */     
/* 102 */     this.bn_quit.setText("Quit");
/* 103 */     this.bn_quit.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 107 */             MainFrame.this.bn_quitActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 111 */     this.jPanel2.add(this.bn_quit);
/*     */     
/* 113 */     getContentPane().add(this.jPanel2, "South");
/*     */     
/* 115 */     pack();
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_svgViewer1ActionPerformed(ActionEvent evt) {
/* 120 */     SVGPlayer.main(null);
/*     */     
/* 122 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_svgViewerActionPerformed(ActionEvent evt) {
/* 127 */     SVGViewer.main(null);
/*     */     
/* 129 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_quitActionPerformed(ActionEvent evt) {
/* 134 */     exitForm((WindowEvent)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void exitForm(WindowEvent evt) {
/* 140 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void close() {
/* 145 */     setVisible(false);
/* 146 */     dispose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 154 */     (new MainFrame()).setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\MainFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */