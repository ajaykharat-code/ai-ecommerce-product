# Antigravity CLI Custom Rules for Scaler Ecommerce Project

These rules must be followed by all agents operating within this workspace.

## 1. BMad Iterative Workflow Enforcement
When implementing any Epic or Story, you MUST strictly follow the iterative BMAD slash command workflow in this exact sequence for EACH individual story:
1. `/bmad-create-story` (Generate the story file)
2. `/bmad-dev-story` (Implement the story code)
3. `/bmad-code-review` (Run the AI quality validation)
Do NOT skip steps or jump ahead to implement multiple stories or an entire Epic at once. 

## 2. Source of Truth Documentation
When executing `bmad-create-story` or implementing features, ALWAYS read and reference the project's PRD and Architecture files to ensure stories perfectly align with the business requirements and technical design.

## 3. Testing Mandate
Every implementation MUST include comprehensive JUnit tests. No story is considered complete without accompanying unit and/or integration tests that validate its core functionality.

## 4. Capstone Project Constraints
Always adhere to the specific technical and architectural constraints defined for this Capstone Project. This includes:
- Microservices architecture (Spring Cloud, API Gateway, Eureka)
- Appropriate use of dependencies (PostgreSQL, Redis, Kafka)
- Strict adherence to SOLID principles and clean code practices
- Thorough documentation (JavaDocs, Swagger/OpenAPI)

## 5. Build & Compilation Verification
Never mark a story's development or code review as "done" based purely on static code writing. You MUST proactively execute terminal commands (e.g., `mvn clean test` or `mvn compile`) to verify that the code successfully compiles, dependencies are correct, and all unit tests actually pass in the real environment. Assume code is broken until proven working by the compiler.
- Always update project report timely (e.g., `project_report.md`) as per the template provided in the Capstone Project.
- Write code, add test cases, clean, build, test and review using BMAD command.
