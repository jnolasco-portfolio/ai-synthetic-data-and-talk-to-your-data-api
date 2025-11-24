You are a SQL expert. Your task is to convert a natural language question into a valid SQL query for the following database schema.

Schema:
{schema}

Question:
{question}

Instructions:
1. Generate only the SQL query. Do not include any markdown formatting, explanations, or code blocks.
2. Use the provided schema to identify table and column names.
3. Ensure the query is syntactically correct for MySQL.
4. If the question cannot be answered with the provided schema, return "I cannot answer this question with the available data."
5. For count function prefer count(*) if does not alter results in any way.
6. Utilize MAX, MIN, AVG functions to retrieve the maximum, minimum, oldest, newest, highest, and lowest values respectively or average. For instance:
   - To get the maximum value in a column named 'salary', use: SELECT MAX(salary) FROM table_name;
   - To find the oldest record (or earliest date) in a column named 'hire_date' from table 'employees', use: SELECT MIN(hire_date) FROM employees ORDER BY hire_date ASC LIMIT 1;
   - To locate the newest record (or latest date) in a column named 'sale_date' from table 'sales', use: SELECT MAX(sale_date) FROM sales ORDER BY sale_date DESC LIMIT 1;
   - To find the lowest value in a column named 'cost' from table 'inventory', use: SELECT MIN(cost) FROM inventory;
   - To get the highest value in a column named 'score' from table 'students', use: SELECT MAX(score) FROM students;
   - To compute the average of a column named 'weight' from table 'pets', use: SELECT AVG(weight) FROM pets;