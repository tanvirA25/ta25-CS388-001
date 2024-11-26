# Milestone 1 - TRAILFLIX (Unit 7)

## Table of Contents

1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview

### Description

- An an dedicated to show move and tv shows trailers with the additon of filtered search and showcasing of trending and upcoming contents.

### App Evaluation

[Evaluation of your app across the following attributes]
- **Category:**
    - Entertainment
- **Mobile:**
    - Uniquely Mobile: The portability and instant accessibility of mobile devices make the app particularly suited for on-the-go users who want quick updates on new trailers.Unlike websites, the app leverages mobile capabilities such as push notifications to alert users to new releases, trending trailers.
The audio experience is enhanced with trailers’ soundtracks, while real-time data updates ensure that users stay informed as soon as new trailers are available
    - Enhanced Features: By incorporating real-time updates and push notifications, the app transforms into an interactive and engaging platform for trailer consumption. These features ensure users don’t miss out on the latest trailers, creating a more dynamic and responsive experience than a traditional website.
- **Story:**
    - The app’s value lies in its simplicity and targeted audience, movie enthusiasts who want quick access to the latest trailers for movies and showsfrom around the world.
    - It caters to an international audience, making it appealing to a global user base among movie buffs.
    - While the idea might not feel groundbreaking to some, it could still find a loyal audience among trailer enthusiasts, cinema lovers, and frequent moviegoers delivering a streamlined browsing expereince.
- **Market:**
    - The potential market includes a vast demographic of movie enthusiasts, cinema goers, and streaming service users globally.
    - It appeals to entertainment lovers who want easily accessible trailers, offering more value than browsing random YouTube searches.
    - Movie buffs, cinema goers, and streaming fans looking for trending or upcoming content are the target audience.
- **Habit:**
    - Likely to be used multiple times a day, especially for users who enjoy staying updated on new releases or upcoming movies/shows. 
    - Mostly a consumtion based app focuses on vieweing trialers and saving liked trailers to watchlist.
    
- **Scope:**
    - The app is technically feasible within a short timeframe. It involves API integrations (e.g., IMDb, YouTube) and presenting data via a structured UI like RecyclerView.
    - A basic version showcasing trailers with essential metadata (title, release date, genre) is easy to build and still offers value.
    - The app is clearly defined with a straightforward features on trailers, metadata, and updates, akin to a simplified Netflix experience focused solely on trailers.

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

1. Trending Page (Home Page)
    - [X] Display trending trailers in a carousel view.
    - [X] By clicking on it, shows the info and the trailer.
    - [X] On the bottom another carousel for tv shows.
2. Upcoming Page
    - [X] Display upcoming trailers in a carousel view.
    - [X] By clicking on it shows the info and the trailer.
    - [X] On the bottom another carousel for tv shows.
3. Search Page
    - [X] A search bar to filter out trailers based on the user query
    - [X] After the query, show the result in a carousel, and another for tv shows if it has the same name.
    - [X] By clicking on it, shows the info and the trailer.
    
4. Account Page
    - [X] View email used to create an account if made.
    - [X] Options to change password, email, and info.
    - [X] Settings options to change notifications, and age restriction.
5. Login Page
    - [X] User can login or create an account if not
    - [X] User can also use as guest

**Optional Features**

1. Clicking on streaming platform to view the content
    - [X] By clicking on the streaming platform icon it will open the content in that platform.
    
2. Add trailers to watchlist
    - [X] An add icon to view the option to add it to watchlist only if they have an account
    
3. Watchlist Page
    - [X] View the added trailers in list and can remove them if the user wants only if they have an account
    - [X] Able to view the info and the trailer when clicked on it  only if they have an account
    
4. Page for highest rated content of all time
    - [X] It will show the content in a coursel
    - [x] By clickin on it, it will show the info and the trailer
    
### 2. Screen Archetypes

- Trending Page (Home Page)
  - As a movie lover, I want to see and receive about the latest movies in the theater.
  - As a user interested in new release, I want to view see the trailers and info such as , Name, Director, Writer, Genre, Actors, Rating, and where it is available if online release. 
  - As a user I want to see the movie directly by clicking on the streaming platform it is available (optional).
  - As a user I want to add it to watchlist (optional).
      
-  Upcoming Page
    - As a movie lover, I want to view the trailers of the upcoming contents.
    - As a user interested in new release, I want to view see the trailers and info such as , Name, Director, Writer, Genre, Actors, Rating, Release Date, and where it will be released if online release.  
    - As a user I want to see the movie directly by clicking on the streaming platform it is available (optional).
    - As a user I want to add it to watchlist (optional).

  - Search Page
      - As a user I want to search content with any of the following filter, Name of the Content, Director, Writer, Actors, Genre, Country, Year.
      - As a user I want to add it to watchlist (optional).
- Account Page
    - As a user I want to view my account details.
    - As a user I want to toggle different settings including notifications and age-restriction.
    - As a user I want to add it to watchlist (optional).
 - Watchlist Page (Optional)
    - As a user I want to view the watchlist I made and modify it (optional).
- Login Page
    - As a user I want to login if I have an account
    - As a user I want to create an account if I don't have it
    - As a user I want to use it as a guest
- All time Page
    - As a use I want to view highest rated content of all time

### 3. Navigation

**Tab Navigation** (Tab to Screen)

- Home (Trending Page):
    * Display trending moves / tv show trialers.
- Upcoming:
    *  Display upcoming moves / tv show trialers.
- Search 
    * Enables searching for conent using filters.
- Account
    * Displays acocunt detaisl and settings.
- Watchlist:
    * Displays saved trailer (optional).
- All time list:
    * Display highest rated all time content.

**Flow Navigation** (Screen to Screen)

- Trending Page (Home)
  - Navigate to Trailer Details on clicking a trailer
  - Navigate to search page from search icon
  - Navigate to Upcoming Page
  - Navigatt to Account Page by account icon
  - Navigate to All time page (optional)
  - Navigate to Wishlist (optional)
  
- Upcoming Page
     - Navigate to Trailer Details on clicking a trailer
     - Navigate to search page from search icon
     - Naviagte back to Trending Page
     - Navigate to Account Page by account icon
     - Navigate to All time page (optional)
     - Navigate to Wishlist (optional)
- Search Page
    - Navigate to Trailer Details on clicking a trailer
    - Navigate back to Trending Page
    - Navigate back to Upcoming Page
    - Navigate to All time page (optinal)
    - Navigate back to Wishlist Page (optional)
- Account Page
  - Navigate to Upcoming Page
  - Navigate to Trending Page
  - Navigate to All time page (optinal)
  - Navigate to Wishlist (optional)

- Login Page
    - Create an account Page
    - Trending Page
- Watchlist Page (Optional)
     - Navigate to Trailer Details on clicking a trailer
     - Navigate to search page from search icon 
     - Navigate to Account Page by account icon
     - Navigate to All time page (optional)
     - Navigate to Upcoming Page
     - Navigate to Trending Page
- All time Page (Optional)
  - Navigate to Trailer Details on clicking a trailer
  - Navigate to search page from search icon
  - Navigate to Upcoming Page
  - Naviaget to Trending Page
  - Navigatt to Account Page by account icon
  - Navigate to Wishlist (optional)

## Wireframes
   <img width="474" alt="image" src="https://github.com/user-attachments/assets/39d81d29-0837-4e9b-81d5-03c8af03baef">










