/*     */ package com.jgoodies.forms.util;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DefaultUnitConverter
/*     */   extends AbstractUnitConverter
/*     */ {
/*     */   public static final String PROPERTY_AVERAGE_CHARACTER_WIDTH_TEST_STRING = "averageCharacterWidthTestString";
/*     */   public static final String PROPERTY_DEFAULT_DIALOG_FONT = "defaultDialogFont";
/*     */   public static final String OLD_AVERAGE_CHARACTER_TEST_STRING = "X";
/*     */   public static final String MODERN_AVERAGE_CHARACTER_TEST_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
/*     */   public static final String BALANCED_AVERAGE_CHARACTER_TEST_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
/* 102 */   private static final Logger LOGGER = Logger.getLogger(DefaultUnitConverter.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DefaultUnitConverter instance;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private String averageCharWidthTestString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Font defaultDialogFont;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private DialogBaseUnits cachedGlobalDialogBaseUnits = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private DialogBaseUnits cachedDialogBaseUnits = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   private FontMetrics cachedFontMetrics = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private Font cachedDefaultDialogFont = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DefaultUnitConverter getInstance() {
/* 176 */     if (instance == null) {
/* 177 */       instance = new DefaultUnitConverter();
/*     */     }
/* 179 */     return instance;
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
/*     */   public String getAverageCharacterWidthTestString() {
/* 193 */     return this.averageCharWidthTestString;
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
/*     */   public void setAverageCharacterWidthTestString(String newTestString) {
/* 212 */     Preconditions.checkNotBlank(newTestString, "The %1$s must not be null, empty, or whitespace.", new Object[] { "test string" });
/* 213 */     String oldTestString = this.averageCharWidthTestString;
/* 214 */     this.averageCharWidthTestString = newTestString;
/* 215 */     firePropertyChange("averageCharacterWidthTestString", oldTestString, newTestString);
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
/*     */   public Font getDefaultDialogFont() {
/* 231 */     return (this.defaultDialogFont != null) ? this.defaultDialogFont : getCachedDefaultDialogFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultDialogFont(Font newFont) {
/* 242 */     Font oldFont = this.defaultDialogFont;
/* 243 */     this.defaultDialogFont = newFont;
/* 244 */     clearCache();
/* 245 */     firePropertyChange("defaultDialogFont", oldFont, newFont);
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
/*     */   protected double getDialogBaseUnitsX(Component component) {
/* 259 */     return (getDialogBaseUnits(component)).x;
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
/*     */   protected double getDialogBaseUnitsY(Component component) {
/* 271 */     return (getDialogBaseUnits(component)).y;
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
/*     */   private DialogBaseUnits getGlobalDialogBaseUnits() {
/* 284 */     if (this.cachedGlobalDialogBaseUnits == null) {
/* 285 */       this.cachedGlobalDialogBaseUnits = computeGlobalDialogBaseUnits();
/*     */     }
/* 287 */     return this.cachedGlobalDialogBaseUnits;
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
/*     */   private DialogBaseUnits getDialogBaseUnits(Component c) {
/* 303 */     FormUtils.ensureValidCache();
/* 304 */     if (c == null)
/*     */     {
/* 306 */       return getGlobalDialogBaseUnits();
/*     */     }
/* 308 */     FontMetrics fm = c.getFontMetrics(getDefaultDialogFont());
/* 309 */     if (fm.equals(this.cachedFontMetrics)) {
/* 310 */       return this.cachedDialogBaseUnits;
/*     */     }
/* 312 */     DialogBaseUnits dialogBaseUnits = computeDialogBaseUnits(fm);
/* 313 */     this.cachedFontMetrics = fm;
/* 314 */     this.cachedDialogBaseUnits = dialogBaseUnits;
/* 315 */     return dialogBaseUnits;
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
/*     */   private DialogBaseUnits computeDialogBaseUnits(FontMetrics metrics) {
/* 335 */     double averageCharWidth = computeAverageCharWidth(metrics, this.averageCharWidthTestString);
/*     */     
/* 337 */     int ascent = metrics.getAscent();
/* 338 */     double height = (ascent > 14) ? ascent : (ascent + (15 - ascent) / 3);
/* 339 */     DialogBaseUnits dialogBaseUnits = new DialogBaseUnits(averageCharWidth, height);
/*     */     
/* 341 */     if (LOGGER.isLoggable(Level.CONFIG)) {
/* 342 */       LOGGER.config("Computed dialog base units " + dialogBaseUnits + " for: " + metrics.getFont());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     return dialogBaseUnits;
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
/*     */   private DialogBaseUnits computeGlobalDialogBaseUnits() {
/* 362 */     LOGGER.config("Computing global dialog base units...");
/* 363 */     Font dialogFont = getDefaultDialogFont();
/* 364 */     FontMetrics metrics = createDefaultGlobalComponent().getFontMetrics(dialogFont);
/* 365 */     DialogBaseUnits globalDialogBaseUnits = computeDialogBaseUnits(metrics);
/* 366 */     return globalDialogBaseUnits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Font getCachedDefaultDialogFont() {
/* 377 */     FormUtils.ensureValidCache();
/* 378 */     if (this.cachedDefaultDialogFont == null) {
/* 379 */       this.cachedDefaultDialogFont = lookupDefaultDialogFont();
/*     */     }
/* 381 */     return this.cachedDefaultDialogFont;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Font lookupDefaultDialogFont() {
/* 392 */     Font buttonFont = UIManager.getFont("Button.font");
/* 393 */     return (buttonFont != null) ? buttonFont : (new JButton()).getFont();
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
/*     */   private static Component createDefaultGlobalComponent() {
/* 411 */     return new JPanel(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearCache() {
/* 421 */     this.cachedGlobalDialogBaseUnits = null;
/* 422 */     this.cachedFontMetrics = null;
/* 423 */     this.cachedDefaultDialogFont = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class DialogBaseUnits
/*     */   {
/*     */     final double x;
/*     */ 
/*     */     
/*     */     final double y;
/*     */ 
/*     */ 
/*     */     
/*     */     DialogBaseUnits(double dialogBaseUnitsX, double dialogBaseUnitsY) {
/* 438 */       this.x = dialogBaseUnitsX;
/* 439 */       this.y = dialogBaseUnitsY;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 444 */       return "DBU(x=" + this.x + "; y=" + this.y + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\form\\util\DefaultUnitConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */