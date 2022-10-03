/*     */ package shells.plugins.generic.seting;
/*     */ 
/*     */ import com.jediterm.terminal.TerminalColor;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.emulator.ColorPalette;
/*     */ import com.jediterm.terminal.emulator.ColorPaletteImpl;
/*     */ import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;
/*     */ import core.Db;
/*     */ import core.ui.MainActivity;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ 
/*     */ public class TerminalSettingsProvider
/*     */   extends DefaultSettingsProvider
/*     */ {
/*     */   public static final String FONT_NAME_KEY = "Terminal-FontName";
/*     */   public static final String FONT_SIZE_KEY = "Terminal-FontSize";
/*     */   public static final String FONT_TYPE_KEY = "Terminal-FontType";
/*     */   public static final String TERMINAL_STYLE_KEY = "Terminal-FontStyle";
/*  27 */   private static final ArrayList<String> TERMINAL_STYLES = new ArrayList<>();
/*     */   
/*     */   private static final String STYLE_ZIP = "assets/alacritty.zip";
/*     */   
/*     */   private TextStyle defaultStyle;
/*     */   
/*     */   private TextStyle selectionStyle;
/*     */   
/*     */   static {
/*     */     try {
/*  37 */       ZipInputStream zipInputStream = new ZipInputStream(TerminalSettingsProvider.class.getResourceAsStream("assets/alacritty.zip"));
/*     */       
/*  39 */       ZipEntry zipEntry = null;
/*     */       
/*  41 */       while ((zipEntry = zipInputStream.getNextEntry()) != null) {
/*  42 */         String zipEntryName = zipEntry.getName();
/*  43 */         if (zipEntryName.endsWith(".yml")) {
/*  44 */           TERMINAL_STYLES.add(zipEntry.getName().replace(".yml", ""));
/*     */         }
/*  46 */         zipInputStream.closeEntry();
/*     */       } 
/*  48 */       zipInputStream.close();
/*  49 */     } catch (Exception e) {
/*  50 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TerminalSettingsProvider() {
/*  56 */     this(getTerminalStyle());
/*     */   }
/*     */   
/*     */   public TerminalSettingsProvider(String styleName) {
/*  60 */     formatStyle(styleName);
/*     */   }
/*     */   
/*     */   public void formatStyle(String styleName) {
/*  64 */     try (ZipInputStream zipInputStream = new ZipInputStream(TerminalSettingsProvider.class.getResourceAsStream("assets/alacritty.zip"))) {
/*     */       
/*  66 */       ZipEntry zipEntry = null;
/*     */       
/*  68 */       while ((zipEntry = zipInputStream.getNextEntry()) != null) {
/*  69 */         String zipEntryName = zipEntry.getName();
/*  70 */         if (zipEntryName.endsWith(".yml")) {
/*  71 */           zipEntryName = zipEntry.getName().replace(".yml", "");
/*  72 */           if (styleName.equals(zipEntryName)) {
/*     */             try {
/*  74 */               Yaml yaml = new Yaml();
/*     */               
/*  76 */               HashMap map = (HashMap)yaml.loadAs(zipInputStream, HashMap.class);
/*     */               
/*  78 */               HashMap colors = (HashMap)map.get("colors");
/*     */               
/*  80 */               HashMap cursorColor = (HashMap)colors.get("cursor");
/*  81 */               HashMap selectionColor = (HashMap)colors.get("selection");
/*  82 */               HashMap primaryColor = (HashMap)colors.get("primary");
/*  83 */               this
/*  84 */                 .defaultStyle = new TextStyle(TerminalColor.awt(Color.decode(primaryColor.get("foreground").toString())), TerminalColor.awt(Color.decode(primaryColor.get("background").toString())));
/*  85 */               this
/*  86 */                 .selectionStyle = new TextStyle(TerminalColor.awt(Color.decode(selectionColor.get("text").toString())), TerminalColor.awt(Color.decode(selectionColor.get("background").toString())));
/*  87 */             } catch (Exception e) {
/*  88 */               Log.error(e);
/*     */             } 
/*     */           }
/*     */         } 
/*  92 */         zipInputStream.closeEntry();
/*     */       }
/*     */     
/*  95 */     } catch (Exception e) {
/*  96 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getDefaultStyle() {
/* 102 */     return (this.defaultStyle != null) ? this.defaultStyle : super.getDefaultStyle();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getSelectionColor() {
/* 107 */     return this.selectionStyle;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getFoundPatternColor() {
/* 112 */     return getSelectionColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public ColorPalette getTerminalColorPalette() {
/* 117 */     return ColorPaletteImpl.XTERM_PALETTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getTerminalFont() {
/*     */     try {
/* 123 */       String fontName = null;
/* 124 */       if ((fontName = getFontName()) != null) {
/* 125 */         Font font = new Font(fontName, UiFunction.getFontType(getFontType()), (int)getTerminalFontSize());
/* 126 */         if (font != null) {
/* 127 */           return font;
/*     */         }
/*     */       } else {
/* 130 */         Font font = MainActivity.getMainActivityFrame().getGraphics().getFont();
/* 131 */         if (font != null) {
/* 132 */           return font;
/*     */         }
/*     */       } 
/* 135 */     } catch (Exception e) {
/* 136 */       Log.error(e);
/*     */     } 
/* 138 */     return super.getTerminalFont();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTerminalFontSize() {
/* 143 */     return getFontSize();
/*     */   }
/*     */   
/*     */   public static String getFontName() {
/* 147 */     return Db.getSetingValue("Terminal-FontName");
/*     */   }
/*     */   
/*     */   public static int getFontSize() {
/* 151 */     return Db.getSetingIntValue("Terminal-FontSize", 14);
/*     */   }
/*     */   public static String getFontType() {
/* 154 */     return Db.getSetingValue("Terminal-FontType", "PLAIN");
/*     */   }
/*     */   public static String getTerminalStyle() {
/* 157 */     return Db.getSetingValue("Terminal-FontStyle", "hack");
/*     */   }
/*     */   
/*     */   public static String[] getTerminalStyles() {
/* 161 */     return TERMINAL_STYLES.<String>toArray(new String[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\seting\TerminalSettingsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */