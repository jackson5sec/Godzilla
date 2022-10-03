/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static final String SOURCE_FILE_ICON = "sourceFileIcon";
/*     */   public static final String PACKAGE_ICON = "packageIcon";
/*     */   public static final String IMPORT_ROOT_ICON = "importRootIcon";
/*     */   public static final String IMPORT_ICON = "importIcon";
/*     */   public static final String DEFAULT_CLASS_ICON = "defaultClassIcon";
/*     */   public static final String DEFAULT_INTERFACE_ICON = "defaultInterfaceIcon";
/*     */   public static final String CLASS_ICON = "classIcon";
/*     */   public static final String ENUM_ICON = "enumIcon";
/*     */   public static final String ENUM_PROTECTED_ICON = "enumProtectedIcon";
/*     */   public static final String ENUM_PRIVATE_ICON = "enumPrivateIcon";
/*     */   public static final String ENUM_DEFAULT_ICON = "enumDefaultIcon";
/*     */   public static final String INNER_CLASS_PUBLIC_ICON = "innerClassPublicIcon";
/*     */   public static final String INNER_CLASS_PROTECTED_ICON = "innerClassProtectedIcon";
/*     */   public static final String INNER_CLASS_PRIVATE_ICON = "innerClassPrivateIcon";
/*     */   public static final String INNER_CLASS_DEFAULT_ICON = "innerClassDefaultIcon";
/*     */   public static final String INTERFACE_ICON = "interfaceIcon";
/*     */   public static final String JAVADOC_ITEM_ICON = "javadocItemIcon";
/*     */   public static final String LOCAL_VARIABLE_ICON = "localVariableIcon";
/*     */   public static final String METHOD_PUBLIC_ICON = "methodPublicIcon";
/*     */   public static final String METHOD_PROTECTED_ICON = "methodProtectedIcon";
/*     */   public static final String METHOD_PRIVATE_ICON = "methodPrivateIcon";
/*     */   public static final String METHOD_DEFAULT_ICON = "methodDefaultIcon";
/*     */   public static final String TEMPLATE_ICON = "templateIcon";
/*     */   public static final String FIELD_PUBLIC_ICON = "fieldPublicIcon";
/*     */   public static final String FIELD_PROTECTED_ICON = "fieldProtectedIcon";
/*     */   public static final String FIELD_PRIVATE_ICON = "fieldPrivateIcon";
/*     */   public static final String FIELD_DEFAULT_ICON = "fieldDefaultIcon";
/*     */   public static final String CONSTRUCTOR_ICON = "constructorIcon";
/*     */   public static final String DEPRECATED_ICON = "deprecatedIcon";
/*     */   public static final String ABSTRACT_ICON = "abstractIcon";
/*     */   public static final String FINAL_ICON = "finalIcon";
/*     */   public static final String STATIC_ICON = "staticIcon";
/*     */   private Map<String, Icon> iconMap;
/*  65 */   private static final IconFactory INSTANCE = new IconFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private IconFactory() {
/*  70 */     this.iconMap = new HashMap<>();
/*  71 */     this.iconMap.put("sourceFileIcon", loadIcon("jcu_obj.gif"));
/*  72 */     this.iconMap.put("packageIcon", loadIcon("package_obj.gif"));
/*  73 */     this.iconMap.put("importRootIcon", loadIcon("impc_obj.gif"));
/*  74 */     this.iconMap.put("importIcon", loadIcon("imp_obj.gif"));
/*  75 */     this.iconMap.put("defaultClassIcon", loadIcon("class_default_obj.gif"));
/*  76 */     this.iconMap.put("defaultInterfaceIcon", loadIcon("int_default_obj.gif"));
/*  77 */     this.iconMap.put("classIcon", loadIcon("class_obj.gif"));
/*  78 */     this.iconMap.put("enumIcon", loadIcon("enum_obj.gif"));
/*  79 */     this.iconMap.put("enumProtectedIcon", loadIcon("enum_protected_obj.gif"));
/*  80 */     this.iconMap.put("enumPrivateIcon", loadIcon("enum_private_obj.gif"));
/*  81 */     this.iconMap.put("enumDefaultIcon", loadIcon("enum_default_obj.gif"));
/*  82 */     this.iconMap.put("innerClassPublicIcon", loadIcon("innerclass_public_obj.gif"));
/*  83 */     this.iconMap.put("innerClassProtectedIcon", loadIcon("innerclass_protected_obj.gif"));
/*  84 */     this.iconMap.put("innerClassPrivateIcon", loadIcon("innerclass_private_obj.gif"));
/*  85 */     this.iconMap.put("innerClassDefaultIcon", loadIcon("innerclass_default_obj.gif"));
/*  86 */     this.iconMap.put("interfaceIcon", loadIcon("int_obj.gif"));
/*  87 */     this.iconMap.put("javadocItemIcon", loadIcon("jdoc_tag_obj.gif"));
/*  88 */     this.iconMap.put("localVariableIcon", loadIcon("localvariable_obj.gif"));
/*  89 */     this.iconMap.put("methodPublicIcon", loadIcon("methpub_obj.gif"));
/*  90 */     this.iconMap.put("methodProtectedIcon", loadIcon("methpro_obj.gif"));
/*  91 */     this.iconMap.put("methodPrivateIcon", loadIcon("methpri_obj.gif"));
/*  92 */     this.iconMap.put("methodDefaultIcon", loadIcon("methdef_obj.gif"));
/*  93 */     this.iconMap.put("templateIcon", loadIcon("template_obj.gif"));
/*  94 */     this.iconMap.put("fieldPublicIcon", loadIcon("field_public_obj.gif"));
/*  95 */     this.iconMap.put("fieldProtectedIcon", loadIcon("field_protected_obj.gif"));
/*  96 */     this.iconMap.put("fieldPrivateIcon", loadIcon("field_private_obj.gif"));
/*  97 */     this.iconMap.put("fieldDefaultIcon", loadIcon("field_default_obj.gif"));
/*     */     
/*  99 */     this.iconMap.put("constructorIcon", loadIcon("constr_ovr.gif"));
/* 100 */     this.iconMap.put("deprecatedIcon", loadIcon("deprecated.gif"));
/* 101 */     this.iconMap.put("abstractIcon", loadIcon("abstract_co.gif"));
/* 102 */     this.iconMap.put("finalIcon", loadIcon("final_co.gif"));
/* 103 */     this.iconMap.put("staticIcon", loadIcon("static_co.gif"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static IconFactory get() {
/* 109 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon getIcon(String key) {
/* 114 */     return getIcon(key, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon getIcon(String key, boolean deprecated) {
/* 119 */     Icon icon = this.iconMap.get(key);
/* 120 */     if (deprecated) {
/* 121 */       DecoratableIcon di = new DecoratableIcon(16, icon);
/* 122 */       di.setDeprecated(deprecated);
/* 123 */       icon = di;
/*     */     } 
/* 125 */     return icon;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon(IconData data) {
/* 131 */     DecoratableIcon icon = new DecoratableIcon(16, getIcon(data.getIcon()));
/* 132 */     icon.setDeprecated(data.isDeprecated());
/* 133 */     if (data.isAbstract()) {
/* 134 */       icon.addDecorationIcon(getIcon("abstractIcon"));
/*     */     }
/* 136 */     if (data.isStatic()) {
/* 137 */       icon.addDecorationIcon(getIcon("staticIcon"));
/*     */     }
/* 139 */     if (data.isFinal()) {
/* 140 */       icon.addDecorationIcon(getIcon("finalIcon"));
/*     */     }
/* 142 */     return icon;
/*     */   }
/*     */ 
/*     */   
/*     */   private Icon loadIcon(String name) {
/* 147 */     URL res = getClass().getResource("img/" + name);
/* 148 */     if (res == null)
/*     */     {
/*     */ 
/*     */       
/* 152 */       throw new IllegalArgumentException("icon not found: img/" + name);
/*     */     }
/* 154 */     return new ImageIcon(res);
/*     */   }
/*     */   
/*     */   public static interface IconData {
/*     */     String getIcon();
/*     */     
/*     */     boolean isAbstract();
/*     */     
/*     */     boolean isDeprecated();
/*     */     
/*     */     boolean isFinal();
/*     */     
/*     */     boolean isStatic();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\IconFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */