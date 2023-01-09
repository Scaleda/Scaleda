package top.criwits.scaleda.idea;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class ScaledaBundle extends DynamicBundle {
    @NonNls private static final String BUNDLE = "bundles.ScaledaBundle";
    private static final ScaledaBundle INSTANCE = new ScaledaBundle();

    private ScaledaBundle() { super(BUNDLE); }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object ...params) {
        return INSTANCE.getMessage(key, params);
    }

}

