/*     */ package com.formdev.flatlaf;
/*     */ 
/*     */ import com.formdev.flatlaf.ui.FlatPopupFactory;
/*     */ import com.formdev.flatlaf.ui.JBRCustomDecorations;
/*     */ import com.formdev.flatlaf.util.GrayFilter;
/*     */ import com.formdev.flatlaf.util.MultiResolutionImageSupport;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.PopupFactory;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UnsupportedLookAndFeelException;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*     */ import javax.swing.text.StyleContext;
/*     */ import javax.swing.text.html.HTMLEditorKit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FlatLaf
/*     */   extends BasicLookAndFeel
/*     */ {
/*  77 */   static final Logger LOG = Logger.getLogger(FlatLaf.class.getName());
/*     */   
/*     */   private static final String DESKTOPFONTHINTS = "awt.font.desktophints";
/*     */   
/*     */   private static List<Object> customDefaultsSources;
/*     */   
/*     */   private String desktopPropertyName;
/*     */   
/*     */   private String desktopPropertyName2;
/*     */   
/*     */   private PropertyChangeListener desktopPropertyListener;
/*     */   
/*     */   private static boolean aquaLoaded;
/*     */   
/*     */   private static boolean updateUIPending;
/*     */   
/*     */   private PopupFactory oldPopupFactory;
/*     */   
/*     */   private MnemonicHandler mnemonicHandler;
/*     */   
/*     */   private Consumer<UIDefaults> postInitialization;
/*     */   private Boolean oldFrameWindowDecorated;
/*     */   private Boolean oldDialogWindowDecorated;
/*     */   
/*     */   public static boolean install(LookAndFeel newLookAndFeel) {
/*     */     try {
/* 103 */       UIManager.setLookAndFeel(newLookAndFeel);
/* 104 */       return true;
/* 105 */     } catch (Exception ex) {
/* 106 */       LOG.log(Level.SEVERE, "FlatLaf: Failed to initialize look and feel '" + newLookAndFeel.getClass().getName() + "'.", ex);
/* 107 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void installLafInfo(String lafName, Class<? extends LookAndFeel> lafClass) {
/* 118 */     UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(lafName, lafClass.getName()));
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
/*     */   public String getID() {
/* 131 */     return "FlatLaf - " + getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLafDark() {
/* 140 */     LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
/* 141 */     return (lookAndFeel instanceof FlatLaf && ((FlatLaf)lookAndFeel).isDark());
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
/*     */   public boolean getSupportsWindowDecorations() {
/* 167 */     if (SystemInfo.isJetBrainsJVM_11_orLater && SystemInfo.isWindows_10_orLater)
/*     */     {
/* 169 */       if (JBRCustomDecorations.isSupported())
/* 170 */         return false; 
/*     */     }
/* 172 */     return SystemInfo.isWindows_10_orLater;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNativeLookAndFeel() {
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSupportedLookAndFeel() {
/* 182 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon getDisabledIcon(JComponent component, Icon icon) {
/* 187 */     if (icon instanceof DisabledIconProvider) {
/* 188 */       return ((DisabledIconProvider)icon).getDisabledIcon();
/*     */     }
/* 190 */     if (icon instanceof ImageIcon) {
/* 191 */       Object grayFilter = UIManager.get("Component.grayFilter");
/*     */ 
/*     */       
/* 194 */       ImageFilter filter = (grayFilter instanceof ImageFilter) ? (ImageFilter)grayFilter : (ImageFilter)GrayFilter.createDisabledIconFilter(isDark());
/*     */       
/* 196 */       Function<Image, Image> mapper = img -> {
/*     */           ImageProducer producer = new FilteredImageSource(img.getSource(), filter);
/*     */           
/*     */           return Toolkit.getDefaultToolkit().createImage(producer);
/*     */         };
/* 201 */       Image image = ((ImageIcon)icon).getImage();
/* 202 */       return new ImageIconUIResource(MultiResolutionImageSupport.map(image, mapper));
/*     */     } 
/*     */     
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 210 */     if (SystemInfo.isMacOS) {
/* 211 */       initializeAqua();
/*     */     }
/* 213 */     super.initialize();
/*     */ 
/*     */     
/* 216 */     this.oldPopupFactory = PopupFactory.getSharedInstance();
/* 217 */     PopupFactory.setSharedInstance((PopupFactory)new FlatPopupFactory());
/*     */ 
/*     */     
/* 220 */     this.mnemonicHandler = new MnemonicHandler();
/* 221 */     this.mnemonicHandler.install();
/*     */ 
/*     */     
/* 224 */     if (SystemInfo.isWindows) {
/*     */ 
/*     */       
/* 227 */       this.desktopPropertyName = "win.messagebox.font";
/* 228 */     } else if (SystemInfo.isLinux) {
/*     */       
/* 230 */       this.desktopPropertyName = "gnome.Gtk/FontName";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       this.desktopPropertyName2 = "gnome.Xft/DPI";
/*     */     } 
/* 238 */     if (this.desktopPropertyName != null) {
/* 239 */       this.desktopPropertyListener = (e -> {
/*     */           String propertyName = e.getPropertyName();
/*     */           
/*     */           if (this.desktopPropertyName.equals(propertyName) || propertyName.equals(this.desktopPropertyName2)) {
/*     */             reSetLookAndFeel();
/*     */           } else if ("awt.font.desktophints".equals(propertyName) && UIManager.getLookAndFeel() instanceof FlatLaf) {
/*     */             putAATextInfo(UIManager.getLookAndFeelDefaults());
/*     */             
/*     */             updateUILater();
/*     */           } 
/*     */         });
/* 250 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 251 */       toolkit.addPropertyChangeListener(this.desktopPropertyName, this.desktopPropertyListener);
/* 252 */       if (this.desktopPropertyName2 != null)
/* 253 */         toolkit.addPropertyChangeListener(this.desktopPropertyName2, this.desktopPropertyListener); 
/* 254 */       toolkit.addPropertyChangeListener("awt.font.desktophints", this.desktopPropertyListener);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     this.postInitialization = (defaults -> {
/*     */         Color linkColor = defaults.getColor("Component.linkColor");
/*     */ 
/*     */ 
/*     */         
/*     */         if (linkColor != null) {
/*     */           (new HTMLEditorKit()).getStyleSheet().addRule(String.format("a { color: #%06x; }", new Object[] { Integer.valueOf(linkColor.getRGB() & 0xFFFFFF) }));
/*     */         }
/*     */       });
/*     */ 
/*     */     
/* 271 */     Boolean useWindowDecorations = FlatSystemProperties.getBooleanStrict("flatlaf.useWindowDecorations", null);
/* 272 */     if (useWindowDecorations != null) {
/* 273 */       this.oldFrameWindowDecorated = Boolean.valueOf(JFrame.isDefaultLookAndFeelDecorated());
/* 274 */       this.oldDialogWindowDecorated = Boolean.valueOf(JDialog.isDefaultLookAndFeelDecorated());
/* 275 */       JFrame.setDefaultLookAndFeelDecorated(useWindowDecorations.booleanValue());
/* 276 */       JDialog.setDefaultLookAndFeelDecorated(useWindowDecorations.booleanValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninitialize() {
/* 283 */     if (this.desktopPropertyListener != null) {
/* 284 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 285 */       toolkit.removePropertyChangeListener(this.desktopPropertyName, this.desktopPropertyListener);
/* 286 */       if (this.desktopPropertyName2 != null)
/* 287 */         toolkit.removePropertyChangeListener(this.desktopPropertyName2, this.desktopPropertyListener); 
/* 288 */       toolkit.removePropertyChangeListener("awt.font.desktophints", this.desktopPropertyListener);
/* 289 */       this.desktopPropertyName = null;
/* 290 */       this.desktopPropertyName2 = null;
/* 291 */       this.desktopPropertyListener = null;
/*     */     } 
/*     */ 
/*     */     
/* 295 */     if (this.oldPopupFactory != null) {
/* 296 */       PopupFactory.setSharedInstance(this.oldPopupFactory);
/* 297 */       this.oldPopupFactory = null;
/*     */     } 
/*     */ 
/*     */     
/* 301 */     if (this.mnemonicHandler != null) {
/* 302 */       this.mnemonicHandler.uninstall();
/* 303 */       this.mnemonicHandler = null;
/*     */     } 
/*     */ 
/*     */     
/* 307 */     (new HTMLEditorKit()).getStyleSheet().addRule("a { color: blue; }");
/* 308 */     this.postInitialization = null;
/*     */ 
/*     */     
/* 311 */     if (this.oldFrameWindowDecorated != null) {
/* 312 */       JFrame.setDefaultLookAndFeelDecorated(this.oldFrameWindowDecorated.booleanValue());
/* 313 */       JDialog.setDefaultLookAndFeelDecorated(this.oldDialogWindowDecorated.booleanValue());
/* 314 */       this.oldFrameWindowDecorated = null;
/* 315 */       this.oldDialogWindowDecorated = null;
/*     */     } 
/*     */     
/* 318 */     super.uninitialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeAqua() {
/*     */     BasicLookAndFeel aquaLaf;
/* 329 */     if (aquaLoaded) {
/*     */       return;
/*     */     }
/* 332 */     aquaLoaded = true;
/*     */ 
/*     */     
/* 335 */     String aquaLafClassName = "com.apple.laf.AquaLookAndFeel";
/*     */     
/*     */     try {
/* 338 */       if (SystemInfo.isJava_9_orLater)
/* 339 */       { Method m = UIManager.class.getMethod("createLookAndFeel", new Class[] { String.class });
/* 340 */         aquaLaf = (BasicLookAndFeel)m.invoke(null, new Object[] { "Mac OS X" }); }
/*     */       else
/* 342 */       { aquaLaf = (BasicLookAndFeel)Class.forName(aquaLafClassName).newInstance(); } 
/* 343 */     } catch (Exception ex) {
/* 344 */       LOG.log(Level.SEVERE, "FlatLaf: Failed to initialize Aqua look and feel '" + aquaLafClassName + "'.", ex);
/* 345 */       throw new IllegalStateException();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 350 */     PopupFactory oldPopupFactory = PopupFactory.getSharedInstance();
/*     */ 
/*     */     
/* 353 */     aquaLaf.initialize();
/* 354 */     aquaLaf.uninitialize();
/*     */ 
/*     */     
/* 357 */     PopupFactory.setSharedInstance(oldPopupFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public UIDefaults getDefaults() {
/* 362 */     UIDefaults defaults = super.getDefaults();
/*     */ 
/*     */ 
/*     */     
/* 366 */     defaults.put("laf.dark", Boolean.valueOf(isDark()));
/*     */ 
/*     */     
/* 369 */     defaults.addResourceBundle("com.formdev.flatlaf.resources.Bundle");
/*     */ 
/*     */ 
/*     */     
/* 373 */     putDefaults(defaults, defaults.getColor("control"), new String[] { "Button.disabledBackground", "EditorPane.disabledBackground", "EditorPane.inactiveBackground", "FormattedTextField.disabledBackground", "PasswordField.disabledBackground", "Spinner.disabledBackground", "TextArea.disabledBackground", "TextArea.inactiveBackground", "TextField.disabledBackground", "TextPane.disabledBackground", "TextPane.inactiveBackground", "ToggleButton.disabledBackground" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     putDefaults(defaults, defaults.getColor("textInactiveText"), new String[] { "Button.disabledText", "CheckBox.disabledText", "CheckBoxMenuItem.disabledForeground", "Menu.disabledForeground", "MenuItem.disabledForeground", "RadioButton.disabledText", "RadioButtonMenuItem.disabledForeground", "Spinner.disabledForeground", "ToggleButton.disabledText" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 396 */     putDefaults(defaults, defaults.getColor("textText"), new String[] { "DesktopIcon.foreground" });
/*     */ 
/*     */     
/* 399 */     initFonts(defaults);
/* 400 */     initIconColors(defaults, isDark());
/* 401 */     FlatInputMaps.initInputMaps(defaults);
/*     */ 
/*     */     
/* 404 */     ServiceLoader<FlatDefaultsAddon> addonLoader = ServiceLoader.load(FlatDefaultsAddon.class);
/* 405 */     List<FlatDefaultsAddon> addons = new ArrayList<>();
/* 406 */     for (FlatDefaultsAddon addon : addonLoader)
/* 407 */       addons.add(addon); 
/* 408 */     addons.sort((addon1, addon2) -> addon1.getPriority() - addon2.getPriority());
/*     */ 
/*     */     
/* 411 */     List<Class<?>> lafClassesForDefaultsLoading = getLafClassesForDefaultsLoading();
/* 412 */     if (lafClassesForDefaultsLoading != null) {
/* 413 */       UIDefaultsLoader.loadDefaultsFromProperties(lafClassesForDefaultsLoading, addons, getAdditionalDefaults(), isDark(), defaults);
/*     */     } else {
/* 415 */       UIDefaultsLoader.loadDefaultsFromProperties(getClass(), addons, getAdditionalDefaults(), isDark(), defaults);
/*     */     } 
/*     */     
/* 418 */     if (SystemInfo.isMacOS && Boolean.getBoolean("apple.laf.useScreenMenuBar")) {
/* 419 */       defaults.put("MenuBarUI", "com.apple.laf.AquaMenuBarUI");
/*     */ 
/*     */       
/* 422 */       defaults.put("MenuBar.backgroundPainter", BorderFactory.createEmptyBorder());
/*     */     } 
/*     */ 
/*     */     
/* 426 */     putAATextInfo(defaults);
/*     */ 
/*     */     
/* 429 */     applyAdditionalDefaults(defaults);
/*     */ 
/*     */     
/* 432 */     for (FlatDefaultsAddon addon : addons) {
/* 433 */       addon.afterDefaultsLoading(this, defaults);
/*     */     }
/*     */     
/* 436 */     defaults.put("laf.scaleFactor", t -> Float.valueOf(UIScale.getUserScaleFactor()));
/*     */ 
/*     */ 
/*     */     
/* 440 */     if (this.postInitialization != null) {
/* 441 */       this.postInitialization.accept(defaults);
/* 442 */       this.postInitialization = null;
/*     */     } 
/*     */     
/* 445 */     return defaults;
/*     */   }
/*     */ 
/*     */   
/*     */   void applyAdditionalDefaults(UIDefaults defaults) {}
/*     */   
/*     */   protected List<Class<?>> getLafClassesForDefaultsLoading() {
/* 452 */     return null;
/*     */   }
/*     */   
/*     */   protected Properties getAdditionalDefaults() {
/* 456 */     return null;
/*     */   }
/*     */   
/*     */   private void initFonts(UIDefaults defaults) {
/* 460 */     FontUIResource uiFont = null;
/*     */     
/* 462 */     if (SystemInfo.isWindows) {
/* 463 */       Font winFont = (Font)Toolkit.getDefaultToolkit().getDesktopProperty("win.messagebox.font");
/* 464 */       if (winFont != null) {
/* 465 */         uiFont = createCompositeFont(winFont.getFamily(), winFont.getStyle(), winFont.getSize());
/*     */       }
/* 467 */     } else if (SystemInfo.isMacOS) {
/*     */       String fontName;
/* 469 */       if (SystemInfo.isMacOS_10_15_Catalina_orLater) {
/* 470 */         if (SystemInfo.isJetBrainsJVM_11_orLater) {
/*     */           
/* 472 */           fontName = ".AppleSystemUIFont";
/*     */         } else {
/*     */           
/* 475 */           fontName = "Helvetica Neue";
/*     */         } 
/* 477 */       } else if (SystemInfo.isMacOS_10_11_ElCapitan_orLater) {
/*     */         
/* 479 */         fontName = ".SF NS Text";
/*     */       } else {
/*     */         
/* 482 */         fontName = "Lucida Grande";
/*     */       } 
/*     */       
/* 485 */       uiFont = createCompositeFont(fontName, 0, 13);
/*     */     }
/* 487 */     else if (SystemInfo.isLinux) {
/* 488 */       Font font = LinuxFontPolicy.getFont();
/* 489 */       uiFont = (font instanceof FontUIResource) ? (FontUIResource)font : new FontUIResource(font);
/*     */     } 
/*     */ 
/*     */     
/* 493 */     if (uiFont == null) {
/* 494 */       uiFont = createCompositeFont("SansSerif", 0, 12);
/*     */     }
/*     */     
/* 497 */     uiFont = UIScale.applyCustomScaleFactor(uiFont);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 502 */     Object activeFont = new ActiveFont(1.0F);
/*     */ 
/*     */     
/* 505 */     for (Object key : defaults.keySet()) {
/* 506 */       if (key instanceof String && (((String)key).endsWith(".font") || ((String)key).endsWith("Font"))) {
/* 507 */         defaults.put(key, activeFont);
/*     */       }
/*     */     } 
/*     */     
/* 511 */     defaults.put("ProgressBar.font", new ActiveFont(0.85F));
/*     */ 
/*     */     
/* 514 */     defaults.put("defaultFont", uiFont);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static FontUIResource createCompositeFont(String family, int style, int size) {
/* 521 */     Font font = StyleContext.getDefaultStyleContext().getFont(family, style, size);
/* 522 */     return (font instanceof FontUIResource) ? (FontUIResource)font : new FontUIResource(font);
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
/*     */   public static void initIconColors(UIDefaults defaults, boolean dark) {
/* 542 */     for (FlatIconColors c : FlatIconColors.values()) {
/* 543 */       if (c.light == (!dark) || c.dark == dark)
/* 544 */         defaults.put(c.key, new ColorUIResource(c.rgb)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putAATextInfo(UIDefaults defaults) {
/* 549 */     if (SystemInfo.isMacOS && SystemInfo.isJetBrainsJVM) {
/*     */ 
/*     */ 
/*     */       
/* 553 */       defaults.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 554 */     } else if (SystemInfo.isJava_9_orLater) {
/* 555 */       Object desktopHints = Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
/* 556 */       if (desktopHints instanceof Map) {
/*     */         
/* 558 */         Map<Object, Object> hints = (Map<Object, Object>)desktopHints;
/* 559 */         Object aaHint = hints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
/* 560 */         if (aaHint != null && aaHint != RenderingHints.VALUE_TEXT_ANTIALIAS_OFF && aaHint != RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
/*     */ 
/*     */ 
/*     */           
/* 564 */           defaults.put(RenderingHints.KEY_TEXT_ANTIALIASING, aaHint);
/* 565 */           defaults.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, hints
/* 566 */               .get(RenderingHints.KEY_TEXT_LCD_CONTRAST));
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 574 */         Object key = Class.forName("sun.swing.SwingUtilities2").getField("AA_TEXT_PROPERTY_KEY").get(null);
/*     */ 
/*     */         
/* 577 */         Object value = Class.forName("sun.swing.SwingUtilities2$AATextInfo").getMethod("getAATextInfo", new Class[] { boolean.class }).invoke(null, new Object[] { Boolean.valueOf(true) });
/* 578 */         defaults.put(key, value);
/* 579 */       } catch (Exception ex) {
/* 580 */         Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 581 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putDefaults(UIDefaults defaults, Object value, String... keys) {
/* 587 */     for (String key : keys)
/* 588 */       defaults.put(key, value); 
/*     */   }
/*     */   
/*     */   static List<Object> getCustomDefaultsSources() {
/* 592 */     return customDefaultsSources;
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
/*     */   public static void registerCustomDefaultsSource(String packageName) {
/* 615 */     registerCustomDefaultsSource(packageName, null);
/*     */   }
/*     */   
/*     */   public static void unregisterCustomDefaultsSource(String packageName) {
/* 619 */     unregisterCustomDefaultsSource(packageName, null);
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
/*     */   public static void registerCustomDefaultsSource(String packageName, ClassLoader classLoader) {
/* 631 */     if (customDefaultsSources == null)
/* 632 */       customDefaultsSources = new ArrayList(); 
/* 633 */     customDefaultsSources.add(packageName);
/* 634 */     customDefaultsSources.add(classLoader);
/*     */   }
/*     */   
/*     */   public static void unregisterCustomDefaultsSource(String packageName, ClassLoader classLoader) {
/* 638 */     if (customDefaultsSources == null) {
/*     */       return;
/*     */     }
/* 641 */     int size = customDefaultsSources.size();
/* 642 */     for (int i = 0; i < size - 1; i++) {
/* 643 */       Object source = customDefaultsSources.get(i);
/* 644 */       if (packageName.equals(source) && customDefaultsSources.get(i + 1) == classLoader) {
/* 645 */         customDefaultsSources.remove(i + 1);
/* 646 */         customDefaultsSources.remove(i);
/*     */         break;
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
/*     */   public static void registerCustomDefaultsSource(File folder) {
/* 660 */     if (customDefaultsSources == null)
/* 661 */       customDefaultsSources = new ArrayList(); 
/* 662 */     customDefaultsSources.add(folder);
/*     */   }
/*     */   
/*     */   public static void unregisterCustomDefaultsSource(File folder) {
/* 666 */     if (customDefaultsSources == null) {
/*     */       return;
/*     */     }
/* 669 */     customDefaultsSources.remove(folder);
/*     */   }
/*     */   
/*     */   private static void reSetLookAndFeel() {
/* 673 */     EventQueue.invokeLater(() -> {
/*     */           LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
/*     */           
/*     */           try {
/*     */             UIManager.setLookAndFeel(lookAndFeel);
/*     */             
/*     */             PropertyChangeEvent e = new PropertyChangeEvent(UIManager.class, "lookAndFeel", lookAndFeel, lookAndFeel);
/*     */             
/*     */             for (PropertyChangeListener l : UIManager.getPropertyChangeListeners()) {
/*     */               l.propertyChange(e);
/*     */             }
/*     */             
/*     */             updateUI();
/* 686 */           } catch (UnsupportedLookAndFeelException ex) {
/*     */             LOG.log(Level.SEVERE, "FlatLaf: Failed to reinitialize look and feel '" + lookAndFeel.getClass().getName() + "'.", ex);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateUI() {
/* 697 */     for (Window w : Window.getWindows()) {
/* 698 */       SwingUtilities.updateComponentTreeUI(w);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateUILater() {
/* 705 */     synchronized (FlatLaf.class) {
/* 706 */       if (updateUIPending) {
/*     */         return;
/*     */       }
/* 709 */       updateUIPending = true;
/*     */     } 
/*     */     
/* 712 */     EventQueue.invokeLater(() -> {
/*     */           updateUI();
/*     */           synchronized (FlatLaf.class) {
/*     */             updateUIPending = false;
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static boolean isShowMnemonics() {
/* 721 */     return MnemonicHandler.isShowMnemonics();
/*     */   }
/*     */   
/*     */   public static void showMnemonics(Component c) {
/* 725 */     MnemonicHandler.showMnemonics(true, c);
/*     */   }
/*     */   
/*     */   public static void hideMnemonics() {
/* 729 */     MnemonicHandler.showMnemonics(false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 735 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 741 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isDark();
/*     */ 
/*     */   
/*     */   private static class ActiveFont
/*     */     implements UIDefaults.ActiveValue
/*     */   {
/*     */     private final float scaleFactor;
/*     */     private Font font;
/*     */     private Font lastDefaultFont;
/*     */     
/*     */     ActiveFont(float scaleFactor) {
/* 756 */       this.scaleFactor = scaleFactor;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object createValue(UIDefaults table) {
/* 761 */       Font defaultFont = UIManager.getFont("defaultFont");
/*     */       
/* 763 */       if (this.lastDefaultFont != defaultFont) {
/* 764 */         this.lastDefaultFont = defaultFont;
/*     */         
/* 766 */         if (this.scaleFactor != 1.0F) {
/*     */           
/* 768 */           int newFontSize = Math.round(defaultFont.getSize() * this.scaleFactor);
/* 769 */           this.font = new FontUIResource(defaultFont.deriveFont(newFontSize));
/*     */         } else {
/*     */           
/* 772 */           this.font = (defaultFont instanceof UIResource) ? defaultFont : new FontUIResource(defaultFont);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 778 */       return this.font;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ImageIconUIResource
/*     */     extends ImageIcon
/*     */     implements UIResource
/*     */   {
/*     */     ImageIconUIResource(Image image) {
/* 789 */       super(image);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface DisabledIconProvider {
/*     */     Icon getDisabledIcon();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\FlatLaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */