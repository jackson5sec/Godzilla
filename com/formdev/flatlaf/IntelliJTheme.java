/*     */ package com.formdev.flatlaf;
/*     */ 
/*     */ import com.formdev.flatlaf.json.Json;
/*     */ import com.formdev.flatlaf.json.ParseException;
/*     */ import com.formdev.flatlaf.util.StringUtils;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntelliJTheme
/*     */ {
/*     */   public final String name;
/*     */   public final boolean dark;
/*     */   public final String author;
/*     */   private final boolean isMaterialUILite;
/*     */   private final Map<String, String> colors;
/*     */   private final Map<String, Object> ui;
/*     */   private final Map<String, Object> icons;
/*     */   private Map<String, ColorUIResource> namedColors;
/*     */   
/*     */   public static boolean install(InputStream in) {
/*     */     try {
/*  77 */       return FlatLaf.install(createLaf(in));
/*  78 */     } catch (Exception ex) {
/*  79 */       FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: Failed to load IntelliJ theme", ex);
/*  80 */       return false;
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
/*     */   public static FlatLaf createLaf(InputStream in) throws IOException {
/*  94 */     return createLaf(new IntelliJTheme(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FlatLaf createLaf(IntelliJTheme theme) {
/* 101 */     return new ThemeLaf(theme);
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
/*     */   public IntelliJTheme(InputStream in) throws IOException {
/*     */     Map<String, Object> json;
/*     */     this.namedColors = Collections.emptyMap();
/* 115 */     try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
/* 116 */       json = (Map<String, Object>)Json.parse(reader);
/* 117 */     } catch (ParseException ex) {
/* 118 */       throw new IOException(ex.getMessage(), ex);
/*     */     } 
/*     */     
/* 121 */     this.name = (String)json.get("name");
/* 122 */     this.dark = Boolean.parseBoolean((String)json.get("dark"));
/* 123 */     this.author = (String)json.get("author");
/*     */     
/* 125 */     this.isMaterialUILite = this.author.equals("Mallowigi");
/*     */     
/* 127 */     this.colors = (Map<String, String>)json.get("colors");
/* 128 */     this.ui = (Map<String, Object>)json.get("ui");
/* 129 */     this.icons = (Map<String, Object>)json.get("icons");
/*     */   }
/*     */   
/*     */   private void applyProperties(UIDefaults defaults) {
/* 133 */     if (this.ui == null) {
/*     */       return;
/*     */     }
/* 136 */     defaults.put("Component.isIntelliJTheme", Boolean.valueOf(true));
/*     */ 
/*     */     
/* 139 */     defaults.put("Button.paintShadow", Boolean.valueOf(true));
/* 140 */     defaults.put("Button.shadowWidth", Integer.valueOf(this.dark ? 2 : 1));
/*     */     
/* 142 */     Map<Object, Object> themeSpecificDefaults = removeThemeSpecificDefaults(defaults);
/*     */     
/* 144 */     loadNamedColors(defaults);
/*     */ 
/*     */     
/* 147 */     ArrayList<Object> defaultsKeysCache = new ArrayList();
/* 148 */     Set<String> uiKeys = new HashSet<>();
/* 149 */     for (Map.Entry<String, Object> e : this.ui.entrySet()) {
/* 150 */       apply(e.getKey(), e.getValue(), defaults, defaultsKeysCache, uiKeys);
/*     */     }
/* 152 */     applyColorPalette(defaults);
/* 153 */     applyCheckBoxColors(defaults);
/*     */ 
/*     */     
/* 156 */     for (Map.Entry<String, String> e : uiKeyCopying.entrySet()) {
/* 157 */       defaults.put(e.getKey(), defaults.get(e.getValue()));
/*     */     }
/*     */     
/* 160 */     Object panelBackground = defaults.get("Panel.background");
/* 161 */     defaults.put("Button.disabledBackground", panelBackground);
/* 162 */     defaults.put("ToggleButton.disabledBackground", panelBackground);
/*     */ 
/*     */     
/* 165 */     copyIfNotSet(defaults, "Button.focusedBorderColor", "Component.focusedBorderColor", uiKeys);
/* 166 */     defaults.put("Button.hoverBorderColor", defaults.get("Button.focusedBorderColor"));
/* 167 */     defaults.put("HelpButton.hoverBorderColor", defaults.get("Button.focusedBorderColor"));
/*     */ 
/*     */     
/* 170 */     Object helpButtonBackground = defaults.get("Button.startBackground");
/* 171 */     Object helpButtonBorderColor = defaults.get("Button.startBorderColor");
/* 172 */     if (helpButtonBackground == null)
/* 173 */       helpButtonBackground = defaults.get("Button.background"); 
/* 174 */     if (helpButtonBorderColor == null)
/* 175 */       helpButtonBorderColor = defaults.get("Button.borderColor"); 
/* 176 */     defaults.put("HelpButton.background", helpButtonBackground);
/* 177 */     defaults.put("HelpButton.borderColor", helpButtonBorderColor);
/* 178 */     defaults.put("HelpButton.disabledBackground", panelBackground);
/* 179 */     defaults.put("HelpButton.disabledBorderColor", defaults.get("Button.disabledBorderColor"));
/* 180 */     defaults.put("HelpButton.focusedBorderColor", defaults.get("Button.focusedBorderColor"));
/* 181 */     defaults.put("HelpButton.focusedBackground", defaults.get("Button.focusedBackground"));
/*     */ 
/*     */     
/* 184 */     defaults.put("ComboBox.editableBackground", defaults.get("TextField.background"));
/* 185 */     defaults.put("Spinner.background", defaults.get("TextField.background"));
/*     */ 
/*     */     
/* 188 */     defaults.put("Spinner.buttonBackground", defaults.get("ComboBox.buttonEditableBackground"));
/* 189 */     defaults.put("Spinner.buttonArrowColor", defaults.get("ComboBox.buttonArrowColor"));
/* 190 */     defaults.put("Spinner.buttonDisabledArrowColor", defaults.get("ComboBox.buttonDisabledArrowColor"));
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (uiKeys.contains("TextField.background")) {
/* 195 */       Object textFieldBackground = defaults.get("TextField.background");
/* 196 */       if (!uiKeys.contains("FormattedTextField.background"))
/* 197 */         defaults.put("FormattedTextField.background", textFieldBackground); 
/* 198 */       if (!uiKeys.contains("PasswordField.background"))
/* 199 */         defaults.put("PasswordField.background", textFieldBackground); 
/* 200 */       if (!uiKeys.contains("EditorPane.background"))
/* 201 */         defaults.put("EditorPane.background", textFieldBackground); 
/* 202 */       if (!uiKeys.contains("TextArea.background"))
/* 203 */         defaults.put("TextArea.background", textFieldBackground); 
/* 204 */       if (!uiKeys.contains("TextPane.background"))
/* 205 */         defaults.put("TextPane.background", textFieldBackground); 
/* 206 */       if (!uiKeys.contains("Spinner.background")) {
/* 207 */         defaults.put("Spinner.background", textFieldBackground);
/*     */       }
/*     */     } 
/*     */     
/* 211 */     if (!uiKeys.contains("ToggleButton.startBackground") && !uiKeys.contains("*.startBackground"))
/* 212 */       defaults.put("ToggleButton.startBackground", defaults.get("Button.startBackground")); 
/* 213 */     if (!uiKeys.contains("ToggleButton.endBackground") && !uiKeys.contains("*.endBackground"))
/* 214 */       defaults.put("ToggleButton.endBackground", defaults.get("Button.endBackground")); 
/* 215 */     if (!uiKeys.contains("ToggleButton.foreground") && uiKeys.contains("Button.foreground")) {
/* 216 */       defaults.put("ToggleButton.foreground", defaults.get("Button.foreground"));
/*     */     }
/*     */     
/* 219 */     if (this.isMaterialUILite) {
/* 220 */       defaults.put("List.background", defaults.get("Tree.background"));
/* 221 */       defaults.put("Table.background", defaults.get("Tree.background"));
/*     */     } 
/*     */ 
/*     */     
/* 225 */     int rowHeight = defaults.getInt("Tree.rowHeight");
/* 226 */     if (rowHeight > 22) {
/* 227 */       defaults.put("Tree.rowHeight", Integer.valueOf(22));
/*     */     }
/*     */     
/* 230 */     defaults.putAll(themeSpecificDefaults);
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<Object, Object> removeThemeSpecificDefaults(UIDefaults defaults) {
/* 235 */     ArrayList<String> themeSpecificKeys = new ArrayList<>();
/* 236 */     for (Object key : defaults.keySet()) {
/* 237 */       if (key instanceof String && ((String)key).startsWith("[")) {
/* 238 */         themeSpecificKeys.add((String)key);
/*     */       }
/*     */     } 
/*     */     
/* 242 */     Map<Object, Object> themeSpecificDefaults = new HashMap<>();
/* 243 */     String currentThemePrefix = '[' + this.name.replace(' ', '_') + ']';
/* 244 */     String currentAuthorPrefix = "[author-" + this.author.replace(' ', '_') + ']';
/* 245 */     String allThemesPrefix = "[*]";
/* 246 */     String[] prefixes = { currentThemePrefix, currentAuthorPrefix, allThemesPrefix };
/* 247 */     for (String key : themeSpecificKeys) {
/* 248 */       Object value = defaults.remove(key);
/* 249 */       for (String prefix : prefixes) {
/* 250 */         if (key.startsWith(prefix)) {
/* 251 */           themeSpecificDefaults.put(key.substring(prefix.length()), value);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 257 */     return themeSpecificDefaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadNamedColors(UIDefaults defaults) {
/* 264 */     if (this.colors == null) {
/*     */       return;
/*     */     }
/* 267 */     this.namedColors = new HashMap<>();
/*     */     
/* 269 */     for (Map.Entry<String, String> e : this.colors.entrySet()) {
/* 270 */       String value = e.getValue();
/* 271 */       ColorUIResource color = UIDefaultsLoader.parseColor(value);
/* 272 */       if (color != null) {
/* 273 */         String key = e.getKey();
/* 274 */         this.namedColors.put(key, color);
/* 275 */         defaults.put("ColorPalette." + key, color);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void apply(String key, Object value, UIDefaults defaults, ArrayList<Object> defaultsKeysCache, Set<String> uiKeys) {
/* 285 */     if (value instanceof Map) {
/* 286 */       for (Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)((Map)value).entrySet())
/* 287 */         apply(key + '.' + (String)e.getKey(), e.getValue(), defaults, defaultsKeysCache, uiKeys); 
/*     */     } else {
/* 289 */       if ("".equals(value)) {
/*     */         return;
/*     */       }
/* 292 */       uiKeys.add(key);
/*     */ 
/*     */       
/* 295 */       if (this.isMaterialUILite && (key.equals("ComboBox.padding") || key.equals("Spinner.border"))) {
/*     */         return;
/*     */       }
/*     */       
/* 299 */       key = uiKeyMapping.getOrDefault(key, key);
/* 300 */       if (key.isEmpty()) {
/*     */         return;
/*     */       }
/* 303 */       String valueStr = value.toString();
/*     */ 
/*     */       
/* 306 */       Object uiValue = this.namedColors.get(valueStr);
/*     */ 
/*     */       
/* 309 */       if (uiValue == null) {
/*     */         
/* 311 */         if (!valueStr.startsWith("#") && (key.endsWith("ground") || key.endsWith("Color"))) {
/* 312 */           valueStr = fixColorIfValid("#" + valueStr, valueStr);
/* 313 */         } else if (valueStr.startsWith("##")) {
/* 314 */           valueStr = fixColorIfValid(valueStr.substring(1), valueStr);
/* 315 */         } else if (key.endsWith(".border") || key.endsWith("Border")) {
/* 316 */           List<String> parts = StringUtils.split(valueStr, ',');
/* 317 */           if (parts.size() == 5 && !((String)parts.get(4)).startsWith("#")) {
/* 318 */             parts.set(4, "#" + (String)parts.get(4));
/* 319 */             valueStr = String.join(",", (Iterable)parts);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 325 */           uiValue = UIDefaultsLoader.parseValue(key, valueStr);
/* 326 */         } catch (RuntimeException ex) {
/* 327 */           UIDefaultsLoader.logParseError(Level.CONFIG, key, valueStr, ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 332 */       if (key.startsWith("*.")) {
/*     */         
/* 334 */         String tail = key.substring(1);
/*     */ 
/*     */ 
/*     */         
/* 338 */         if (defaultsKeysCache.size() != defaults.size()) {
/* 339 */           defaultsKeysCache.clear();
/* 340 */           Enumeration<Object> e = defaults.keys();
/* 341 */           while (e.hasMoreElements()) {
/* 342 */             defaultsKeysCache.add(e.nextElement());
/*     */           }
/*     */         } 
/*     */         
/* 346 */         for (Object k : defaultsKeysCache) {
/* 347 */           if (k instanceof String) {
/*     */ 
/*     */ 
/*     */             
/* 351 */             String km = uiKeyInverseMapping.getOrDefault(k, (String)k);
/* 352 */             if (km.endsWith(tail) && !((String)k).startsWith("CheckBox.icon."))
/* 353 */               defaults.put(k, uiValue); 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 357 */         defaults.put(key, uiValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String fixColorIfValid(String newColorStr, String colorStr) {
/*     */     try {
/* 364 */       UIDefaultsLoader.parseColorRGBA(newColorStr);
/*     */       
/* 366 */       return newColorStr;
/* 367 */     } catch (IllegalArgumentException ex) {
/* 368 */       return colorStr;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyColorPalette(UIDefaults defaults) {
/* 373 */     if (this.icons == null) {
/*     */       return;
/*     */     }
/* 376 */     Object palette = this.icons.get("ColorPalette");
/* 377 */     if (!(palette instanceof Map)) {
/*     */       return;
/*     */     }
/*     */     
/* 381 */     Map<String, Object> colorPalette = (Map<String, Object>)palette;
/* 382 */     for (Map.Entry<String, Object> e : colorPalette.entrySet()) {
/* 383 */       String key = e.getKey();
/* 384 */       Object value = e.getValue();
/* 385 */       if (key.startsWith("Checkbox.") || !(value instanceof String)) {
/*     */         continue;
/*     */       }
/* 388 */       if (this.dark) {
/* 389 */         key = StringUtils.removeTrailing(key, ".Dark");
/*     */       }
/* 391 */       ColorUIResource color = toColor((String)value);
/* 392 */       if (color != null) {
/* 393 */         defaults.put(key, color);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private ColorUIResource toColor(String value) {
/* 399 */     ColorUIResource color = this.namedColors.get(value);
/*     */ 
/*     */     
/* 402 */     return (color != null) ? color : UIDefaultsLoader.parseColor(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyCheckBoxColors(UIDefaults defaults) {
/* 411 */     if (this.icons == null) {
/*     */       return;
/*     */     }
/* 414 */     Object palette = this.icons.get("ColorPalette");
/* 415 */     if (!(palette instanceof Map)) {
/*     */       return;
/*     */     }
/* 418 */     boolean checkboxModified = false;
/*     */     
/* 420 */     Map<String, Object> colorPalette = (Map<String, Object>)palette;
/* 421 */     for (Map.Entry<String, Object> e : colorPalette.entrySet()) {
/* 422 */       String key = e.getKey();
/* 423 */       Object value = e.getValue();
/* 424 */       if (!key.startsWith("Checkbox.") || !(value instanceof String)) {
/*     */         continue;
/*     */       }
/* 427 */       if (key.equals("Checkbox.Background.Default") || key
/* 428 */         .equals("Checkbox.Foreground.Selected"))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 434 */         value = "#ffffff";
/*     */       }
/*     */       
/* 437 */       String key2 = checkboxDuplicateColors.get(key);
/*     */       
/* 439 */       if (this.dark) {
/* 440 */         key = StringUtils.removeTrailing(key, ".Dark");
/*     */       }
/* 442 */       String newKey = checkboxKeyMapping.get(key);
/* 443 */       if (newKey != null) {
/* 444 */         String checkBoxIconPrefix = "CheckBox.icon.";
/* 445 */         if (!this.dark && newKey.startsWith(checkBoxIconPrefix)) {
/* 446 */           newKey = "CheckBox.icon[filled].".concat(newKey.substring(checkBoxIconPrefix.length()));
/*     */         }
/* 448 */         ColorUIResource color = toColor((String)value);
/* 449 */         if (color != null) {
/* 450 */           defaults.put(newKey, color);
/*     */           
/* 452 */           if (key2 != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 465 */             if (this.dark) {
/* 466 */               key2 = StringUtils.removeTrailing(key2, ".Dark");
/*     */             }
/* 468 */             String newKey2 = checkboxKeyMapping.get(key2);
/* 469 */             if (newKey2 != null) {
/* 470 */               defaults.put(newKey2, color);
/*     */             }
/*     */           } 
/*     */         } 
/* 474 */         checkboxModified = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 479 */     if (checkboxModified) {
/*     */       
/* 481 */       defaults.remove("CheckBox.icon.focusWidth");
/* 482 */       defaults.put("CheckBox.icon.hoverBorderColor", defaults.get("CheckBox.icon.focusedBorderColor"));
/*     */ 
/*     */       
/* 485 */       defaults.remove("CheckBox.icon[filled].focusWidth");
/* 486 */       defaults.put("CheckBox.icon[filled].hoverBorderColor", defaults.get("CheckBox.icon[filled].focusedBorderColor"));
/* 487 */       defaults.put("CheckBox.icon[filled].selectedFocusedBackground", defaults.get("CheckBox.icon[filled].selectedBackground"));
/*     */       
/* 489 */       if (this.dark) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 494 */         String[] focusedBorderColorKeys = { "CheckBox.icon.focusedBorderColor", "CheckBox.icon.selectedFocusedBorderColor", "CheckBox.icon[filled].focusedBorderColor", "CheckBox.icon[filled].selectedFocusedBorderColor" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 500 */         for (String key : focusedBorderColorKeys) {
/* 501 */           Color color = defaults.getColor(key);
/* 502 */           if (color != null) {
/* 503 */             defaults.put(key, new ColorUIResource(new Color(color
/* 504 */                     .getRGB() & 0xFFFFFF | 0xA6000000, true)));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copyIfNotSet(UIDefaults defaults, String destKey, String srcKey, Set<String> uiKeys) {
/* 512 */     if (!uiKeys.contains(destKey)) {
/* 513 */       defaults.put(destKey, defaults.get(srcKey));
/*     */     }
/*     */   }
/*     */   
/* 517 */   private static Map<String, String> uiKeyMapping = new HashMap<>();
/*     */   
/* 519 */   private static Map<String, String> uiKeyCopying = new HashMap<>();
/* 520 */   private static Map<String, String> uiKeyInverseMapping = new HashMap<>();
/* 521 */   private static Map<String, String> checkboxKeyMapping = new HashMap<>();
/* 522 */   private static Map<String, String> checkboxDuplicateColors = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/* 526 */     uiKeyMapping.put("ComboBox.background", "");
/* 527 */     uiKeyMapping.put("ComboBox.nonEditableBackground", "ComboBox.background");
/* 528 */     uiKeyMapping.put("ComboBox.ArrowButton.background", "ComboBox.buttonEditableBackground");
/* 529 */     uiKeyMapping.put("ComboBox.ArrowButton.disabledIconColor", "ComboBox.buttonDisabledArrowColor");
/* 530 */     uiKeyMapping.put("ComboBox.ArrowButton.iconColor", "ComboBox.buttonArrowColor");
/* 531 */     uiKeyMapping.put("ComboBox.ArrowButton.nonEditableBackground", "ComboBox.buttonBackground");
/*     */ 
/*     */     
/* 534 */     uiKeyMapping.put("Component.inactiveErrorFocusColor", "Component.error.borderColor");
/* 535 */     uiKeyMapping.put("Component.errorFocusColor", "Component.error.focusedBorderColor");
/* 536 */     uiKeyMapping.put("Component.inactiveWarningFocusColor", "Component.warning.borderColor");
/* 537 */     uiKeyMapping.put("Component.warningFocusColor", "Component.warning.focusedBorderColor");
/*     */ 
/*     */     
/* 540 */     uiKeyMapping.put("Link.activeForeground", "Component.linkColor");
/*     */ 
/*     */     
/* 543 */     uiKeyMapping.put("Menu.border", "Menu.margin");
/* 544 */     uiKeyMapping.put("MenuItem.border", "MenuItem.margin");
/* 545 */     uiKeyCopying.put("CheckBoxMenuItem.margin", "MenuItem.margin");
/* 546 */     uiKeyCopying.put("RadioButtonMenuItem.margin", "MenuItem.margin");
/* 547 */     uiKeyMapping.put("PopupMenu.border", "PopupMenu.borderInsets");
/* 548 */     uiKeyCopying.put("MenuItem.underlineSelectionColor", "TabbedPane.underlineColor");
/*     */ 
/*     */     
/* 551 */     uiKeyCopying.put("Menu.selectionBackground", "List.selectionBackground");
/* 552 */     uiKeyCopying.put("MenuItem.selectionBackground", "List.selectionBackground");
/* 553 */     uiKeyCopying.put("CheckBoxMenuItem.selectionBackground", "List.selectionBackground");
/* 554 */     uiKeyCopying.put("RadioButtonMenuItem.selectionBackground", "List.selectionBackground");
/*     */ 
/*     */     
/* 557 */     uiKeyMapping.put("ProgressBar.background", "");
/* 558 */     uiKeyMapping.put("ProgressBar.foreground", "");
/* 559 */     uiKeyMapping.put("ProgressBar.trackColor", "ProgressBar.background");
/* 560 */     uiKeyMapping.put("ProgressBar.progressColor", "ProgressBar.foreground");
/* 561 */     uiKeyCopying.put("ProgressBar.selectionForeground", "ProgressBar.background");
/* 562 */     uiKeyCopying.put("ProgressBar.selectionBackground", "ProgressBar.foreground");
/*     */ 
/*     */     
/* 565 */     uiKeyMapping.put("ScrollBar.trackColor", "ScrollBar.track");
/* 566 */     uiKeyMapping.put("ScrollBar.thumbColor", "ScrollBar.thumb");
/*     */ 
/*     */     
/* 569 */     uiKeyMapping.put("Separator.separatorColor", "Separator.foreground");
/*     */ 
/*     */     
/* 572 */     uiKeyMapping.put("Slider.trackWidth", "");
/* 573 */     uiKeyCopying.put("Slider.trackValueColor", "ProgressBar.foreground");
/* 574 */     uiKeyCopying.put("Slider.thumbColor", "ProgressBar.foreground");
/* 575 */     uiKeyCopying.put("Slider.trackColor", "ProgressBar.background");
/*     */ 
/*     */     
/* 578 */     uiKeyCopying.put("TitlePane.inactiveBackground", "TitlePane.background");
/* 579 */     uiKeyMapping.put("TitlePane.infoForeground", "TitlePane.foreground");
/* 580 */     uiKeyMapping.put("TitlePane.inactiveInfoForeground", "TitlePane.inactiveForeground");
/*     */     
/* 582 */     for (Map.Entry<String, String> e : uiKeyMapping.entrySet()) {
/* 583 */       uiKeyInverseMapping.put(e.getValue(), e.getKey());
/*     */     }
/* 585 */     uiKeyCopying.put("ToggleButton.tab.underlineColor", "TabbedPane.underlineColor");
/* 586 */     uiKeyCopying.put("ToggleButton.tab.disabledUnderlineColor", "TabbedPane.disabledUnderlineColor");
/* 587 */     uiKeyCopying.put("ToggleButton.tab.selectedBackground", "TabbedPane.selectedBackground");
/* 588 */     uiKeyCopying.put("ToggleButton.tab.hoverBackground", "TabbedPane.hoverColor");
/* 589 */     uiKeyCopying.put("ToggleButton.tab.focusBackground", "TabbedPane.focusColor");
/*     */     
/* 591 */     checkboxKeyMapping.put("Checkbox.Background.Default", "CheckBox.icon.background");
/* 592 */     checkboxKeyMapping.put("Checkbox.Background.Disabled", "CheckBox.icon.disabledBackground");
/* 593 */     checkboxKeyMapping.put("Checkbox.Border.Default", "CheckBox.icon.borderColor");
/* 594 */     checkboxKeyMapping.put("Checkbox.Border.Disabled", "CheckBox.icon.disabledBorderColor");
/* 595 */     checkboxKeyMapping.put("Checkbox.Focus.Thin.Default", "CheckBox.icon.focusedBorderColor");
/* 596 */     checkboxKeyMapping.put("Checkbox.Focus.Wide", "CheckBox.icon.focusColor");
/* 597 */     checkboxKeyMapping.put("Checkbox.Foreground.Disabled", "CheckBox.icon.disabledCheckmarkColor");
/* 598 */     checkboxKeyMapping.put("Checkbox.Background.Selected", "CheckBox.icon.selectedBackground");
/* 599 */     checkboxKeyMapping.put("Checkbox.Border.Selected", "CheckBox.icon.selectedBorderColor");
/* 600 */     checkboxKeyMapping.put("Checkbox.Foreground.Selected", "CheckBox.icon.checkmarkColor");
/* 601 */     checkboxKeyMapping.put("Checkbox.Focus.Thin.Selected", "CheckBox.icon.selectedFocusedBorderColor");
/*     */     
/* 603 */     checkboxDuplicateColors.put("Checkbox.Background.Default.Dark", "Checkbox.Background.Selected.Dark");
/* 604 */     checkboxDuplicateColors.put("Checkbox.Border.Default.Dark", "Checkbox.Border.Selected.Dark");
/* 605 */     checkboxDuplicateColors.put("Checkbox.Focus.Thin.Default.Dark", "Checkbox.Focus.Thin.Selected.Dark");
/*     */     
/* 607 */     Map.Entry[] arrayOfEntry = (Map.Entry[])checkboxDuplicateColors.entrySet().toArray((Object[])new Map.Entry[checkboxDuplicateColors.size()]);
/* 608 */     for (Map.Entry<String, String> e : arrayOfEntry) {
/* 609 */       checkboxDuplicateColors.put(e.getValue(), e.getKey());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ThemeLaf
/*     */     extends FlatLaf
/*     */   {
/*     */     private final IntelliJTheme theme;
/*     */     
/*     */     public ThemeLaf(IntelliJTheme theme) {
/* 620 */       this.theme = theme;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 625 */       return this.theme.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 630 */       return getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDark() {
/* 635 */       return this.theme.dark;
/*     */     }
/*     */     
/*     */     public IntelliJTheme getTheme() {
/* 639 */       return this.theme;
/*     */     }
/*     */ 
/*     */     
/*     */     void applyAdditionalDefaults(UIDefaults defaults) {
/* 644 */       this.theme.applyProperties(defaults);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ArrayList<Class<?>> getLafClassesForDefaultsLoading() {
/* 649 */       ArrayList<Class<?>> lafClasses = new ArrayList<>();
/* 650 */       lafClasses.add(FlatLaf.class);
/* 651 */       lafClasses.add(this.theme.dark ? FlatDarkLaf.class : FlatLightLaf.class);
/* 652 */       lafClasses.add(this.theme.dark ? FlatDarculaLaf.class : FlatIntelliJLaf.class);
/* 653 */       lafClasses.add(ThemeLaf.class);
/* 654 */       return lafClasses;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\IntelliJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */