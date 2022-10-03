/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import org.fife.ui.autocomplete.EmptyIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IconFactory
/*     */ {
/*     */   public static final String FUNCTION_ICON = "function";
/*     */   public static final String LOCAL_VARIABLE_ICON = "local_variable";
/*     */   public static final String TEMPLATE_ICON = "template";
/*     */   public static final String EMPTY_ICON = "empty";
/*     */   public static final String GLOBAL_VARIABLE_ICON = "global_variable";
/*     */   public static final String DEFAULT_FUNCTION_ICON = "default_function";
/*     */   public static final String PUBLIC_STATIC_FUNCTION_ICON = "public_static_function";
/*     */   public static final String STATIC_VAR_ICON = "static_var";
/*     */   public static final String DEFAULT_VARIABLE_ICON = "default_variable";
/*     */   public static final String DEFAULT_CLASS_ICON = "default_class";
/*     */   public static final String PUBLIC_METHOD_ICON = "methpub_obj";
/*     */   public static final String PUBLIC_FIELD_ICON = "field_public_obj";
/*     */   public static final String JSDOC_ITEM_ICON = "jsdoc_item";
/*     */   private Map<String, Icon> iconMap;
/*  47 */   private static final IconFactory INSTANCE = new IconFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private IconFactory() {
/*  52 */     this.iconMap = new HashMap<>();
/*     */     
/*  54 */     this.iconMap.put("function", loadIcon("methpub_obj.gif"));
/*  55 */     this.iconMap.put("public_static_function", loadIcon("methpub_static.gif"));
/*  56 */     this.iconMap.put("local_variable", loadIcon("localvariable_obj.gif"));
/*  57 */     this.iconMap.put("global_variable", loadIcon("field_public_obj.gif"));
/*  58 */     this.iconMap.put("template", loadIcon("template_obj.gif"));
/*  59 */     this.iconMap.put("default_function", loadIcon("methdef_obj.gif"));
/*  60 */     this.iconMap.put("static_var", loadIcon("static_co.gif"));
/*  61 */     this.iconMap.put("default_variable", loadIcon("field_default_obj.gif"));
/*  62 */     this.iconMap.put("default_class", loadIcon("class_obj.gif"));
/*  63 */     this.iconMap.put("methpub_obj", loadIcon("methpub_obj.gif"));
/*  64 */     this.iconMap.put("field_public_obj", loadIcon("field_public_obj.gif"));
/*  65 */     this.iconMap.put("jsdoc_item", loadIcon("jdoc_tag_obj.gif"));
/*  66 */     this.iconMap.put("empty", new EmptyIcon(16));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Icon getIconImage(String name) {
/*  72 */     return this.iconMap.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Icon getIcon(String name) {
/*  77 */     return INSTANCE.getIconImage(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getEmptyIcon() {
/*  82 */     return "empty";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Icon loadIcon(String name) {
/*  93 */     name = "org/fife/rsta/ac/js/img/" + name;
/*  94 */     URL res = getClass().getClassLoader().getResource(name);
/*  95 */     if (res == null)
/*     */     {
/*     */ 
/*     */       
/*  99 */       throw new IllegalArgumentException("icon not found: " + name);
/*     */     }
/* 101 */     return new ImageIcon(res);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\IconFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */