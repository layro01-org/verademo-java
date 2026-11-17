# Agent Testing Prompts

These are prompts for evaluating AI coding agents against this project.

VeraDemo ("Blab-a-Gag") is a small, deliberately vulnerable Spring MVC 3.2 / JSP / MySQL app on Java 8. It has about 1,500 lines of Java in three controllers plus a few utils, no tests, and a basic Maven CI workflow. The prompts are grouped by what each one tests in the agent.

## 1. Small, well-scoped changes (baseline)

A checked box means the prompt has already been run and its change is in the repo. If you want a clean comparison, pick an unchecked one.

- [x] "Add a character limit of 280 to blab posts. Enforce it on the server in `BlabController` and show a counter in `feed.jsp`."
- [ ] "Add a 'delete my blab' button to the feed. Only the author should be able to delete their own blab."
- [x] "Show each user's total blab count on the `/blabbers` page."
  - Done on the `blab-count` branch, not merged to `master` yet.
- [ ] "Implement the `/blabber?userid=` page so it shows that user's profile and their blabs. It currently returns `notimplemented`."
- [ ] "Show relative timestamps like '5 minutes ago' on blabs in the feed instead of the raw date."
- [ ] "Reject empty or whitespace-only blabs and comments on the server, and show an error message in the view."
- [ ] "Add a search box to `/blabbers` that filters the list by username or blab name."
- [ ] "Let users edit their own blab within 5 minutes of posting it."

## 2. Security fixes

- "Find and fix all SQL injection vulnerabilities in the controllers. Use PreparedStatements throughout."
  - There are raw `createStatement()` calls in [UserController.java](src/main/java/com/veracode/verademo/controller/UserController.java) (around lines 97 and 223).
- "Audit the JSP views for XSS and fix it with proper output encoding."
- "Review how passwords are stored and replace it with bcrypt. Include migrating the existing users."
- "Do a security review of this app and rank the findings by severity."
  - This one is open-ended. The app is seeded with known flaws, so you can grade how many the agent finds and how many false positives it reports.

## 3. Investigation and debugging

- "The commit 'Made a bad code change' (dc48a68) broke something. Figure out what it did and whether it should be reverted."
  - Tests whether the agent uses git history and reasons about intent.
- "The `/reset` endpoint is slow. Profile it and make it faster."

## 4. Testing

- "There are no tests. Add JUnit and Mockito, and write unit tests for `UserController` login and registration."
  - Spring 3.2 plus a direct JDBC connection makes the code hard to mock, so watch whether the agent refactors to make it testable.
- "Add an integration test that runs against an H2 in-memory database instead of MySQL."

## 5. Larger refactors and upgrades (multi-step, higher risk)

- "Upgrade the project from Java 8 to Java 17 and from Spring 3.2 to Spring 5. Keep it building."
- "Migrate this app to Spring Boot with an embedded Tomcat, so it runs with `mvn spring-boot:run`."
- "Pull the duplicated JDBC connection and cleanup code out into a DAO layer."
- "Move the hardcoded DB credentials out of the code and README into environment variables."

## 6. Repo hygiene and DevOps

- "The `target/` directory and `.DS_Store` files are committed. Clean up the repo and fix `.gitignore`."
- "Add a Dockerfile and docker-compose with MySQL so the app runs with one command."
- "Update the GitHub Actions workflow to current action versions, cache Maven, and run tests."

## 7. Docs and understanding

- "Explain the request flow from login to posting a blab, with file and line references."
- "Rewrite the README setup instructions so they're accurate and work on a fresh Mac."
  - The README currently has leftover test text at the top.

## Tips for comparing agents

- Run every agent from the same commit. A git worktree or branch per run works well.
- For the security prompts, give the bare request and don't list the bugs. That way you see what each agent finds by itself.
- Use the upgrade and Spring Boot prompts to test long-horizon work. Check whether the agent actually runs `mvn package` to verify its changes or just claims they work.
