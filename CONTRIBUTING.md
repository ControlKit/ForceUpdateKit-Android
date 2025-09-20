# Contributing to ForceUpdateKit

We love your input! We want to make contributing to ForceUpdateKit as easy and transparent as possible, whether it's:

- Reporting a bug
- Discussing the current state of the code
- Submitting a fix
- Proposing new features
- Becoming a maintainer

## Development Process

We use GitHub to host code, to track issues and feature requests, as well as accept pull requests.

## Pull Requests

Pull requests are the best way to propose changes to the codebase. We actively welcome your pull requests:

1. Fork the repo and create your branch from `main`.
2. If you've added code that should be tested, add tests.
3. If you've changed APIs, update the documentation.
4. Ensure the test suite passes.
5. Make sure your code lints.
6. Issue that pull request!

## Any contributions you make will be under the MIT Software License

In short, when you submit code changes, your submissions are understood to be under the same [MIT License](http://choosealicense.com/licenses/mit/) that covers the project. Feel free to contact the maintainers if that's a concern.

## Report bugs using GitHub Issues

We use GitHub issues to track public bugs. Report a bug by [opening a new issue](https://github.com/ControlKit/ForceUpdateKit-Android/issues).

**Great Bug Reports** tend to have:

- A quick summary and/or background
- Steps to reproduce
  - Be specific!
  - Give sample code if you can
- What you expected would happen
- What actually happens
- Notes (possibly including why you think this might be happening, or stuff you tried that didn't work)

## License

By contributing, you agree that your contributions will be licensed under its MIT License.

## Code Style

### Kotlin Style Guide

We follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).

### Key Points:

- Use 4 spaces for indentation
- Use camelCase for function and variable names
- Use PascalCase for class names
- Use UPPER_SNAKE_CASE for constants
- Prefer `val` over `var` when possible
- Use meaningful names for variables and functions

### Example:

```kotlin
class ForceUpdateViewModel(
    private val api: ForceUpdateApi
) : ViewModel() {
    
    private val _mutableState = MutableStateFlow<ForceUpdateState>(ForceUpdateState.Initial)
    val state: StateFlow<ForceUpdateState> = _mutableState.asStateFlow()
    
    fun getData() {
        // Implementation
    }
}
```

## Testing

### Unit Tests

- Write unit tests for all new functionality
- Aim for at least 80% code coverage
- Use descriptive test names
- Follow the Given-When-Then pattern

### Example:

```kotlin
@Test
fun `should return ShowView state when update is available`() = runTest {
    // Given
    val mockApi = mockk<ForceUpdateApi>()
    val expectedResponse = CheckUpdateResponse(...)
    
    coEvery { mockApi.getForceUpdateData(any(), any(), any(), any(), any()) } returns 
        NetworkResult.Success(expectedResponse)
    
    val viewModel = ForceUpdateViewModel(mockApi)
    
    // When
    viewModel.getData()
    
    // Then
    val state = viewModel.state.value
    assertTrue(state is ForceUpdateState.ShowView)
}
```

### UI Tests

- Write UI tests for new UI components
- Test user interactions
- Verify state changes

### Example:

```kotlin
@Test
fun testUpdateButtonClick() {
    composeTestRule.setContent {
        ForceUpdateViewFullScreen1().ShowView(
            config = testConfig,
            response = testResponse,
            viewModel = testViewModel
        )
    }
    
    composeTestRule.onNodeWithText("Update Now").performClick()
    
    // Verify expected behavior
}
```

## Documentation

### Code Documentation

- Document all public APIs
- Use KDoc format for documentation
- Include examples for complex functions

### Example:

```kotlin
/**
 * Shows the force update dialog with the specified configuration.
 * 
 * @param config The service configuration containing API settings and view preferences
 * @param onDismiss Optional callback when the dialog is dismissed
 * @param onState Optional callback for state changes
 * @return ForceUpdateKit instance for further operations
 * 
 * @sample
 * ```kotlin
 * val kit = forceUpdateKitHost(
 *     config = ForceUpdateServiceConfig(
 *         version = "1.0.0",
 *         appId = "com.example.app"
 *     )
 * )
 * kit.showView()
 * ```
 */
@Composable
fun forceUpdateKitHost(
    config: ForceUpdateServiceConfig,
    onDismiss: (() -> Unit)? = null,
    onState: ((ForceUpdateState) -> Unit)? = null
): ForceUpdateKit
```

### README Updates

- Update README.md for new features
- Add examples for new functionality
- Update installation instructions if needed

## Commit Messages

Use clear and descriptive commit messages:

### Format:

```
type(scope): description

[optional body]

[optional footer]
```

### Types:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples:

```
feat(ui): add new FullScreen4 style

Add a new full-screen UI style with modern design elements
and enhanced visual hierarchy.

Closes #123
```

```
fix(api): handle network timeout errors

Fix issue where network timeout errors were not properly
handled, causing the app to crash.

Fixes #456
```

## Branch Naming

Use descriptive branch names:

- `feature/add-new-ui-style`
- `fix/handle-network-errors`
- `docs/update-api-documentation`
- `refactor/improve-state-management`

## Issue Labels

We use the following labels for issues:

- `bug`: Something isn't working
- `enhancement`: New feature or request
- `documentation`: Improvements or additions to documentation
- `good first issue`: Good for newcomers
- `help wanted`: Extra attention is needed
- `question`: Further information is requested

## Release Process

### Versioning

We follow [Semantic Versioning](https://semver.org/):

- `MAJOR`: Incompatible API changes
- `MINOR`: New functionality in a backwards compatible manner
- `PATCH`: Backwards compatible bug fixes

### Release Checklist

- [ ] Update version in `build.gradle.kts`
- [ ] Update `CHANGELOG.md`
- [ ] Update documentation
- [ ] Run all tests
- [ ] Create release tag
- [ ] Update JitPack

## Getting Help

If you need help with contributing:

1. Check existing [issues](https://github.com/ControlKit/ForceUpdateKit-Android/issues)
2. Read the [documentation](README.md)
3. Join our [Discord community](https://discord.gg/controlkit)
4. Email us at support@controlkit.com

## Recognition

Contributors will be recognized in:

- README.md contributors section
- Release notes
- Project documentation

Thank you for contributing to ForceUpdateKit! 🎉
