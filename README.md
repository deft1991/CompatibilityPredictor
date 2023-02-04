# Predictor

## Steps to Run

- Download files from GitHub and run application on an IDE (I did it in JetBrains IDE)
- Start Postman and make a GET request to "http://localhost:8080/api/v1/compatibility/evaluate"

## Project info

- Calculate score based on team info.
- More score --> higher coefficient (can change it in params)

## Ways to Improve

- Add auth
- Add @RefreshScope annotation for refresh props (trade-off)
- Move props into DB (trade-off)
- Make POST controller if we want to save smth in DB. For now it is just calculating
- Add validators
- Set up an H2 in-memory DB for storing applicants
- If DB exists, create APIs to update and delete (assuming applicants can reapply and that their attributes have
  improved/degraded since)
