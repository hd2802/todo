# TODO

## Introduction
Small project for experimenting with SpringBoot and React integration and for finishing a project.
This application is a super simple to-do list where the user can submit a task that they need to do, give it a due date, view all their tasks that need to be completed and then mark them as completed when they are done.

![screenshot of the main page of the application](./images/Screenshot%202026-09-04%20at%2016.25.21.png)

## Future Features
The following features are to be added to the project
- Submitting a new task (infrastructure and UI is there, just need to link it up)
- More sorting and information by the due date of the task
- Tagging
- Priority (besides the due date)

## Bugfixes
There are a few bugs currently in the project that require fixing. 
- [ ] Marking a task as complete gives a couple of visual bugs on the frontend

## Running the Application

### Backend

``` bash
cd backend
```

``` bash
./gradlew bootRun
```
### Frontend

``` bash
cd frontend
```

``` bash
npm install
```

``` bash
npm run dev
```

## Testing

### Backend

To run the tests:
``` bash
./gradlew test
```

There is currently (as of 04/09/2026) 96% coverage of the backend
![screenshot of the main page of the application](./images/Screenshot%202026-09-04%20at%2016.33.14.png)

### Frontend

Frontend testing to come

## Important Dev Commands

### Backend

To clean the gradle build

``` bash
./gradlew clean build
```