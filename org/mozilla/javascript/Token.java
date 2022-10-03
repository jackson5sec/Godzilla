/*     */ package org.mozilla.javascript;public class Token { public static final boolean printTrees = false; static final boolean printICode = false; static final boolean printNames = false; public static final int ERROR = -1; public static final int EOF = 0; public static final int EOL = 1; public static final int FIRST_BYTECODE_TOKEN = 2; public static final int ENTERWITH = 2; public static final int LEAVEWITH = 3; public static final int RETURN = 4; public static final int GOTO = 5; public static final int IFEQ = 6; public static final int IFNE = 7; public static final int SETNAME = 8; public static final int BITOR = 9; public static final int BITXOR = 10; public static final int BITAND = 11; public static final int EQ = 12; public static final int NE = 13; public static final int LT = 14; public static final int LE = 15; public static final int GT = 16; public static final int GE = 17; public static final int LSH = 18; public static final int RSH = 19; public static final int URSH = 20; public static final int ADD = 21; public static final int SUB = 22; public static final int MUL = 23; public static final int DIV = 24; public static final int MOD = 25; public static final int NOT = 26; public static final int BITNOT = 27; public static final int POS = 28; public static final int NEG = 29; public static final int NEW = 30; public static final int DELPROP = 31; public static final int TYPEOF = 32; public static final int GETPROP = 33; public static final int GETPROPNOWARN = 34; public static final int SETPROP = 35; public static final int GETELEM = 36; public static final int SETELEM = 37; public static final int CALL = 38; public static final int NAME = 39; public static final int NUMBER = 40; public static final int STRING = 41; public static final int NULL = 42; public static final int THIS = 43; public static final int FALSE = 44; public static final int TRUE = 45; public static final int SHEQ = 46; public static final int SHNE = 47; public static final int REGEXP = 48; public static final int BINDNAME = 49; public static final int THROW = 50; public static final int RETHROW = 51; public static final int IN = 52; public static final int INSTANCEOF = 53; public static final int LOCAL_LOAD = 54; public static final int GETVAR = 55; public static final int SETVAR = 56; public static final int CATCH_SCOPE = 57; public static final int ENUM_INIT_KEYS = 58; public static final int ENUM_INIT_VALUES = 59; public static final int ENUM_INIT_ARRAY = 60; public static final int ENUM_NEXT = 61; public static final int ENUM_ID = 62; public static final int THISFN = 63; public static final int RETURN_RESULT = 64; public static final int ARRAYLIT = 65; public static final int OBJECTLIT = 66; public static final int GET_REF = 67; public static final int SET_REF = 68; public static final int DEL_REF = 69; public static final int REF_CALL = 70; public static final int REF_SPECIAL = 71; public static final int YIELD = 72; public static final int STRICT_SETNAME = 73; public static final int DEFAULTNAMESPACE = 74; public static final int ESCXMLATTR = 75; public static final int ESCXMLTEXT = 76; public static final int REF_MEMBER = 77; public static final int REF_NS_MEMBER = 78; public static final int REF_NAME = 79; public static final int REF_NS_NAME = 80; public static final int LAST_BYTECODE_TOKEN = 80; public static final int TRY = 81; public static final int SEMI = 82; public static final int LB = 83; public static final int RB = 84; public static final int LC = 85; public static final int RC = 86; public static final int LP = 87; public static final int RP = 88; public static final int COMMA = 89; public static final int ASSIGN = 90; public static final int ASSIGN_BITOR = 91; public static final int ASSIGN_BITXOR = 92; public static final int ASSIGN_BITAND = 93; public static final int ASSIGN_LSH = 94; public static final int ASSIGN_RSH = 95; public static final int ASSIGN_URSH = 96; public static final int ASSIGN_ADD = 97; public static final int ASSIGN_SUB = 98; public static final int ASSIGN_MUL = 99; public static final int ASSIGN_DIV = 100; public static final int ASSIGN_MOD = 101; public static final int FIRST_ASSIGN = 90; public static final int LAST_ASSIGN = 101; public static final int HOOK = 102; public static final int COLON = 103; public static final int OR = 104; public static final int AND = 105; public static final int INC = 106; public static final int DEC = 107; public static final int DOT = 108; public static final int FUNCTION = 109; public static final int EXPORT = 110; public static final int IMPORT = 111; public static final int IF = 112; public static final int ELSE = 113; public static final int SWITCH = 114; public static final int CASE = 115; public static final int DEFAULT = 116; public static final int WHILE = 117; public static final int DO = 118; public static final int FOR = 119; public static final int BREAK = 120; public static final int CONTINUE = 121; public static final int VAR = 122; public static final int WITH = 123; public static final int CATCH = 124; public static final int FINALLY = 125; public static final int VOID = 126; public static final int RESERVED = 127; public static final int EMPTY = 128; public static final int BLOCK = 129; public static final int LABEL = 130; public static final int TARGET = 131; public static final int LOOP = 132; public static final int EXPR_VOID = 133; public static final int EXPR_RESULT = 134; public static final int JSR = 135; public static final int SCRIPT = 136; public static final int TYPEOFNAME = 137; public static final int USE_STACK = 138; public static final int SETPROP_OP = 139; public static final int SETELEM_OP = 140; public static final int LOCAL_BLOCK = 141; public static final int SET_REF_OP = 142;
/*     */   public static final int DOTDOT = 143;
/*     */   public static final int COLONCOLON = 144;
/*     */   public static final int XML = 145;
/*     */   public static final int DOTQUERY = 146;
/*     */   public static final int XMLATTR = 147;
/*     */   public static final int XMLEND = 148;
/*     */   public static final int TO_OBJECT = 149;
/*     */   public static final int TO_DOUBLE = 150;
/*     */   public static final int GET = 151;
/*     */   public static final int SET = 152;
/*     */   public static final int LET = 153;
/*     */   public static final int CONST = 154;
/*     */   public static final int SETCONST = 155;
/*     */   public static final int SETCONSTVAR = 156;
/*     */   public static final int ARRAYCOMP = 157;
/*     */   public static final int LETEXPR = 158;
/*     */   public static final int WITHEXPR = 159;
/*     */   public static final int DEBUGGER = 160;
/*     */   public static final int COMMENT = 161;
/*     */   public static final int GENEXPR = 162;
/*     */   public static final int LAST_TOKEN = 163;
/*     */   
/*  24 */   public enum CommentType { LINE, BLOCK_COMMENT, JSDOC, HTML; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String name(int token) {
/* 241 */     return String.valueOf(token);
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
/*     */   public static String typeToName(int token) {
/* 253 */     switch (token) { case -1:
/* 254 */         return "ERROR";
/* 255 */       case 0: return "EOF";
/* 256 */       case 1: return "EOL";
/* 257 */       case 2: return "ENTERWITH";
/* 258 */       case 3: return "LEAVEWITH";
/* 259 */       case 4: return "RETURN";
/* 260 */       case 5: return "GOTO";
/* 261 */       case 6: return "IFEQ";
/* 262 */       case 7: return "IFNE";
/* 263 */       case 8: return "SETNAME";
/* 264 */       case 9: return "BITOR";
/* 265 */       case 10: return "BITXOR";
/* 266 */       case 11: return "BITAND";
/* 267 */       case 12: return "EQ";
/* 268 */       case 13: return "NE";
/* 269 */       case 14: return "LT";
/* 270 */       case 15: return "LE";
/* 271 */       case 16: return "GT";
/* 272 */       case 17: return "GE";
/* 273 */       case 18: return "LSH";
/* 274 */       case 19: return "RSH";
/* 275 */       case 20: return "URSH";
/* 276 */       case 21: return "ADD";
/* 277 */       case 22: return "SUB";
/* 278 */       case 23: return "MUL";
/* 279 */       case 24: return "DIV";
/* 280 */       case 25: return "MOD";
/* 281 */       case 26: return "NOT";
/* 282 */       case 27: return "BITNOT";
/* 283 */       case 28: return "POS";
/* 284 */       case 29: return "NEG";
/* 285 */       case 30: return "NEW";
/* 286 */       case 31: return "DELPROP";
/* 287 */       case 32: return "TYPEOF";
/* 288 */       case 33: return "GETPROP";
/* 289 */       case 34: return "GETPROPNOWARN";
/* 290 */       case 35: return "SETPROP";
/* 291 */       case 36: return "GETELEM";
/* 292 */       case 37: return "SETELEM";
/* 293 */       case 38: return "CALL";
/* 294 */       case 39: return "NAME";
/* 295 */       case 40: return "NUMBER";
/* 296 */       case 41: return "STRING";
/* 297 */       case 42: return "NULL";
/* 298 */       case 43: return "THIS";
/* 299 */       case 44: return "FALSE";
/* 300 */       case 45: return "TRUE";
/* 301 */       case 46: return "SHEQ";
/* 302 */       case 47: return "SHNE";
/* 303 */       case 48: return "REGEXP";
/* 304 */       case 49: return "BINDNAME";
/* 305 */       case 50: return "THROW";
/* 306 */       case 51: return "RETHROW";
/* 307 */       case 52: return "IN";
/* 308 */       case 53: return "INSTANCEOF";
/* 309 */       case 54: return "LOCAL_LOAD";
/* 310 */       case 55: return "GETVAR";
/* 311 */       case 56: return "SETVAR";
/* 312 */       case 57: return "CATCH_SCOPE";
/* 313 */       case 58: return "ENUM_INIT_KEYS";
/* 314 */       case 59: return "ENUM_INIT_VALUES";
/* 315 */       case 60: return "ENUM_INIT_ARRAY";
/* 316 */       case 61: return "ENUM_NEXT";
/* 317 */       case 62: return "ENUM_ID";
/* 318 */       case 63: return "THISFN";
/* 319 */       case 64: return "RETURN_RESULT";
/* 320 */       case 65: return "ARRAYLIT";
/* 321 */       case 66: return "OBJECTLIT";
/* 322 */       case 67: return "GET_REF";
/* 323 */       case 68: return "SET_REF";
/* 324 */       case 69: return "DEL_REF";
/* 325 */       case 70: return "REF_CALL";
/* 326 */       case 71: return "REF_SPECIAL";
/* 327 */       case 74: return "DEFAULTNAMESPACE";
/* 328 */       case 76: return "ESCXMLTEXT";
/* 329 */       case 75: return "ESCXMLATTR";
/* 330 */       case 77: return "REF_MEMBER";
/* 331 */       case 78: return "REF_NS_MEMBER";
/* 332 */       case 79: return "REF_NAME";
/* 333 */       case 80: return "REF_NS_NAME";
/* 334 */       case 81: return "TRY";
/* 335 */       case 82: return "SEMI";
/* 336 */       case 83: return "LB";
/* 337 */       case 84: return "RB";
/* 338 */       case 85: return "LC";
/* 339 */       case 86: return "RC";
/* 340 */       case 87: return "LP";
/* 341 */       case 88: return "RP";
/* 342 */       case 89: return "COMMA";
/* 343 */       case 90: return "ASSIGN";
/* 344 */       case 91: return "ASSIGN_BITOR";
/* 345 */       case 92: return "ASSIGN_BITXOR";
/* 346 */       case 93: return "ASSIGN_BITAND";
/* 347 */       case 94: return "ASSIGN_LSH";
/* 348 */       case 95: return "ASSIGN_RSH";
/* 349 */       case 96: return "ASSIGN_URSH";
/* 350 */       case 97: return "ASSIGN_ADD";
/* 351 */       case 98: return "ASSIGN_SUB";
/* 352 */       case 99: return "ASSIGN_MUL";
/* 353 */       case 100: return "ASSIGN_DIV";
/* 354 */       case 101: return "ASSIGN_MOD";
/* 355 */       case 102: return "HOOK";
/* 356 */       case 103: return "COLON";
/* 357 */       case 104: return "OR";
/* 358 */       case 105: return "AND";
/* 359 */       case 106: return "INC";
/* 360 */       case 107: return "DEC";
/* 361 */       case 108: return "DOT";
/* 362 */       case 109: return "FUNCTION";
/* 363 */       case 110: return "EXPORT";
/* 364 */       case 111: return "IMPORT";
/* 365 */       case 112: return "IF";
/* 366 */       case 113: return "ELSE";
/* 367 */       case 114: return "SWITCH";
/* 368 */       case 115: return "CASE";
/* 369 */       case 116: return "DEFAULT";
/* 370 */       case 117: return "WHILE";
/* 371 */       case 118: return "DO";
/* 372 */       case 119: return "FOR";
/* 373 */       case 120: return "BREAK";
/* 374 */       case 121: return "CONTINUE";
/* 375 */       case 122: return "VAR";
/* 376 */       case 123: return "WITH";
/* 377 */       case 124: return "CATCH";
/* 378 */       case 125: return "FINALLY";
/* 379 */       case 126: return "VOID";
/* 380 */       case 127: return "RESERVED";
/* 381 */       case 128: return "EMPTY";
/* 382 */       case 129: return "BLOCK";
/* 383 */       case 130: return "LABEL";
/* 384 */       case 131: return "TARGET";
/* 385 */       case 132: return "LOOP";
/* 386 */       case 133: return "EXPR_VOID";
/* 387 */       case 134: return "EXPR_RESULT";
/* 388 */       case 135: return "JSR";
/* 389 */       case 136: return "SCRIPT";
/* 390 */       case 137: return "TYPEOFNAME";
/* 391 */       case 138: return "USE_STACK";
/* 392 */       case 139: return "SETPROP_OP";
/* 393 */       case 140: return "SETELEM_OP";
/* 394 */       case 141: return "LOCAL_BLOCK";
/* 395 */       case 142: return "SET_REF_OP";
/* 396 */       case 143: return "DOTDOT";
/* 397 */       case 144: return "COLONCOLON";
/* 398 */       case 145: return "XML";
/* 399 */       case 146: return "DOTQUERY";
/* 400 */       case 147: return "XMLATTR";
/* 401 */       case 148: return "XMLEND";
/* 402 */       case 149: return "TO_OBJECT";
/* 403 */       case 150: return "TO_DOUBLE";
/* 404 */       case 151: return "GET";
/* 405 */       case 152: return "SET";
/* 406 */       case 153: return "LET";
/* 407 */       case 72: return "YIELD";
/* 408 */       case 154: return "CONST";
/* 409 */       case 155: return "SETCONST";
/* 410 */       case 157: return "ARRAYCOMP";
/* 411 */       case 159: return "WITHEXPR";
/* 412 */       case 158: return "LETEXPR";
/* 413 */       case 160: return "DEBUGGER";
/* 414 */       case 161: return "COMMENT";
/* 415 */       case 162: return "GENEXPR"; }
/*     */ 
/*     */ 
/*     */     
/* 419 */     throw new IllegalStateException(String.valueOf(token));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String keywordToName(int token) {
/* 429 */     switch (token) { case 120:
/* 430 */         return "break";
/* 431 */       case 115: return "case";
/* 432 */       case 121: return "continue";
/* 433 */       case 116: return "default";
/* 434 */       case 31: return "delete";
/* 435 */       case 118: return "do";
/* 436 */       case 113: return "else";
/* 437 */       case 44: return "false";
/* 438 */       case 119: return "for";
/* 439 */       case 109: return "function";
/* 440 */       case 112: return "if";
/* 441 */       case 52: return "in";
/* 442 */       case 153: return "let";
/* 443 */       case 30: return "new";
/* 444 */       case 42: return "null";
/* 445 */       case 4: return "return";
/* 446 */       case 114: return "switch";
/* 447 */       case 43: return "this";
/* 448 */       case 45: return "true";
/* 449 */       case 32: return "typeof";
/* 450 */       case 122: return "var";
/* 451 */       case 126: return "void";
/* 452 */       case 117: return "while";
/* 453 */       case 123: return "with";
/* 454 */       case 72: return "yield";
/* 455 */       case 124: return "catch";
/* 456 */       case 154: return "const";
/* 457 */       case 160: return "debugger";
/* 458 */       case 125: return "finally";
/* 459 */       case 53: return "instanceof";
/* 460 */       case 50: return "throw";
/* 461 */       case 81: return "try"; }
/* 462 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidToken(int code) {
/* 472 */     return (code >= -1 && code <= 163);
/*     */   } }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */