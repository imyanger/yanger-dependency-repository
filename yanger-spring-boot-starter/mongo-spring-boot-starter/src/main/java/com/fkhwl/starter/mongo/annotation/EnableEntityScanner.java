package com.fkhwl.starter.mongo.annotation;

import com.fkhwl.starter.mongo.scanner.EntityScannerRegistrar;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 12:46
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EntityScannerRegistrar.class)
public @interface EnableEntityScanner {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @EntityScan("org.my.pkg")} instead of
     * {@code @EntityScan(basePackages="org.my.pkg")}.
     *
     * @return the base packages to scan
     * @since 1.0.0
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for entities. {@link #value()} is an alias for (and mutually
     * exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     *
     * @return the base packages to scan
     * @since 1.0.0
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for entities. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return classes from the base packages to scan
     * @since 1.0.0
     */
    Class<?>[] basePackageClasses() default {};
}
