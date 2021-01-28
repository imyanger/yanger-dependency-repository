/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <https://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package com.fkhwl.starter.mybatis.util;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Performs formatting of basic SQL statements (DML + query).
 * <p>Copy Hibernate BasicFormatterImpl</p>
 * <p>last commit on 2018-03-15</p>
 *
 * @author Gavin King
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.17 18:10
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.UndefineMagicConstantRule"})
public class SqlFormatter {

    /** WHITESPACE */
    private static final String WHITESPACE = " \n\r\f\t";
    /** BEGIN_CLAUSES */
    private static final Set<String> BEGIN_CLAUSES = new HashSet<>();
    /** END_CLAUSES */
    private static final Set<String> END_CLAUSES = new HashSet<>();
    /** LOGICAL */
    private static final Set<String> LOGICAL = new HashSet<>();
    /** QUANTIFIERS */
    private static final Set<String> QUANTIFIERS = new HashSet<>();
    /** DML */
    private static final Set<String> DML = new HashSet<>();
    /** MISC */
    private static final Set<String> MISC = new HashSet<>();
    /** INDENT_STRING */
    private static final String INDENT_STRING = "    ";
    /** INITIAL */
    private static final String INITIAL = System.lineSeparator() + INDENT_STRING;

    static {
        BEGIN_CLAUSES.add("left");
        BEGIN_CLAUSES.add("right");
        BEGIN_CLAUSES.add("inner");
        BEGIN_CLAUSES.add("outer");
        BEGIN_CLAUSES.add("group");
        BEGIN_CLAUSES.add("order");

        END_CLAUSES.add("where");
        END_CLAUSES.add("set");
        END_CLAUSES.add("having");
        END_CLAUSES.add("from");
        END_CLAUSES.add("by");
        END_CLAUSES.add("join");
        END_CLAUSES.add("into");
        END_CLAUSES.add("union");

        LOGICAL.add("and");
        LOGICAL.add("or");
        LOGICAL.add("when");
        LOGICAL.add("else");
        LOGICAL.add("end");

        QUANTIFIERS.add("in");
        QUANTIFIERS.add("all");
        QUANTIFIERS.add("exists");
        QUANTIFIERS.add("some");
        QUANTIFIERS.add("any");

        DML.add("insert");
        DML.add("update");
        DML.add("delete");

        MISC.add("select");
        MISC.add("on");
    }

    /**
     * Format string
     *
     * @param source source
     * @return the string
     * @since 1.0.0
     */
    public String format(String source) {
        return new FormatProcess(source).perform();
    }

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.4
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.02.17 18:10
     * @since 1.0.0
     */
    private static class FormatProcess {
        /** Begin line */
        boolean beginLine = true;
        /** After begin before end */
        boolean afterBeginBeforeEnd;
        /** After by or set or from or select */
        boolean afterByOrSetOrFromOrSelect;
        /** After values */
        boolean afterValues;
        /** After on */
        boolean afterOn;
        /** After between */
        boolean afterBetween;
        /** After insert */
        boolean afterInsert;
        /** In function */
        int inFunction;
        /** Parens since select */
        int parensSinceSelect;
        /** Result */
        StringBuilder result = new StringBuilder();
        /** Tokens */
        StringTokenizer tokens;

        /** Indent */
        int indent = 1;
        /** Paren counts */
        private final LinkedList<Integer> parenCounts = new LinkedList<>();
        /** After by or from or selects */
        private final LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<>();
        /** Last token */
        String lastToken;
        /** Token */
        String token;
        /** Lc token */
        String lcToken;

        /**
         * Format process
         *
         * @param sql sql
         * @since 1.0.0
         */
        FormatProcess(String sql) {
            this.tokens = new StringTokenizer(
                sql,
                "()+*/-=<>'`\"[]," + WHITESPACE,
                true
            );
        }

        /**
         * Is function name boolean
         *
         * @param tok tok
         * @return the boolean
         * @since 1.0.0
         */
        @Contract("null -> false")
        private static boolean isFunctionName(String tok) {
            if (tok == null || tok.length() == 0) {
                return false;
            }

            char begin = tok.charAt(0);
            boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;
            return isIdentifier
                   && !LOGICAL.contains(tok)
                   && !END_CLAUSES.contains(tok)
                   && !QUANTIFIERS.contains(tok)
                   && !DML.contains(tok)
                   && !MISC.contains(tok);
        }

        /**
         * Comma after on
         *
         * @since 1.0.0
         */
        private void commaAfterOn() {
            this.out();
            this.indent--;
            this.newline();
            this.afterOn = false;
            this.afterByOrSetOrFromOrSelect = true;
        }

        /**
         * Comma after by or from or select
         *
         * @since 1.0.0
         */
        private void commaAfterByOrFromOrSelect() {
            this.out();
            this.newline();
        }

        /**
         * Logical
         *
         * @since 1.0.0
         */
        private void logical() {
            if ("end".equals(this.lcToken)) {
                this.indent--;
            }
            this.newline();
            this.out();
            this.beginLine = false;
        }

        /**
         * On
         *
         * @since 1.0.0
         */
        private void on() {
            this.indent++;
            this.afterOn = true;
            this.newline();
            this.out();
            this.beginLine = false;
        }

        /**
         * Misc
         *
         * @since 1.0.0
         */
        private void misc() {
            this.out();
            if ("between".equals(this.lcToken)) {
                this.afterBetween = true;
            }
            if (this.afterInsert) {
                this.newline();
                this.afterInsert = false;
            } else {
                this.beginLine = false;
                if ("case".equals(this.lcToken)) {
                    this.indent++;
                }
            }
        }

        /**
         * Perform string
         *
         * @return the string
         * @since 1.0.0
         */
        String perform() {

            this.result.append(INITIAL);

            while (this.tokens.hasMoreTokens()) {
                this.token = this.tokens.nextToken();
                this.lcToken = this.token.toLowerCase(Locale.ROOT);

                if ("'".equals(this.token)) {
                    String t;
                    do {
                        t = this.tokens.nextToken();
                        this.token += t;
                    }
                    // cannot handle single quotes
                    while (!"'".equals(t) && this.tokens.hasMoreTokens());
                } else if ("\"".equals(this.token)) {
                    String t;
                    do {
                        t = this.tokens.nextToken();
                        this.token += t;
                    }
                    while (!"\"".equals(t) && this.tokens.hasMoreTokens());
                } else if ("[".equals(this.token)) {
                    // SQL Server uses "[" and "]" to escape reserved words
                    // see SQLServerDialect.openQuote and SQLServerDialect.closeQuote
                    String t;
                    do {
                        t = this.tokens.nextToken();
                        this.token += t;
                    }
                    while (!"]".equals(t) && this.tokens.hasMoreTokens());
                }

                if (this.afterByOrSetOrFromOrSelect && ",".equals(this.token)) {
                    this.commaAfterByOrFromOrSelect();
                } else if (this.afterOn && ",".equals(this.token)) {
                    this.commaAfterOn();
                } else if ("(".equals(this.token)) {
                    this.openParen();
                } else if (")".equals(this.token)) {
                    this.closeParen();
                } else if (BEGIN_CLAUSES.contains(this.lcToken)) {
                    this.beginNewClause();
                } else if (END_CLAUSES.contains(this.lcToken)) {
                    this.endNewClause();
                } else if ("select".equals(this.lcToken)) {
                    this.select();
                } else if (DML.contains(this.lcToken)) {
                    this.updateOrInsertOrDelete();
                } else if ("values".equals(this.lcToken)) {
                    this.values();
                } else if ("on".equals(this.lcToken)) {
                    this.on();
                } else if (this.afterBetween && this.lcToken.equals("and")) {
                    this.misc();
                    this.afterBetween = false;
                } else if (LOGICAL.contains(this.lcToken)) {
                    this.logical();
                } else if (isWhitespace(this.token)) {
                    this.white();
                } else {
                    this.misc();
                }

                if (!isWhitespace(this.token)) {
                    this.lastToken = this.lcToken;
                }

            }
            return this.result.toString();
        }

        /**
         * Update or insert or delete
         *
         * @since 1.0.0
         */
        private void updateOrInsertOrDelete() {
            this.out();
            this.indent++;
            this.beginLine = false;
            if ("update".equals(this.lcToken)) {
                this.newline();
            }
            if ("insert".equals(this.lcToken)) {
                this.afterInsert = true;
            }
        }

        /**
         * Select
         *
         * @since 1.0.0
         */
        private void select() {
            this.out();
            this.indent++;
            this.newline();
            this.parenCounts.addLast(this.parensSinceSelect);
            this.afterByOrFromOrSelects.addLast(this.afterByOrSetOrFromOrSelect);
            this.parensSinceSelect = 0;
            this.afterByOrSetOrFromOrSelect = true;
        }

        /**
         * Out
         *
         * @since 1.0.0
         */
        private void out() {
            this.result.append(this.token);
        }

        /**
         * End new clause
         *
         * @since 1.0.0
         */
        private void endNewClause() {
            if (!this.afterBeginBeforeEnd) {
                this.indent--;
                if (this.afterOn) {
                    this.indent--;
                    this.afterOn = false;
                }
                this.newline();
            }
            this.out();
            if (!"union".equals(this.lcToken)) {
                this.indent++;
            }
            this.newline();
            this.afterBeginBeforeEnd = false;
            this.afterByOrSetOrFromOrSelect = "by".equals(this.lcToken)
                                              || "set".equals(this.lcToken)
                                              || "from".equals(this.lcToken);
        }

        /**
         * Begin new clause
         *
         * @since 1.0.0
         */
        private void beginNewClause() {
            if (!this.afterBeginBeforeEnd) {
                if (this.afterOn) {
                    this.indent--;
                    this.afterOn = false;
                }
                this.indent--;
                this.newline();
            }
            this.out();
            this.beginLine = false;
            this.afterBeginBeforeEnd = true;
        }

        /**
         * Values
         *
         * @since 1.0.0
         */
        private void values() {
            this.indent--;
            this.newline();
            this.out();
            this.indent++;
            this.newline();
            this.afterValues = true;
        }

        /**
         * Close paren
         *
         * @since 1.0.0
         */
        private void closeParen() {
            this.parensSinceSelect--;
            if (this.parensSinceSelect < 0) {
                this.indent--;
                this.parensSinceSelect = this.parenCounts.removeLast();
                this.afterByOrSetOrFromOrSelect = this.afterByOrFromOrSelects.removeLast();
            }
            if (this.inFunction > 0) {
                this.inFunction--;
                this.out();
            } else {
                if (!this.afterByOrSetOrFromOrSelect) {
                    this.indent--;
                    this.newline();
                }
                this.out();
            }
            this.beginLine = false;
        }

        /**
         * Open paren
         *
         * @since 1.0.0
         */
        private void openParen() {
            if (isFunctionName(this.lastToken) || this.inFunction > 0) {
                this.inFunction++;
            }
            this.beginLine = false;
            if (this.inFunction > 0) {
                this.out();
            } else {
                this.out();
                if (!this.afterByOrSetOrFromOrSelect) {
                    this.indent++;
                    this.newline();
                    this.beginLine = true;
                }
            }
            this.parensSinceSelect++;
        }

        /**
         * White
         *
         * @since 1.0.0
         */
        private void white() {
            if (!this.beginLine) {
                this.result.append(" ");
            }
        }

        /**
         * Is whitespace boolean
         *
         * @param token token
         * @return the boolean
         * @since 1.0.0
         */
        @Contract(pure = true)
        private static boolean isWhitespace(String token) {
            return WHITESPACE.contains(token);
        }

        /**
         * Newline
         *
         * @since 1.0.0
         */
        private void newline() {
            this.result.append(System.lineSeparator());
            for (int i = 0; i < this.indent; i++) {
                this.result.append(INDENT_STRING);
            }
            this.beginLine = true;
        }
    }
}
