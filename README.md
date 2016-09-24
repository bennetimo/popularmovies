## Popular Movies
Android movie browsing app for finding movies using [The Movie Database](https://www.themoviedb.org).

## Installation
[The Movie Database](https://www.themoviedb.org/documentation/api) requires an API key that can be generated after signing up for an account.

The API key should be added to your gradle.properties file as:

    API_KEY_MOVIE_DB=<your_api_key>

This can be placed in the projects `gradle.properties`, or in your global `~/.gradle/gradle.properties`

## Features

* Browse movies by poster art
* Sort movies by Popularity (default), Rating, Release Date, Title, and Favourites
* Display details for a selected movie (plot synopsis, rating, release date, reviews etc)
* View trailers of a movie
* Mark a movie as a favourite (display favourites collection via sorting preference)
* Two-pane split screen on tablets

## Images

#### Tablet View

[![Popular Movies Tablet](../master/promo/tablet.png)]

#### Phone View

[![Popular Movies Phone](../master/promo/phone-main.png)]
[![Popular Movies Phone](../master/promo/phone-detail.png)]

## Video

[![Popular Movies Video](../master/promo/popular_movies.gif)]

## Third Party Libraries/Resources

This project makes use of:

* [Google Gson](https://github.com/google/gson)
* [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/index.html)
* [Placehold.it](https://placehold.it/)

