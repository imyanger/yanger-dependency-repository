package com.yanger.starter.web.xss;

import com.google.common.collect.Maps;

import com.yanger.tools.general.constant.StringPool;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * HTML filtering utility for protecting against XSS (Cross Site Scripting).
 * <p>
 * This code is licensed LGPLv3
 * <p>
 * This code is a Java port of the original work in PHP by Cal Hendersen.
 * http://code.iamcal.com/php/lib_filter/
 * <p>
 * The trickiest part of the translation was handling the differences in regex handling
 * between PHP and Java.  These resources were helpful in the process:
 * <p>
 * http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html
 * http://us2.php.net/manual/en/reference.pcre.pattern.modifiers.php
 * http://www.regular-expressions.info/modifiers.html
 * <p>
 * A note on naming conventions: instance variables are prefixed with a "v"; global
 * constants are in all caps.
 * <p>
 * Sample use:
 * String input = ...
 * String clean = new HtmlFilter().filter( input );
 * <p>
 * The class is not thread safe. Create a new instance if in doubt.
 * <p>
 * If you find bugs or have suggestions on improvement (especially regarding
 * performance), please contact us.  The latest version of this
 * source, and our contact details, can be found at http://xss-html-filter.sf.net
 */
@Slf4j
public final class HtmlFilter {

    /** regex flag union representing /si modifiers in php */
    private static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

    /** P_COMMENTS */
    private static final Pattern P_COMMENTS = Pattern.compile("<!--(.*?)-->", Pattern.DOTALL);

    /** P_COMMENT */
    private static final Pattern P_COMMENT = Pattern.compile("^!--(.*)--$", REGEX_FLAGS_SI);

    /** P_TAGS */
    private static final Pattern P_TAGS = Pattern.compile("<(.*?)>", Pattern.DOTALL);

    /** P_END_TAG */
    private static final Pattern P_END_TAG = Pattern.compile("^/([a-z0-9]+)", REGEX_FLAGS_SI);

    /** P_START_TAG */
    private static final Pattern P_START_TAG = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", REGEX_FLAGS_SI);

    /** P_QUOTED_ATTRIBUTES */
    private static final Pattern P_QUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2", REGEX_FLAGS_SI);

    /** P_UNQUOTED_ATTRIBUTES */
    private static final Pattern P_UNQUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)", REGEX_FLAGS_SI);

    /** P_PROTOCOL */
    private static final Pattern P_PROTOCOL = Pattern.compile("^([^:]+):", REGEX_FLAGS_SI);

    /** P_ENTITY */
    private static final Pattern P_ENTITY = Pattern.compile("&#(\\d+);?");

    /** P_ENTITY_UNICODE */
    private static final Pattern P_ENTITY_UNICODE = Pattern.compile("&#x([0-9a-f]+);?");

    /** P_ENCODE */
    private static final Pattern P_ENCODE = Pattern.compile("%([0-9a-f]{2});?");

    /** P_VALID_ENTITIES */
    private static final Pattern P_VALID_ENTITIES = Pattern.compile("&([^&;]*)(?=(;|&|$))");

    /** P_VALID_QUOTES */
    private static final Pattern P_VALID_QUOTES = Pattern.compile("(>|^)([^<]+?)(<|$)", Pattern.DOTALL);

    /** P_END_ARROW */
    private static final Pattern P_END_ARROW = Pattern.compile("^>");

    /** P_BODY_TO_END */
    private static final Pattern P_BODY_TO_END = Pattern.compile("<([^>]*?)(?=<|$)");

    /** P_XML_CONTENT */
    private static final Pattern P_XML_CONTENT = Pattern.compile("(^|>)([^<]*?)(?=>)");

    /** P_STRAY_LEFT_ARROW */
    private static final Pattern P_STRAY_LEFT_ARROW = Pattern.compile("<([^>]*?)(?=<|$)");

    /** P_STRAY_RIGHT_ARROW */
    private static final Pattern P_STRAY_RIGHT_ARROW = Pattern.compile("(^|>)([^<]*?)(?=>)");

    /** P_AMP */
    private static final Pattern P_AMP = Pattern.compile("&");

    /** P_QUOTE */
    private static final Pattern P_QUOTE = Pattern.compile("<");

    /** P_LEFT_ARROW */
    private static final Pattern P_LEFT_ARROW = Pattern.compile("<");

    /** P_RIGHT_ARROW */
    private static final Pattern P_RIGHT_ARROW = Pattern.compile(">");

    /** P_BOTH_ARROWS */
    private static final Pattern P_BOTH_ARROWS = Pattern.compile("<>");


    /** P_REMOVE_PAIR_BLANKS */
    private static final ConcurrentMap<String, Pattern> P_REMOVE_PAIR_BLANKS = new ConcurrentHashMap<>();

    /** P_REMOVE_SELF_BLANKS */
    private static final ConcurrentMap<String, Pattern> P_REMOVE_SELF_BLANKS = new ConcurrentHashMap<>();

    /**
     * set of allowed html elements, along with allowed attributes for each element
     */
    private final Map<String, List<String>> vAllowed;

    /**
     * counts of open tags for each (allowable) html element
     */
    private final Map<String, Integer> vTagCounts = Maps.newHashMap();

    /**
     * html elements which must always be self-closing (e.g. "<img />")
     */
    private final String[] vSelfClosingTags;

    /**
     * html elements which must always have separate opening and closing tags (e.g. "<b></b>")
     */
    private final String[] vNeedClosingTags;

    /**
     * set of disallowed html elements
     */
    private final String[] vDisallowed;

    /**
     * attributes which should be checked for valid protocols
     */
    private final String[] vProtocolAtts;

    /**
     * allowed protocols
     */
    private final String[] vAllowedProtocols;

    /**
     * tags which should be removed if they contain no content (e.g. "<b></b>" or "<b />")
     */
    private final String[] vRemoveBlanks;

    /**
     * entities allowed within html markup
     */
    private final String[] vAllowedEntities;

    /**
     * flag determining whether comments are allowed in input String.
     */
    private final boolean stripComment;

    /** Encode quotes */
    private final boolean encodeQuotes;

    /**
     * flag determining whether to try to make tags when presented with "unbalanced"
     * angle brackets.  If set to false,
     * unbalanced angle brackets will be html escaped.
     */
    private final boolean alwaysMakeTags;

    /** V debug */
    private boolean vDebug = false;

    /**
     * Set debug flag to true. Otherwise use default settings. See the default constructor.
     *
     * @param debug turn debug on with a true argument
     */
    public HtmlFilter(boolean debug) {
        this();
        this.vDebug = debug;

    }

    /**
     * Default constructor.
     */
    public HtmlFilter() {
        this.vAllowed = Maps.newHashMapWithExpectedSize(6);

        List<String> aAtts = new ArrayList<>();
        aAtts.add("href");
        aAtts.add("target");
        this.vAllowed.put("a", aAtts);

        List<String> imgAtts = new ArrayList<>();
        imgAtts.add("src");
        imgAtts.add("width");
        imgAtts.add("height");
        imgAtts.add("alt");
        this.vAllowed.put("img", imgAtts);

        List<String> noAtts = new ArrayList<>();
        this.vAllowed.put("b", noAtts);
        this.vAllowed.put("strong", noAtts);
        this.vAllowed.put("i", noAtts);
        this.vAllowed.put("em", noAtts);

        this.vSelfClosingTags = new String[] {"img"};
        this.vNeedClosingTags = new String[] {"a", "b", "strong", "i", "em"};
        this.vDisallowed = new String[] {};
        this.vAllowedProtocols = new String[] {"http", "mailto", "https"};
        this.vProtocolAtts = new String[] {"src", "href"};
        this.vRemoveBlanks = new String[] {"a", "b", "strong", "i", "em"};
        this.vAllowedEntities = new String[] {"amp", "gt", "lt", "quot"};
        this.stripComment = true;
        this.encodeQuotes = true;
        this.alwaysMakeTags = true;
    }

    /**
     * Map-parameter configurable constructor.
     *
     * @param conf map containing configuration. keys match field names.
     */
    @SuppressWarnings("unchecked")
    public HtmlFilter(@NotNull Map<String, Object> conf) {

        assert conf.containsKey("vAllowed") : "configuration requires vAllowed";
        assert conf.containsKey("vSelfClosingTags") : "configuration requires vSelfClosingTags";
        assert conf.containsKey("vNeedClosingTags") : "configuration requires vNeedClosingTags";
        assert conf.containsKey("vDisallowed") : "configuration requires vDisallowed";
        assert conf.containsKey("vAllowedProtocols") : "configuration requires vAllowedProtocols";
        assert conf.containsKey("vProtocolAtts") : "configuration requires vProtocolAtts";
        assert conf.containsKey("vRemoveBlanks") : "configuration requires vRemoveBlanks";
        assert conf.containsKey("vAllowedEntities") : "configuration requires vAllowedEntities";

        this.vAllowed = Collections.unmodifiableMap((Map<String, List<String>>) conf.get("vAllowed"));
        this.vSelfClosingTags = (String[]) conf.get("vSelfClosingTags");
        this.vNeedClosingTags = (String[]) conf.get("vNeedClosingTags");
        this.vDisallowed = (String[]) conf.get("vDisallowed");
        this.vAllowedProtocols = (String[]) conf.get("vAllowedProtocols");
        this.vProtocolAtts = (String[]) conf.get("vProtocolAtts");
        this.vRemoveBlanks = (String[]) conf.get("vRemoveBlanks");
        this.vAllowedEntities = (String[]) conf.get("vAllowedEntities");
        this.stripComment = conf.containsKey("stripComment") ? (Boolean) conf.get("stripComment") : true;
        this.encodeQuotes = conf.containsKey("encodeQuotes") ? (Boolean) conf.get("encodeQuotes") : true;
        this.alwaysMakeTags = conf.containsKey("alwaysMakeTags") ? (Boolean) conf.get("alwaysMakeTags") : true;
    }

    /**
     * Chr string.
     *
     * @param decimal the decimal
     * @return the string
     */
    @NotNull
    @Contract(pure = true)
    public static String chr(int decimal) {
        return String.valueOf((char) decimal);
    }

    /**
     * Html special chars string.
     *
     * @param s the s
     * @return the string
     */
    public static String htmlSpecialChars(String s) {
        String result = s;
        result = regexReplace(P_AMP, "&amp;", result);
        result = regexReplace(P_QUOTE, "&quot;", result);
        result = regexReplace(P_LEFT_ARROW, "&lt;", result);
        result = regexReplace(P_RIGHT_ARROW, "&gt;", result);
        return result;
    }

    /**
     * Regex replace string
     *
     * @param regexPattern regex pattern
     * @param replacement  replacement
     * @param s            s
     * @return the string
     */
    private static String regexReplace(@NotNull Pattern regexPattern, String replacement, String s) {
        Matcher m = regexPattern.matcher(s);
        return m.replaceAll(replacement);
    }

    /**
     * In array boolean
     *
     * @param s     s
     * @param array array
     * @return the boolean
     */
    @Contract(pure = true)
    private static boolean inArray(String s, @NotNull String[] array) {
        for (String item : array) {
            if (item.equals(s)) {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Reset
     */
    private void reset() {
        this.vTagCounts.clear();
    }

    /**
     * Debug *
     *
     * @param msg msg
     */
    private void debug(String msg) {
        if (this.vDebug) {
            log.info(msg);
        }
    }

    /**
     * given a user submitted input String, filter out any invalid or restricted
     * html.
     *
     * @param input text (i.e. submitted by a user) than may contain html
     * @return "clean" version of input, with only valid, whitelisted html elements allowed
     */
    public String filter(String input) {
        this.reset();
        String s = input;

        this.debug("************************************************");
        this.debug("              INPUT: " + input);

        s = escapeComments(s);
        this.debug("     escapeComments: " + s);

        s = this.balanceHtml(s);
        this.debug("        balanceHtml: " + s);

        s = this.checkTags(s);
        this.debug("          checkTags: " + s);

        s = this.processRemoveBlanks(s);
        this.debug("processRemoveBlanks: " + s);

        s = this.validateEntities(s);
        this.debug("    validateEntites: " + s);

        this.debug("************************************************\n\n");
        return s;
    }

    /**
     * Is always make tags boolean.
     *
     * @return the boolean
     */
    @Contract(pure = true)
    public boolean isAlwaysMakeTags() {
        return this.alwaysMakeTags;
    }

    /**
     * Is strip comments boolean.
     *
     * @return the boolean
     */
    @Contract(pure = true)
    public boolean isStripComments() {
        return this.stripComment;
    }

    /**
     * Escape comments string
     *
     * @param s s
     * @return the string
     */
    @NotNull
    private static String escapeComments(String s) {
        Matcher m = P_COMMENTS.matcher(s);
        StringBuffer buf = new StringBuffer();
        if (m.find()) {
            String match = m.group(1);
            m.appendReplacement(buf, Matcher.quoteReplacement("<!--" + htmlSpecialChars(match) + "-->"));
        }
        m.appendTail(buf);

        return buf.toString();
    }

    /**
     * Balance html string
     *
     * @param s s
     * @return the string
     */
    private String balanceHtml(String s) {
        if (this.alwaysMakeTags) {
            //
            // try and form html
            //
            s = regexReplace(P_END_ARROW, "", s);
            s = regexReplace(P_BODY_TO_END, "<$1>", s);
            s = regexReplace(P_XML_CONTENT, "$1<$2", s);

        } else {
            //
            // escape stray brackets
            //
            s = regexReplace(P_STRAY_LEFT_ARROW, "&lt;$1", s);
            s = regexReplace(P_STRAY_RIGHT_ARROW, "$1$2&gt;<", s);

            //
            // the last regexp causes '<>' entities to appear
            // (we need to do a lookahead assertion so that the last bracket can
            // be used in the next pass of the regexp)
            //
            s = regexReplace(P_BOTH_ARROWS, "", s);
        }

        return s;
    }

    /**
     * Check tags string
     *
     * @param s s
     * @return the string
     */
    @NotNull
    private String checkTags(String s) {
        Matcher m = P_TAGS.matcher(s);

        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            String replaceStr = m.group(1);
            replaceStr = this.processTag(replaceStr);
            m.appendReplacement(buf, Matcher.quoteReplacement(replaceStr));
        }
        m.appendTail(buf);

        s = buf.toString();

        // these get tallied in processTag
        // (remember to reset before subsequent calls to filter method)
        StringBuilder sb = new StringBuilder(s);

        for (String key : this.vTagCounts.keySet()) {
            for (int ii = 0; ii < this.vTagCounts.get(key); ii++) {
                sb.append("</").append(key).append(">");
            }
        }

        return sb.toString();
    }

    /**
     * Process remove blanks string
     *
     * @param s s
     * @return the string
     */
    private String processRemoveBlanks(String s) {
        String result = s;
        for (String tag : this.vRemoveBlanks) {
            if (!P_REMOVE_PAIR_BLANKS.containsKey(tag)) {
                P_REMOVE_PAIR_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?></" + tag + ">"));
            }
            result = regexReplace(P_REMOVE_PAIR_BLANKS.get(tag), "", result);
            if (!P_REMOVE_SELF_BLANKS.containsKey(tag)) {
                P_REMOVE_SELF_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?/>"));
            }
            result = regexReplace(P_REMOVE_SELF_BLANKS.get(tag), "", result);
        }

        return result;
    }

    /**
     * Process tag string
     *
     * @param s s
     * @return the string
     */
    @NotNull
    @SuppressWarnings("checkstyle:ReturnCount")
    private String processTag(String s) {
        Matcher m = P_END_TAG.matcher(s);
        if (m.find()) {
            String name = m.group(1).toLowerCase();
            if (this.allowed(name)) {
                if (!inArray(name, this.vSelfClosingTags)) {
                    if (this.vTagCounts.containsKey(name)) {
                        this.vTagCounts.put(name, this.vTagCounts.get(name) - 1);
                        return "</" + name + ">";
                    }
                }
            }
        }
        m = P_START_TAG.matcher(s);
        if (m.find()) {
            String name = m.group(1).toLowerCase();
            String body = m.group(2);
            String ending = m.group(3);
            if (this.allowed(name)) {
                StringBuilder params = new StringBuilder();
                Matcher m2 = P_QUOTED_ATTRIBUTES.matcher(body);
                Matcher m3 = P_UNQUOTED_ATTRIBUTES.matcher(body);
                List<String> paramNames = new ArrayList<>();
                List<String> paramValues = new ArrayList<>();
                while (m2.find()) {
                    paramNames.add(m2.group(1));
                    paramValues.add(m2.group(3));
                }
                while (m3.find()) {
                    paramNames.add(m3.group(1));
                    paramValues.add(m3.group(3));
                }
                String paramName, paramValue;
                for (int ii = 0; ii < paramNames.size(); ii++) {
                    paramName = paramNames.get(ii).toLowerCase();
                    paramValue = paramValues.get(ii);
                    if (this.allowedAttribute(name, paramName)) {
                        if (inArray(paramName, this.vProtocolAtts)) {
                            paramValue = this.processParamProtocol(paramValue);
                        }
                        params.append(" ").append(paramName).append("=\"").append(paramValue).append("\"");
                    }
                }
                if (inArray(name, this.vSelfClosingTags)) {
                    ending = " /";
                }
                if (inArray(name, this.vNeedClosingTags)) {
                    ending = "";
                }
                if (ending == null || ending.length() < 1) {
                    if (this.vTagCounts.containsKey(name)) {
                        this.vTagCounts.put(name, this.vTagCounts.get(name) + 1);
                    } else {
                        this.vTagCounts.put(name, 1);
                    }
                } else {
                    ending = " /";
                }
                return "<" + name + params + ending + ">";
            } else {
                return "";
            }
        }
        m = P_COMMENT.matcher(s);
        if (!this.stripComment && m.find()) {
            return "<" + m.group() + ">";
        }
        return "";
    }

    /**
     * Process param protocol string
     *
     * @param s s
     * @return the string
     */
    private String processParamProtocol(String s) {
        s = this.decodeEntities(s);
        Matcher m = P_PROTOCOL.matcher(s);
        if (m.find()) {
            String protocol = m.group(1);
            if (!inArray(protocol, this.vAllowedProtocols)) {
                // bad protocol, turn into local anchor link instead
                s = "#" + s.substring(protocol.length() + 1);
                if (s.startsWith(StringPool.DOUBLE_SLASH)) {
                    s = "#" + s.substring(3);
                }
            }
        }

        return s;
    }

    /**
     * Decode entities string
     *
     * @param s s
     * @return the string
     */
    private String decodeEntities(String s) {
        StringBuffer buf = new StringBuffer();

        Matcher m = P_ENTITY.matcher(s);
        while (m.find()) {
            String match = m.group(1);
            int decimal = Integer.decode(match);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }
        m.appendTail(buf);
        s = buf.toString();

        buf = new StringBuffer();
        m = P_ENTITY_UNICODE.matcher(s);

        s = this.getString(buf, m);

        buf = new StringBuffer();
        m = P_ENCODE.matcher(s);

        s = this.getString(buf, m);

        s = this.validateEntities(s);
        return s;
    }

    /**
     * Gets string *
     *
     * @param buf buf
     * @param m   m
     * @return the string
     */
    @NotNull
    private String getString(StringBuffer buf, @NotNull Matcher m) {
        String s;
        while (m.find()) {
            String match = m.group(1);
            int decimal = Integer.valueOf(match, 16);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }
        m.appendTail(buf);
        s = buf.toString();
        return s;
    }

    /**
     * Validate entities string
     *
     * @param s s
     * @return the string
     */
    private String validateEntities(String s) {
        StringBuffer buf = new StringBuffer();

        // validate entities throughout the string
        Matcher m = P_VALID_ENTITIES.matcher(s);
        while (m.find()) {
            String one = m.group(1);
            String two = m.group(2);
            m.appendReplacement(buf, Matcher.quoteReplacement(this.checkEntity(one, two)));
        }
        m.appendTail(buf);

        return this.encodeQuotes(buf.toString());
    }

    /**
     * Encode quotes string
     *
     * @param s s
     * @return the string
     */
    private String encodeQuotes(String s) {
        if (this.encodeQuotes) {
            StringBuffer buf = new StringBuffer();
            Matcher m = P_VALID_QUOTES.matcher(s);
            while (m.find()) {
                String one = m.group(1);
                String two = m.group(2);
                String three = m.group(3);
                m.appendReplacement(buf, Matcher.quoteReplacement(one + regexReplace(P_QUOTE, "&quot;", two) + three));
            }
            m.appendTail(buf);
            return buf.toString();
        } else {
            return s;
        }
    }

    /**
     * Check entity string
     *
     * @param preamble preamble
     * @param term     term
     * @return the string
     */
    @NotNull
    private String checkEntity(String preamble, String term) {

        return ";".equals(term)
               && this.isValidEntity(preamble)
               ? '&' + preamble
               : "&amp;" + preamble;
    }

    /**
     * Is valid entity boolean
     *
     * @param entity entity
     * @return the boolean
     */
    @Contract(pure = true)
    private boolean isValidEntity(String entity) {
        return inArray(entity, this.vAllowedEntities);
    }

    /**
     * Allowed boolean
     *
     * @param name name
     * @return the boolean
     */
    private boolean allowed(String name) {
        return (this.vAllowed.isEmpty() || this.vAllowed.containsKey(name)) && !inArray(name, this.vDisallowed);
    }

    /**
     * Allowed attribute boolean
     *
     * @param name      name
     * @param paramName param name
     * @return the boolean
     */
    private boolean allowedAttribute(String name, String paramName) {
        return this.allowed(name) && (this.vAllowed.isEmpty() || this.vAllowed.get(name).contains(paramName));
    }
}
