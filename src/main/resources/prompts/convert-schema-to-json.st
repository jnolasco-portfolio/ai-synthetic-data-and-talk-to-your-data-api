Analyze the provided SQL schema below, first infer what database server (mysql, postgres, SQLSever, etc) is from schema. Pay close attention to table definitions, column data types, primary keys, and especially foreign key constraints which are critical for understanding table relationships. You must be precise and follow the output format exactly. Your output will be parsed by a machine, so it is very important that you adhere to the specified format.

In case user prompt is included take them into account.

**User prompt:**

{user_prompt}

**Output Format:**

The JSON output must have the following structure:
- `server`: The name of the database server. Infer from ddl syntax and in case ambiguety use a best guess value.
- `schemaName`: The name of the database. In case schema name could be infered from ddl use it otherwise use template value;
- `tables`: An array of table objects.

Each table object must have the following fields:
- `name`: The name of the table.
- `comment`: A concise and informative description of the table.
- `columns`: An array of column objects.
- `primaryKey`: The name of the primary key column.
- `foreignKeys`: An array of foreign key objects. It is crucial to correctly identify all foreign keys. If there are no foreign keys, this must be an empty array `[]`.
- `indexes`: An array of index objects. This must include the primary key and any unique constraints.

Each column object must have the following fields:
- `name`: The name of the column.
- `type`: The data type of the column.
- `nullable`: `true` if the column can be null, `false` otherwise.
- `defaultValue`: The default value of the column, or `null` if not specified. For string literals, do not include the surrounding quotes in the JSON output.
- `autoIncrement`: `true` if the column is auto-incrementing, `false` otherwise.
- `comment`: A concise and informative description of the column.

Each foreign key object must have the following fields:
- `name`: The name of the foreign key constraint. If the name is not explicitly defined, generate one using the format `FK_\{TableName\}_\{ColumnName\}`.
- `columns`: An array with the name of the column in the current table.
- `referencedTable`: The name of the referenced table.
- `referencedColumns`: An array with the name of the column in the referenced table.
- `updateRule`: The update rule (e.g., `ON UPDATE CASCADE`), or `null` if not specified.
- `deleteRule`: The delete rule (e.g., `ON DELETE CASCADE`), or `null` if not specified.

Each index object must have the following fields:
- `name`: The name of the index. For primary keys, the name should be `PK_\{TableName\}_\{ColumnName\}`. For unique constraints, the name should be `UNIQ_\{TableName\}_\{ColumnName\}`.
- `columns`: An array of column names included in the index.
- `unique`: `true` if the index has a unique constraint, `false` otherwise.
- `type`: The type of the index, e.g., `PRIMARY KEY`, `UNIQUE`.

**SQL Schema to Convert:**

SQL Schema for database '{schemaName}':

{schema}

Generate the JSON document now. Your response MUST contain ONLY the JSON document, enclosed within ```json and ``` delimiters. Do NOT add any other text, explanations, or formatting outside of these delimiters.