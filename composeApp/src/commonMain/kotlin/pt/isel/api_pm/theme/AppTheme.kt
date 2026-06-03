package pt.isel.api_pm.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    // brand
    primary              = Primary,
    onPrimary            = TextPrimary,
    primaryContainer     = SurfaceHigh,
    onPrimaryContainer   = PrimaryVariant,

    secondary            = PrimaryVariant,
    onSecondary          = TextPrimary,
    secondaryContainer   = SurfaceHigh,
    onSecondaryContainer = TextSecondary,

    // surfaces
    background           = Background,
    onBackground         = TextPrimary,
    surface              = Surface,
    onSurface            = TextPrimary,
    surfaceVariant       = SurfaceHigh,
    onSurfaceVariant     = TextSecondary,

    // surfaceContainer* (used by card, sheets, etc)
    surfaceContainerLowest = Background,
    surfaceContainerLow    = Surface,
    surfaceContainer       = SurfaceHigh,
    surfaceContainerHigh   = SurfaceHigh,
    surfaceContainerHighest= SurfaceHighest,

    // errors
    error                = Error,
    onError              = TextPrimary,
    errorContainer       = ErrorDim,
    onErrorContainer     = Error,

    // outlines
    outline              = Outline,
    outlineVariant       = OutlineVariant,

    // inverse (used by snackbar, tooltips)
    inverseSurface       = TextPrimary,
    inverseOnSurface     = Background,
    inversePrimary       = Primary,
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography  = AppTypography,
        content     = content
    )
}