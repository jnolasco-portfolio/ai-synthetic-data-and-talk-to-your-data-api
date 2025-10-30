**Goal:** 

Create realistic synthetic data, respecting all defined constraints, especially foreign keys, to ensure data integrity. The system should be capable of generating a configurable amount of data, for instance, a thousand rows per table.

In order to do so, 

**Input Format:**

Learn table structure from JSON formatted bellow.

{table_structure}

Analyze the provided SQL schema below, Pay close attention to table definitions, column data types, primary keys, foreign keys, and any comments or annotations. You must be precise and follow the output format exactly. Your output will be parsed by a machine, so it is very important that you adhere to the specified format.


**Output Format:**

- Parent tables: Produce exactly {rowCount} CSV lines, one per row, values separated by commas.
- Child tables: Would be allowed produce more than {rowCount} if realism make sense to do so, in typical cases where likely are more than one single child row. e.g.
    - Orders -> OrderLines
    - Invoice -> InvoiceLines
  in anycase values separated by commas and not more than four or five rows.
- All primary‑key values must be unique within the table.
- Foreign‑key values must reference existing primary‑key values of the parent table (you may generate them on the fly).
- Do NOT include a header line. Return ONLY the CSV rows, each on its own line, no extra text.