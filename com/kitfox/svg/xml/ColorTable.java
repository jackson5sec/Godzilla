/*     */ package com.kitfox.svg.xml;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColorTable
/*     */ {
/*     */   static final Map<String, Color> colorTable;
/*     */   
/*     */   static {
/*  53 */     HashMap<String, Color> table = new HashMap<String, Color>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     table.put("currentcolor", new Color(0));
/*     */     
/*  60 */     table.put("aliceblue", new Color(15792383));
/*  61 */     table.put("antiquewhite", new Color(16444375));
/*  62 */     table.put("aqua", new Color(65535));
/*  63 */     table.put("aquamarine", new Color(8388564));
/*  64 */     table.put("azure", new Color(15794175));
/*  65 */     table.put("beige", new Color(16119260));
/*  66 */     table.put("bisque", new Color(16770244));
/*  67 */     table.put("black", new Color(0));
/*  68 */     table.put("blanchedalmond", new Color(16772045));
/*  69 */     table.put("blue", new Color(255));
/*  70 */     table.put("blueviolet", new Color(9055202));
/*  71 */     table.put("brown", new Color(10824234));
/*  72 */     table.put("burlywood", new Color(14596231));
/*  73 */     table.put("cadetblue", new Color(6266528));
/*  74 */     table.put("chartreuse", new Color(8388352));
/*  75 */     table.put("chocolate", new Color(13789470));
/*  76 */     table.put("coral", new Color(16744272));
/*  77 */     table.put("cornflowerblue", new Color(6591981));
/*  78 */     table.put("cornsilk", new Color(16775388));
/*  79 */     table.put("crimson", new Color(14423100));
/*  80 */     table.put("cyan", new Color(65535));
/*  81 */     table.put("darkblue", new Color(139));
/*  82 */     table.put("darkcyan", new Color(35723));
/*  83 */     table.put("darkgoldenrod", new Color(12092939));
/*  84 */     table.put("darkgray", new Color(11119017));
/*  85 */     table.put("darkgreen", new Color(25600));
/*  86 */     table.put("darkkhaki", new Color(12433259));
/*  87 */     table.put("darkmagenta", new Color(9109643));
/*  88 */     table.put("darkolivegreen", new Color(5597999));
/*  89 */     table.put("darkorange", new Color(16747520));
/*  90 */     table.put("darkorchid", new Color(10040012));
/*  91 */     table.put("darkred", new Color(9109504));
/*  92 */     table.put("darksalmon", new Color(15308410));
/*  93 */     table.put("darkseagreen", new Color(9419919));
/*  94 */     table.put("darkslateblue", new Color(4734347));
/*  95 */     table.put("darkslategray", new Color(3100495));
/*  96 */     table.put("darkturquoise", new Color(52945));
/*  97 */     table.put("darkviolet", new Color(9699539));
/*  98 */     table.put("deeppink", new Color(16716947));
/*  99 */     table.put("deepskyblue", new Color(49151));
/* 100 */     table.put("dimgray", new Color(6908265));
/* 101 */     table.put("dodgerblue", new Color(2003199));
/* 102 */     table.put("feldspar", new Color(13734517));
/* 103 */     table.put("firebrick", new Color(11674146));
/* 104 */     table.put("floralwhite", new Color(16775920));
/* 105 */     table.put("forestgreen", new Color(2263842));
/* 106 */     table.put("fuchsia", new Color(16711935));
/* 107 */     table.put("gainsboro", new Color(14474460));
/* 108 */     table.put("ghostwhite", new Color(16316671));
/* 109 */     table.put("gold", new Color(16766720));
/* 110 */     table.put("goldenrod", new Color(14329120));
/* 111 */     table.put("gray", new Color(8421504));
/* 112 */     table.put("green", new Color(32768));
/* 113 */     table.put("greenyellow", new Color(11403055));
/* 114 */     table.put("honeydew", new Color(15794160));
/* 115 */     table.put("hotpink", new Color(16738740));
/* 116 */     table.put("indianred", new Color(13458524));
/* 117 */     table.put("indigo", new Color(4915330));
/* 118 */     table.put("ivory", new Color(16777200));
/* 119 */     table.put("khaki", new Color(15787660));
/* 120 */     table.put("lavender", new Color(15132410));
/* 121 */     table.put("lavenderblush", new Color(16773365));
/* 122 */     table.put("lawngreen", new Color(8190976));
/* 123 */     table.put("lemonchiffon", new Color(16775885));
/* 124 */     table.put("lightblue", new Color(11393254));
/* 125 */     table.put("lightcoral", new Color(15761536));
/* 126 */     table.put("lightcyan", new Color(14745599));
/* 127 */     table.put("lightgoldenrodyellow", new Color(16448210));
/* 128 */     table.put("lightgrey", new Color(13882323));
/* 129 */     table.put("lightgreen", new Color(9498256));
/* 130 */     table.put("lightpink", new Color(16758465));
/* 131 */     table.put("lightsalmon", new Color(16752762));
/* 132 */     table.put("lightseagreen", new Color(2142890));
/* 133 */     table.put("lightskyblue", new Color(8900346));
/* 134 */     table.put("lightslateblue", new Color(8679679));
/* 135 */     table.put("lightslategray", new Color(7833753));
/* 136 */     table.put("lightsteelblue", new Color(11584734));
/* 137 */     table.put("lightyellow", new Color(16777184));
/* 138 */     table.put("lime", new Color(65280));
/* 139 */     table.put("limegreen", new Color(3329330));
/* 140 */     table.put("linen", new Color(16445670));
/* 141 */     table.put("magenta", new Color(16711935));
/* 142 */     table.put("maroon", new Color(8388608));
/* 143 */     table.put("mediumaquamarine", new Color(6737322));
/* 144 */     table.put("mediumblue", new Color(205));
/* 145 */     table.put("mediumorchid", new Color(12211667));
/* 146 */     table.put("mediumpurple", new Color(9662680));
/* 147 */     table.put("mediumseagreen", new Color(3978097));
/* 148 */     table.put("mediumslateblue", new Color(8087790));
/* 149 */     table.put("mediumspringgreen", new Color(64154));
/* 150 */     table.put("mediumturquoise", new Color(4772300));
/* 151 */     table.put("mediumvioletred", new Color(13047173));
/* 152 */     table.put("midnightblue", new Color(1644912));
/* 153 */     table.put("mintcream", new Color(16121850));
/* 154 */     table.put("mistyrose", new Color(16770273));
/* 155 */     table.put("moccasin", new Color(16770229));
/* 156 */     table.put("navajowhite", new Color(16768685));
/* 157 */     table.put("navy", new Color(128));
/* 158 */     table.put("oldlace", new Color(16643558));
/* 159 */     table.put("olive", new Color(8421376));
/* 160 */     table.put("olivedrab", new Color(7048739));
/* 161 */     table.put("orange", new Color(16753920));
/* 162 */     table.put("orangered", new Color(16729344));
/* 163 */     table.put("orchid", new Color(14315734));
/* 164 */     table.put("palegoldenrod", new Color(15657130));
/* 165 */     table.put("palegreen", new Color(10025880));
/* 166 */     table.put("paleturquoise", new Color(11529966));
/* 167 */     table.put("palevioletred", new Color(14184595));
/* 168 */     table.put("papayawhip", new Color(16773077));
/* 169 */     table.put("peachpuff", new Color(16767673));
/* 170 */     table.put("peru", new Color(13468991));
/* 171 */     table.put("pink", new Color(16761035));
/* 172 */     table.put("plum", new Color(14524637));
/* 173 */     table.put("powderblue", new Color(11591910));
/* 174 */     table.put("purple", new Color(8388736));
/* 175 */     table.put("red", new Color(16711680));
/* 176 */     table.put("rosybrown", new Color(12357519));
/* 177 */     table.put("royalblue", new Color(4286945));
/* 178 */     table.put("saddlebrown", new Color(9127187));
/* 179 */     table.put("salmon", new Color(16416882));
/* 180 */     table.put("sandybrown", new Color(16032864));
/* 181 */     table.put("seagreen", new Color(3050327));
/* 182 */     table.put("seashell", new Color(16774638));
/* 183 */     table.put("sienna", new Color(10506797));
/* 184 */     table.put("silver", new Color(12632256));
/* 185 */     table.put("skyblue", new Color(8900331));
/* 186 */     table.put("slateblue", new Color(6970061));
/* 187 */     table.put("slategray", new Color(7372944));
/* 188 */     table.put("snow", new Color(16775930));
/* 189 */     table.put("springgreen", new Color(65407));
/* 190 */     table.put("steelblue", new Color(4620980));
/* 191 */     table.put("tan", new Color(13808780));
/* 192 */     table.put("teal", new Color(32896));
/* 193 */     table.put("thistle", new Color(14204888));
/* 194 */     table.put("tomato", new Color(16737095));
/* 195 */     table.put("turquoise", new Color(4251856));
/* 196 */     table.put("violet", new Color(15631086));
/* 197 */     table.put("violetred", new Color(13639824));
/* 198 */     table.put("wheat", new Color(16113331));
/* 199 */     table.put("white", new Color(16777215));
/* 200 */     table.put("whitesmoke", new Color(16119285));
/* 201 */     table.put("yellow", new Color(16776960));
/* 202 */     table.put("yellowgreen", new Color(10145074));
/*     */     
/* 204 */     colorTable = Collections.unmodifiableMap(table);
/*     */   }
/*     */   
/* 207 */   static ColorTable singleton = new ColorTable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ColorTable instance() {
/* 214 */     return singleton;
/*     */   }
/*     */   public Color lookupColor(String name) {
/* 217 */     return colorTable.get(name.toLowerCase());
/*     */   }
/*     */ 
/*     */   
/*     */   public static Color parseColor(String val) {
/* 222 */     Color retVal = null;
/*     */     
/* 224 */     if ("".equals(val))
/*     */     {
/* 226 */       return null;
/*     */     }
/*     */     
/* 229 */     if (val.charAt(0) == '#') {
/*     */       
/* 231 */       String hexStrn = val.substring(1);
/*     */       
/* 233 */       if (hexStrn.length() == 3)
/*     */       {
/* 235 */         hexStrn = "" + hexStrn.charAt(0) + hexStrn.charAt(0) + hexStrn.charAt(1) + hexStrn.charAt(1) + hexStrn.charAt(2) + hexStrn.charAt(2);
/*     */       }
/* 237 */       int hexVal = parseHex(hexStrn);
/*     */       
/* 239 */       retVal = new Color(hexVal);
/*     */     }
/*     */     else {
/*     */       
/* 243 */       String number = "\\s*(((\\d+)(\\.\\d*)?)|(\\.\\d+))(%)?\\s*";
/* 244 */       Matcher rgbMatch = Pattern.compile("rgb\\(\\s*(((\\d+)(\\.\\d*)?)|(\\.\\d+))(%)?\\s*,\\s*(((\\d+)(\\.\\d*)?)|(\\.\\d+))(%)?\\s*,\\s*(((\\d+)(\\.\\d*)?)|(\\.\\d+))(%)?\\s*\\)", 2).matcher("");
/*     */       
/* 246 */       rgbMatch.reset(val);
/* 247 */       if (rgbMatch.matches()) {
/*     */         
/* 249 */         float rr = Float.parseFloat(rgbMatch.group(1));
/* 250 */         float gg = Float.parseFloat(rgbMatch.group(7));
/* 251 */         float bb = Float.parseFloat(rgbMatch.group(13));
/* 252 */         rr /= "%".equals(rgbMatch.group(6)) ? 100.0F : 255.0F;
/* 253 */         gg /= "%".equals(rgbMatch.group(12)) ? 100.0F : 255.0F;
/* 254 */         bb /= "%".equals(rgbMatch.group(18)) ? 100.0F : 255.0F;
/* 255 */         retVal = new Color(rr, gg, bb);
/*     */       }
/*     */       else {
/*     */         
/* 259 */         Color lookupCol = instance().lookupColor(val);
/* 260 */         if (lookupCol != null) retVal = lookupCol;
/*     */       
/*     */       } 
/*     */     } 
/* 264 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseHex(String val) {
/* 269 */     int retVal = 0;
/*     */     
/* 271 */     for (int i = 0; i < val.length(); i++) {
/*     */       
/* 273 */       retVal <<= 4;
/*     */       
/* 275 */       char ch = val.charAt(i);
/* 276 */       if (ch >= '0' && ch <= '9') {
/*     */         
/* 278 */         retVal |= ch - 48;
/*     */       }
/* 280 */       else if (ch >= 'a' && ch <= 'z') {
/*     */         
/* 282 */         retVal |= ch - 97 + 10;
/*     */       }
/* 284 */       else if (ch >= 'A' && ch <= 'Z') {
/*     */         
/* 286 */         retVal |= ch - 65 + 10;
/*     */       } else {
/* 288 */         throw new RuntimeException();
/*     */       } 
/*     */     } 
/* 291 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\ColorTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */