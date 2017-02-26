#Instructions

## Quick Start

1. Ensure current JDK is installed (nothing else is required -- other dependencies will automatically download )
2. To run tests with Headless Browser (PhantomJS) run: ./gradlew test
3. To tun tests with Chrome run: ./gradlew test -Dgeb.env=chrome

## Debugging and running test individually in IDE's

Default WebDriver (Chrome or PhantomJs) is configure in the GebConfig.groovy file in resources.

Example for running individual tests from the commandline: gradle test -Dtest.single=WikipediaExample

Example of passing in a different baseURL: gradle test -PbaseUrl=http://www.otherBaseUrl.com
