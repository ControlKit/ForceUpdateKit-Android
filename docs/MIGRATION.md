# Migration Guide

This guide helps you migrate between different versions of ForceUpdateKit.

## Migrating from 0.0.1 to 0.0.2

### Breaking Changes

#### 1. Configuration Structure
The configuration structure has been updated for better organization.

**Before (0.0.1):**
```kotlin
ForceUpdateServiceConfig(
    version = "1.0.0",
    appId = "your-app-id",
    deviceId = "device-id"
    // Other properties mixed together
)
```

**After (0.0.2):**
```kotlin
ForceUpdateServiceConfig(
    version = "1.0.0",
    appId = "your-app-id",
    deviceId = "device-id",
    viewConfig = ForceUpdateViewConfig(
        forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1
        // UI-specific configurations
    )
)
```

#### 2. State Management
State handling has been improved with better error management and external error handling library.

**Before (0.0.1):**
```kotlin
onState = { state ->
    when (state) {
        is ShowUpdate -> { /* Handle update */ }
        is ShowError -> { /* Handle error */ }
    }
}
```

**After (0.0.2):**
```kotlin
onState = { state ->
    when (state) {
        is ForceUpdateState.ShowView -> { /* Handle update */ }
        is ForceUpdateState.ShowViewError -> { /* Handle error */ }
        is ForceUpdateState.UpdateError -> { /* Handle update error */ }
        is ForceUpdateState.SkipError -> { /* Handle skipped error */ }
    }
}
```

**Note**: Error types now use `com.sepanta.errorhandler.ApiError<*>` instead of custom error types.

#### 3. API Integration Changes
API integration has been updated with new authentication method and configurable base URL.

**Before (0.0.1):**
```kotlin
// Query parameters
GET /force-updates?appId={appId}&version={version}&deviceId={deviceId}&sdkVersion={sdkVersion}
```

**After (0.0.2):**
```kotlin
// Header-based authentication
GET /force-updates
Headers:
  - x-app-id: {appId}
  - x-version: {version}
  - x-sdk-version: {sdkVersion}
  - x-device-uuid: {deviceId}
```

**Configuration:**
Example base URL: `https://example.com/api/force-updates`

#### 4. UI Customization
UI customization has been enhanced with more options.

**Before (0.0.1):**
```kotlin
// Limited customization options
ForceUpdateServiceConfig(
    // Basic properties only
)
```

**After (0.0.2):**
```kotlin
ForceUpdateServiceConfig(
    viewConfig = ForceUpdateViewConfig(
        forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
        imageDrawble = R.drawable.custom_image,
        buttonColor = Color.Blue,
        headerTitle = "Custom Title",
        // Many more customization options
    )
)
```

### New Features

#### 1. External Error Handling Library
Integration with `com.sepanta.errorhandler` for better error management:

```kotlin
// Automatic error entity registration
fun setupErrorEntities() {
    ErrorEntityRegistry.register(ErrorValidation::class.java)
}
```

#### 2. Configurable API URL
API base URL is configurable per environment (dev/staging/prod). Use your own configuration mechanism. Example: `https://example.com/api/force-updates`.

**Benefits:**
- Easy environment switching (dev, staging, production)
- No hardcoded URLs in source code
- Secure configuration management

#### 3. Multiple UI Styles
Six different UI styles are now available:

```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1  // or FullScreen2, FullScreen3, FullScreen4, Popover1, Popover2
)
```

#### 4. Multi-language Support
Support for localized content:

```kotlin
ForceUpdateServiceConfig(
    lang = "fa"  // Persian, Arabic, English, etc.
)
```

#### 5. Custom Views
Advanced customization with custom Composable views:

```kotlin
ForceUpdateViewConfig(
    imageView = { imageUrl ->
        AsyncImage(
            model = imageUrl,
            contentDescription = "Update image"
        )
    },
    buttonView = { onClick ->
        Button(onClick = onClick) {
            Text("Custom Button")
        }
    }
)
```

#### 6. Enhanced Error Handling
Better error management with retry mechanisms:

```kotlin
ForceUpdateServiceConfig(
    skipException = false,
    maxRetry = 3,
    timeRetryThreadSleep = 2000L
)
```

### Migration Steps

1. **Update Dependencies**
   ```gradle
   implementation 'com.github.ControlKit:ForceUpdateKit-Android:0.0.2'
   ```

2. **Configure API URL**
   Set your base URL via your preferred configuration. Example: `https://example.com/api/force-updates`.

3. **Update Configuration**
   ```kotlin
   // Wrap existing config in viewConfig
   ForceUpdateServiceConfig(
       version = "1.0.0",
       appId = "your-app-id",
       deviceId = "device-id",
       viewConfig = ForceUpdateViewConfig(
           forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1
       )
   )
   ```

4. **Update State Handling**
   ```kotlin
   onState = { state ->
       when (state) {
           is ForceUpdateState.ShowView -> {
               // Handle update dialog
           }
           is ForceUpdateState.ShowViewError -> {
               // Handle network errors
           }
           is ForceUpdateState.UpdateError -> {
               // Handle update action errors
           }
           is ForceUpdateState.SkipError -> {
               // Handle skipped errors
           }
           is ForceUpdateState.NoUpdate -> {
               // No update available
           }
       }
   }
   ```

5. **Test Your Implementation**
   - Verify all states are handled correctly
   - Test error scenarios
   - Ensure UI displays properly
   - Test with different network conditions

### Deprecated Features

The following features from 0.0.1 are deprecated and will be removed in future versions:

- Direct configuration properties (moved to `viewConfig`)
- Simple state handling (replaced with comprehensive state management)
- Basic error handling (replaced with enhanced error management)
- Query parameter-based API authentication (replaced with header-based authentication)
- Custom error types (replaced with external error handling library)

### Troubleshooting

#### Common Issues

1. **State Not Updating**
   - Ensure you're handling all state cases
   - Check if `skipException` is set correctly

2. **UI Not Displaying**
   - Verify `forceUpdateViewStyle` is set correctly
   - Check if `viewConfig` is properly configured

3. **API Errors**
   - Verify API endpoint is accessible
   - Check network permissions
   - Ensure proper error handling
   - Verify header-based authentication is working
   - Check external error handling library integration
   - Verify API base URL is correctly configured

#### Getting Help

If you encounter issues during migration:

1. Check the [API Documentation](API.md)
2. Review the [README](README.md) for examples
3. Open an issue on [GitHub](https://github.com/ControlKit/ForceUpdateKit-Android/issues)
4. Contact support at support@controlkit.com

### Future Versions

Planned features for upcoming versions:

- **0.0.3**: Enhanced caching mechanisms
- **0.0.4**: Additional UI themes
- **0.0.5**: Advanced analytics integration
- **1.0.0**: Stable API with long-term support

Stay updated by watching the repository and checking the [Changelog](CHANGELOG.md).
