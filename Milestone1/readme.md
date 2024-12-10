# Milestone 1 - TRAILFLIX (Unit 7)

## Table of Contents

1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview

### Description

- A dedicated app to show move and tv shows trailers with the additon of filtered search and showcasing of trending and upcoming contents.

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
    - [ ] Display trending trailers in a carousel view.
    - [ ] By clicking on it, shows the info and the trailer.
    - [ ] On the bottom another carousel for tv shows.
2. Upcoming Page
    - [ ] Display upcoming trailers in a carousel view.
    - [ ] By clicking on it shows the info and the trailer.
    - [ ] On the bottom another carousel for tv shows.
3. Search Page
    - [ ] A search bar to filter out trailers based on the user query
    - [ ] After the query, show the result in a carousel, and another for tv shows if it has the same name.
    - [ ] By clicking on it, shows the info and the trailer.
    
4. Account Page
    - [ ] View email used to create an account if made.
    - [ ] Options to change password, email, and info.
    - [ ] Settings options to change notifications, and age restriction.
5. Login Page
    - [X] User can view the login page
    - [ ] User can login
    - [ ] User can create an account 
    - [X] User can also login as guest

**Optional Features**

1. Clicking on streaming platform to view the content
    - [ ] By clicking on the streaming platform icon it will open the content in that platform.
    
2. Add trailers to watchlist
    - [ ] An add icon to view the option to add it to watchlist only if they have an account
    
3. Watchlist Page
    - [ ] View the added trailers in list and can remove them if the user wants only if they have an account
    - [ ] Able to view the info and the trailer when clicked on it  only if they have an account
    
4. Page for highest rated content of all time
    - [ ] It will show the content in a coursel
    - [ ] By clickin on it, it will show the info and the trailer
    
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
- All time Page (optional)
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
   <img width="1219" alt="389791289-217e0eac-183c-421c-b440-2968ba775838" src="https://github.com/user-attachments/assets/bfcf0214-5b5a-42af-8384-7087fa2eb65a">


# Milestone 2 - Build Sprint 1 (Unit 8)
GitHub Project board
[Add screenshot of your Project Board with three milestones visible in this section] 
<img width="1352" alt="Screenshot at Dec 02 22-06-15" src="https://github.com/user-attachments/assets/775de35c-0b1d-48da-8ef2-e4d4ca5ee2ca">

# Issue cards
[Add screenshot of your Project Board with the issues that you've been working on for this unit's milestone] 
<img width="1443" alt="image" src="https://github.com/user-attachments/assets/19b3aebe-0fc5-4cb2-a011-1a9054365a7e">


[Add screenshot of your Project Board with the issues that you're working on in the NEXT sprint. It should include issues for next unit with assigned owners.] 
<img width="1434" alt="image" src="https://github.com/user-attachments/assets/0e963400-000b-4cda-b29c-866afe2b6402">
<img width="1426" alt="image" src="https://github.com/user-attachments/assets/c551b523-cb23-4193-b8e9-927a7ee6f2f9">
<img width="1614" alt="image" src="https://github.com/user-attachments/assets/4b8023ae-459c-4bcb-9732-7009e3dcf540">


# Issues worked on this sprint
List the issues you completed this sprint
- [X] User can view the login page
- [X] User can also login as guest

[Add giphy that shows current build progress for Milestone 2. Note: We will be looking for progression of work between Milestone 2 and 3. Make sure your giphys are not duplicated and clearly show the change from Sprint 1 to 2.]
![Guest Login](https://github.com/user-attachments/assets/decdcddd-45be-4097-859d-6b7217fdf1e8)

# Milestone 3 - Build Sprint 2 (Unit 9)
GitHub Project board

# Issue cards
[Add screenshot of your Project Board with the updates issues that you've been working on for this unit's milestone] 
<img width="1619" alt="image" src="https://github.com/user-attachments/assets/b3596549-1557-40ab-a423-293e571b897d">


[Add screenshot of your Project Board with the issues that you're working on in the NEXT sprint. It should include issues for next unit with assigned owners.] 
<img width="1619" alt="image" src="https://github.com/user-attachments/assets/b3596549-1557-40ab-a423-293e571b897d">

# Issues worked on this sprint
List the issues you completed this sprint
- [X] Display trending trailers in a carousel view.
- [X] By clicking on it, shows the info and the trailer.
- [X] On the bottom another carousel for tv shows.
- [X] A search bar to filter out trailers based on the user query
- [X] After the query, show the result in a carousel, and another for tv shows if it has the same name.
- [X] By clicking on it, shows the info and the trailer.
- [X] View email used to create an account if made.
- [X] User can login
- [X] User can create an account
- [X] An add icon to view the option to add it to watchlist only if they have an account
- [X] View the added trailers in list and can remove them if the user wants only if they have an account
- [X] Able to view the info and the trailer when clicked on it  only if they have an account
- [X] It will show the content in a coursel
- [X] By clickin on it, it will show the info and the trailer

[Add giphy that shows current build progress for Milestone 3. Note: We will be looking for progression of work between Milestone 2 and 3. Make sure your giphys are not duplicated and clearly show the change from Sprint 1 to 2.]


[Demo Day Prep Video]
[https://github.com/user-attachments/assets/6d03b8a5-aaf4-4c37-bcf7-a99a3dfe1fba](https://github.com/user-attachments/assets/e34e16c2-83cd-4932-89c2-1226b4339c9a)










