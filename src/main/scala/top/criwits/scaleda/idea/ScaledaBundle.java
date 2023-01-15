package top.criwits.scaleda.idea;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.*;

import java.util.Locale;
import java.util.function.Supplier;

public final class ScaledaBundle extends DynamicBundle {
    @NonNls private static final String BUNDLE = "bundles.ScaledaBundle";
    // TODO: support i18n
    // note that if we replace INSTANCE with BUNDLE_ZH on launch, shown language may change
    @NonNls private static final String BUNDLE_ZH = "bundles.ScaledaBundle_zh";
    private static final ScaledaBundle INSTANCE = new ScaledaBundle();

    private ScaledaBundle() { super(BUNDLE); }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object ...params) {
        return INSTANCE.getMessage(key, params);
    }

}

