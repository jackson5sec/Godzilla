/*     */ package com.httpProxy.server.response;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponseStatus
/*     */ {
/*   7 */   private int code = 500;
/*     */ 
/*     */   
/*  10 */   private String reasonPhrase = "Internal Server Error";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  16 */   public static final HttpResponseStatus CONTINUE = newStatus(100, "Continue");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  21 */   public static final HttpResponseStatus SWITCHING_PROTOCOLS = newStatus(101, "Switching Protocols");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final HttpResponseStatus PROCESSING = newStatus(102, "Processing");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static final HttpResponseStatus OK = newStatus(200, "OK");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final HttpResponseStatus CREATED = newStatus(201, "Created");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final HttpResponseStatus ACCEPTED = newStatus(202, "Accepted");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final HttpResponseStatus NON_AUTHORITATIVE_INFORMATION = newStatus(203, "Non-Authoritative Information");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final HttpResponseStatus NO_CONTENT = newStatus(204, "No Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final HttpResponseStatus RESET_CONTENT = newStatus(205, "Reset Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final HttpResponseStatus PARTIAL_CONTENT = newStatus(206, "Partial Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final HttpResponseStatus MULTI_STATUS = newStatus(207, "Multi-Status");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static final HttpResponseStatus MULTIPLE_CHOICES = newStatus(300, "Multiple Choices");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final HttpResponseStatus MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final HttpResponseStatus FOUND = newStatus(302, "Found");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final HttpResponseStatus SEE_OTHER = newStatus(303, "See Other");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final HttpResponseStatus NOT_MODIFIED = newStatus(304, "Not Modified");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final HttpResponseStatus USE_PROXY = newStatus(305, "Use Proxy");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public static final HttpResponseStatus TEMPORARY_REDIRECT = newStatus(307, "Temporary Redirect");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static final HttpResponseStatus PERMANENT_REDIRECT = newStatus(308, "Permanent Redirect");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static final HttpResponseStatus BAD_REQUEST = newStatus(400, "Bad Request");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public static final HttpResponseStatus UNAUTHORIZED = newStatus(401, "Unauthorized");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static final HttpResponseStatus PAYMENT_REQUIRED = newStatus(402, "Payment Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static final HttpResponseStatus FORBIDDEN = newStatus(403, "Forbidden");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public static final HttpResponseStatus NOT_FOUND = newStatus(404, "Not Found");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public static final HttpResponseStatus METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public static final HttpResponseStatus NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public static final HttpResponseStatus REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public static final HttpResponseStatus CONFLICT = newStatus(409, "Conflict");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public static final HttpResponseStatus GONE = newStatus(410, "Gone");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static final HttpResponseStatus LENGTH_REQUIRED = newStatus(411, "Length Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public static final HttpResponseStatus PRECONDITION_FAILED = newStatus(412, "Precondition Failed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE = newStatus(413, "Request Entity Too Large");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final HttpResponseStatus REQUEST_URI_TOO_LONG = newStatus(414, "Request-URI Too Long");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE = newStatus(415, "Unsupported Media Type");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public static final HttpResponseStatus REQUESTED_RANGE_NOT_SATISFIABLE = newStatus(416, "Requested Range Not Satisfiable");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public static final HttpResponseStatus EXPECTATION_FAILED = newStatus(417, "Expectation Failed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public static final HttpResponseStatus MISDIRECTED_REQUEST = newStatus(421, "Misdirected Request");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public static final HttpResponseStatus UNPROCESSABLE_ENTITY = newStatus(422, "Unprocessable Entity");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static final HttpResponseStatus LOCKED = newStatus(423, "Locked");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   public static final HttpResponseStatus FAILED_DEPENDENCY = newStatus(424, "Failed Dependency");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   public static final HttpResponseStatus UNORDERED_COLLECTION = newStatus(425, "Unordered Collection");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public static final HttpResponseStatus UPGRADE_REQUIRED = newStatus(426, "Upgrade Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public static final HttpResponseStatus PRECONDITION_REQUIRED = newStatus(428, "Precondition Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public static final HttpResponseStatus TOO_MANY_REQUESTS = newStatus(429, "Too Many Requests");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public static final HttpResponseStatus REQUEST_HEADER_FIELDS_TOO_LARGE = newStatus(431, "Request Header Fields Too Large");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public static final HttpResponseStatus INTERNAL_SERVER_ERROR = newStatus(500, "Internal Server Error");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public static final HttpResponseStatus NOT_IMPLEMENTED = newStatus(501, "Not Implemented");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public static final HttpResponseStatus BAD_GATEWAY = newStatus(502, "Bad Gateway");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public static final HttpResponseStatus SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public static final HttpResponseStatus GATEWAY_TIMEOUT = newStatus(504, "Gateway Timeout");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 279 */   public static final HttpResponseStatus HTTP_VERSION_NOT_SUPPORTED = newStatus(505, "HTTP Version Not Supported");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static final HttpResponseStatus VARIANT_ALSO_NEGOTIATES = newStatus(506, "Variant Also Negotiates");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static final HttpResponseStatus INSUFFICIENT_STORAGE = newStatus(507, "Insufficient Storage");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 294 */   public static final HttpResponseStatus NOT_EXTENDED = newStatus(510, "Not Extended");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   public static final HttpResponseStatus NETWORK_AUTHENTICATION_REQUIRED = newStatus(511, "Network Authentication Required");
/*     */   
/*     */   public HttpResponseStatus(int code, String reasonPhrase, boolean b) {
/* 303 */     this.code = code;
/* 304 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */   
/*     */   public HttpResponseStatus(int code) {
/* 308 */     this(code, (valueOf(code)).reasonPhrase, false);
/*     */   }
/*     */   
/*     */   private static HttpResponseStatus newStatus(int statusCode, String reasonPhrase) {
/* 312 */     return new HttpResponseStatus(statusCode, reasonPhrase, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpResponseStatus valueOf(int code) {
/* 321 */     HttpResponseStatus status = valueOf0(code);
/* 322 */     return (status != null) ? status : new HttpResponseStatus(code);
/*     */   }
/*     */   
/*     */   private static HttpResponseStatus valueOf0(int code) {
/* 326 */     switch (code) {
/*     */       case 100:
/* 328 */         return CONTINUE;
/*     */       case 101:
/* 330 */         return SWITCHING_PROTOCOLS;
/*     */       case 102:
/* 332 */         return PROCESSING;
/*     */       case 200:
/* 334 */         return OK;
/*     */       case 201:
/* 336 */         return CREATED;
/*     */       case 202:
/* 338 */         return ACCEPTED;
/*     */       case 203:
/* 340 */         return NON_AUTHORITATIVE_INFORMATION;
/*     */       case 204:
/* 342 */         return NO_CONTENT;
/*     */       case 205:
/* 344 */         return RESET_CONTENT;
/*     */       case 206:
/* 346 */         return PARTIAL_CONTENT;
/*     */       case 207:
/* 348 */         return MULTI_STATUS;
/*     */       case 300:
/* 350 */         return MULTIPLE_CHOICES;
/*     */       case 301:
/* 352 */         return MOVED_PERMANENTLY;
/*     */       case 302:
/* 354 */         return FOUND;
/*     */       case 303:
/* 356 */         return SEE_OTHER;
/*     */       case 304:
/* 358 */         return NOT_MODIFIED;
/*     */       case 305:
/* 360 */         return USE_PROXY;
/*     */       case 307:
/* 362 */         return TEMPORARY_REDIRECT;
/*     */       case 308:
/* 364 */         return PERMANENT_REDIRECT;
/*     */       case 400:
/* 366 */         return BAD_REQUEST;
/*     */       case 401:
/* 368 */         return UNAUTHORIZED;
/*     */       case 402:
/* 370 */         return PAYMENT_REQUIRED;
/*     */       case 403:
/* 372 */         return FORBIDDEN;
/*     */       case 404:
/* 374 */         return NOT_FOUND;
/*     */       case 405:
/* 376 */         return METHOD_NOT_ALLOWED;
/*     */       case 406:
/* 378 */         return NOT_ACCEPTABLE;
/*     */       case 407:
/* 380 */         return PROXY_AUTHENTICATION_REQUIRED;
/*     */       case 408:
/* 382 */         return REQUEST_TIMEOUT;
/*     */       case 409:
/* 384 */         return CONFLICT;
/*     */       case 410:
/* 386 */         return GONE;
/*     */       case 411:
/* 388 */         return LENGTH_REQUIRED;
/*     */       case 412:
/* 390 */         return PRECONDITION_FAILED;
/*     */       case 413:
/* 392 */         return REQUEST_ENTITY_TOO_LARGE;
/*     */       case 414:
/* 394 */         return REQUEST_URI_TOO_LONG;
/*     */       case 415:
/* 396 */         return UNSUPPORTED_MEDIA_TYPE;
/*     */       case 416:
/* 398 */         return REQUESTED_RANGE_NOT_SATISFIABLE;
/*     */       case 417:
/* 400 */         return EXPECTATION_FAILED;
/*     */       case 421:
/* 402 */         return MISDIRECTED_REQUEST;
/*     */       case 422:
/* 404 */         return UNPROCESSABLE_ENTITY;
/*     */       case 423:
/* 406 */         return LOCKED;
/*     */       case 424:
/* 408 */         return FAILED_DEPENDENCY;
/*     */       case 425:
/* 410 */         return UNORDERED_COLLECTION;
/*     */       case 426:
/* 412 */         return UPGRADE_REQUIRED;
/*     */       case 428:
/* 414 */         return PRECONDITION_REQUIRED;
/*     */       case 429:
/* 416 */         return TOO_MANY_REQUESTS;
/*     */       case 431:
/* 418 */         return REQUEST_HEADER_FIELDS_TOO_LARGE;
/*     */       case 500:
/* 420 */         return INTERNAL_SERVER_ERROR;
/*     */       case 501:
/* 422 */         return NOT_IMPLEMENTED;
/*     */       case 502:
/* 424 */         return BAD_GATEWAY;
/*     */       case 503:
/* 426 */         return SERVICE_UNAVAILABLE;
/*     */       case 504:
/* 428 */         return GATEWAY_TIMEOUT;
/*     */       case 505:
/* 430 */         return HTTP_VERSION_NOT_SUPPORTED;
/*     */       case 506:
/* 432 */         return VARIANT_ALSO_NEGOTIATES;
/*     */       case 507:
/* 434 */         return INSUFFICIENT_STORAGE;
/*     */       case 510:
/* 436 */         return NOT_EXTENDED;
/*     */       case 511:
/* 438 */         return NETWORK_AUTHENTICATION_REQUIRED;
/*     */     } 
/* 440 */     return null;
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
/*     */   public static HttpResponseStatus valueOf(int code, String reasonPhrase) {
/* 452 */     HttpResponseStatus responseStatus = valueOf0(code);
/* 453 */     return (responseStatus != null && responseStatus.reasonPhrase().contentEquals(reasonPhrase)) ? responseStatus : new HttpResponseStatus(code, reasonPhrase);
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
/*     */   public static HttpResponseStatus parseLine(String line) {
/*     */     try {
/* 469 */       int space = line.indexOf(' ');
/* 470 */       return (space == -1) ? valueOf(Integer.parseInt(line)) : 
/* 471 */         valueOf(Integer.parseInt(line.substring(0, space)), line.substring(space + 1));
/* 472 */     } catch (Exception e) {
/* 473 */       throw new IllegalArgumentException("malformed status line: " + line, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponseStatus(int code, String reasonPhrase) {
/* 482 */     this(code, reasonPhrase, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int code() {
/* 491 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String reasonPhrase() {
/* 499 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 506 */     return code();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 515 */     if (!(o instanceof HttpResponseStatus)) {
/* 516 */       return false;
/*     */     }
/*     */     
/* 519 */     return (code() == ((HttpResponseStatus)o).code());
/*     */   }
/*     */   
/*     */   public int getCode() {
/* 523 */     return this.code;
/*     */   }
/*     */   
/*     */   public HttpResponseStatus setCode(int code) {
/* 527 */     this.code = code;
/* 528 */     return this;
/*     */   }
/*     */   
/*     */   public String getReasonPhrase() {
/* 532 */     return this.reasonPhrase;
/*     */   }
/*     */   
/*     */   public HttpResponseStatus setReasonPhrase(String reasonPhrase) {
/* 536 */     this.reasonPhrase = reasonPhrase;
/* 537 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 542 */     return "HttpResponseStatus{code=" + this.code + ", reasonPhrase='" + this.reasonPhrase + '\'' + '}';
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\response\HttpResponseStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */