/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.FormsSetup;
/*     */ import com.jgoodies.forms.internal.AbstractFormBuilder;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
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
/*     */ @Deprecated
/*     */ public class PanelBuilder
/*     */   extends AbstractFormBuilder<PanelBuilder>
/*     */ {
/*     */   private static final String LABELED_BY_PROPERTY = "labeledBy";
/*     */   private boolean labelForFeatureEnabled;
/* 136 */   private WeakReference mostRecentlyAddedLabelReference = null;
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
/*     */   public PanelBuilder(FormLayout layout) {
/* 151 */     this(layout, new JPanel(null));
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
/*     */   public PanelBuilder(FormLayout layout, JPanel panel) {
/* 164 */     super(layout, panel);
/* 165 */     opaque(FormsSetup.getOpaqueDefault());
/* 166 */     this.labelForFeatureEnabled = FormsSetup.getLabelForFeatureEnabledDefault();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PanelBuilder focusTraversal(FocusTraversalPolicy policy) {
/* 189 */     getPanel().setFocusTraversalPolicy(policy);
/* 190 */     getPanel().setFocusTraversalPolicyProvider(true);
/* 191 */     return this;
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
/*     */   public PanelBuilder labelForFeatureEnabled(boolean b) {
/* 204 */     this.labelForFeatureEnabled = b;
/* 205 */     return this;
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
/*     */   public final JPanel build() {
/* 221 */     return getPanel();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addLabel(String textWithMnemonic) {
/* 244 */     return addLabel(textWithMnemonic, cellConstraints());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addLabel(String textWithMnemonic, CellConstraints constraints) {
/* 266 */     JLabel label = getComponentFactory().createLabel(textWithMnemonic);
/* 267 */     add(label, constraints);
/* 268 */     return label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addLabel(String textWithMnemonic, String encodedConstraints) {
/* 290 */     return addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
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
/*     */   public final JLabel addLabel(String textWithMnemonic, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
/* 346 */     if (labelConstraints == componentConstraints) {
/* 347 */       throw new IllegalArgumentException("You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC class. See the JavaDocs for details.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     JLabel label = addLabel(textWithMnemonic, labelConstraints);
/* 354 */     add(component, componentConstraints);
/* 355 */     label.setLabelFor(component);
/* 356 */     return label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addROLabel(String textWithMnemonic) {
/* 380 */     return addROLabel(textWithMnemonic, cellConstraints());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addROLabel(String textWithMnemonic, CellConstraints constraints) {
/* 403 */     JLabel label = getComponentFactory().createReadOnlyLabel(textWithMnemonic);
/* 404 */     add(label, constraints);
/* 405 */     return label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addROLabel(String textWithMnemonic, String encodedConstraints) {
/* 428 */     return addROLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
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
/*     */   public final JLabel addROLabel(String textWithMnemonic, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
/* 484 */     checkConstraints(labelConstraints, componentConstraints);
/* 485 */     JLabel label = addROLabel(textWithMnemonic, labelConstraints);
/* 486 */     add(component, componentConstraints);
/* 487 */     label.setLabelFor(component);
/* 488 */     return label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addTitle(String textWithMnemonic) {
/* 511 */     return addTitle(textWithMnemonic, cellConstraints());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addTitle(String textWithMnemonic, CellConstraints constraints) {
/* 533 */     JLabel titleLabel = getComponentFactory().createTitle(textWithMnemonic);
/* 534 */     add(titleLabel, constraints);
/* 535 */     return titleLabel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addTitle(String textWithMnemonic, String encodedConstraints) {
/* 557 */     return addTitle(textWithMnemonic, new CellConstraints(encodedConstraints));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final JComponent addSeparator(String textWithMnemonic) {
/* 578 */     return addSeparator(textWithMnemonic, getLayout().getColumnCount());
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
/*     */ 
/*     */   
/*     */   public final JComponent addSeparator(String textWithMnemonic, CellConstraints constraints) {
/* 598 */     int titleAlignment = isLeftToRight() ? 2 : 4;
/*     */ 
/*     */     
/* 601 */     JComponent titledSeparator = getComponentFactory().createSeparator(textWithMnemonic, titleAlignment);
/*     */     
/* 603 */     add(titledSeparator, constraints);
/* 604 */     return titledSeparator;
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
/*     */ 
/*     */   
/*     */   public final JComponent addSeparator(String textWithMnemonic, String encodedConstraints) {
/* 624 */     return addSeparator(textWithMnemonic, new CellConstraints(encodedConstraints));
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
/*     */ 
/*     */   
/*     */   public final JComponent addSeparator(String textWithMnemonic, int columnSpan) {
/* 644 */     return addSeparator(textWithMnemonic, createLeftAdjustedConstraints(columnSpan));
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
/*     */   public final JLabel add(JLabel label, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
/* 697 */     checkConstraints(labelConstraints, componentConstraints);
/* 698 */     add(label, labelConstraints);
/* 699 */     add(component, componentConstraints);
/* 700 */     label.setLabelFor(component);
/* 701 */     return label;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Component add(Component component, CellConstraints cellConstraints) {
/* 722 */     Component result = super.add(component, cellConstraints);
/* 723 */     manageLabelsAndComponents(component);
/* 724 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void manageLabelsAndComponents(Component c) {
/* 731 */     if (!this.labelForFeatureEnabled) {
/*     */       return;
/*     */     }
/* 734 */     if (c instanceof JLabel) {
/* 735 */       JLabel label = (JLabel)c;
/* 736 */       if (label.getLabelFor() == null) {
/* 737 */         setMostRecentlyAddedLabel(label);
/*     */       } else {
/* 739 */         clearMostRecentlyAddedLabel();
/*     */       } 
/*     */       return;
/*     */     } 
/* 743 */     JLabel mostRecentlyAddedLabel = getMostRecentlyAddedLabel();
/* 744 */     if (mostRecentlyAddedLabel != null && isLabelForApplicable(mostRecentlyAddedLabel, c)) {
/*     */       
/* 746 */       setLabelFor(mostRecentlyAddedLabel, c);
/* 747 */       clearMostRecentlyAddedLabel();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLabelForApplicable(JLabel label, Component component) {
/* 767 */     if (label.getLabelFor() != null) {
/* 768 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 772 */     if (!component.isFocusable()) {
/* 773 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 777 */     if (!(component instanceof JComponent)) {
/* 778 */       return true;
/*     */     }
/* 780 */     JComponent c = (JComponent)component;
/* 781 */     return (c.getClientProperty("labeledBy") == null);
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
/*     */   protected void setLabelFor(JLabel label, Component component) {
/*     */     Component labeledComponent;
/* 797 */     if (component instanceof JScrollPane) {
/* 798 */       JScrollPane scrollPane = (JScrollPane)component;
/* 799 */       labeledComponent = scrollPane.getViewport().getView();
/*     */     } else {
/* 801 */       labeledComponent = component;
/*     */     } 
/* 803 */     label.setLabelFor(labeledComponent);
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
/*     */   private JLabel getMostRecentlyAddedLabel() {
/* 820 */     if (this.mostRecentlyAddedLabelReference == null) {
/* 821 */       return null;
/*     */     }
/* 823 */     JLabel label = this.mostRecentlyAddedLabelReference.get();
/* 824 */     if (label == null) {
/* 825 */       return null;
/*     */     }
/* 827 */     return label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setMostRecentlyAddedLabel(JLabel label) {
/* 837 */     this.mostRecentlyAddedLabelReference = new WeakReference<JLabel>(label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearMostRecentlyAddedLabel() {
/* 845 */     this.mostRecentlyAddedLabelReference = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkConstraints(CellConstraints c1, CellConstraints c2) {
/* 850 */     Preconditions.checkArgument((c1 != c2), "You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC factory. See the JavaDocs for details.");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\PanelBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */