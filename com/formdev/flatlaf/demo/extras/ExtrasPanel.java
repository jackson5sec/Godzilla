/*     */ package com.formdev.flatlaf.demo.extras;
/*     */ 
/*     */ import com.formdev.flatlaf.extras.FlatSVGIcon;
/*     */ import com.formdev.flatlaf.extras.components.FlatTriStateCheckBox;
/*     */ import java.awt.Component;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtrasPanel
/*     */   extends JPanel
/*     */ {
/*     */   private JLabel label4;
/*     */   private JLabel label1;
/*     */   private FlatTriStateCheckBox triStateCheckBox1;
/*     */   private JLabel triStateLabel1;
/*     */   private JLabel label2;
/*     */   private JPanel svgIconsPanel;
/*     */   private JLabel label3;
/*     */   
/*     */   public ExtrasPanel() {
/*  31 */     initComponents();
/*     */     
/*  33 */     this.triStateLabel1.setText(this.triStateCheckBox1.getState().toString());
/*     */     
/*  35 */     addSVGIcon("actions/copy.svg");
/*  36 */     addSVGIcon("actions/colors.svg");
/*  37 */     addSVGIcon("actions/execute.svg");
/*  38 */     addSVGIcon("actions/suspend.svg");
/*  39 */     addSVGIcon("actions/intentionBulb.svg");
/*  40 */     addSVGIcon("actions/quickfixOffBulb.svg");
/*     */     
/*  42 */     addSVGIcon("objects/abstractClass.svg");
/*  43 */     addSVGIcon("objects/abstractMethod.svg");
/*  44 */     addSVGIcon("objects/annotationtype.svg");
/*  45 */     addSVGIcon("objects/annotationtype.svg");
/*  46 */     addSVGIcon("objects/css.svg");
/*  47 */     addSVGIcon("objects/javaScript.svg");
/*  48 */     addSVGIcon("objects/xhtml.svg");
/*     */     
/*  50 */     addSVGIcon("errorDialog.svg");
/*  51 */     addSVGIcon("informationDialog.svg");
/*  52 */     addSVGIcon("warningDialog.svg");
/*     */   }
/*     */   
/*     */   private void addSVGIcon(String name) {
/*  56 */     this.svgIconsPanel.add(new JLabel((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/extras/svg/" + name)));
/*     */   }
/*     */   
/*     */   private void triStateCheckBox1Changed() {
/*  60 */     this.triStateLabel1.setText(this.triStateCheckBox1.getState().toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  65 */     this.label4 = new JLabel();
/*  66 */     this.label1 = new JLabel();
/*  67 */     this.triStateCheckBox1 = new FlatTriStateCheckBox();
/*  68 */     this.triStateLabel1 = new JLabel();
/*  69 */     this.label2 = new JLabel();
/*  70 */     this.svgIconsPanel = new JPanel();
/*  71 */     this.label3 = new JLabel();
/*     */ 
/*     */     
/*  74 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[][][left]", "[]para[][][]"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     this.label4.setText("Note: Components on this page require the flatlaf-extras library.");
/*  88 */     add(this.label4, "cell 0 0 3 1");
/*     */ 
/*     */     
/*  91 */     this.label1.setText("TriStateCheckBox:");
/*  92 */     add(this.label1, "cell 0 1");
/*     */ 
/*     */     
/*  95 */     this.triStateCheckBox1.setText("Three States");
/*  96 */     this.triStateCheckBox1.addActionListener(e -> triStateCheckBox1Changed());
/*  97 */     add((Component)this.triStateCheckBox1, "cell 1 1");
/*     */ 
/*     */     
/* 100 */     this.triStateLabel1.setText("text");
/* 101 */     this.triStateLabel1.setEnabled(false);
/* 102 */     add(this.triStateLabel1, "cell 2 1,gapx 30");
/*     */ 
/*     */     
/* 105 */     this.label2.setText("SVG Icons:");
/* 106 */     add(this.label2, "cell 0 2");
/*     */ 
/*     */ 
/*     */     
/* 110 */     this.svgIconsPanel.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[fill]", "[grow,center]"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     add(this.svgIconsPanel, "cell 1 2 2 1");
/*     */ 
/*     */     
/* 120 */     this.label3.setText("The icons may change colors when switching to another theme.");
/* 121 */     add(this.label3, "cell 1 3 2 1");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\extras\ExtrasPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */