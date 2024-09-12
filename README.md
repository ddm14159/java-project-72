### Hexlet tests and linter status:
[![Actions Status](https://github.com/ddm14159/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/ddm14159/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/514d1912ee4dfa5e706d/maintainability)](https://codeclimate.com/github/ddm14159/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/514d1912ee4dfa5e706d/test_coverage)](https://codeclimate.com/github/ddm14159/java-project-72/test_coverage)


https://java-project-72-v1op.onrender.com/


# URL Analyzer

URL Analyzer is a web application that allows users to submit URLs for analysis and retrieves information about the provided web page. The application uses Javalin for the web framework, PostgreSQL for the database, and Jsoup for parsing HTML content.

## Features

- Submit a URL for analysis
- Check and display the following information about a web page:
  - Page title
  - First `<h1>` element
  - Description meta tag
  - HTTP status code
- Save the analysis results in the database
- List and manage analyzed URLs
