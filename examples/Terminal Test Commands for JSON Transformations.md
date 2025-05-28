# JSON Transformer API - Terminal Tests

## Setup
Assuming your Spring Boot app runs on `http://localhost:8080`

## 1. MINIFY Transformation Tests

### Test 1.1: Basic Minification
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Single line JSON without formatting
```json
{"name":"John","age":30,"address":{"street":"Main St","city":"New York","zip":"10001"},"hobbies":["reading","swimming","coding"]}
```

### Test 1.2: Minify Already Minified (Should work fine)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Same minified JSON

### Test 1.3: Minify Invalid JSON (Error case)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d '{"name":"John","age":30,'
```
**Expected Result:** Error message about invalid JSON

## 2. PRETTY Transformation Tests

### Test 2.1: Basic Pretty Printing
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Formatted JSON with indentation
```json
{
  "name" : "John",
  "age" : 30,
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  },
  "hobbies" : [ "reading", "swimming", "coding" ]
}
```

### Test 2.2: Pretty Print Already Pretty (Should work fine)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Same pretty-printed JSON

### Test 2.3: Pretty Print Complex Nested Structure
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d '{"users":[{"name":"John","details":{"age":30,"skills":["java","python"]}},{"name":"Jane","details":{"age":25,"skills":["javascript","react"]}}]}'
```
**Expected Result:** Well-formatted nested structure

## 3. FILTER Transformation Tests

### Test 3.1: Basic Key Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&includeKeys=name,age" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Only name and age fields
```json
{"name":"John","age":30}
```

### Test 3.2: Filter with Nested Objects
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&includeKeys=name,address" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Name and entire address object (nested structure preserved)
```json
{"name":"John","address":{"street":"Main St","city":"New York","zip":"10001"}}
```

### Test 3.3: Filter Non-existent Keys (Should return empty object)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&includeKeys=nonexistent,alsomissing" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Empty object
```json
{}
```

## 4. FILTER-OUT Transformation Tests

### Test 4.1: Basic Key Removal
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out&excludeKeys=age,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Everything except age and hobbies fields
```json
{"name":"John","address":{"street":"Main St","city":"New York","zip":"10001"}}
```

### Test 4.2: Filter-Out with Nested Objects (Removes keys recursively)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out&excludeKeys=street,zip" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Removes street and zip from nested address object
```json
{"name":"John","age":30,"address":{"city":"New York"},"hobbies":["reading","swimming","coding"]}
```

### Test 4.3: Filter-Out Non-existent Keys (Should return original)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out&excludeKeys=nonexistent,alsomissing" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Original JSON unchanged
```json
{"name":"John","age":30,"address":{"street":"Main St","city":"New York","zip":"10001"},"hobbies":["reading","swimming","coding"]}
```

### Test 4.4: Filter-Out All Top-Level Keys (Should return empty object)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out&excludeKeys=name,age,address,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Empty object
```json
{}
```

### Test 4.5: Filter-Out on Array of Objects
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out&excludeKeys=email" \
  -H "Content-Type: application/json" \
  -d '[
    {"name": "John", "age": 30, "email": "john@example.com"},
    {"name": "Jane", "age": 25, "email": "jane@example.com"}
  ]'
```
**Expected Result:** Array with email removed from each object
```json
[{"name":"John","age":30},{"name":"Jane","age":25}]
```

## 5. COMBINATION Tests (Chaining Transformations)

### Test 5.1: Filter + Pretty Print
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,pretty&includeKeys=name,address" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Filtered and pretty-printed
```json
{
  "name" : "John",
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  }
}
```

### Test 5.2: Pretty + Minify (Should end up minified)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty,minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Back to minified format

### Test 5.3: Filter + Minify + Pretty
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,minify,pretty&includeKeys=name,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Filtered, then pretty-printed
```json
{
  "name" : "John",
  "hobbies" : [ "reading", "swimming", "coding" ]
}
```

### Test 5.4: Filter-Out + Pretty Print
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out,pretty&excludeKeys=age,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Age and hobbies removed, then pretty-printed
```json
{
  "name" : "John",
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  }
}
```

### Test 5.5: Filter + Filter-Out Combination
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,filter-out&includeKeys=name,age,address&excludeKeys=age" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** First keep only name, age, address, then remove age
```json
{"name":"John","address":{"street":"Main St","city":"New York","zip":"10001"}}
```

### Test 5.6: Filter-Out + Minify + Pretty
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out,minify,pretty&excludeKeys=age,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Remove specified fields, minify, then pretty-print
```json
{
  "name" : "John",
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  }
}
```

## 6. ERROR Cases

### Test 6.1: Filter Without Keys Parameter
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Error message about missing keys parameter (Note: The error message might now refer to `includeKeys`)

### Test 6.2: Filter-Out Without Keys Parameter
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Error message about missing keys parameter (Note: The error message might now refer to `excludeKeys`)

### Test 6.3: Empty Request Body
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json"
```
**Expected Result:** Error message about missing JSON input

### Test 6.4: Invalid Transformation Type
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=unknown" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Falls back to raw (no transformation)

## 7. UTILITY Endpoints

### Test 7.1: Get Available Options
```bash
curl -X GET "http://localhost:8080/json-tools/options"
```
**Expected Result:**
```json
["minify","pretty","filter","filter-out","raw"]
```

## Advanced Test Cases

### Test A1: Complex Nested Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&includeKeys=name,address" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "age": 30,
    "email": "john@example.com",
    "address": {
      "street": "Main St",
      "city": "New York",
      "zip": "10001",
      "country": "USA"
    },
    "contacts": [
      {"type": "phone", "value": "123-456-7890"},
      {"type": "email", "value": "john@work.com"}
    ]
  }'
```

### Test A2: Array of Objects Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,pretty&includeKeys=name,age" \
  -H "Content-Type: application/json" \
  -d '[
    {"name": "John", "age": 30, "email": "john@example.com"},
    {"name": "Jane", "age": 25, "email": "jane@example.com"},
    {"name": "Bob", "age": 35, "email": "bob@example.com"}
  ]'
```

### Test A3: Complex Nested Filter-Out
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out,pretty&excludeKeys=email,country,type" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "age": 30,
    "email": "john@example.com",
    "address": {
      "street": "Main St",
      "city": "New York",
      "zip": "10001",
      "country": "USA"
    },
    "contacts": [
      {"type": "phone", "value": "123-456-7890"},
      {"type": "email", "value": "john@work.com"}
    ]
  }'
```
**Expected Result:** Removes email from root, country from address, and type from contacts array elements
```json
{
  "name" : "John",
  "age" : 30,
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  },
  "contacts" : [ {
    "value" : "123-456-7890"
  }, {
    "value" : "john@work.com"
  } ]
}
```

### Test A4: Mixed Filter Operations on Arrays
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter-out,pretty&excludeKeys=email" \
  -H "Content-Type: application/json" \
  -d '[
    {"name": "John", "age": 30, "email": "john@example.com", "department": "IT"},
    {"name": "Jane", "age": 25, "email": "jane@example.com", "department": "HR"},
    {"name": "Bob", "age": 35, "email": "bob@example.com", "department": "Finance"}
  ]'
```
**Expected Result:** Array with email field removed from all objects
```json
[ {
  "name" : "John",
  "age" : 30,
  "department" : "IT"
}, {
  "name" : "Jane",
  "age" : 25,
  "department" : "HR"
}, {
  "name" : "Bob",
  "age" : 35,
  "department" : "Finance"
} ]
```