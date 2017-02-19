# DirtDetector
Simple Spring TestExecutionListener that scans application context for changes between tests.

When you don't want to use @DirtiesContext on every test class, but don't know which tests do the mess. DirtDetector will log suspicious modifications of fields on every bean in application context for you to check and fix.
