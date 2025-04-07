
# Today Only! Campus Deal-Finder App
## Overview
Today Only! is a mobile application designed to help users discover food deals on the UIUC campus. The app is built around a calendar structure, allowing users to quickly navigate through days, weeks, and months to find deals. By leveraging the Google Calendar API, we were able to establish a robust foundation for the app, enabling us to focus on refining the user experience and adding features tailored to deal-spotting.

The app prioritizes simplicity and speed, ensuring users can easily find deals without unnecessary distractions. Unlike social or review platforms, Today Only! focuses solely on quick interactions and efficient deal discovery.

For a demonstration of the app, please visit my [portfolio](https://portfolio.adobe.com/13b08875-4b68-45f6-86ef-5afa7c833ce3/editor/today-only).

## Repository Structure
### Root Directory
* [build.gradle.kts](./build.gradle.kts), [gradle.properties](./gradle.properties), [settings.gradle.kts](./settings.gradle.kts): Gradle configuration files for managing dependencies and project settings.

* [gradlew](./gradlew), [gradlew.bat](./gradlew.bat), [gradle/](./gradle/): Gradle wrapper files for consistent build environments.: Gradle wrapper files for consistent build environments.
* [readme.md](./readme.md): This documentation file.: 
app/
The Android application codebase.

***

[app/](./app/): The Android application codebase.
* [build.gradle.kts](./app/build.gradle.kts): Build configuration for the app module.
* [src/](./app/src/): Contains the source code for the Android app.
     *  [activities/](./app/src/main/java/com/example/today_only/activities/): Includes UI components like [MainActivity](./app/src/main/java/com/example/today_only/activities/MainActivity.java), which manages calendar views and deal interactions.
    * [adapters/](./app/src/main/java/com/example/today_only/adapters/): Contains adapters like [WeekDayAdapter](./app/src/main/java/com/example/today_only/adapters/WeekDayAdapter.java) for managing calendar data and user interactions.
    * [entities/](./app/src/main/java/com/example/today_only/entities/): Defines data models such as [Day](./app/src/main/java/com/example/today_only/entities/Day.java) for calendar representation.


***

[backend/](./backend/): 

The backend API for managing deals and serving data to the mobile app.
* [manage.py](./backend/manage.py): Entry point for Django's management commands.
* [requirements.txt](./backend/requirements.txt): Lists Python dependencies, including Django and Django REST Framework.
* [todayonly/](./backend/todayonly/): Core Django project files.
  * [settings.py](./backend/todayonly/settings.py): Configuration for the Django project.
  * [urls.py](./backend/todayonly/urls.py): URL routing for the backend.
* [deals/](./backend/deals/): Django app for managing deals.
  * [models.py](./backend/deals/models.py): Defines the database schema for deals.
  * [serializers.py](./backend/deals/serializers.py): Serializes deal data for API responses.
  * [urls.py](./backend/deals/urls.py): API endpoints for deal-related operations.
  * [views.py](./backend/deals/views.py): Implements logic for listing and filtering deals.

*** 
apis.md

Documentation for the backend API, including details about endpoints like GET /api/deals for retrieving deals.
***

### Key Features
#### 1. Calendar-Based Navigation:

* Users can switch between day, week, and month views.
* Quick selection of dates to view deals.
#### 2. Deal Discovery:

* Focused on finding food deals efficiently.
* Highlights deals for the current day or selected date.
#### 3. Simple and Intuitive UI:

* Prioritizes ease of use and fast interactions.
* Avoids unnecessary features like reviews or social sharing.
*** 
### Technologies Used
*  (Frontend): Android (Java)
* Backend: Django with Django REST Framework
* Calendar Integration: Google Calendar API
***
### Getting Started
1.  #### Backend:
    *  Install dependencies: pip install -r requirements.txt
    * Run the server: python manage.py runserver
2. #### Frontend:

    * Open the app/ module in Android Studio.
    * Build and run the app on an emulator or device.
***
### Future Enhancements
* Implement push notifications for new deals.
* Optimize performance for large datasets.