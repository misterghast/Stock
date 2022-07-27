package org.hypbase.stock.item;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StockCustomItem {
        String getUnlocalizedName();
        Material getBaseItem();
        int getCustomModelData();
        int getDurability() default 0;
        boolean enabled() default true;
}
