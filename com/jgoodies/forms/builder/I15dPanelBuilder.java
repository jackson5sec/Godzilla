/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.internal.ResourceBundleAccessor;
/*     */ import com.jgoodies.common.internal.StringResourceAccess;
/*     */ import com.jgoodies.common.internal.StringResourceAccessor;
/*     */ import com.jgoodies.forms.FormsSetup;
/*     */ import com.jgoodies.forms.internal.AbstractBuilder;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class I15dPanelBuilder
/*     */   extends PanelBuilder
/*     */ {
/*     */   private final StringResourceAccessor resources;
/*  98 */   private boolean debugToolTipsEnabled = FormsSetup.getDebugToolTipsEnabledDefault();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle) {
/* 114 */     this(layout, bundle, new JPanel(null));
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
/*     */   public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle, JPanel container) {
/* 130 */     this(layout, (StringResourceAccessor)new ResourceBundleAccessor(bundle), container);
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
/*     */   public I15dPanelBuilder(FormLayout layout, StringResourceAccessor localizer) {
/* 144 */     this(layout, localizer, new JPanel(null));
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
/*     */   public I15dPanelBuilder(FormLayout layout, StringResourceAccessor localizer, JPanel container) {
/* 159 */     super(layout, container);
/* 160 */     this.resources = localizer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder background(Color background) {
/* 168 */     super.background(background);
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder border(Border border) {
/* 175 */     super.border(border);
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder border(String emptyBorderSpec) {
/* 182 */     super.border(emptyBorderSpec);
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder padding(EmptyBorder padding) {
/* 189 */     super.padding(padding);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder padding(String paddingSpec, Object... args) {
/* 196 */     super.padding(paddingSpec, new Object[0]);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder opaque(boolean b) {
/* 203 */     super.opaque(b);
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder focusTraversal(FocusTraversalPolicy policy) {
/* 210 */     super.focusTraversal(policy);
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public I15dPanelBuilder debugToolTipsEnabled(boolean b) {
/* 216 */     this.debugToolTipsEnabled = b;
/* 217 */     return this;
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
/*     */   public final JLabel addI15dLabel(String resourceKey, CellConstraints constraints) {
/* 232 */     JLabel label = addLabel(getResourceString(resourceKey), constraints);
/* 233 */     if (isDebugToolTipsEnabled()) {
/* 234 */       label.setToolTipText(resourceKey);
/*     */     }
/* 236 */     return label;
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
/*     */   public final JLabel addI15dLabel(String resourceKey, String encodedConstraints) {
/* 248 */     return addI15dLabel(resourceKey, new CellConstraints(encodedConstraints));
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
/*     */   public final JLabel addI15dLabel(String resourceKey, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
/* 297 */     JLabel label = addLabel(getResourceString(resourceKey), labelConstraints, component, componentConstraints);
/*     */     
/* 299 */     if (isDebugToolTipsEnabled()) {
/* 300 */       label.setToolTipText(resourceKey);
/*     */     }
/* 302 */     return label;
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
/*     */   public final JLabel addI15dROLabel(String resourceKey, CellConstraints constraints) {
/* 319 */     JLabel label = addROLabel(getResourceString(resourceKey), constraints);
/* 320 */     if (isDebugToolTipsEnabled()) {
/* 321 */       label.setToolTipText(resourceKey);
/*     */     }
/* 323 */     return label;
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
/*     */   public final JLabel addI15dROLabel(String resourceKey, String encodedConstraints) {
/* 338 */     return addI15dROLabel(resourceKey, new CellConstraints(encodedConstraints));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JLabel addI15dROLabel(String resourceKey, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
/* 399 */     JLabel label = addROLabel(getResourceString(resourceKey), labelConstraints, component, componentConstraints);
/*     */ 
/*     */     
/* 402 */     if (isDebugToolTipsEnabled()) {
/* 403 */       label.setToolTipText(resourceKey);
/*     */     }
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
/*     */   public final JComponent addI15dSeparator(String resourceKey, CellConstraints constraints) {
/* 420 */     JComponent component = addSeparator(getResourceString(resourceKey), constraints);
/* 421 */     if (isDebugToolTipsEnabled()) {
/* 422 */       component.setToolTipText(resourceKey);
/*     */     }
/* 424 */     return component;
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
/*     */   public final JComponent addI15dSeparator(String resourceKey, String encodedConstraints) {
/* 437 */     return addI15dSeparator(resourceKey, new CellConstraints(encodedConstraints));
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
/*     */   public final JLabel addI15dTitle(String resourceKey, CellConstraints constraints) {
/* 449 */     JLabel label = addTitle(getResourceString(resourceKey), constraints);
/* 450 */     if (isDebugToolTipsEnabled()) {
/* 451 */       label.setToolTipText(resourceKey);
/*     */     }
/* 453 */     return label;
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
/*     */   public final JLabel addI15dTitle(String resourceKey, String encodedConstraints) {
/* 465 */     return addI15dTitle(resourceKey, new CellConstraints(encodedConstraints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isDebugToolTipsEnabled() {
/* 472 */     return this.debugToolTipsEnabled;
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
/*     */   protected final String getResourceString(String key) {
/* 489 */     return StringResourceAccess.getResourceString(this.resources, key, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\I15dPanelBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */