package com.gameapps.phillip.singlethreadgame.data_handle;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by Phillip on 1/18/2017.
 */

public class Keywords {


    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String PRIM_KEY = "PRIMARY KEY";


    public static final String TYPE_INT = "INTEGER";
    public static final String TYPE_STR = "TEXT";

    public static String getCreateTableQuery(String tableName , String[] fields , String[] types , String keyFields) {
        String[] strArr = {keyFields};
        return getCreateTableQuery(tableName , fields , types , strArr);
    }
    public static String getCreateTableQuery(String tableName , String[] fields , String[] types , String[] keyFields) {
        if(fields.length != types.length) {

            Log.e("create table" , "field types not matching fields " +
                    "\nfields: " + Arrays.toString(fields) +
                    "\ntypes: " + Arrays.toString(types));

            return "";
        }

        String fieldDefinitions = "";
        for(int i = 0 ; i < fields.length ; i++) {
            fieldDefinitions += fields[i] + " " + types[i];
            fieldDefinitions += ",";
        }
        String primaryDefinition = PRIM_KEY + " ";
        primaryDefinition += "(";
        for(int i = 0 ; i < keyFields.length ; i++) {
            primaryDefinition += keyFields[i];
            if(i < keyFields.length-1) {
                primaryDefinition += " , ";
            }
        }
        primaryDefinition += ")";

        String query = CREATE_TABLE + " " + tableName +
                "(" +
                    fieldDefinitions  +
                    primaryDefinition +
                ")";

        Log.d("Create Table query" , query);

        return query;
    }


    //TODO - use
    public class TableField {
        public String name;
        public String type;

        public TableField(String name , String type) {
            //TODO - add check: is type necessarily using a reserved word?

            this.name = name;
            this.type = type;

        }

    }




    //Allowed words:
    /*
ABORT
ACTION
ADD
AFTER
ALL
ALTER
ANALYZE
AND
AS
ASC
ATTACH
AUTOINCREMENT
BEFORE
BEGIN
BETWEEN
BY
CASCADE
CASE
CAST
CHECK
COLLATE
COLUMN
COMMIT
CONFLICT
CONSTRAINT
CREATE
CROSS
CURRENT_DATE
CURRENT_TIME
CURRENT_TIMESTAMP
DATABASE
DEFAULT
DEFERRABLE
DEFERRED
DELETE
DESC
DETACH
DISTINCT
DROP
EACH
ELSE
END
ESCAPE
EXCEPT
EXCLUSIVE
EXISTS
EXPLAIN
FAIL
FOR
FOREIGN
FROM
FULL
GLOB
GROUP
HAVING
IF
IGNORE
IMMEDIATE
IN
INDEX
INDEXED
INITIALLY
INNER
INSERT
INSTEAD
INTERSECT
INTO
IS
ISNULL
JOIN
KEY
LEFT
LIKE
LIMIT
MATCH
NATURAL
NO
NOT
NOTNULL
NULL
OF
OFFSET
ON
OR
ORDER
OUTER
PLAN
PRAGMA
PRIMARY
QUERY
RAISE
RECURSIVE
REFERENCES
REGEXP
REINDEX
RELEASE
RENAME
REPLACE
RESTRICT
RIGHT
ROLLBACK
ROW
SAVEPOINT
SELECT
SET
TABLE
TEMP
TEMPORARY
THEN
TO
TRANSACTION
TRIGGER
UNION
UNIQUE
UPDATE
USING
VACUUM
VALUES
VIEW
VIRTUAL
WHEN
WHERE
WITH
WITHOUT
     */


}
