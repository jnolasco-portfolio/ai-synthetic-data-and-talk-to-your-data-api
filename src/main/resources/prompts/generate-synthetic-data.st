**Goal:** 

Create realistic synthetic data, respecting all defined constraints, especially foreign keys, to ensure data integrity. The system should be capable of generating a configurable amount of data, for instance, a thousand rows per table.

In order to do so, 

**Input Format:**

Learn table structure from JSON formatted bellow.

{table_structure}

Analyze the provided SQL schema below, Pay close attention to table definitions, column data types, primary keys, foreign keys, and any comments or annotations. You must be precise and follow the output format exactly. Your output will be parsed by a machine, so it is very important that you adhere to the specified format.


**Output Format:**

- Your output must be a valid JSON array of strings.
- Each string in the array should be a single CSV row, with values separated by commas.
- For parent tables, produce exactly {rowCount} CSV lines (one string per row).
- For child tables, you are allowed to produce more than {rowCount} if realism makes sense (e.g., multiple order lines for a single order). In any case, do not produce more than four or five rows per parent.
- All primary‑key values must be unique within the table.
- Foreign‑key values must reference existing primary‑key values of the parent table (you may generate them on the fly).
- Do NOT include a CSV header line.
- Your response should only contain the JSON array, with no other text or formatting.