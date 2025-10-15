
# Taskify

A Spring-boot based project that provieds REST API endpoints to create, update, and view employees and tasks, along with features to assign tasks, update task statuses, and retrieve task statistics for each employee.


## Available APIs

**Employee Management:**
- `GET /api/employees` - List of employees in the system.
					               Optional query params: page, size (defaults page=0, size=20)

- `GET /api/employees/{id}` - Fetch data of an employee with id.

- `POST /api/employees` - Create an employee with the given data.
                          Constraints: 
                          - Name and Email to be present.
                          - Email to be unique and valid.

- `PATCH /api/employees/{id}` - Update the employee with the given updated data.
                                Constrains:
                                - If Name/Email is updated, they should have valid values.
                                - Email to be unique.
                                - null updates are ignored.

- `GET /api/employees/{id}/tasks` - List of tasks assigned to the employee.
								                    Optional query param: status (value should be any of [PENDING, IN_PROGRESS, COMPLETED])

- `GET /api/employees/{id}/stats` - Counts of the tasks assigned to the employee grouped by task status.


**Task Management:**
- `GET /api/tasks` - List of tasks in the system with sideloaded data of assigned employees (if available)
				             Optional query params: page, size (defaults page=0, size=20)

- `GET /api/tasks/{id}` - Fetch data of a task and assigned employee (if available)

- `POST /api/tasks` - Create a task with the given data.
                      Constraints:
                      - Title to be present.
                      - If assignedTo is provided, the employee id given should be available in the system.

- `PATCH /api/tasks/{id}` - Update the task with the given updated data.
                            Constraints:
                            - If Title is updated, it should be valid.
                            - If assignedTo is provided, the employee id given should be available in the system.
  
