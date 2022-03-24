package com.yanger.starter.mongo.annotation;

import com.yanger.starter.mongo.scanner.EntityScannerRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
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
     * @return the base packages to scan
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for entities. {@link #value()} is an alias for (and mutually
     * exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     * @return the base packages to scan
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for entities. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     * @return classes from the base packages to scan
     */
    Class<?>[] basePackageClasses() default {};

}
