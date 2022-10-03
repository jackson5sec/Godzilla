/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableListMultimap;
/*      */ import com.google.common.collect.ImmutableMultiset;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.ListMultimap;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Multimaps;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.errorprone.annotations.Immutable;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Immutable
/*      */ @Beta
/*      */ @GwtCompatible
/*      */ public final class MediaType
/*      */ {
/*      */   private static final String CHARSET_ATTRIBUTE = "charset";
/*   81 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*      */ 
/*      */ 
/*      */   
/*   85 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*   86 */     .and(CharMatcher.javaIsoControl().negate())
/*   87 */     .and(CharMatcher.isNot(' '))
/*   88 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*      */   
/*   90 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*      */   
/*      */   private static final String APPLICATION_TYPE = "application";
/*      */   
/*      */   private static final String AUDIO_TYPE = "audio";
/*      */   
/*      */   private static final String IMAGE_TYPE = "image";
/*      */   
/*      */   private static final String TEXT_TYPE = "text";
/*      */   private static final String VIDEO_TYPE = "video";
/*      */   private static final String WILDCARD = "*";
/*  107 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*      */ 
/*      */   
/*      */   private static MediaType createConstant(String type, String subtype) {
/*  111 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*  112 */     mediaType.parsedCharset = Optional.absent();
/*  113 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType createConstantUtf8(String type, String subtype) {
/*  117 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*  118 */     mediaType.parsedCharset = Optional.of(Charsets.UTF_8);
/*  119 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType addKnownType(MediaType mediaType) {
/*  123 */     KNOWN_TYPES.put(mediaType, mediaType);
/*  124 */     return mediaType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  137 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/*  138 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/*  139 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/*  140 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/*  141 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/*  142 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*      */ 
/*      */ 
/*      */   
/*  146 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/*  147 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/*  148 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/*  149 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/*  150 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/*  151 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  165 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/*      */   
/*  167 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  175 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  182 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  190 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  198 */   public static final MediaType BMP = createConstant("image", "bmp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  209 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/*      */   
/*  211 */   public static final MediaType GIF = createConstant("image", "gif");
/*  212 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/*  213 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/*  214 */   public static final MediaType PNG = createConstant("image", "png");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  233 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/*      */   
/*  235 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/*  236 */   public static final MediaType TIFF = createConstant("image", "tiff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  243 */   public static final MediaType WEBP = createConstant("image", "webp");
/*      */ 
/*      */   
/*  246 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/*  247 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/*  248 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/*  249 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  256 */   public static final MediaType L16_AUDIO = createConstant("audio", "l16");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  263 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  271 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  279 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  286 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  295 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  304 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  312 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  319 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*      */ 
/*      */   
/*  322 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/*  323 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/*  324 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/*  325 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/*  326 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/*  327 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  336 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  345 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  354 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  364 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/*      */   
/*  366 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/*  367 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  375 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  383 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  392 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  403 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*      */ 
/*      */   
/*  406 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  415 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  427 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*      */   
/*  429 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  437 */   public static final MediaType HAL_JSON = createConstant("application", "hal+json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  445 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  453 */   public static final MediaType JOSE = createConstant("application", "jose");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  461 */   public static final MediaType JOSE_JSON = createConstant("application", "jose+json");
/*      */   
/*  463 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  471 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  476 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  482 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  489 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  497 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/*      */ 
/*      */   
/*  500 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  508 */   public static final MediaType MICROSOFT_OUTLOOK = createConstant("application", "vnd.ms-outlook");
/*      */ 
/*      */ 
/*      */   
/*  512 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/*      */ 
/*      */   
/*  515 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  523 */   public static final MediaType WASM_APPLICATION = createConstant("application", "wasm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  532 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  542 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*      */   
/*  544 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*      */   
/*  546 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*      */   
/*  548 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*      */ 
/*      */   
/*  551 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*      */ 
/*      */   
/*  554 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*      */   
/*  556 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*      */   
/*  558 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*      */   
/*  560 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*      */   
/*  562 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/*  563 */   public static final MediaType PDF = createConstant("application", "pdf");
/*  564 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  571 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  581 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/*      */   
/*  583 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  593 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*      */ 
/*      */   
/*  596 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  604 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  617 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/*      */   
/*  619 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  629 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  637 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/*      */   
/*  639 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  649 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/*      */   
/*  651 */   public static final MediaType ZIP = createConstant("application", "zip");
/*      */   
/*      */   private final String type;
/*      */   private final String subtype;
/*      */   private final ImmutableListMultimap<String, String> parameters;
/*      */   @LazyInit
/*      */   private String toString;
/*      */   @LazyInit
/*      */   private int hashCode;
/*      */   @LazyInit
/*      */   private Optional<Charset> parsedCharset;
/*      */   
/*      */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
/*  664 */     this.type = type;
/*  665 */     this.subtype = subtype;
/*  666 */     this.parameters = parameters;
/*      */   }
/*      */ 
/*      */   
/*      */   public String type() {
/*  671 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public String subtype() {
/*  676 */     return this.subtype;
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableListMultimap<String, String> parameters() {
/*  681 */     return this.parameters;
/*      */   }
/*      */   
/*      */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/*  685 */     return Maps.transformValues((Map)this.parameters
/*  686 */         .asMap(), new Function<Collection<String>, ImmutableMultiset<String>>()
/*      */         {
/*      */           public ImmutableMultiset<String> apply(Collection<String> input)
/*      */           {
/*  690 */             return ImmutableMultiset.copyOf(input);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Optional<Charset> charset() {
/*  705 */     Optional<Charset> local = this.parsedCharset;
/*  706 */     if (local == null) {
/*  707 */       String value = null;
/*  708 */       local = Optional.absent();
/*  709 */       for (UnmodifiableIterator<String> unmodifiableIterator = this.parameters.get("charset").iterator(); unmodifiableIterator.hasNext(); ) { String currentValue = unmodifiableIterator.next();
/*  710 */         if (value == null) {
/*  711 */           value = currentValue;
/*  712 */           local = Optional.of(Charset.forName(value)); continue;
/*  713 */         }  if (!value.equals(currentValue)) {
/*  714 */           throw new IllegalStateException("Multiple charset values defined: " + value + ", " + currentValue);
/*      */         } }
/*      */ 
/*      */       
/*  718 */       this.parsedCharset = local;
/*      */     } 
/*  720 */     return local;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withoutParameters() {
/*  728 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(Multimap<String, String> parameters) {
/*  737 */     return create(this.type, this.subtype, parameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(String attribute, Iterable<String> values) {
/*  748 */     Preconditions.checkNotNull(attribute);
/*  749 */     Preconditions.checkNotNull(values);
/*  750 */     String normalizedAttribute = normalizeToken(attribute);
/*  751 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  752 */     for (UnmodifiableIterator<Map.Entry<String, String>> unmodifiableIterator = this.parameters.entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<String, String> entry = unmodifiableIterator.next();
/*  753 */       String key = entry.getKey();
/*  754 */       if (!normalizedAttribute.equals(key)) {
/*  755 */         builder.put(key, entry.getValue());
/*      */       } }
/*      */     
/*  758 */     for (String value : values) {
/*  759 */       builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/*      */     }
/*  761 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*      */     
/*  763 */     if (!normalizedAttribute.equals("charset")) {
/*  764 */       mediaType.parsedCharset = this.parsedCharset;
/*      */     }
/*      */     
/*  767 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameter(String attribute, String value) {
/*  779 */     return withParameters(attribute, (Iterable<String>)ImmutableSet.of(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withCharset(Charset charset) {
/*  792 */     Preconditions.checkNotNull(charset);
/*  793 */     MediaType withCharset = withParameter("charset", charset.name());
/*      */     
/*  795 */     withCharset.parsedCharset = Optional.of(charset);
/*  796 */     return withCharset;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasWildcard() {
/*  801 */     return ("*".equals(this.type) || "*".equals(this.subtype));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is(MediaType mediaTypeRange) {
/*  834 */     return ((mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype
/*  835 */       .equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters
/*  836 */       .entries().containsAll((Collection)mediaTypeRange.parameters.entries()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MediaType create(String type, String subtype) {
/*  846 */     MediaType mediaType = create(type, subtype, (Multimap<String, String>)ImmutableListMultimap.of());
/*  847 */     mediaType.parsedCharset = Optional.absent();
/*  848 */     return mediaType;
/*      */   }
/*      */ 
/*      */   
/*      */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
/*  853 */     Preconditions.checkNotNull(type);
/*  854 */     Preconditions.checkNotNull(subtype);
/*  855 */     Preconditions.checkNotNull(parameters);
/*  856 */     String normalizedType = normalizeToken(type);
/*  857 */     String normalizedSubtype = normalizeToken(subtype);
/*  858 */     Preconditions.checkArgument((
/*  859 */         !"*".equals(normalizedType) || "*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*      */     
/*  861 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  862 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)parameters.entries()) {
/*  863 */       String attribute = normalizeToken(entry.getKey());
/*  864 */       builder.put(attribute, normalizeParameterValue(attribute, entry.getValue()));
/*      */     } 
/*  866 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*      */     
/*  868 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createApplicationType(String subtype) {
/*  877 */     return create("application", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createAudioType(String subtype) {
/*  886 */     return create("audio", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createImageType(String subtype) {
/*  895 */     return create("image", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createTextType(String subtype) {
/*  904 */     return create("text", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createVideoType(String subtype) {
/*  913 */     return create("video", subtype);
/*      */   }
/*      */   
/*      */   private static String normalizeToken(String token) {
/*  917 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/*  918 */     return Ascii.toLowerCase(token);
/*      */   }
/*      */   
/*      */   private static String normalizeParameterValue(String attribute, String value) {
/*  922 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MediaType parse(String input) {
/*  931 */     Preconditions.checkNotNull(input);
/*  932 */     Tokenizer tokenizer = new Tokenizer(input);
/*      */     try {
/*  934 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/*  935 */       tokenizer.consumeCharacter('/');
/*  936 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/*  937 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/*  938 */       while (tokenizer.hasMore()) {
/*  939 */         String value; tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/*  940 */         tokenizer.consumeCharacter(';');
/*  941 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/*  942 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/*  943 */         tokenizer.consumeCharacter('=');
/*      */         
/*  945 */         if ('"' == tokenizer.previewChar()) {
/*  946 */           tokenizer.consumeCharacter('"');
/*  947 */           StringBuilder valueBuilder = new StringBuilder();
/*  948 */           while ('"' != tokenizer.previewChar()) {
/*  949 */             if ('\\' == tokenizer.previewChar()) {
/*  950 */               tokenizer.consumeCharacter('\\');
/*  951 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii())); continue;
/*      */             } 
/*  953 */             valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*      */           } 
/*      */           
/*  956 */           value = valueBuilder.toString();
/*  957 */           tokenizer.consumeCharacter('"');
/*      */         } else {
/*  959 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*      */         } 
/*  961 */         parameters.put(attribute, value);
/*      */       } 
/*  963 */       return create(type, subtype, (Multimap<String, String>)parameters.build());
/*  964 */     } catch (IllegalStateException e) {
/*  965 */       throw new IllegalArgumentException("Could not parse '" + input + "'", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Tokenizer {
/*      */     final String input;
/*  971 */     int position = 0;
/*      */     
/*      */     Tokenizer(String input) {
/*  974 */       this.input = input;
/*      */     }
/*      */     
/*      */     String consumeTokenIfPresent(CharMatcher matcher) {
/*  978 */       Preconditions.checkState(hasMore());
/*  979 */       int startPosition = this.position;
/*  980 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/*  981 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*      */     }
/*      */     
/*      */     String consumeToken(CharMatcher matcher) {
/*  985 */       int startPosition = this.position;
/*  986 */       String token = consumeTokenIfPresent(matcher);
/*  987 */       Preconditions.checkState((this.position != startPosition));
/*  988 */       return token;
/*      */     }
/*      */     
/*      */     char consumeCharacter(CharMatcher matcher) {
/*  992 */       Preconditions.checkState(hasMore());
/*  993 */       char c = previewChar();
/*  994 */       Preconditions.checkState(matcher.matches(c));
/*  995 */       this.position++;
/*  996 */       return c;
/*      */     }
/*      */     
/*      */     char consumeCharacter(char c) {
/* 1000 */       Preconditions.checkState(hasMore());
/* 1001 */       Preconditions.checkState((previewChar() == c));
/* 1002 */       this.position++;
/* 1003 */       return c;
/*      */     }
/*      */     
/*      */     char previewChar() {
/* 1007 */       Preconditions.checkState(hasMore());
/* 1008 */       return this.input.charAt(this.position);
/*      */     }
/*      */     
/*      */     boolean hasMore() {
/* 1012 */       return (this.position >= 0 && this.position < this.input.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1018 */     if (obj == this)
/* 1019 */       return true; 
/* 1020 */     if (obj instanceof MediaType) {
/* 1021 */       MediaType that = (MediaType)obj;
/* 1022 */       return (this.type.equals(that.type) && this.subtype
/* 1023 */         .equals(that.subtype) && 
/*      */         
/* 1025 */         parametersAsMap().equals(that.parametersAsMap()));
/*      */     } 
/* 1027 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1034 */     int h = this.hashCode;
/* 1035 */     if (h == 0) {
/* 1036 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/* 1037 */       this.hashCode = h;
/*      */     } 
/* 1039 */     return h;
/*      */   }
/*      */   
/* 1042 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1051 */     String result = this.toString;
/* 1052 */     if (result == null) {
/* 1053 */       result = computeToString();
/* 1054 */       this.toString = result;
/*      */     } 
/* 1056 */     return result;
/*      */   }
/*      */   
/*      */   private String computeToString() {
/* 1060 */     StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
/* 1061 */     if (!this.parameters.isEmpty()) {
/* 1062 */       builder.append("; ");
/*      */       
/* 1064 */       ListMultimap listMultimap = Multimaps.transformValues((ListMultimap)this.parameters, new Function<String, String>()
/*      */           {
/*      */             
/*      */             public String apply(String value)
/*      */             {
/* 1069 */               return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
/*      */             }
/*      */           });
/* 1072 */       PARAMETER_JOINER.appendTo(builder, listMultimap.entries());
/*      */     } 
/* 1074 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private static String escapeAndQuote(String value) {
/* 1078 */     StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
/* 1079 */     for (int i = 0; i < value.length(); i++) {
/* 1080 */       char ch = value.charAt(i);
/* 1081 */       if (ch == '\r' || ch == '\\' || ch == '"') {
/* 1082 */         escaped.append('\\');
/*      */       }
/* 1084 */       escaped.append(ch);
/*      */     } 
/* 1086 */     return escaped.append('"').toString();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */