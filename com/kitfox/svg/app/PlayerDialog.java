/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerDialog
/*     */   extends JDialog
/*     */   implements PlayerThreadListener
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   PlayerThread thread;
/*     */   final SVGPlayer parent;
/*     */   private JButton bn_playBack;
/*     */   private JButton bn_playFwd;
/*     */   private JButton bn_stop;
/*     */   private JButton bn_time0;
/*     */   private JLabel jLabel1;
/*     */   private JLabel jLabel2;
/*     */   private JPanel jPanel1;
/*     */   private JPanel jPanel2;
/*     */   private JPanel jPanel3;
/*     */   private JPanel jPanel4;
/*     */   private JTextField text_curTime;
/*     */   private JTextField text_timeStep;
/*     */   
/*     */   public PlayerDialog(SVGPlayer parent) {
/*  54 */     super(parent, false);
/*  55 */     initComponents();
/*     */     
/*  57 */     this.parent = parent;
/*     */     
/*  59 */     this.thread = new PlayerThread();
/*  60 */     this.thread.addListener(this);
/*     */     
/*  62 */     text_timeStepActionPerformed((ActionEvent)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTime(double curTime, double timeStep, int playState) {
/*  67 */     if (playState == 0)
/*     */       return; 
/*  69 */     this.text_curTime.setText("" + (float)curTime);
/*  70 */     this.parent.updateTime(curTime);
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
/*  82 */     this.jPanel1 = new JPanel();
/*  83 */     this.bn_playBack = new JButton();
/*  84 */     this.bn_stop = new JButton();
/*  85 */     this.bn_playFwd = new JButton();
/*  86 */     this.jPanel2 = new JPanel();
/*  87 */     this.jPanel3 = new JPanel();
/*  88 */     this.jLabel1 = new JLabel();
/*  89 */     this.text_curTime = new JTextField();
/*  90 */     this.bn_time0 = new JButton();
/*  91 */     this.jPanel4 = new JPanel();
/*  92 */     this.jLabel2 = new JLabel();
/*  93 */     this.text_timeStep = new JTextField();
/*     */     
/*  95 */     setDefaultCloseOperation(2);
/*  96 */     setTitle("Player");
/*  97 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           
/*     */           public void windowClosed(WindowEvent evt)
/*     */           {
/* 102 */             PlayerDialog.this.formWindowClosed(evt);
/*     */           }
/*     */         });
/*     */     
/* 106 */     this.bn_playBack.setText("<");
/* 107 */     this.bn_playBack.setToolTipText("Play backwards");
/* 108 */     this.bn_playBack.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 112 */             PlayerDialog.this.bn_playBackActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 116 */     this.jPanel1.add(this.bn_playBack);
/*     */     
/* 118 */     this.bn_stop.setText("||");
/* 119 */     this.bn_stop.setToolTipText("Stop playback");
/* 120 */     this.bn_stop.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 124 */             PlayerDialog.this.bn_stopActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 128 */     this.jPanel1.add(this.bn_stop);
/*     */     
/* 130 */     this.bn_playFwd.setText(">");
/* 131 */     this.bn_playFwd.setToolTipText("Play Forwards");
/* 132 */     this.bn_playFwd.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 136 */             PlayerDialog.this.bn_playFwdActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 140 */     this.jPanel1.add(this.bn_playFwd);
/*     */     
/* 142 */     getContentPane().add(this.jPanel1, "North");
/*     */     
/* 144 */     this.jPanel2.setLayout(new BoxLayout(this.jPanel2, 1));
/*     */     
/* 146 */     this.jLabel1.setText("Cur Time");
/* 147 */     this.jPanel3.add(this.jLabel1);
/*     */     
/* 149 */     this.text_curTime.setHorizontalAlignment(2);
/* 150 */     this.text_curTime.setText("0");
/* 151 */     this.text_curTime.setPreferredSize(new Dimension(100, 21));
/* 152 */     this.text_curTime.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 156 */             PlayerDialog.this.text_curTimeActionPerformed(evt);
/*     */           }
/*     */         });
/* 159 */     this.text_curTime.addFocusListener(new FocusAdapter()
/*     */         {
/*     */           
/*     */           public void focusLost(FocusEvent evt)
/*     */           {
/* 164 */             PlayerDialog.this.text_curTimeFocusLost(evt);
/*     */           }
/*     */         });
/*     */     
/* 168 */     this.jPanel3.add(this.text_curTime);
/*     */     
/* 170 */     this.bn_time0.setText("Time 0");
/* 171 */     this.bn_time0.setToolTipText("Reset time to first frame");
/* 172 */     this.bn_time0.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 176 */             PlayerDialog.this.bn_time0ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 180 */     this.jPanel3.add(this.bn_time0);
/*     */     
/* 182 */     this.jPanel2.add(this.jPanel3);
/*     */     
/* 184 */     this.jLabel2.setText("Frames Per Second");
/* 185 */     this.jPanel4.add(this.jLabel2);
/*     */     
/* 187 */     this.text_timeStep.setHorizontalAlignment(4);
/* 188 */     this.text_timeStep.setText("60");
/* 189 */     this.text_timeStep.setPreferredSize(new Dimension(100, 21));
/* 190 */     this.text_timeStep.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 194 */             PlayerDialog.this.text_timeStepActionPerformed(evt);
/*     */           }
/*     */         });
/* 197 */     this.text_timeStep.addFocusListener(new FocusAdapter()
/*     */         {
/*     */           
/*     */           public void focusLost(FocusEvent evt)
/*     */           {
/* 202 */             PlayerDialog.this.text_timeStepFocusLost(evt);
/*     */           }
/*     */         });
/*     */     
/* 206 */     this.jPanel4.add(this.text_timeStep);
/*     */     
/* 208 */     this.jPanel2.add(this.jPanel4);
/*     */     
/* 210 */     getContentPane().add(this.jPanel2, "Center");
/*     */     
/* 212 */     pack();
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_time0ActionPerformed(ActionEvent evt) {
/* 217 */     this.thread.setCurTime(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_playFwdActionPerformed(ActionEvent evt) {
/* 222 */     this.thread.setPlayState(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_stopActionPerformed(ActionEvent evt) {
/* 227 */     this.thread.setPlayState(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void bn_playBackActionPerformed(ActionEvent evt) {
/* 232 */     this.thread.setPlayState(2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void formWindowClosed(WindowEvent evt) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private void text_timeStepFocusLost(FocusEvent evt) {
/* 242 */     text_timeStepActionPerformed((ActionEvent)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void text_timeStepActionPerformed(ActionEvent evt) {
/*     */     try {
/* 249 */       int val = Integer.parseInt(this.text_timeStep.getText());
/* 250 */       this.thread.setTimeStep(1.0D / val);
/*     */     }
/* 252 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 256 */     double d = this.thread.getTimeStep();
/* 257 */     String newStrn = "" + (int)(1.0D / d);
/* 258 */     if (newStrn.equals(this.text_timeStep.getText()))
/* 259 */       return;  this.text_timeStep.setText(newStrn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void text_curTimeActionPerformed(ActionEvent evt) {
/*     */     try {
/* 268 */       double val = Double.parseDouble(this.text_curTime.getText());
/* 269 */       this.thread.setCurTime(val);
/*     */     }
/* 271 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 275 */     double d = this.thread.getCurTime();
/* 276 */     this.text_curTime.setText("" + (float)d);
/*     */     
/* 278 */     text_timeStepActionPerformed((ActionEvent)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void text_curTimeFocusLost(FocusEvent evt) {
/* 283 */     text_curTimeActionPerformed((ActionEvent)null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\PlayerDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */