package dev.qther.psi_addon_template.annotations;

import dev.qther.psi_addon_template.spell.ModPieces;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PieceMetadata {
    @Nonnull String id();
    @Nonnull ModPieces.Group group();
    boolean main() default false;
}
