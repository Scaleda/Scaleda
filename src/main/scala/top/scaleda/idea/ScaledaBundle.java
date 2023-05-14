package top.scaleda.idea;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import top.scaleda.idea.i18n.DynamicScaledaBundle;

public final class ScaledaBundle extends DynamicScaledaBundle {
    @NonNls private static final String BUNDLE = "bundles.ScaledaBundle";
    private static final ScaledaBundle INSTANCE = new ScaledaBundle();

    private ScaledaBundle() { super(BUNDLE); }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object ...params) {
        return INSTANCE.getMessage(key, params);
    }

}

