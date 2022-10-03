/*     */ package com.jediterm.terminal.emulator.charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum CharacterSet
/*     */ {
/*   8 */   ASCII(new int[] { 66 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  13 */       return -1;
/*     */     }
/*     */   },
/*  16 */   BRITISH(new int[] { 65 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  21 */       if (index == 3)
/*     */       {
/*     */         
/*  24 */         return 163;
/*     */       }
/*  26 */       return -1;
/*     */     }
/*     */   },
/*  29 */   DANISH(new int[] { 69, 54 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  34 */       switch (index) {
/*     */         
/*     */         case 32:
/*  37 */           return 196;
/*     */         case 59:
/*  39 */           return 198;
/*     */         case 60:
/*  41 */           return 216;
/*     */         case 61:
/*  43 */           return 197;
/*     */         case 62:
/*  45 */           return 220;
/*     */         case 64:
/*  47 */           return 228;
/*     */         case 91:
/*  49 */           return 230;
/*     */         case 92:
/*  51 */           return 248;
/*     */         case 93:
/*  53 */           return 229;
/*     */         case 94:
/*  55 */           return 252;
/*     */       } 
/*  57 */       return -1;
/*     */     }
/*     */   },
/*     */   
/*  61 */   DEC_SPECIAL_GRAPHICS(new int[] { 48, 50 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  66 */       if (index >= 64 && index < 96)
/*     */       {
/*  68 */         return ((Character)CharacterSets.DEC_SPECIAL_CHARS[index - 64][0]).charValue();
/*     */       }
/*  70 */       return -1;
/*     */     }
/*     */   },
/*  73 */   DEC_SUPPLEMENTAL(new int[] { 85, 60 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  78 */       if (index >= 0 && index < 64)
/*     */       {
/*     */         
/*  81 */         return index + 160;
/*     */       }
/*  83 */       return -1;
/*     */     }
/*     */   },
/*  86 */   DUTCH(new int[] { 52 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/*  91 */       switch (index) {
/*     */         
/*     */         case 3:
/*  94 */           return 163;
/*     */         case 32:
/*  96 */           return 190;
/*     */         case 59:
/*  98 */           return 307;
/*     */         case 60:
/* 100 */           return 189;
/*     */         case 61:
/* 102 */           return 124;
/*     */         case 91:
/* 104 */           return 168;
/*     */         case 92:
/* 106 */           return 402;
/*     */         case 93:
/* 108 */           return 188;
/*     */         case 94:
/* 110 */           return 180;
/*     */       } 
/* 112 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 116 */   FINNISH(new int[] { 67, 53 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 121 */       switch (index) {
/*     */         
/*     */         case 59:
/* 124 */           return 196;
/*     */         case 60:
/* 126 */           return 212;
/*     */         case 61:
/* 128 */           return 197;
/*     */         case 62:
/* 130 */           return 220;
/*     */         case 64:
/* 132 */           return 233;
/*     */         case 91:
/* 134 */           return 228;
/*     */         case 92:
/* 136 */           return 246;
/*     */         case 93:
/* 138 */           return 229;
/*     */         case 94:
/* 140 */           return 252;
/*     */       } 
/* 142 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 146 */   FRENCH(new int[] { 82 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 151 */       switch (index) {
/*     */         
/*     */         case 3:
/* 154 */           return 163;
/*     */         case 32:
/* 156 */           return 224;
/*     */         case 59:
/* 158 */           return 176;
/*     */         case 60:
/* 160 */           return 231;
/*     */         case 61:
/* 162 */           return 166;
/*     */         case 91:
/* 164 */           return 233;
/*     */         case 92:
/* 166 */           return 249;
/*     */         case 93:
/* 168 */           return 232;
/*     */         case 94:
/* 170 */           return 168;
/*     */       } 
/* 172 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 176 */   FRENCH_CANADIAN(new int[] { 81 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 181 */       switch (index) {
/*     */         
/*     */         case 32:
/* 184 */           return 224;
/*     */         case 59:
/* 186 */           return 226;
/*     */         case 60:
/* 188 */           return 231;
/*     */         case 61:
/* 190 */           return 234;
/*     */         case 62:
/* 192 */           return 238;
/*     */         case 91:
/* 194 */           return 233;
/*     */         case 92:
/* 196 */           return 249;
/*     */         case 93:
/* 198 */           return 232;
/*     */         case 94:
/* 200 */           return 251;
/*     */       } 
/* 202 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 206 */   GERMAN(new int[] { 75 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 211 */       switch (index) {
/*     */         
/*     */         case 32:
/* 214 */           return 167;
/*     */         case 59:
/* 216 */           return 196;
/*     */         case 60:
/* 218 */           return 214;
/*     */         case 61:
/* 220 */           return 220;
/*     */         case 91:
/* 222 */           return 228;
/*     */         case 92:
/* 224 */           return 246;
/*     */         case 93:
/* 226 */           return 252;
/*     */         case 94:
/* 228 */           return 223;
/*     */       } 
/* 230 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 234 */   ITALIAN(new int[] { 89 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 239 */       switch (index) {
/*     */         
/*     */         case 3:
/* 242 */           return 163;
/*     */         case 32:
/* 244 */           return 167;
/*     */         case 59:
/* 246 */           return 186;
/*     */         case 60:
/* 248 */           return 231;
/*     */         case 61:
/* 250 */           return 233;
/*     */         case 91:
/* 252 */           return 224;
/*     */         case 92:
/* 254 */           return 242;
/*     */         case 93:
/* 256 */           return 232;
/*     */         case 94:
/* 258 */           return 236;
/*     */       } 
/* 260 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 264 */   SPANISH(new int[] { 90 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 269 */       switch (index) {
/*     */         
/*     */         case 3:
/* 272 */           return 163;
/*     */         case 32:
/* 274 */           return 167;
/*     */         case 59:
/* 276 */           return 161;
/*     */         case 60:
/* 278 */           return 209;
/*     */         case 61:
/* 280 */           return 191;
/*     */         case 91:
/* 282 */           return 176;
/*     */         case 92:
/* 284 */           return 241;
/*     */         case 93:
/* 286 */           return 231;
/*     */       } 
/* 288 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 292 */   SWEDISH(new int[] { 72, 55 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 297 */       switch (index) {
/*     */         
/*     */         case 32:
/* 300 */           return 201;
/*     */         case 59:
/* 302 */           return 196;
/*     */         case 60:
/* 304 */           return 214;
/*     */         case 61:
/* 306 */           return 197;
/*     */         case 62:
/* 308 */           return 220;
/*     */         case 64:
/* 310 */           return 233;
/*     */         case 91:
/* 312 */           return 228;
/*     */         case 92:
/* 314 */           return 246;
/*     */         case 93:
/* 316 */           return 229;
/*     */         case 94:
/* 318 */           return 252;
/*     */       } 
/* 320 */       return -1;
/*     */     }
/*     */   },
/*     */   
/* 324 */   SWISS(new int[] { 61 })
/*     */   {
/*     */     
/*     */     public int map(int index)
/*     */     {
/* 329 */       switch (index) {
/*     */         
/*     */         case 3:
/* 332 */           return 249;
/*     */         case 32:
/* 334 */           return 224;
/*     */         case 59:
/* 336 */           return 233;
/*     */         case 60:
/* 338 */           return 231;
/*     */         case 61:
/* 340 */           return 234;
/*     */         case 62:
/* 342 */           return 238;
/*     */         case 63:
/* 344 */           return 232;
/*     */         case 64:
/* 346 */           return 244;
/*     */         case 91:
/* 348 */           return 228;
/*     */         case 92:
/* 350 */           return 246;
/*     */         case 93:
/* 352 */           return 252;
/*     */         case 94:
/* 354 */           return 251;
/*     */       } 
/* 356 */       return -1;
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] myDesignations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CharacterSet(int... designations) {
/* 372 */     this.myDesignations = designations;
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
/*     */   private boolean isDesignation(char designation) {
/* 419 */     for (int myDesignation : this.myDesignations) {
/* 420 */       if (myDesignation == designation) {
/* 421 */         return true;
/*     */       }
/*     */     } 
/* 424 */     return false;
/*     */   }
/*     */   
/*     */   public abstract int map(int paramInt);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\charset\CharacterSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */