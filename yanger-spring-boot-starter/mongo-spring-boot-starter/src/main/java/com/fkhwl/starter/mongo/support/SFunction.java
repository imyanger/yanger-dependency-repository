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

package com.fkhwl.starter.mongo.support;

import java.io.*;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 *
 * @param <T> parameter
 * @param <R> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 18:03
 * @since 1.0.0
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
