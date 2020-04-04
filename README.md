# GithubBrowser
Android sample app to browse popular github repos for a given organization

<img src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser_initial.png" data-canonical-src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser_initial.png" width="200" height="400" /> <img src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser.png" data-canonical-src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser.png" width="200" height="400" /> <img src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_details.png" data-canonical-src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_details.png" width="200" height="400" /> <img src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser_landscape.png" data-canonical-src="https://github.com/laurenyew/GithubBrowser/blob/master/screenshots/repo_browser_landscape.png" width="400" height="200" />

## Requirements
  - [x] Search for an organization name 
  - [x] Given a valid organization name, show top 3 most popular github repos (by stars) in list form 
  - [x] Clicking on list item should open up the github repo page in a webview / google chrome tab
  - [x] Given an invalid organization name, and empty list / error state should show 

## Extra features
  - [x] Pull to refresh
  - [x] Handle WebView fallback if Chrome Tabs not available
  - [x] Chrome tabs pre-load
  - [x] Unit tests
  - [x] Espresso tests

## Tech used
- Kotlin
- RxJava
- Dagger 2
- Moshi
- Retrofit
- Okhttp
- Gradle
- MockitoKotlin

## Disclaimers
Github API: https://developer.github.com/v3/
